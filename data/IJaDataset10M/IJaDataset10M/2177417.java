package org.makados.ims;

import org.makados.ims.data.DBHandler;
import org.makados.ims.data.ServerSynchronizer;
import org.makados.ims.data.SettingsConstants;
import org.makados.ims.parsers.Parser;
import org.makados.ims.gui.SettingsDialog;
import org.makados.ims.gui.firstlaunch.FirstLaunchWizard;
import org.makados.ims.images.ImsImages;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author makados
 */
public class IMSClient {

    private DBHandler db;

    private Properties props;

    private Parser parser;

    private ServerSynchronizer sync;

    private Pinger pinger;

    TrayIcon trayIcon;

    private static IMSClient instance = new IMSClient();

    private IMSClient() {
        try {
            db = new DBHandler();
            props = new Properties();
            props.load(new FileInputStream(new File("ims-client.properties")));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pinger = new Pinger();
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                close();
            }
        });
    }

    public void start() {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            ImageIcon ii = new ImageIcon(ImsImages.class.getResource(ImsImages.ARROW_FINISH.substring(ImsImages.ARROW_FINISH.lastIndexOf("/") + 1)));
            Image image = ii.getImage();
            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Exit");
            MenuItem settingsWizard = new MenuItem("Setting Wizard");
            ActionListener exitListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
            };
            settingsWizard.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    firstLaunch();
                }
            });
            defaultItem.addActionListener(exitListener);
            popup.add(settingsWizard);
            popup.add(defaultItem);
            trayIcon = new TrayIcon(image, "Tray Demo", popup);
            ActionListener actionListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    trayIcon.displayMessage("Action Event", "An Action Event Has Been Performed!", TrayIcon.MessageType.INFO);
                }
            };
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(actionListener);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
        }
        if (db.isFirstLaunch()) {
            firstLaunch();
        } else {
            trayIcon.displayMessage("Finished downloading", "Your Java application has finished downloading", TrayIcon.MessageType.INFO);
        }
    }

    private void firstLaunch() {
        FirstLaunchWizard pw = new FirstLaunchWizard();
        int retcode = pw.showProcedureWizard();
    }

    public static IMSClient getInstance() {
        return instance;
    }

    public void setProperties(Properties props) throws SQLException {
        db.storeProperties(props);
        this.props = db.getProperties();
    }

    public DBHandler getDbHandler() {
        return db;
    }

    public ServerSynchronizer getSynchronizer() {
        return sync;
    }

    public Properties getProperties() {
        return props;
    }

    public Parser getParser() {
        return parser;
    }

    public void fullSynchronization() {
    }

    public void manualSynchronization() {
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        IMSClient.getInstance().start();
    }

    public void close() {
        try {
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void restartParser() {
        try {
            props = db.getProperties();
            sync = new ServerSynchronizer();
            parser.setProps(props);
            parser.setSync(sync);
            parser.start();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopParser() {
        if (parser != null) {
            parser.stop();
        }
    }

    public void stop() {
    }

    public void disconnect() {
    }
}
