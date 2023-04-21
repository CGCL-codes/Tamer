package com.android.camera;

import com.android.camera.PhotoAppWidgetProvider.PhotoDatabaseHelper;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import java.util.ArrayList;

class PhotoAppWidgetBind extends Activity {

    private static final String TAG = "PhotoAppWidgetBind";

    private static final String EXTRA_APPWIDGET_BITMAPS = "com.android.camera.appwidgetbitmaps";

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        finish();
        final Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        final int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        final ArrayList<Bitmap> bitmaps = extras.getParcelableArrayList(EXTRA_APPWIDGET_BITMAPS);
        if (appWidgetIds == null || bitmaps == null || appWidgetIds.length != bitmaps.size()) {
            Log.e(TAG, "Problem parsing photo widget bind request");
            return;
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        PhotoDatabaseHelper helper = new PhotoDatabaseHelper(this);
        for (int i = 0; i < appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];
            helper.setPhoto(appWidgetId, bitmaps.get(i));
            RemoteViews views = PhotoAppWidgetProvider.buildUpdate(this, appWidgetId, helper);
            appWidgetManager.updateAppWidget(new int[] { appWidgetId }, views);
        }
        helper.close();
    }
}
