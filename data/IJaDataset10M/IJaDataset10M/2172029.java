package org.efimov.tomboedit;

import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.content.Context;

public class Prefs extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    public static String getCodePage(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("codepage", "Cp1251");
    }
}
