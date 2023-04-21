package com.aol.omp.common;

import com.aol.omp.base.debug.Logger;
import com.aol.omp.base.interfaces.*;
import com.aol.omp.common.AudioPlayer;
import com.aol.omp.base.interfaces.IAudio;
import java.util.Vector;

public class Turtle extends Thread {

    private Logger logger = Logger.getInstatnce();

    public IOMPMain shell;

    public Navigate navigate = null;

    public Layout layout = null;

    public Page page = null;

    public Connect connect;

    public Text text = null;

    public Parse parse = null;

    public Scrollbar scrollbar = null;

    public Services services = null;

    public ContextManager ctxMgr = null;

    private Object tThreadLock = new Object();

    private boolean threadLocked = true;

    public static final boolean RMS_CACHING = false;

    public static boolean turtleShellInitialized = false;

    public long totalMemory;

    public long freeMemory;

    public boolean keepInMemory = false;

    public boolean resetFullscreenMode = false;

    public boolean cacheMgr = true;

    public boolean audioDelayThrobber = true;

    public boolean useClipForRegion = false;

    public String userAgent;

    public String shellVersionString;

    public int shellVersion;

    public String checkLatestURL;

    private int lowMemoryThreshold;

    private static String objectStartedName = null;

    private static boolean paused = false;

    private Vector scrollbarNames = null;

    private INetwork iNetwork = null;

    private IPersistentStore iPersistentStore = null;

    private IDisplay iDisplay = null;

    private IGraphics iGraphics = null;

    private INativeForm iNativeForm = null;

    private IFile iFile = null;

    private IAudio iAudio = null;

    private String[] provisionedProperties = { "MIDlet-Jar-URL", "GalleryAudioExt", "GalleryAudioMimeType", "GalleryImageExt", "OK_keycode", "LEFTSOFT_keycode", "RIGHTSOFT_keycode", "BACK_keycode", "NetworkCancel", "ForceRepaint", "ForceThreadYield", "ResetFullscreenMode", "LaunchBrowser", "ImageLoadBufferSize", "FullKeyboard", "MSISDN", "MIDlet-Icon", "MIDlet-1", "MIDlet-Permissions", "CacheMgr" };

    public static MemoryManager memMgr;

    public Turtle(IOMPMain shell) {
        super();
        this.shell = shell;
        start();
        memMgr = MemoryManager.getInstance();
        int maxImageLoadBufferSize = Integer.valueOf(shell.getApplicationProperty("ImageLoadBufferSize")).intValue();
        memMgr.createReuseBuffer(maxImageLoadBufferSize);
    }

    public void startApp() {
        synchronized (tThreadLock) {
            tThreadLock.notify();
            threadLocked = false;
        }
    }

    public void run() {
        while (true) {
            try {
                if (threadLocked) {
                    synchronized (tThreadLock) {
                        tThreadLock.wait();
                    }
                }
            } catch (InterruptedException ie) {
                logger.print(Logger.ERROR, "turtle::thread exception: " + ie.toString());
            }
            threadLocked = true;
            logger.print(Logger.SPECIAL, "Turtle::startApp called.");
            initializeShell();
            turtleShellInitialized = true;
            paused = false;
        }
    }

    private void initializeShell() {
        logger.print(Logger.INFO, "started app...\n");
        Runtime runtime = Runtime.getRuntime();
        totalMemory = runtime.totalMemory();
        freeMemory = runtime.freeMemory();
        logger.print(Logger.SPECIAL, "total mem : " + totalMemory);
        logger.print(Logger.SPECIAL, "free mem : " + freeMemory);
        logger.print(Logger.SPECIAL, "---- Provisioned Properties ----");
        for (int i = 0; i < provisionedProperties.length; i++) logger.print(Logger.SPECIAL, provisionedProperties[i] + ": " + shell.getApplicationProperty(provisionedProperties[i]));
        logger.print(Logger.SPECIAL, "--------------------------------");
        IFactory iFactory = shell.getFactory();
        iNetwork = (INetwork) iFactory.createObject(IFactory.INETWORK_CLASS_ID);
        iPersistentStore = (IPersistentStore) iFactory.createObject(IFactory.IPERSISTENTSTORE_CLASS_ID);
        iDisplay = (IDisplay) iFactory.createObject(IFactory.IDISPLAY_CLASS_ID);
        iGraphics = (IGraphics) iFactory.createObject(IFactory.IGRAPHICS_CLASS_ID);
        iNativeForm = (INativeForm) iFactory.createObject(IFactory.INATIVEFORM_CLASS_ID);
        iFile = (IFile) iFactory.createObject(IFactory.IFILE_CLASS_ID);
        iAudio = (IAudio) iFactory.createObject(IFactory.IAUDIO_CLASS_ID);
        int ok_kc = -16001;
        int back_kc = -8;
        int clear_kc = -8;
        int leftsoft_kc = -6;
        int rightsoft_kc = -7;
        boolean fullKeyboard = false;
        boolean forceRepaint = false;
        try {
            ok_kc = Integer.valueOf(shell.getApplicationProperty("OK_keycode")).intValue();
            back_kc = Integer.valueOf(shell.getApplicationProperty("BACK_keycode")).intValue();
            clear_kc = Integer.valueOf(shell.getApplicationProperty("CLR_keycode")).intValue();
            leftsoft_kc = Integer.valueOf(shell.getApplicationProperty("LEFTSOFT_keycode")).intValue();
            rightsoft_kc = Integer.valueOf(shell.getApplicationProperty("RIGHTSOFT_keycode")).intValue();
            fullKeyboard = shell.getApplicationProperty("FullKeyboard").equals("1") ? true : false;
            forceRepaint = shell.getApplicationProperty("ForceRepaint").equals("1") ? true : false;
            resetFullscreenMode = shell.getApplicationProperty("ResetFullscreenMode").equals("1") ? true : false;
            cacheMgr = shell.getApplicationProperty("CacheMgr").equals("1") ? true : false;
            audioDelayThrobber = "0".equals(shell.getApplicationProperty("AudioDelayThrobber")) ? false : true;
            useClipForRegion = shell.getApplicationProperty("UseClipForRegion").equals("1") ? true : false;
        } catch (Exception e) {
            logger.print(Logger.INFO, "Turtle::initializeShell:exception=" + e.toString());
        }
        text = new Text(this);
        connect = new Connect(this);
        page = new Page(this, ok_kc, back_kc, clear_kc, leftsoft_kc, rightsoft_kc, forceRepaint, fullKeyboard);
        layout = new Layout(this);
        services = new Services(this);
        parse = new Parse(this);
        navigate = new Navigate(this);
        scrollbar = new Scrollbar(this);
        ctxMgr = new ContextManager(this);
        page.init();
        layout.init();
        String configPropsFile = shell.getApplicationProperty("ConfigProperties");
        logger.print(Logger.INFO, "configPropsFile = " + configPropsFile);
        ConfigProperties configProperties = new ConfigProperties(configPropsFile, this);
        shellVersionString = shell.getApplicationProperty("ShellVersion");
        shellVersion = services.getVersionNum(shellVersionString);
        String startApplet = configProperties.getProperty("StartApplet");
        String appletNames = configProperties.getProperty("AppletList");
        checkLatestURL = configProperties.getProperty("CheckLatestURL");
        ctxMgr.init(appletNames);
        Shared.setAppletName(startApplet);
        connect.loadAppletContext(startApplet);
        this.lowMemoryThreshold = (int) (Long.parseLong((configProperties.getProperty("LowMemory")).substring(2), 16));
        this.keepInMemory = (this.freeMemory < this.lowMemoryThreshold) ? false : true;
        AudioPlayer aPlayer = AudioPlayer.getInstance();
        aPlayer.setShell(this);
        aPlayer.setUseDataBuffer("1".equals(shell.getApplicationProperty("UseAudioDataBuffer")));
        String imageName = configProperties.getProperty("DefaultSplashImage");
        page.setOpeningImage(imageName, connect.getImage(imageName));
        iDisplay.setCurrentPage();
        iDisplay.updateCanvas(forceRepaint);
        Shared.putScalarVariable("$Global::__LaunchBrowser", shell.getApplicationProperty("LaunchBrowser"));
        Shared.putScalarVariable("$Global::__MidletVersion", shell.getApplicationProperty("MIDlet-Version"));
        Shared.putScalarVariable("$Global::__shellVersion", shellVersionString);
        Shared.putScalarVariable("$Global::__GalleryImageExt", shell.getApplicationProperty("GalleryImageExt"));
        Shared.putScalarVariable("$Global::__ProvisioningURL", shell.getApplicationProperty("ProvisioningURL"));
        logger.print(Logger.SPECIAL, "display width = " + iDisplay.getWidth() + " ; height = " + iDisplay.getHeight());
        connect.start();
        services.init();
        ctxMgr.switchApplet(startApplet);
        freeMemory = runtime.freeMemory();
        logger.print(Logger.SPECIAL, "free mem[end initializeShell] : " + freeMemory);
    }

    public void launchVendorApp(String appName) {
        try {
            Class v = Class.forName(appName);
        } catch (Exception e) {
            logger.print(Logger.INFO, "Turtle::launchVendorApp:exception=" + e.toString());
        }
    }

    public static boolean paused() {
        return paused;
    }

    public void pauseApp() {
        logger.print(Logger.SPECIAL, "Turtle::pauseApp called.");
        paused = true;
    }

    public void destroyApp() {
        logger.print(Logger.SPECIAL, "Turtle::destroyApp called.");
        services.savePersistentVariables();
        turtleShellInitialized = false;
    }

    public void exit() {
        logger.print(Logger.INFO, "Turtle::exit() invoked");
        shell.exit();
    }

    public void toBrowser(String URL) {
        logger.print(Logger.INFO, "Turtle::toBrowser() invoked");
        services.savePersistentVariables();
        turtleShellInitialized = false;
        shell.toBrowser(URL);
    }

    public Text getText() {
        return text;
    }

    public boolean isResetFullscreenMode() {
        return resetFullscreenMode;
    }

    public boolean isAudioDelayThrobber() {
        return audioDelayThrobber;
    }

    public Connect getConnect() {
        return connect;
    }

    public Services getServices() {
        return services;
    }

    public Layout getLayout() {
        return layout;
    }

    public Scrollbar getScrollbar() {
        return scrollbar;
    }

    public Navigate getNavigate() {
        return navigate;
    }

    public INetwork getINetwork() {
        return iNetwork;
    }

    public IPersistentStore getIPersistentStore() {
        return iPersistentStore;
    }

    public IDisplay getIDisplay() {
        return iDisplay;
    }

    public IGraphics getIGraphics() {
        return iGraphics;
    }

    public INativeForm getINativeForm() {
        return iNativeForm;
    }

    public IFile getIFile() {
        return iFile;
    }

    public IAudio getIAudio() {
        return iAudio;
    }
}
