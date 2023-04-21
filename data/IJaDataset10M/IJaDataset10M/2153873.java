package net.sf.repairslab;

import java.sql.Connection;
import java.util.Locale;
import javax.swing.UIManager;
import net.sf.repairslab.control.CheckUpdates;
import net.sf.repairslab.control.CommonMetodBin;
import net.sf.repairslab.model.BinRelease;
import net.sf.repairslab.ui.VcMainFrame;
import net.sf.repairslab.ui.VcSplashScreen;
import net.sf.repairslab.ui.installwizard.VcDlgMetadataSetting;
import net.sf.repairslab.util.ui.WindowUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class StartApp {

    private static Logger logger = Logger.getLogger(StartApp.class.getName());

    /**
	 * @author ferraf01
	 * 
	 * Main di F2Edit.
	 * Questa classe setta il LookAndFeel utilizzando il parametro LOOK
	 * della classe singleton EnvProperties e instanzia il frame F2Edit.java
	 *  
	 * @param args
	 */
    public static void main(String[] args) {
        initLog4j();
        logger.debug("Current working directory + : " + System.getProperty("user.dir"));
        try {
            logger.debug("Loading look&feel");
            UIManager.setLookAndFeel(EnvProperties.getInstance().getProperty(EnvProperties.LOOK));
        } catch (Exception e) {
            logger.error("Exception in Loading look&feel\n" + e + "\n");
        }
        showSplash();
        splash.setStatus("Starting...", 10);
        splash.setStatus("Get release...", 20);
        VersionReader versionReader = new VersionReader();
        BinRelease binRelease = new BinRelease(versionReader.getAppname(), versionReader.getVersion(), versionReader.getRelease());
        CommonMetodBin.getInstance().setCurrentRelease(binRelease);
        splash.setRelease(CommonMetodBin.getInstance().getCurrentRelease().toString());
        splash.setStatus("Setting locale", 30);
        String selVal = EnvProperties.getInstance().getProperty(EnvProperties.LOCALE);
        Locale.setDefault(new Locale(selVal.split("-")[0], selVal.split("-")[1]));
        splash.setStatus("Loading App...", 50);
        VcMainFrame frame = new VcMainFrame();
        logger.debug("Application loaded...");
        splash.setStatus("Testing Connection & metadata version...", 80);
        if (!testConn() || !currentMetadataVersion()) {
            VcDlgMetadataSetting dialog = new VcDlgMetadataSetting(frame);
            WindowUtil.centerWindow(dialog);
            dialog.setVisible(true);
        }
        CommonMetodBin.getInstance().setMainFrame(frame);
        splash.setStatus("Started...", 100);
        frame.setVisible(true);
        hideSplash();
        startChekForUpdate();
    }

    private static void initLog4j() {
        String configFile = EnvProperties.getInstance().getProperty("Log4jConfig");
        String logFile = EnvProperties.getInstance().getProperty("Log4jLogFile");
        try {
            System.setProperty("Log4jLogFile", logFile);
            System.out.println("LogFile di Log4j: " + logFile);
        } catch (Exception ex) {
            System.out.println("Eccezione : " + ex.getMessage());
            return;
        }
        if (configFile != null) {
            PropertyConfigurator.configure(configFile);
            System.out.println("Configurazione Log4j: " + configFile);
        }
        logger.info("File di log inizializzato con successo....");
    }

    private static boolean testConn() {
        Connection con = CommonMetodBin.getConn();
        if (con == null) {
            return false;
        } else {
            CommonMetodBin.closeConn(con);
            return true;
        }
    }

    private static boolean currentMetadataVersion() {
        return CommonMetodBin.getInstance().getCurrentRelease().getVersion().equals(CommonMetodBin.getInstance().getInstalledMetadata());
    }

    private static VcSplashScreen splash;

    public static VcSplashScreen showSplash() {
        if (splash != null) {
            if (splash.isShowing()) {
                return splash;
            } else {
                splash = null;
            }
        }
        splash = new VcSplashScreen();
        WindowUtil.centerWindow(splash);
        splash.setVisible(true);
        return splash;
    }

    public static void hideSplash() {
        if (splash != null) {
            splash.dispose();
            splash = null;
        }
    }

    /**
	 * Thread to search new update
	 * @author Fabrizio Ferraiuolo 02/feb/2011 12.46.12 
	 */
    public static void startChekForUpdate() {
        logger.debug("Start  ChekForUpdate...");
        Thread t = new Thread() {

            @Override
            public void run() {
                CheckUpdates.check();
                logger.debug("End  ChekForUpdate...");
            }
        };
        t.run();
    }
}
