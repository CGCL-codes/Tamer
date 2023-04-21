package fr.jbrunet.win.ndriveconnector;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import fr.jbrunet.logger.JLog;
import fr.jbrunet.win.gui.ndriveconnector.windows.MainWindow;
import fr.jbrunet.win.ndriveconnector.config.Config;
import fr.jbrunet.win.ndriveconnector.utils.Connection;
import fr.jbrunet.win.ndriveconnector.utils.Constants;

/**
 * @author Julien
 *
 */
public class ConnectionTry extends Thread {

    private static final Logger log = JLog.getLogger(ConnectionTry.class);

    private static Config config = null;

    private static Shell shell;

    private static ProgressBar progressBar;

    private int previousStatus;

    public ConnectionTry(Shell s, ProgressBar p) throws Exception {
        ConnectionTry.shell = s;
        ConnectionTry.progressBar = p;
        config = Config.getInstance();
    }

    public void run() {
        tryToConnect();
    }

    /**
	 * Si le serveur est disponible, se connecter au serveur
	 */
    public void tryToConnect() {
        log.debug("ConnectionTry starts...");
        previousStatus = Connection.getInstance().getStatus();
        switch(previousStatus) {
            case Constants.STATUS_CONNECTED:
                log.debug("Etat pr�c�dent : connect�");
                break;
            case Constants.STATUS_NOT_CONNECTED:
                log.debug("Etat pr�c�dent : d�connect�");
                break;
            case Constants.STATUS_CONNECTION_IN_PROGRESS:
                log.debug("Etat pr�c�dent : connexion en cours");
                break;
            default:
                log.debug("Etat pr�c�dent : inconnu");
        }
        shell.getDisplay().syncExec(new Runnable() {

            public void run() {
                try {
                    MainWindow window;
                    window = MainWindow.getInstance();
                    Button connectionBtn = window.getConnectionBtn();
                    connectionBtn.setEnabled(false);
                    connectionBtn.setGrayed(true);
                    connectionBtn.update();
                } catch (Exception e) {
                    log.fatal(e);
                }
            }
        });
        boolean connected = false;
        shell.getDisplay().syncExec(new Runnable() {

            public void run() {
                try {
                    MainWindow window = MainWindow.getInstance();
                    window.setConnectionTryNumber(window.getConnectionTryNumber() + 1);
                    window.setConnectionStatus(Constants.STATUS_CONNECTION_IN_PROGRESS);
                } catch (Exception e) {
                    log.fatal(e);
                }
            }
        });
        int counter = 0;
        while (!connected && counter < config.getCheckPings()) {
            counter++;
            shell.getDisplay().syncExec(new Runnable() {

                public void run() {
                    if (progressBar.isDisposed()) {
                        return;
                    }
                    if (progressBar.getSelection() < config.getCheckPings()) {
                        progressBar.setSelection(progressBar.getSelection() + 1);
                    }
                }
            });
            try {
                connected = InetAddress.getByName(config.getNasIp()).isReachable(config.getPingTimeout());
                byte b[] = { 127, 0, 0, 1 };
                InetAddress inetAddress = InetAddress.getByAddress(b);
                NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);
                Enumeration e = networkInterface.getNetworkInterfaces();
                while (e.hasMoreElements()) {
                    NetworkInterface ni = (NetworkInterface) e.nextElement();
                    String name = ni.getDisplayName();
                    if (!ni.isLoopback() && !ni.isPointToPoint() && !ni.isVirtual()) {
                        if (ni.isUp()) {
                            System.out.println(name + " UP");
                        } else {
                            System.out.println(name + " DOWN");
                        }
                    }
                }
                if (connected) {
                    log.debug("Tentative de connexion " + counter + " : Le nas est joignable!");
                } else {
                    log.debug("Tentative de connexion " + counter + " : Le nas n'est pas joignable!");
                    Thread.sleep(config.getPingInterval());
                }
            } catch (UnknownHostException e) {
                log.error("Unknown Host Exception!");
            } catch (IOException e) {
                log.error("IO Error", e);
            } catch (InterruptedException e) {
                log.fatal("Thread Sleep error", e);
            }
        }
        if (connected) {
            log.info("Le serveur est joignable");
            shell.getDisplay().syncExec(new Runnable() {

                public void run() {
                    try {
                        MainWindow window = MainWindow.getInstance();
                        window.setConnectionStatus(Constants.STATUS_CONNECTED);
                    } catch (Exception e) {
                        log.fatal(e);
                    }
                }
            });
        } else {
            log.info("Le serveur n'est pas joignable");
            shell.getDisplay().syncExec(new Runnable() {

                public void run() {
                    try {
                        MainWindow window = MainWindow.getInstance();
                        window.setConnectionStatus(Constants.STATUS_NOT_CONNECTED);
                    } catch (Exception e) {
                        log.fatal(e);
                    }
                }
            });
        }
        if (previousStatus != Constants.STATUS_CONNECTED && Connection.getInstance().getStatus() == Constants.STATUS_CONNECTED) {
            try {
                DriveBinder.getInstance().connectDrives();
            } catch (Exception e) {
                log.error("Erreur lors du lancement des commandes de connexion aux lecteurs r�seau", e);
            }
        }
        shell.getDisplay().syncExec(new Runnable() {

            public void run() {
                try {
                    MainWindow window = MainWindow.getInstance();
                    window.setLastConnectionAttemp(System.currentTimeMillis());
                    Button connectionBtn = window.getConnectionBtn();
                    connectionBtn.setEnabled(true);
                    connectionBtn.setGrayed(false);
                    connectionBtn.update();
                } catch (Exception e) {
                    log.fatal(e);
                }
            }
        });
        log.debug("End of ConnectionTry.");
    }
}
