package pt.isel.meic.agendaagent.android.service;

import jade.android.ConnectionListener;
import jade.android.JadeGateway;
import jade.core.Profile;
import jade.imtp.leap.JICP.JICPProtocol;
import jade.util.Logger;
import jade.util.leap.Properties;
import pt.isel.meic.agendaagent.UserNotifierInterface;
import pt.isel.meic.agendaagent.android.agent.MyNewAgent;
import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class AgendaService extends Service implements ConnectionListener, UserNotifierInterface {

    private final Logger logger = Logger.getMyLogger(this.getClass().getName());

    private JadeGateway gateway;

    private Properties jadeProps;

    @Override
    public IBinder onBind(Intent arg0) {
        Toast.makeText(this, "Agenda Service onBind...", 5000).show();
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Agenda Service onStart...", 5000).show();
        jadeProps = new Properties();
        jadeProps.setProperty(Profile.MAIN_HOST, intent.getExtras().getString(Profile.MAIN_HOST));
        jadeProps.setProperty(Profile.MAIN_PORT, intent.getExtras().getString(Profile.MAIN_PORT));
        jadeProps.setProperty(JICPProtocol.MSISDN_KEY, intent.getExtras().getString(JICPProtocol.MSISDN_KEY));
        try {
            JadeGateway.connect(MyNewAgent.class.getName(), null, jadeProps, getBaseContext(), this);
        } catch (Exception e) {
            Toast.makeText(this, "Error connecting to JADE platform: " + e.getMessage(), 5000).show();
            logger.log(Logger.SEVERE, e.getMessage(), e);
        }
        return START_STICKY;
    }

    public void onCreate() {
        Toast.makeText(this, "Agenda Service onCreate...", 5000).show();
    }

    public void onDestroy() {
        Toast.makeText(this, "Agenda Service onDestroy...", 5000).show();
        if (gateway != null) gateway.disconnect(getBaseContext());
    }

    public void onConnected(JadeGateway pGateway) {
        gateway = pGateway;
        Toast.makeText(this, "Agenda Service onConnected...", 5000).show();
        try {
            gateway.execute(getBaseContext());
            gateway.execute((UserNotifierInterface) this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDisconnected() {
        Toast.makeText(this, "Agenda Service onDisconnect...", 5000).show();
    }

    public void onNewEventProposalReceive(String pName) {
    }

    public void onNewEventAccepted(String pName) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int icon = R.drawable.ic_menu_today;
        CharSequence tickerText = "Hello";
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
        Context context = getApplicationContext();
        CharSequence contentTitle = "My notification";
        CharSequence contentText = "Hello World!";
        Intent notificationIntent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        mNotificationManager.notify(1, notification);
    }

    public void onNotify(String pText) {
        Toast.makeText(this, "AgendaService:" + pText, 5500).show();
    }

    public void onNewEventProposed(String pName) {
    }

    public void onAllProposalsReceived(String pName) {
    }

    public void onRefuseReceived(String pName) {
    }

    public void onInformReceived(String pName) {
    }

    public void onFailureReceived(String pName) {
    }

    public void onEventCanceled(String pName) {
    }
}
