package org.openintents.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

public class DateTimeFormater {

    public static DateFormat mDateFormater;

    public static DateFormat mTimeFormater;

    public static DateFormat mTimeWithSecondsFormater;

    public static boolean mUse24hour = true;

    public static void getFormatFromPreferences(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String dateFormat = "MM/dd/yyyy";
        String timeFormat = "HH:mm";
        if (TextUtils.isEmpty(dateFormat)) {
            String androidDateFormat = Settings.System.getString(context.getContentResolver(), Settings.System.DATE_FORMAT);
            if (androidDateFormat != null) {
                dateFormat = androidDateFormat;
            } else {
                dateFormat = "MM/dd/yyyy";
            }
        }
        if (TextUtils.isEmpty(timeFormat)) {
            String androidTimeFormat = Settings.System.getString(context.getContentResolver(), Settings.System.TIME_12_24);
            if (androidTimeFormat != null && androidTimeFormat.equals("24")) {
                timeFormat = "HH:mm";
            } else {
                timeFormat = "hh:mm a";
            }
        }
        mDateFormater = new SimpleDateFormat(dateFormat);
        mTimeFormater = new SimpleDateFormat(timeFormat);
        if (timeFormat.equals("hh:mm a")) {
            mTimeWithSecondsFormater = new SimpleDateFormat("hh:mm:ss a");
            mUse24hour = false;
        } else {
            mTimeWithSecondsFormater = new SimpleDateFormat("HH:mm:ss");
            mUse24hour = true;
        }
        ;
    }
}
