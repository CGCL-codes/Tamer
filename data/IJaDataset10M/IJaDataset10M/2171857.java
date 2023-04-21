package securus.client.thread;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import securus.client.MainApp;
import securus.client.utils.StatusBarMessage;
import securus.client.utils.TableUtils;

/**
 *
 * @author e.dovgopoliy
 */
public class ProgressBackupMonitor implements Runnable {

    public AtomicBoolean shutdown = new AtomicBoolean(false);

    private Thread thread;

    private StatusBarMessage securusStatus;

    private Long startSize = MainApp.getCommandFactory().getBackupedFileSize();

    private Long lastTime = (new Date()).getTime();

    private securus.client.system.Logger LOG = securus.client.system.Logger.getLogger(ProgressBackupMonitor.class);

    private Long remainSize;

    private String remain = "";

    public ProgressBackupMonitor(long sessionSize) {
        securusStatus = MainApp.getNewStatusBarMessage();
        remainSize = sessionSize;
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        Long lastSize = startSize;
        while (!shutdown.get()) {
            try {
                Long curSize = MainApp.getCommandFactory().getBackupedFileSize();
                if (!curSize.equals(lastSize)) {
                    Long goesSec = ((new Date()).getTime() - lastTime);
                    Double n = ((curSize.doubleValue() - startSize.doubleValue()) / (goesSec.doubleValue() / 1000));
                    remainSize = remainSize - (curSize - lastSize);
                    lastSize = curSize;
                    if (n != 0) {
                        securusStatus.setText(String.format(MainApp.getString("ProgressBackupMonitor.UPLOADING_S"), TableUtils.getFileSize(n)) + " " + remain);
                    }
                    MainApp.setBackupSpeed(n);
                } else if (lastSize.equals(startSize)) {
                    securusStatus.setText(MainApp.getString("ProgressBackupMonitor.UPLOADING"));
                }
                if (MainApp.getBackupSpeed() > 0) {
                    long secs = (long) (remainSize.doubleValue() / MainApp.getBackupSpeed() + 0.5f);
                    remain = String.format(MainApp.getString("ProgressBackupMonitor.ESTIMATED"), TableUtils.getPeriodAsString(secs), TableUtils.getFileSize(remainSize));
                }
                Thread.sleep(500);
            } catch (Exception ex) {
                LOG.info(MainApp.getString("ProgressBackupMonitor.errorInProgressBackupMonitor") + ex);
            }
        }
    }

    public void stop() {
        shutdown.set(true);
        securusStatus.remove();
        LOG.info(MainApp.getString("ProgressBackupMonitor.progressBackupMonitorStopped"));
    }
}
