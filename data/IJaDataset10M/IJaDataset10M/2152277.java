package org.rjam.gui.admin;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import org.rjam.gui.AnalysisApplicatioin;
import org.rjam.gui.UncaughtExceptionDialog;
import org.rjam.gui.base.Constants;
import org.rjam.gui.base.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class JamSystemTray implements Runnable {

    public static final String PROP_FILE_NAME = "AlertFile";

    private static Logger logger = Logger.getLogger(JamSystemTray.class);

    private Thread thread;

    private boolean running = false;

    private int interval = 1000 * 60;

    private TrayIcon trayIcon;

    private AnalysisApplicatioin analysis;

    public JamSystemTray(final AnalysisApplicatioin analysis) {
        this.analysis = analysis;
        URL url = getClass().getResource(Constants.IMAGE_TRAY);
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = null;
            if (url == null) {
                image = Toolkit.getDefaultToolkit().getImage("tray.gif");
            } else {
                image = Toolkit.getDefaultToolkit().createImage(url);
            }
            ActionListener exitListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
            };
            PopupMenu popup = new PopupMenu();
            MenuItem item = new MenuItem("Exit");
            item.addActionListener(exitListener);
            popup.add(item);
            item = new MenuItem("Open Console");
            item.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    JamSystemTray.this.analysis.toFront();
                    JamSystemTray.this.analysis.setVisible(true);
                    JamSystemTray.this.analysis.toFront();
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            JamSystemTray.this.analysis.toFront();
                        }
                    });
                }
            });
            popup.add(item);
            trayIcon = new TrayIcon(image, analysis.getTitle(), popup);
            ActionListener actionListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    analysis.setVisible(true);
                }
            };
            trayIcon.setImageAutoSize(true);
            trayIcon.setActionCommand("LeftClick");
            trayIcon.addActionListener(actionListener);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
        } else {
            System.err.println("System tray is currently not supported.");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            LookAndFeelInfo[] list = UIManager.getInstalledLookAndFeels();
            for (LookAndFeelInfo lf : list) {
                if (lf.getName().equals("Nimbus")) {
                    UIManager.setLookAndFeel(lf.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }
        logger.debug("Args = " + args.length);
        if (args.length % 2 == 0) {
            for (int idx = 0; idx < args.length; idx++) {
                logger.debug("Arg [" + args[idx] + "] = " + args[idx + 1]);
                System.setProperty(args[idx++], args[idx]);
            }
        }
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionDialog());
        AnalysisApplicatioin ana = new AnalysisApplicatioin("Application Monitor", false);
        JamSystemTray main = new JamSystemTray(ana);
        main.start();
        String fileName = System.getProperty(PROP_FILE_NAME);
        if (fileName != null) {
            File file = new File(fileName);
            if (file.exists() && file.canRead() && file.isFile()) {
                ana.loadAlerts(file, true);
            }
        }
    }

    public void start() {
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.setName("Application Monitor");
        thread.start();
    }

    public void stop() {
        running = false;
    }

    public void run() {
        running = true;
        while (running) {
            try {
                Thread.sleep(getInterval());
            } catch (InterruptedException e) {
            }
        }
    }

    public void showStatus(String msg) {
        trayIcon.displayMessage("Application Monitor", msg, TrayIcon.MessageType.INFO);
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public boolean isRunning() {
        return running;
    }
}
