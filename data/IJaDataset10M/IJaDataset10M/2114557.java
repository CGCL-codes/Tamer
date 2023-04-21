package br.ufmg.ubicomp.droidguide.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import br.ufmg.ubicomp.droidguide.R;
import br.ufmg.ubicomp.droidguide.context.MyLocationListener;
import br.ufmg.ubicomp.droidguide.eventservice.notification.DGNotification;
import br.ufmg.ubicomp.droidguide.eventservice.notification.DGNotificationType;
import br.ufmg.ubicomp.droidguide.eventservice.notification.NotificationBucket;

public class AndroidUtils {

    private static int id = 1;

    public static void runTask(Runnable task, long delay) {
        Handler handler = new Handler();
        handler.removeCallbacks(task);
        handler.postDelayed(task, delay);
    }

    public static void showNotification(android.app.Activity activity, String title, String text, DGNotificationType type, String origin) {
        NotificationManager notManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        long time = System.currentTimeMillis();
        DGNotification dgNotification = new DGNotification(type, time, text, origin);
        NotificationBucket.getInstance().add(dgNotification);
        Notification not = new Notification(R.drawable.icon, text, time);
        PendingIntent contentIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, activity.getClass()), 0);
        not.setLatestEventInfo(activity, title, text, contentIntent);
        notManager.notify(id, not);
        id++;
        if (id == Integer.MAX_VALUE) {
            id = 1;
        }
    }

    public static void sleepThread(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void startActivity(Activity activity, Class clazz) {
        Intent i = new Intent(activity.getApplicationContext(), clazz);
        activity.startActivity(i);
    }

    public static void showAlert(Activity activity, String message) {
        showAlert(activity.getApplicationContext(), message);
    }

    public static void notifyError(Exception e, Activity activity, String op) {
        NotificationBucket.getInstance().add(new DGNotification(DGNotificationType.ERROR, System.currentTimeMillis(), op + ": " + e.getMessage(), "ServiceList"));
        AndroidUtils.showAlert(activity, "An error ocurred.");
        Log.e(op, e.getMessage());
    }

    public static void notifyError(Exception e, Context context, String op, String source) {
        NotificationBucket.getInstance().add(new DGNotification(DGNotificationType.ERROR, System.currentTimeMillis(), op + ": " + e.getMessage(), source));
        AndroidUtils.showAlert(context, "An error ocurred.");
        Log.e(op, e.getMessage());
    }

    public static void notifyError(String message, Activity activity, String op) {
        NotificationBucket.getInstance().add(new DGNotification(DGNotificationType.ERROR, System.currentTimeMillis(), message, "ServiceList"));
        AndroidUtils.showAlert(activity, "An error ocurred: " + message);
        Log.e(op, message);
    }

    public static void showAlert(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String[] getStringArray(Activity activity, int id) {
        return activity.getResources().getStringArray(id);
    }

    public static String getConnectivityInfo(Activity activity) {
        ConnectivityManager conMan = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = conMan.getActiveNetworkInfo();
        String conType = activeNetworkInfo.getTypeName();
        String state = activeNetworkInfo.getDetailedState().toString();
        return conType + " : " + state;
    }

    public static String getConnectivityName(Activity activity) {
        ConnectivityManager conMan = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = conMan.getActiveNetworkInfo();
        return activeNetworkInfo.getTypeName();
    }

    public static WebView getWebView(Activity activity, int id, String url) {
        WebView wv;
        wv = (WebView) activity.findViewById(id);
        wv.setWebViewClient(new MapWebViewClient());
        wv.loadUrl(url);
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        return wv;
    }

    public static boolean checkPermission(Context ctx, String permission) {
        return ctx.checkCallingPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static ArrayAdapter<String> createAdapter(Activity activity, int arrayId) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, activity.getResources().getStringArray(arrayId));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public static void getLocation(Activity activity) {
        LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        MyLocationListener locationListener = new MyLocationListener(activity);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
}
