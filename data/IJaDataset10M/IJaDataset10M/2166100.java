package opl.textc.util;

import opl.textc.Stop;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StopHistoryController {

    private static final int HOURS_TO_REMEMBER = 4;

    public static Stop openLastStop(Context mContext) {
        Stop stop = null;
        SharedPreferences prefs = mContext.getSharedPreferences("StopHistoryController", 0);
        SharedPreferences.Editor editor = prefs.edit();
        Long date_last_saved = prefs.getLong("date_last_saved", 0);
        Long now = System.currentTimeMillis();
        if (now - date_last_saved <= HOURS_TO_REMEMBER * 60 * 60 * 1000) {
            String stopID = prefs.getString("stopID_history", "");
            String dirDetailed = prefs.getString("dirDetailed_history", "");
            if (!stopID.trim().equals("")) {
                stop = Util.dh.selectStopWithTagIDAndDirDetailed(stopID, dirDetailed);
            }
        }
        editor.commit();
        return stop;
    }

    public static void saveLastStop(Context mContext, Stop stop) {
        SharedPreferences prefs = mContext.getSharedPreferences("StopHistoryController", 0);
        SharedPreferences.Editor editor = prefs.edit();
        Long now = System.currentTimeMillis();
        editor.putLong("date_last_saved", now);
        editor.putString("stopID_history", stop.getTag());
        editor.putString("dirDetailed_history", stop.getDirectionDetailedActual());
        editor.commit();
    }

    public static void clearHistory(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("StopHistoryController", 0);
        SharedPreferences.Editor editor = prefs.edit();
        Long now = System.currentTimeMillis();
        editor.putLong("date_last_saved", 0);
        editor.putString("stopID_history", "");
        editor.putString("dirDetailed_history", "");
        editor.commit();
    }
}
