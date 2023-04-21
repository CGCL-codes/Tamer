package com.never.util;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import com.never.db.DBHelper;

public class ScanSDReceiver extends BroadcastReceiver {

    private AlertDialog.Builder alertdialogbuilder = null;

    private AlertDialog alertdialog = null;

    private Cursor cursor;

    DBHelper dbHelper = null;

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        String receiveaction = arg1.getAction();
        if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(receiveaction)) {
            alertdialogbuilder = new AlertDialog.Builder(arg0);
            alertdialogbuilder.setMessage("����ɨ��洢��......");
            alertdialog = alertdialogbuilder.create();
            alertdialog.show();
            cursor = arg0.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME }, null, null, null);
        } else if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(receiveaction)) {
            alertdialog.cancel();
        }
    }
}
