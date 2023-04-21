package org.xebra.client;

import java.lang.reflect.Method;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.xebra.client.com.LaunchCommunicationUtil;
import org.xebra.client.com.XebraConnection;
import org.xebra.client.com.XebraConnectionFactory;
import org.xebra.client.events.EventDispatcher;
import org.xebra.client.gui.ApplicationFrame;
import org.xebra.client.gui.ErrorDialog;
import org.xebra.client.gui.LoadWindow;
import org.xebra.client.gui.MacOSXIntegration;
import org.xebra.client.gui.StudyBrowser;
import org.xebra.client.gui.ErrorDialog.ErrorCode;
import org.xebra.client.util.ApplicationProperties;
import org.xebra.client.util.XebraConfig;
import org.xebra.client.util.ApplicationProperties.XebraMode;

/**
 * Contains xebra's main method.
 * @author kenny
 *
 */
public abstract class HxDIVMain {

    public static final String ALLOWABLE_ATTEMPTS = "allowableAttempts";

    public static final String ANONYMIZE = "anonymize";

    public static final String APPLICATION_HEIGHT = "applicationHeight";

    public static final String APPLICATION_WIDTH = "applicationWidth";

    public static final String DATA_BUFFER_SIZE = "dataBufferSize";

    public static final String DEFAULT_ANNOT_COLOR = "defaultAnnotationColor";

    public static final String FONT = "font";

    public static final String JPEG_2000 = "jpeg2000";

    public static final String KOSD_REQ_URL = "kosdRequestURL";

    public static final String MODALITY_CR = "modalityCR";

    public static final String MODALITY_CT = "modalityCT";

    public static final String MODALITY_DR = "modalityDR";

    public static final String MODALITY_DS = "modalityDS";

    public static final String MODALITY_MG = "modalityMG";

    public static final String MODALITY_MR = "modalityMR";

    public static final String MODALITY_NM = "modalityNM";

    public static final String MODALITY_PT = "modalityPT";

    public static final String MODALITY_RF = "modalityRF";

    public static final String MODALITY_RG = "modalityRG";

    public static final String MODALITY_SC = "modalitySC";

    public static final String MODALITY_US = "modalityUS";

    public static final String MODALITY_XA = "modalityXA";

    public static final String MODALITY_UN = "modalityUN";

    public static final String SIMULTANEOUS_LOADS = "simultaneousLoad";

    public static final String TITLE = "title";

    public static final String TOTAL_IMG_CACHE = "totalImageCache";

    public static final String STUDY_UID = "studyUID";

    public static final String CODEBASE = "codebase";

    public static final String TOKEN = "token";

    public static final String MODE = "mode";

    public static final String DEBUG = "debug";

    /**
	 * Should be invoked by Java WebStart. Starts the GUI in the Event Dispatcher Thread. 
	 * 
	 * @param args The command line arguments.
	 */
    public static void main(final String[] args) {
        System.out.println("Starting the xebra client");
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                HxDIVMain.launch(args);
            }
        });
    }

    /**
	 * Takes arguments of the form name=value.
	 * @param args
	 */
    public static void launch(String[] args) {
        System.out.println("Launching the client");
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            public void uncaughtException(Thread t, Throwable e) {
                new ErrorDialog(ErrorCode.UNKNOWN_ERROR, true, e);
            }
        });
        try {
            if (System.getProperty("os.name").indexOf("Linux") == -1 || System.getProperty("java.version").startsWith("1.6")) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (args.length == 0 || extractParameter(STUDY_UID, args) == null) {
            new ErrorDialog(ErrorCode.MISSING_ARGUMENTS, true, null);
        }
        ApplicationProperties ap = ApplicationProperties.getSingletonInstance();
        String studyUid = extractParameter(STUDY_UID, args);
        String codebase = extractParameter(CODEBASE, args);
        String token = extractParameter(TOKEN, args);
        System.out.println("StudyUID: " + studyUid);
        System.out.println("Codebase: " + codebase);
        System.out.println("Token: " + token);
        if (!codebase.endsWith("/")) {
            codebase += "/";
        }
        int port = ap.getLocalPort();
        if (LaunchCommunicationUtil.ping(port)) {
            LaunchCommunicationUtil.sendInfo(port, studyUid, codebase, token);
            System.exit(0);
        }
        XebraMode mode;
        String modeStr = extractParameter(MODE, args);
        if (modeStr == null) {
            mode = XebraMode.COMPRESSED;
        } else {
            try {
                mode = XebraMode.valueOf(modeStr);
            } catch (IllegalArgumentException e) {
                mode = XebraMode.COMPRESSED;
            }
        }
        ap.setMode(mode);
        ap.setAllowableNumberOfReloadAttempts(extractParameter(ALLOWABLE_ATTEMPTS, args));
        ap.setAnonymized(extractParameter(ANONYMIZE, args));
        ap.setApplicationSize(extractParameter(APPLICATION_HEIGHT, args), extractParameter(APPLICATION_WIDTH, args));
        ap.setDefaultDataBuffer(extractParameter(DATA_BUFFER_SIZE, args));
        ap.setAnnotationColor(extractParameter(DEFAULT_ANNOT_COLOR, args));
        ap.setFont(extractParameter(FONT, args));
        ap.setUseJpeg2000(extractParameter(JPEG_2000, args));
        ap.setSimultaneousDownloads(extractParameter(SIMULTANEOUS_LOADS, args));
        ap.setTitle(extractParameter(TITLE, args));
        ap.setTotalAllowableBytesInCache(extractParameter(TOTAL_IMG_CACHE, args));
        ap.setDebug(extractParameter(DEBUG, args));
        if (System.getProperty("os.name").indexOf("Mac") != -1) {
            ap.setIsMac(true);
            try {
                Class<?> macAppClass = Class.forName("com.apple.eawt.Application");
                Method addListener = macAppClass.getMethod("addApplicationListener", new Class[] { Class.forName("com.apple.eawt.ApplicationListener") });
                Method removePrefs = macAppClass.getMethod("removePreferencesMenuItem", new Class[] {});
                Object myApp = macAppClass.newInstance();
                addListener.invoke(myApp, new Object[] { new MacOSXIntegration() });
                removePrefs.invoke(myApp, new Object[] {});
            } catch (Exception e) {
                System.err.println("MacOS integration setup failed:");
                e.printStackTrace(System.err);
            }
            ImageIO.scanForPlugins();
        }
        try {
            Class<?> connectionFactoryClass = Class.forName(XebraConfig.getString("xebra.config.connectionFactoryClass"));
            Object factory = connectionFactoryClass.newInstance();
            XebraConnection.setXebraConnectionFactory((XebraConnectionFactory) factory);
        } catch (Throwable t) {
            System.err.println("Couldn't get xebra connection factory.");
            t.printStackTrace(System.err);
        }
        EventDispatcher.getSingletonInstance().reset();
        LoadWindow loader = new LoadWindow();
        loader.setVisible(true);
        ApplicationFrame browser = null;
        if (studyUid != null && studyUid.length() > 0) {
            browser = new StudyBrowser(studyUid, codebase, token);
        } else {
            new ErrorDialog(ErrorDialog.ErrorCode.NO_STUDY_REQUESTED, true, null);
        }
        loader.dispose();
        browser.setVisible(true);
    }

    /**
	 * Extracts the parameter <code>name</code> from <code>args</code>: that is, it finds the
	 * argument beginning with <code>name</code>= and returns the part after the =. If no such
	 * argument exists, returns null.
	 * @param name   the parameter name
	 * @param args   the arguments passed to the application
	 * @return       the parameter value
	 */
    private static String extractParameter(String name, String[] args) {
        String val = null;
        for (int i = 0; i < args.length; i++) {
            String[] pair = args[i].split("=");
            if (pair.length == 2 && pair[0].equalsIgnoreCase(name)) {
                val = pair[1];
                break;
            }
        }
        return val;
    }
}
