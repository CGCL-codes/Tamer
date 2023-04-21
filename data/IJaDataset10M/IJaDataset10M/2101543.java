package com.chl.edu.whackamole.view;

import com.chl.edu.whackamole.R;
import com.chl.edu.whackamole.utils.UIHelper;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

/**
 * 
 * @author Aron Manucheri / Joel Hammar
 * 
 */
public class OptionsViewUI extends Activity implements OnClickListener {

    private ToggleButton soundButton;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.optionslayout);
        soundButton = (ToggleButton) findViewById(R.id.soundbutton);
        soundButton.setOnClickListener(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        soundButton.setChecked(preferences.getBoolean("soundOnOff", false));
    }

    public void onClick(View view) {
        if (view.equals((Button) findViewById(R.id.soundbutton))) {
            if (!(preferences.getBoolean("soundOnOff", false))) {
                preferences.edit().putBoolean("soundOnOff", true);
                preferences.edit().commit();
                UIHelper.getMPlayerTheme().start();
            } else {
                preferences.edit().putBoolean("soundOnOff", false);
                preferences.edit().commit();
                UIHelper.getMPlayerTheme().stop();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences.getBoolean("soundOnOff", false)) {
            soundButton.setChecked(true);
        } else {
            soundButton.setChecked(false);
        }
    }
}
