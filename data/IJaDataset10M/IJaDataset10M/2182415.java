package com.javacodegeeks.android.preferences;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

public class ShowSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_settings_layout);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        StringBuilder builder = new StringBuilder();
        builder.append("\n" + sharedPrefs.getBoolean("status_check_box", false));
        builder.append("\n" + sharedPrefs.getString("list_freq_category", "NULL"));
        builder.append("\n" + sharedPrefs.getString("data_options", "NULL"));
        builder.append("\n" + sharedPrefs.getString("layout_category", "NULL"));
        builder.append("\n" + sharedPrefs.getString("map_view_options", "NULL"));
        builder.append("\n" + sharedPrefs.getBoolean("security_pass", false));
        builder.append("\n" + sharedPrefs.getBoolean("auto_start", false));
        TextView settingsTextView = (TextView) findViewById(R.id.settings_text_view);
        settingsTextView.setText(builder.toString());
    }
}
