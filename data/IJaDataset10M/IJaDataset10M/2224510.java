package securus.client.system;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import org.apache.log4j.Priority;
import securus.client.MainApp;

/**
 *
 * @author e.dovgopoliy
 */
public class Logger {

    private final org.apache.log4j.Logger log;

    public Logger(org.apache.log4j.Logger log) {
        this.log = log;
    }

    public static Logger getLogger(Class clazz) {
        return new Logger(org.apache.log4j.Logger.getLogger(clazz));
    }

    public void error(java.lang.Object message) {
        log.error(message);
        appendLogOnPane(message, null);
        showErrorMessage(message, null);
    }

    public void error(java.lang.Object message, java.lang.Throwable t) {
        log.error(message, t);
        appendLogOnPane(message, t);
        showErrorMessage(message, t);
    }

    public void errorResource(String resourceKey, java.lang.Throwable t) {
        info(MainApp.getString(resourceKey), t);
    }

    public void info(java.lang.Object message) {
        log.info(message);
    }

    public void info(java.lang.Object message, java.lang.Throwable t) {
        log.info(message, t);
    }

    public void info(java.lang.Object message, boolean showUser) {
        log.info(message);
        if (showUser) {
            appendLogOnPane(message, null);
        }
    }

    public void info(java.lang.Object message, java.lang.Throwable t, boolean showUser) {
        log.info(message, t);
        if (showUser) {
            appendLogOnPane(message, t);
        }
    }

    public void infoFormat(String message, boolean showUser, Object... args) {
        info(String.format(message, args), showUser);
    }

    public void infoResource(String resourceKey, boolean showUser) {
        info(MainApp.getString(resourceKey), showUser);
    }

    public void infoFormatResource(String resourceKeyOfFormatString, boolean showUser, Object... args) {
        info(String.format(MainApp.getString(resourceKeyOfFormatString), args), showUser);
    }

    public void log(Priority priority, java.lang.Object message) {
        log.log(priority, message);
    }

    private void appendLogOnPane(java.lang.Object message, java.lang.Throwable t) {
        if (MainApp.getFrmMain() != null) {
            if (message != null) {
                MainApp.getFrmMain().appendLog((new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format((new Date()))) + " " + message);
                if (t != null) {
                    MainApp.getFrmMain().appendLog(t.getMessage());
                    for (StackTraceElement item : t.getStackTrace()) {
                        MainApp.getFrmMain().appendLog(item.toString());
                    }
                }
            }
        }
    }

    private void showErrorMessage(java.lang.Object message, java.lang.Throwable t) {
        if (message != null) {
            System.out.println("-------------- ERROR " + message);
            JOptionPane.showMessageDialog(MainApp.getFrmMain(), message, "SecurusVault Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
