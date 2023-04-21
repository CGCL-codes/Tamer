package de.peacei.android.foodwatcher.gui;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import de.peacei.android.foodwatcher.gui.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

/**
 *
 * @author peacei
 */
public class SettingsActivity extends SherlockPreferenceActivity implements Preference.OnPreferenceChangeListener {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.settings);
        findPreference("default_location_setting").setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference pref, Object newValue) {
        final ListPreference listPref = (ListPreference) pref;
        final int idx = listPref.findIndexOfValue((String) newValue);
        listPref.setSummary(listPref.getEntries()[idx]);
        SharedPreferences settings = getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(listPref.getKey(), idx);
        editor.commit();
        return true;
    }
}
