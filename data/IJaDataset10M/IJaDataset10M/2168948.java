package com.projetoptymo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class SelectBusStopActivity extends Activity implements OnClickListener {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectbusstop);
        String[] strarray = BusStopList.getInstance().strarray;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, strarray);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.AutoCompleteTextView01);
        textView.setAdapter(adapter);
        Button valider = (Button) findViewById(R.id.ButtonChooseStop);
        valider.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.ButtonChooseStop:
                AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.AutoCompleteTextView01);
                String arret = textView.getText().toString();
                arret = arret.trim();
                if (!BusStopList.getInstance().arrets_textuel.containsValue(arret)) {
                    System.out.println("Arret inconnu.");
                    finish();
                    break;
                }
                SharedPreferences pref = getSharedPreferences("settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                if (pref.getBoolean("ChooseDepart", true)) {
                    editor.putString("DepartNom", arret);
                } else {
                    editor.putString("ArriveeNom", arret);
                }
                editor.commit();
                finish();
                break;
        }
    }
}
