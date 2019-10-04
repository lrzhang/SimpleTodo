package com.example.simpletodo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    List<String> items;

    Button btnAdd;
    EditText textEdt;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btnAdd = findViewById(R.id.btnAdd);
        textEdt = findViewById(R.id.itemEdt);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

       ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //Delete the item from the model
                items.remove(position);
                //notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));


        btnAdd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String todoItem = textEdt.getText().toString();
                items.add(todoItem);
                itemsAdapter.notifyItemInserted(items.size()-1);
                textEdt.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

    }


    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    //function loads items from saved data.txt file
    private void loadItems() {
        try {
            items = new ArrayList<>(org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch(IOException e){
            Log.e("MainActivity","Error reading items", e);
            items = new ArrayList<>();
        }
    }

    // function saves items by writing them into the data.txt file
    private void saveItems(){
            try {
                org.apache.commons.io.FileUtils.writeLines(getDataFile(), items);
            } catch (IOException e){
            Log.e("MainActivity","Error reading items", e);
            items = new ArrayList<>();
        }
    }
}
