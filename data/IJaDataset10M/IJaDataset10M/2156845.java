package com.andriy.autosilent;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

public class AutoSilentAppWidgetProvider extends AppWidgetProvider {

    private static final String changeString = "com.andriy.autosilent.broadcast";

    private static boolean isRegistered = false;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];
            Intent clickIntent = new Intent(changeString);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.autosilent_appwidget);
            remoteViews.setOnClickPendingIntent(R.id.imageView1, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        this.unregister(context);
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        this.unregister(context);
        super.onDisabled(context);
    }

    private void unregister(Context context) {
        Intent intent = new Intent("com.andriy.autoSilentService");
        Bundle bundle = new Bundle();
        bundle.putBoolean("register", false);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (changeString.equals(intent.getAction())) {
            this.changeRegister(context);
        }
    }

    private void changeRegister(Context context) {
        isRegistered = !isRegistered;
        Intent changeIntent = new Intent("com.andriy.autoSilentService");
        Bundle bundle = new Bundle();
        bundle.putBoolean("register", isRegistered);
        changeIntent.putExtras(bundle);
        context.startService(changeIntent);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.autosilent_appwidget);
        if (isRegistered) {
            remoteViews.setImageViewResource(R.id.imageView1, R.drawable.register);
        } else {
            remoteViews.setImageViewResource(R.id.imageView1, R.drawable.unregister);
        }
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, AutoSilentAppWidgetProvider.class);
        manager.updateAppWidget(componentName, remoteViews);
    }
}
