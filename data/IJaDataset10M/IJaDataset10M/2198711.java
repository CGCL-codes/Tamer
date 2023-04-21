package com.ek.mitapp;

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import com.ek.mitapp.event.MitAppListener;
import com.ek.mitapp.ui.MainFrame;

/**
 * Main application class.
 * <br>
 * Id: $Id: $
 *
 * @author dhirwinjr
 */
public class MitigationApp implements Application {

    /**
	 * Define the root logger.
	 */
    private static Logger logger = Logger.getLogger(MitigationApp.class.getName());

    private MainFrame mainView;

    /**
	 * Application settings.
	 */
    private static AppSettings appSettings;

    /**
	 * The application start time.
	 */
    private long startTime;

    /**
	 * Keep a list of the quit listeners.
	 */
    private ArrayList<MitAppListener> mitAppListeners;

    /**
	 * Default no-argument constructor.
	 */
    public MitigationApp() {
        super();
        init();
    }

    /**
	 * Initialize the mitigation application.
	 */
    private final void init() {
        mitAppListeners = new ArrayList<MitAppListener>();
        appSettings = AppSettings.getDefaultSettings();
    }

    /**
	 * Start the application.
	 */
    private void startApp() {
        startTime = System.currentTimeMillis();
        logger.info("Mitigation prioritization application");
        logger.info("Developed in part by Edwards and Kelcey (http://www.ekcorp.com)");
        logger.info("Version: " + AppSettings.getMajorVersion() + "." + AppSettings.getMinorVersion());
        logger.info("Build Id: " + AppSettings.getBuildId());
        try {
            mainView = MainFrame.createAndShowGUI(this, false);
        } catch (Exception e) {
            logger.error("Error creating application: " + e.getMessage());
        }
    }

    /**
	 * Terminate the application.
	 * 
	 * @return
	 */
    public boolean quit() {
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit the application?", "Mitigation Prioritization", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            for (Iterator it = mitAppListeners.iterator(); it.hasNext(); ) {
                ((MitAppListener) it.next()).quit();
            }
            mitAppListeners.clear();
            System.exit(0);
            return true;
        }
        return false;
    }

    /**
	 * The application initialization is complete.
	 */
    public void initializationComplete() {
        for (Iterator it = mitAppListeners.iterator(); it.hasNext(); ) {
            ((MitAppListener) it.next()).initializationComplete();
        }
    }

    /**
	 * 
	 * @param listener
	 */
    public void addMitAppListener(MitAppListener listener) {
        if (listener == null) throw new IllegalArgumentException("Listener cannot be null");
        mitAppListeners.add(listener);
    }

    /**
	 * 
	 * @param listener
	 */
    public void removeMitAppListener(MitAppListener listener) {
        if (listener != null) {
            mitAppListeners.remove(listener);
        }
    }

    /**
	 * Main application starting point.
	 * 
	 * @param args Command line arguments
	 */
    public static void main(String[] args) {
        new MitigationApp().startApp();
    }
}
