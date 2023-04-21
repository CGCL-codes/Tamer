package com.retain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;

public class SettingsManager extends PreferenceActivity {

    public static final int ORDER_BY_NOTSET = -1;

    public static final int ORDER_BY_TIME = 0;

    public static final int ORDER_BY_SITE = 1;

    public static final int BACKGROUND_COLOR_DEFAULT = 0xFFFFFFFF;

    public static final int FOREGROUND_COLOR_DEFAULT = 0xFF000000;

    private static final String LOG_TAG = "Prefs";

    private ColorPickerDialog bgColorPickerDialog;

    private ColorPickerDialog fgColorPickerDialog;

    private static final int DIALOG_YES_NO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String bgKey = getString(R.string.pref_key_bgcolor);
        Preference bgColorsPref = (Preference) findPreference(bgKey);
        int bgColor = sp.getInt(bgKey, SettingsManager.BACKGROUND_COLOR_DEFAULT);
        bgColorPickerDialog = new ColorPickerDialog(this, new SettingsOnColorChangedListener(this, bgKey), bgColor);
        bgColorsPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {
                bgColorPickerDialog.show();
                return true;
            }
        });
        String fgKey = getString(R.string.pref_key_fgcolor);
        Preference fgColorsPref = (Preference) findPreference(fgKey);
        int fgColor = sp.getInt(fgKey, SettingsManager.FOREGROUND_COLOR_DEFAULT);
        fgColorPickerDialog = new ColorPickerDialog(this, new SettingsOnColorChangedListener(this, fgKey), fgColor);
        fgColorsPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {
                fgColorPickerDialog.show();
                return true;
            }
        });
        String dKey = getString(R.string.pref_key_delete_all);
        Preference deleteDataPref = (Preference) findPreference(dKey);
        deleteDataPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {
                showDialog(SettingsManager.DIALOG_YES_NO);
                return true;
            }
        });
        Preference aboutPref = (Preference) findPreference(getString(R.string.pref_key_about));
        aboutPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {
                SettingsManager.this.startActivity(new Intent(SettingsManager.this, HelpActivity.class));
                return true;
            }
        });
        Preference feedbackPref = (Preference) findPreference(getString(R.string.pref_key_feedback));
        feedbackPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {
                String versionName = "<Unknown>";
                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getApplication().getPackageName(), PackageManager.GET_META_DATA);
                    versionName = pInfo.versionName;
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
                String appNameWithVersion = getString(R.string.app_name) + " " + versionName;
                String subject = getString(R.string.feedback_title) + " " + appNameWithVersion;
                String body = "\n\n----------------\n" + appNameWithVersion + " on ";
                body += Build.MANUFACTURER + " " + Build.MODEL + "(" + Build.DEVICE + ") Android " + Build.VERSION.RELEASE;
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { getString(R.string.feedback_email) });
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
                SettingsManager.this.startActivity(Intent.createChooser(emailIntent, subject));
                return true;
            }
        });
        Preference defColorPref = (Preference) findPreference(getString(R.string.pref_key_default_colors));
        defColorPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean useDefaultColors = ((Boolean) newValue).booleanValue();
                if (useDefaultColors) {
                    File file = new File(DownloadHandler.RETAIN_COLORS_CSS);
                    if (file.exists()) file.delete();
                } else initColorsCSS(SettingsManager.this);
                setColorPrefsEnabled(useDefaultColors);
                return true;
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_YES_NO) {
            return new AlertDialog.Builder(this).setTitle("Are you sure you want to delete all entries?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    WebDbAdapter mDbAdapter = new WebDbAdapter(SettingsManager.this);
                    mDbAdapter.open();
                    mDbAdapter.deleteAll();
                    mDbAdapter.close();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    Log.d(LOG_TAG, "cancel clicked.");
                }
            }).create();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Resuming");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useDefaultColors = sp.getBoolean(getString(R.string.pref_key_default_colors), true);
        setColorPrefsEnabled(useDefaultColors);
    }

    private void setColorPrefsEnabled(boolean useDefaultColors) {
        String bgKey = getString(R.string.pref_key_bgcolor);
        Preference bgColorsPref = (Preference) findPreference(bgKey);
        bgColorsPref.setEnabled(!useDefaultColors);
        String fgKey = getString(R.string.pref_key_fgcolor);
        Preference fgColorsPref = (Preference) findPreference(fgKey);
        fgColorsPref.setEnabled(!useDefaultColors);
    }

    public static void initColorsCSS(Activity activity) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        int bgColor = sp.getInt(activity.getString(R.string.pref_key_bgcolor), SettingsManager.BACKGROUND_COLOR_DEFAULT);
        int fgColor = sp.getInt(activity.getString(R.string.pref_key_fgcolor), SettingsManager.FOREGROUND_COLOR_DEFAULT);
        SettingsManager.initColorsCSS(bgColor, fgColor, activity);
    }

    public static void initColorsCSS(int bgColor, int fgColor, Activity activity) {
        try {
            AppUtils.createDir(DownloadHandler.RESOURCES_DIR + "/");
            File file = new File(DownloadHandler.RETAIN_COLORS_CSS);
            boolean fileDidntExist = !file.exists();
            if (fileDidntExist && !file.createNewFile()) {
                Log.e(LOG_TAG, "Unable to create file " + DownloadHandler.RETAIN_COLORS_CSS);
                return;
            }
            String bgColorHexStr = getHexString(bgColor);
            String fgColorHexStr = getHexString(fgColor);
            String templateData = AppUtils.fromRawResourceFile(R.raw.retain_colors, activity);
            templateData = templateData.replaceAll("@pref_key_fgcolor", fgColorHexStr);
            templateData = templateData.replaceAll("@pref_key_bgcolor", bgColorHexStr);
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(templateData);
            out.close();
        } catch (IOException ioe) {
            Log.e(LOG_TAG, ioe.getMessage());
            return;
        }
    }

    private static String getPaddedHexString(int rgbPart) {
        String hexStr = Integer.toHexString(rgbPart);
        if (hexStr.length() == 1) return ("0" + hexStr);
        return hexStr;
    }

    public static String getHexString(int color) {
        String redStr = getPaddedHexString(Color.red(color));
        String greenStr = getPaddedHexString(Color.green(color));
        String blueStr = getPaddedHexString(Color.blue(color));
        return ("#" + redStr + greenStr + blueStr).toUpperCase();
    }
}

class SettingsOnColorChangedListener implements ColorPickerDialog.OnColorChangedListener {

    String mPrefKey;

    SettingsManager mActivity;

    public SettingsOnColorChangedListener(SettingsManager activity, String prefKey) {
        mActivity = activity;
        mPrefKey = prefKey;
    }

    public void colorChanged(int color) {
        Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(mActivity).edit();
        Log.d("Settings;", "New color: " + color + " str=" + SettingsManager.getHexString(color));
        prefEditor.putInt(mPrefKey, color);
        prefEditor.commit();
        SettingsManager.initColorsCSS(mActivity);
    }
}
