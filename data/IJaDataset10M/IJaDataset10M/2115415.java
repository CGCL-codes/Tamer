package com.andro.yaniv.ui;

import com.andro.yaniv.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RadioButton;

public class Settings extends Activity {

    Context mContext = null;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.settings);
        mContext = findViewById(R.id.sortHand).getContext();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        CheckBox sortHand = (CheckBox) findViewById(R.id.sortHand);
        boolean shouldSort = sharedPrefs.getBoolean("sortHand", false);
        sortHand.setChecked(shouldSort);
        ((RadioButton) findViewById(R.id.sortFace)).setChecked(sharedPrefs.getBoolean("sortFace", false));
        ((RadioButton) findViewById(R.id.sortValue)).setChecked(sharedPrefs.getBoolean("sortValue", false));
        ((RadioButton) findViewById(R.id.sortSuit)).setChecked(sharedPrefs.getBoolean("sortSuit", false));
        ((RadioButton) findViewById(R.id.sortFace)).setEnabled(shouldSort);
        ((RadioButton) findViewById(R.id.sortValue)).setEnabled(shouldSort);
        ((RadioButton) findViewById(R.id.sortSuit)).setEnabled(shouldSort);
        String aiLevel = sharedPrefs.getString("AI_Level", "easy");
        if (aiLevel.equals("easy")) {
            ((RadioButton) findViewById(R.id.aiEasy)).setChecked(true);
        } else if (aiLevel.equals("moderate")) {
            ((RadioButton) findViewById(R.id.aiModerate)).setChecked(true);
        } else if (aiLevel.equals("insane")) {
            ((RadioButton) findViewById(R.id.aiInsane)).setChecked(true);
        }
        sortHand.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                Editor ed = sharedPrefs.edit();
                ed.putBoolean("sortHand", ((CheckBox) v).isChecked());
                ed.commit();
                ((RadioButton) findViewById(R.id.sortFace)).setEnabled(((CheckBox) v).isChecked());
                ((RadioButton) findViewById(R.id.sortValue)).setEnabled(((CheckBox) v).isChecked());
                ((RadioButton) findViewById(R.id.sortSuit)).setEnabled(((CheckBox) v).isChecked());
            }
        });
        RadioListener rl = new RadioListener();
        ((RadioButton) findViewById(R.id.sortFace)).setOnClickListener(rl);
        ((RadioButton) findViewById(R.id.sortValue)).setOnClickListener(rl);
        ((RadioButton) findViewById(R.id.sortSuit)).setOnClickListener(rl);
        ((RadioButton) findViewById(R.id.aiEasy)).setOnClickListener(rl);
        ((RadioButton) findViewById(R.id.aiModerate)).setOnClickListener(rl);
        ((RadioButton) findViewById(R.id.aiInsane)).setOnClickListener(rl);
    }

    class RadioListener implements OnClickListener {

        public void onClick(View v) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
            Editor ed = sharedPrefs.edit();
            switch(v.getId()) {
                case R.id.sortFace:
                    ed.putBoolean("sortFace", true);
                    ed.putBoolean("sortValue", false);
                    ed.putBoolean("sortSuit", false);
                    break;
                case R.id.sortValue:
                    ed.putBoolean("sortFace", false);
                    ed.putBoolean("sortValue", true);
                    ed.putBoolean("sortSuit", false);
                    break;
                case R.id.sortSuit:
                    ed.putBoolean("sortFace", false);
                    ed.putBoolean("sortValue", false);
                    ed.putBoolean("sortSuit", true);
                    break;
                case R.id.aiEasy:
                    ed.putString("AI_Level", "easy");
                    break;
                case R.id.aiModerate:
                    ed.putString("AI_Level", "moderate");
                    break;
                case R.id.aiInsane:
                    ed.putString("AI_Level", "insane");
                    break;
            }
            ed.commit();
        }
    }
}
