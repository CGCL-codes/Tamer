package net.learn2develop.PurchaseOrders;

import java.util.List;
import java.util.Vector;
import net.learn2develop.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class Prefs extends Activity {

    private EditText autocompleteClient;

    private EditText numarbucati;

    private EditText discount;

    private Button Adauga02;

    private String mIntentString;

    static final int DIALOG_ID = 0;

    DataManipulator dataMan;

    List<String[]> listOfRoutes = null;

    String[] stringOfProducts;

    String theClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autocompleteclient2);
        autocompleteClient = (EditText) findViewById(R.id.autocompleteClient);
        numarbucati = (EditText) findViewById(R.id.numarbucati);
        discount = (EditText) findViewById(R.id.discount);
        Adauga02 = (Button) findViewById(R.id.Adauga02);
        Adauga02.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Bundle bundledPrefs = new Bundle();
                bundledPrefs.putString("produs", autocompleteClient.getText().toString());
                bundledPrefs.putString("bucati", numarbucati.getText().toString());
                bundledPrefs.putString("cost", discount.getText().toString());
                Intent ourIntent = new Intent(Prefs.this, ComandaNoua.class);
                ourIntent.putExtras(bundledPrefs);
                setResult(RESULT_OK, ourIntent);
                finish();
            }
        });
        final AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.autocompleteClient);
        dataMan = new DataManipulator(this);
        listOfRoutes = dataMan.selectAllProducts();
        stringOfProducts = new String[listOfRoutes.size()];
        int x = 0;
        String stringTemporar;
        for (String[] name : listOfRoutes) {
            stringTemporar = name[1];
            stringOfProducts[x] = stringTemporar;
            x++;
        }
        for (int i = 0; i < stringOfProducts.length; i++) {
            Log.i(this.toString(), stringOfProducts[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, stringOfProducts);
        acTextView.setAdapter(adapter);
        acTextView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                theClient = arg0.getItemAtPosition(arg2).toString();
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
