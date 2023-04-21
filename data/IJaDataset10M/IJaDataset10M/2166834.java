package org.imogene.android.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.imogene.android.Constants.Intents;
import org.imogene.android.Constants.Paths;
import org.imogene.android.app.WakefulIntentService;
import org.imogene.android.common.SyncHistory;
import org.imogene.android.notification.MessagingNotification;
import org.imogene.android.preference.PreferenceHelper;
import org.imogene.android.sync.OptimizedSyncClient;
import org.imogene.android.sync.SynchronizationException;
import org.imogene.android.sync.http.OptimizedSyncClientHttp;
import org.imogene.android.template.R;
import org.imogene.android.util.ntp.SntpException;
import org.imogene.android.util.ntp.SntpProvider;
import org.xmlpull.v1.XmlPullParserException;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public abstract class AbstractSyncService extends WakefulIntentService {

    public static final void actionReschedule(Context context) {
        sendWakefulWork(context, new Intent(Intents.ACTION_RESCHEDULE));
    }

    public static final void actionCancel(Context context) {
        sendWakefulWork(context, new Intent(Intents.ACTION_CANCEL));
    }

    public static final void startService(Context context) {
        sendWakefulWork(context, new Intent(Intents.ACTION_CHECK_SYNC));
    }

    public static final void startServiceManually(Context context) {
        Toast.makeText(context, R.string.ig_sync_manually, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intents.ACTION_CHECK_SYNC);
        intent.putExtra(EXTRA_MANUAL, true);
        sendWakefulWork(context, intent);
    }

    private static final String TAG = AbstractSyncService.class.getName();

    private static final String EXTRA_MANUAL = "AbstractSyncService_manual";

    private static final int SYNC_STATE_ID = 1111;

    private static final int NOTIFICATION_SENT_ID = 1112;

    public static class OnClickSentReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(NOTIFICATION_SENT_ID);
        }
    }

    ;

    public static class OnClickSentFailedReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(NOTIFICATION_SENT_ID);
            startServiceManually(context);
        }
    }

    ;

    private OptimizedSyncClient syncClient;

    private String login;

    private String password;

    private String hardwareId;

    private boolean debug;

    private boolean bidirectional;

    private boolean manual;

    public AbstractSyncService(String name) {
        super(name);
    }

    protected abstract int getDataToSynchronize(FileOutputStream fos, String hardwareId) throws IllegalArgumentException, IllegalStateException, IOException;

    protected abstract int applyIncomingModifications(InputStream is) throws XmlPullParserException, IOException;

    protected abstract void markAsSentForSession(long time);

    protected abstract void markAsReadHidden();

    @Override
    protected void doWakefulWork(Intent intent) {
        manual = intent.getBooleanExtra(EXTRA_MANUAL, false);
        hardwareId = PreferenceHelper.getHardwareId(this);
        login = PreferenceHelper.getSyncLogin(this);
        password = PreferenceHelper.getSyncPassword(this);
        debug = PreferenceHelper.isDebugActive(this);
        String serverUrl = PreferenceHelper.getServerUrl(this);
        bidirectional = PreferenceHelper.getSynchronizationBidirectional(this);
        if (PreferenceHelper.isHttpAuthenticated(this)) {
            syncClient = new OptimizedSyncClientHttp(serverUrl, login, password);
        } else {
            syncClient = new OptimizedSyncClientHttp(serverUrl);
        }
        if (Intents.ACTION_CHECK_SYNC.equals(intent.getAction())) {
            int received = 0;
            try {
                onStart();
                Paths.PATH_SYNCHRO.mkdirs();
                updateTimeOffset();
                SyncHistory syncError = SyncHistory.getLastErrorSyncHistory(this);
                if (syncError != null) {
                    Log.i(TAG, "resume on error : " + syncError.id + ", level : " + syncError.level + ", date : " + syncError.date);
                    received += resumeOnError(syncError);
                }
                onInit();
                UUID sessionId = UUID.fromString(syncClient.initSession(login, password, hardwareId, "xml"));
                onSend();
                long syncTime = PreferenceHelper.getRealTime(this);
                File outFile = new File(Paths.PATH_SYNCHRO, sessionId + ".lmodif");
                FileOutputStream fos = new FileOutputStream(outFile);
                getDataToSynchronize(fos, hardwareId);
                fos.close();
                SyncHistory his = new SyncHistory();
                his.id = sessionId;
                his.date = syncTime;
                his.status = SyncHistory.Columns.STATUS_ERROR;
                his.level = SyncHistory.Columns.LEVEL_SEND;
                his.saveOrUpdate(this);
                FileInputStream fis = new FileInputStream(outFile);
                int res = syncClient.sendClientModification(sessionId, fis);
                fis.close();
                if (!debug) {
                    outFile.delete();
                }
                markAsSentForSession(syncTime);
                if (res > 0) {
                    notifySent();
                }
                Log.i(TAG, "number of server modifications applied: " + res);
                if (res > -1) {
                    his.level = SyncHistory.Columns.LEVEL_RECEIVE;
                    his.saveOrUpdate(this);
                } else {
                    throw new SynchronizationException("Error sending data to the server.", SynchronizationException.ERROR_SEND);
                }
                onReceive();
                received += requestServerModification(sessionId);
                his.level = SyncHistory.Columns.LEVEL_RECEIVE;
                his.status = SyncHistory.Columns.STATUS_OK;
                his.saveOrUpdate(this);
                onClose();
                syncClient.closeSession(sessionId, debug);
            } catch (FileNotFoundException e) {
                MetadataService.startMetadataService(this, e, PreferenceHelper.getRealTime(this));
            } catch (IOException e) {
                MetadataService.startMetadataService(this, e, PreferenceHelper.getRealTime(this));
            } catch (SynchronizationException e) {
                if (manual && SynchronizationException.ERROR_SEND == e.getCode()) {
                    notifySentFailed();
                }
                Log.e(TAG, "error during synchronization", e);
            } catch (Exception e) {
                Log.e(TAG, "error during synchronization", e);
            } finally {
                markAsReadHidden();
                if (received > 0) {
                    MessagingNotification.blockingUpdateNewMessageIndicator(this);
                }
                onFinish();
            }
        } else if (Intents.ACTION_CANCEL.equals(intent.getAction())) {
            onCancel();
        } else if (Intents.ACTION_RESCHEDULE.equals(intent.getAction())) {
            onReschedule();
        }
    }

    private void updateTimeOffset() {
        try {
            String url = PreferenceHelper.getNtpServerUrl(this);
            long offset = SntpProvider.getTimeOffsetFromNtp(url);
            onUpdateOffset(offset);
        } catch (SntpException e) {
            Log.e(TAG, "update offset from ntp ->", e);
        }
    }

    private int resumeOnError(SyncHistory his) throws SynchronizationException {
        int received = 0;
        if (his.level == SyncHistory.Columns.LEVEL_SEND) {
            Log.i(TAG, "Resuming the sent for the session " + his.id);
            try {
                onInitResume();
                String result = syncClient.resumeSend(login, password, hardwareId, "xml", his.id);
                onSendResume();
                if (result.equals("error")) {
                    throw new SynchronizationException("Error resuming the session, the server return an error code", SynchronizationException.ERROR_SEND);
                } else {
                    int bytesReceived = Integer.parseInt(result);
                    File outFile = new File(Paths.PATH_SYNCHRO, his.id + ".lmodif");
                    FileInputStream fis = new FileInputStream(outFile);
                    fis.skip(bytesReceived);
                    Log.i(TAG, "Re-sending data from the file " + outFile.getAbsolutePath() + " skipping " + bytesReceived + " bytes");
                    int res = syncClient.resumeSendModification(his.id, fis);
                    fis.close();
                    if (!debug) {
                        outFile.delete();
                    }
                    markAsSentForSession(his.date);
                    if (res > 0) {
                        notifySent();
                    }
                    Log.i(TAG, "number of server modifications applied on resume: " + res);
                }
                his.level = SyncHistory.Columns.LEVEL_RECEIVE;
                his.saveOrUpdate(this);
                onReceiveResume();
                received = requestServerModification(his.id);
                his.status = SyncHistory.Columns.STATUS_OK;
                his.saveOrUpdate(this);
                onCloseResume();
                syncClient.closeSession(his.id, debug);
            } catch (Exception ex) {
                SynchronizationException syx = new SynchronizationException("Error resuming a sent", ex, SynchronizationException.DEFAULT_ERROR);
                if (ex instanceof SynchronizationException) syx.setCode(((SynchronizationException) ex).getCode());
                throw syx;
            }
        }
        if (his.level == SyncHistory.Columns.LEVEL_RECEIVE) {
            Log.i(TAG, "Resuming the receive operation for the session " + his.id);
            try {
                if (!debug) {
                    File tmp = new File(Paths.PATH_SYNCHRO, his.id + ".lmodif");
                    if (tmp.exists()) tmp.delete();
                }
                onInitResume();
                File inFile = new File(Paths.PATH_SYNCHRO, his.id + ".smodif");
                String result = syncClient.resumeReceive(login, password, hardwareId, "xml", his.id, inFile.length());
                onReceiveResume();
                if (!result.equals("error")) {
                    received = resumeRequestModification(his.id);
                    his.status = SyncHistory.Columns.STATUS_OK;
                    his.saveOrUpdate(this);
                    onCloseResume();
                    syncClient.closeSession(his.id, debug);
                } else {
                    throw new SynchronizationException("The server return an error code", SynchronizationException.ERROR_RECEIVE);
                }
            } catch (Exception ex) {
                SynchronizationException syx = new SynchronizationException("Error resuming a receive operation", ex, SynchronizationException.ERROR_RECEIVE);
                if (ex instanceof SynchronizationException) syx.setCode(((SynchronizationException) ex).getCode());
                throw syx;
            }
        }
        return received;
    }

    /**
	 * Request the server modification in normal mode
	 * 
	 * @param sessionId
	 *            the session Id
	 * @throws SynchronizationException
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
    private int requestServerModification(UUID sessionId) throws SynchronizationException, IOException, XmlPullParserException {
        if (!bidirectional) return 0;
        File inFile = new File(Paths.PATH_SYNCHRO, sessionId + ".smodif");
        FileOutputStream sFos = new FileOutputStream(inFile);
        syncClient.requestServerModifications(sessionId, sFos);
        sFos.close();
        FileInputStream sFis = new FileInputStream(inFile);
        int result = applyIncomingModifications(sFis);
        sFis.close();
        if (!debug) {
            inFile.delete();
        }
        return result;
    }

    private int resumeRequestModification(UUID errorId) throws SynchronizationException, IOException, XmlPullParserException {
        if (!bidirectional) return 0;
        File inputFile = new File(Paths.PATH_SYNCHRO, errorId + ".smodif");
        FileOutputStream fos = new FileOutputStream(inputFile, true);
        syncClient.resumeRequestModification(errorId, fos, inputFile.length());
        fos.close();
        FileInputStream fis = new FileInputStream(inputFile);
        int result = applyIncomingModifications(fis);
        fis.close();
        if (!debug) {
            inputFile.delete();
        }
        return result;
    }

    private void onCancel() {
        Log.i(TAG, "*** OptimizedSynchronizationService: cancel");
        cancel();
    }

    private void onReschedule() {
        Log.i(TAG, "*** OptimizedSynchronizationService: reschedule");
        reschedule();
    }

    private void onFinish() {
        NotificationManager notifMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifMgr.cancel(SYNC_STATE_ID);
        if (PreferenceHelper.getSynchronizationStatus(this)) {
            reschedule();
        }
    }

    private void onStart() {
        Log.i(TAG, "Starting");
        notifyState(getString(R.string.ig_sync_start));
    }

    private void onInit() {
        Log.i(TAG, "Initializing");
        notifyState(getString(R.string.ig_sync_init));
    }

    private void onInitResume() {
        Log.i(TAG, "Initializing a resumed session");
        notifyState(getString(R.string.ig_sync_init_resume));
    }

    private void onSend() {
        Log.i(TAG, "Sending");
        notifyState(getString(R.string.ig_sync_send));
    }

    private void onSendResume() {
        Log.i(TAG, "Sending from a resumed session");
        notifyState(getString(R.string.ig_sync_send_resume));
    }

    private void onReceive() {
        Log.i(TAG, "Receiving");
        notifyState(getString(R.string.ig_sync_receive));
    }

    private void onReceiveResume() {
        Log.i(TAG, "Receiving from a resumed session");
        notifyState(getString(R.string.ig_sync_receive_resume));
    }

    private void onClose() {
        Log.i(TAG, "Closing");
        notifyState(getString(R.string.ig_sync_close));
    }

    private void onCloseResume() {
        Log.i(TAG, "Closing a resumed session");
        notifyState(getString(R.string.ig_sync_close_resume));
    }

    private void onUpdateOffset(long offset) {
        SntpProvider.updateTimeOffset(this, offset);
    }

    private void cancel() {
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(Intents.ACTION_CHECK_SYNC);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.cancel(pi);
    }

    private void reschedule() {
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(Intents.ACTION_CHECK_SYNC);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        long period = PreferenceHelper.getSynchronizationPeriod(this);
        if (period < 15) {
            period = 15;
        }
        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + period * 1000, pi);
    }

    private void notifyState(String msg) {
        NotificationManager notifMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = new Notification(R.drawable.ig_logo_android_s, msg, System.currentTimeMillis());
        notif.flags = Notification.FLAG_NO_CLEAR;
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, null, 0);
        notif.setLatestEventInfo(this, getString(R.string.ig_synchronization), msg, contentIntent);
        notifMgr.notify(SYNC_STATE_ID, notif);
    }

    private void notifySent() {
        Notification notification = new Notification(R.drawable.ig_logo_android_s, getString(R.string.ig_notification_sent_ticker), System.currentTimeMillis());
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(this, OnClickSentReceiver.class), 0);
        notification.setLatestEventInfo(this, getString(R.string.ig_notification_sent_title), getString(R.string.ig_notification_sent_description), pi);
        boolean vibrateAlways = true;
        boolean vibrateSilent = false;
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        boolean nowSilent = audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE;
        if (vibrateAlways || vibrateSilent && nowSilent) {
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        }
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_SENT_ID, notification);
    }

    private void notifySentFailed() {
        Notification notification = new Notification(R.drawable.ig_logo_android_s, getString(R.string.ig_notification_sent_failed_ticker), System.currentTimeMillis());
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(this, OnClickSentFailedReceiver.class), 0);
        notification.setLatestEventInfo(this, getString(R.string.ig_notification_sent_failed_title), getString(R.string.ig_notification_sent_failed_description), pi);
        boolean vibrateAlways = true;
        boolean vibrateSilent = false;
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        boolean nowSilent = audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE;
        if (vibrateAlways || vibrateSilent && nowSilent) {
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        }
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_SENT_ID, notification);
    }
}
