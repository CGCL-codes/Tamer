package gov.nasa.gsfc.visbard.model;

import gov.nasa.gsfc.spdf.cdas.ViewDescription;
import gov.nasa.gsfc.visbard.gui.*;
import gov.nasa.gsfc.visbard.gui.resourcemanip.remote.OpenRemotePanel;
import gov.nasa.gsfc.visbard.model.threadtask.soap.CdaWebTask;
import gov.nasa.gsfc.visbard.model.threadtask.soap.SoapConnectionTask;
import gov.nasa.gsfc.visbard.model.threadtask.soap.SoapTasks;
import gov.nasa.gsfc.visbard.repository.category.*;
import gov.nasa.gsfc.visbard.util.VisbardException;
import gov.nasa.gsfc.visbard.vis.*;
import gov.nasa.gsfc.vspo.client.util.DateRange;
import gsfc.nssdc.cdf.CDF;
import gsfc.nssdc.cdf.CDFException;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class VisbardMain {

    static org.apache.log4j.Category sLogger = org.apache.log4j.Category.getInstance(VisbardMain.class.getName());

    private static Universe3D sUniverse = null;

    private static RangeModel sRangeModel = null;

    private static DatasetContainer sDatasetContainer = null;

    private static RangeControllerView sRangeControllerView = null;

    private static DefaultCategoryPool sCatPool = null;

    private static VisWindow sVisWindow = null;

    private static ModeManager sModeManager = null;

    private static CoordinateModeManager sCoordinateModeManager = null;

    private static UnitManager sUnitManager = null;

    private static SettingsManager sSettingsManager = null;

    private static SessionManager sSessionManager = null;

    private static File sDataDir = null;

    private static File sRemoteDataDir = null;

    private static File sSettingsDir = null;

    private static File sStartDir = null;

    private static File sTempDir = null;

    private static SplashWindow sSplashWindow;

    private static boolean isFullScreen = false;

    static {
        Log.init();
        sVisWindow = new VisWindow();
        sSplashWindow = new SplashWindow(sVisWindow);
        sSplashWindow.setVisible(true);
        try {
            String lookFeel = javax.swing.UIManager.getSystemLookAndFeelClassName();
            javax.swing.UIManager.setLookAndFeel(lookFeel);
        } catch (javax.swing.UnsupportedLookAndFeelException ulfe) {
            sLogger.error("VisWindow: could not change look and feel: " + "UnsupportedLookAndFeelException");
        } catch (java.lang.IllegalAccessException iae) {
            sLogger.error("VisWindow: could not change look and feel: " + "IllegalAccessException");
        } catch (java.lang.InstantiationException ie) {
            sLogger.error("VisWindow: could not change look and feel: " + "InstantiationException");
        } catch (java.lang.ClassNotFoundException cnfe) {
            sLogger.error("VisWindow: could not change look and feel: " + "ClassNotFoundException");
        }
        sTempDir = new File(System.getProperty("java.io.tmpdir") + File.separator + "visbard_temp");
        if (!sTempDir.isDirectory()) {
            if (!sTempDir.mkdir()) {
                sLogger.error("Could not create visbard temporary directory in " + sTempDir.toString());
            }
        }
        String startDir = System.getProperty("user.home");
        sStartDir = new File(startDir);
        String fullStartDir = null;
        try {
            fullStartDir = sStartDir.getCanonicalPath();
        } catch (IOException ioe) {
            sLogger.debug("Cannot resolve starting directory: " + ioe.getMessage());
            fullStartDir = null;
        }
        if (fullStartDir != null) startDir = fullStartDir;
        sDataDir = new File(startDir);
        sRemoteDataDir = new File(sTempDir.toString());
        sStartDir = new File(startDir);
        sLogger.debug("Default start directory:  " + startDir);
        sSettingsDir = new File(startDir + File.separatorChar + ".visbard");
        if (!sSettingsDir.isDirectory()) {
            sLogger.info("Default preferences directory does not exist; creating (" + sSettingsDir + ")");
            if (!sSettingsDir.mkdir()) {
                sLogger.error("Problem creating ViSBARD preferences directory");
            }
        }
        sSettingsManager = new SettingsManager();
        try {
            sSettingsManager.load();
        } catch (VisbardException e) {
            e.showErrorMessage();
        }
        sSessionManager = new SessionManager();
        sUniverse = new Universe3D();
        sSettingsManager.registerSettingsHolder(sUniverse);
        sCatPool = new DefaultCategoryPool();
        Category time = CategoryFactory.getInstance().createBaseCategory(CategoryType.TIME);
        Category location = CategoryFactory.getInstance().createBaseCategory(CategoryType.LOCATION);
        PseudoCategory pseudoLocation = CategoryFactory.getInstance().createPseudoCategory(location);
        sCatPool.addCategory(location);
        sCatPool.addCategory(pseudoLocation);
        sCatPool.addCategory(time);
        DefaultPropertyContainer cont = new DefaultPropertyContainer(false);
        cont.setPropertyValue(Property.sTime, time);
        cont.setPropertyValue(Property.sLocation, pseudoLocation);
        sSettingsManager.registerSettingsHolder(cont);
        sRangeModel = new DefaultRangeModel();
        sDatasetContainer = new DefaultDatasetContainer(sUniverse, sRangeModel, cont, sCatPool);
        sRangeControllerView = new RangeControllerView(sDatasetContainer);
        sRangeModel.addListener((PseudoLocation) pseudoLocation);
        sCoordinateModeManager = new CoordinateModeManager(sDatasetContainer);
        sCoordinateModeManager.addListener(sUniverse);
        sUnitManager = new UnitManager(sCoordinateModeManager);
        sVisWindow.initialize(sUniverse.getCanvas());
        sVisWindow.addPickListener(sUniverse);
        sSettingsManager.registerSettingsHolder(sVisWindow);
        sModeManager = new ModeManager();
        sModeManager.addListener(sVisWindow);
        sSettingsManager.registerSettingsHolder(sModeManager);
        System.loadLibrary("ovt_visbard-2.31");
        try {
            String cdfLib1 = System.getProperty("cdfLib1");
            if (cdfLib1 != null) {
                System.loadLibrary(cdfLib1);
            }
            String cdfLib2 = System.getProperty("cdfLib2");
            if (cdfLib2 != null) {
                System.loadLibrary(cdfLib2);
            }
        } catch (SecurityException e) {
            sLogger.warn("Security violation loading CDF library:" + e.getMessage());
        } catch (UnsatisfiedLinkError e) {
            sLogger.warn("Unable to load CDF library:" + e.getMessage());
        }
        ;
        try {
            sLogger.info("CDF Library version: " + CDF.getLibraryVersion());
        } catch (CDFException cdfe) {
            sLogger.warn("Problem getting CDF library version: " + cdfe.getMessage());
        }
        System.loadLibrary("CCMC2");
    }

    public static ArrowManager getArrowManager() {
        return sUniverse;
    }

    public static CategoryPool getCategoryPool() {
        return sCatPool;
    }

    public static Universe getUniverse() {
        return sUniverse;
    }

    public static DatasetContainer getDatasetContainer() {
        return sDatasetContainer;
    }

    public static RangeModel getRangeModel() {
        return sRangeModel;
    }

    public static RangeControllerView getRangeControllerView() {
        return sRangeControllerView;
    }

    public static VisWindow getVisWindow() {
        return sVisWindow;
    }

    public static CoordinateModeManager getCoordinateModeManager() {
        return sCoordinateModeManager;
    }

    public static ModeManager getModeManager() {
        return sModeManager;
    }

    public static UnitManager getUnitManager() {
        return sUnitManager;
    }

    public static SettingsManager getSettingsManager() {
        return sSettingsManager;
    }

    public static SessionManager getSessionManager() {
        return sSessionManager;
    }

    public static File getDataDir() {
        return sDataDir;
    }

    public static File getRemoteDataDir() {
        return sRemoteDataDir;
    }

    public static File getTempDir() {
        return sTempDir;
    }

    public static void setDataDir(String dir) {
        if ((new File(dir)).isDirectory()) sDataDir = new File(dir); else {
            sLogger.warn("Data directory is invalid.  Setting to user's home dir.");
            sDataDir = new File(System.getProperty("user.home"));
        }
    }

    public static void setRemoteDataDir(String dir) {
        if ((new File(dir)).isDirectory()) sRemoteDataDir = new File(dir); else {
            sLogger.warn("Local storage area for remote data directory is invalid.  " + "Setting to user's temp dir.");
            sRemoteDataDir = new File(System.getProperty("java.io.tmpdir"));
        }
    }

    public static File getSettingsDir() {
        return sSettingsDir;
    }

    public static void setFullScreen(boolean set) {
        if (set) {
            sVisWindow.changeViewModeToFullScreen();
            isFullScreen = true;
        } else {
            sVisWindow.changeViewModeToWindowed();
            isFullScreen = false;
        }
    }

    public static boolean isFullScreen() {
        return isFullScreen;
    }

    /**
     * Parse and use any arguments passed to the application
     * @param args
     */
    public static void handleApplicationArguments(String[] args) {
        ArrayList<String> startupDataUrlList = new ArrayList<String>();
        String cdawebProduct = null;
        String cdawebBeginTime = null;
        String cdawebEndTime = null;
        String lastArg = "__NOARG";
        for (int i = 0; i < args.length; i++) {
            if (lastArg.equals("-u")) {
                sLogger.info("URL of data to load: " + args[i]);
                startupDataUrlList.add(args[i]);
            } else if (lastArg.equals("-p")) {
                sLogger.info("Parsed CDAWeb product: " + args[i]);
                cdawebProduct = args[i];
            } else if (lastArg.equals("-b")) {
                sLogger.info("Parsed begin time of CDAWeb data to load: " + args[i]);
                cdawebBeginTime = args[i];
            } else if (lastArg.equals("-e")) {
                sLogger.info("Parsed end time of CDAWeb data to load: " + args[i]);
                cdawebEndTime = args[i];
            }
            lastArg = new String(args[i]);
        }
        try {
            if ((cdawebProduct != null) && (cdawebBeginTime != null) && (cdawebEndTime != null)) {
                Date startDate = null;
                Date endDate = null;
                DateRange dateRange = null;
                DateFormat formatterShort = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat formatterLong = new SimpleDateFormat("yyyy-MM-dd','HH:mm");
                try {
                    startDate = formatterShort.parse(cdawebBeginTime);
                    endDate = formatterShort.parse(cdawebEndTime);
                    dateRange = new DateRange(startDate, endDate);
                } catch (ParseException pe) {
                    try {
                        startDate = formatterLong.parse(cdawebBeginTime);
                        endDate = formatterLong.parse(cdawebEndTime);
                    } catch (ParseException pe2) {
                        throw new Exception("Error parsing CDAWeb date range on startup: " + pe2.getMessage());
                    }
                }
                CdaWebTask task = new CdaWebTask(SoapTasks.GET_VIEW_DESCRIPTIONS);
                task.go();
                if (task.isSuccess()) {
                    ViewDescription[] views = task.getMissionViews();
                    ViewDescription view = new ViewDescription();
                    for (int i = 0; i < views.length; i++) {
                        if ((views[i].id).equals("sp_phys")) {
                            view = views[i];
                        }
                    }
                    sLogger.info("Selected mission data view: " + view.id + " - " + view.title + "\n  EndpointAddress: " + view.endpointAddress + "\n  SubTitle: " + view.subtitle + "\n  Overview: " + view.overview + "\n  UnderConstruction: " + view.underConstruction + "\n  Notice: " + view.noticeUrl + "\n  Public Access: " + view.publicAccess + "\n");
                    task.setConnectionUrl(view.endpointAddress);
                } else {
                    throw new Exception("Couldn't connect to CDAWeb on startup");
                }
                task.newTask(SoapTasks.GET_DATASET_VARIABLES);
                task.setSelectedDataset(cdawebProduct);
                task.go();
                if (!task.isSuccess()) {
                    throw new Exception("Error retrieving CDAWeb dataset variables at startup");
                }
                task.newTask(SoapTasks.GET_DATA_URLS);
                task.setInfo(new String[] { "ALL" }, dateRange);
                task.go();
                String dataUrls[] = null;
                dataUrls = task.getDataUrls();
                if (task.isSuccess()) {
                    if (dataUrls != null) {
                        for (int i = 0; i < dataUrls.length; i++) startupDataUrlList.add(dataUrls[i]);
                    } else {
                        throw new Exception("Error retrieving CDAWeb data URLs at startup;  " + "data may not exist at specified time");
                    }
                } else {
                    if (task.getException() != null) sLogger.error("Error retrieving CDAWeb dataset variables at startup: " + task.getException().getMessage());
                    throw new Exception("Error retrieving CDAWeb data URLs at startup");
                }
            }
            if (startupDataUrlList.size() > 0) {
                SoapConnectionTask task = new SoapConnectionTask(SoapTasks.TRANSFER_FILES);
                String startupDataUrlStrings[] = new String[startupDataUrlList.size()];
                for (int i = 0; i < startupDataUrlList.size(); i++) startupDataUrlStrings[i] = startupDataUrlList.get(i);
                task.setDataUrls(startupDataUrlStrings);
                task.go();
                if (!task.isSuccess()) {
                    throw new Exception("Error while retrieving data URLs at startup");
                }
                File localFiles[] = task.getLocalFiles();
                if (localFiles == null) {
                    throw new Exception(OpenRemotePanel.ABORT_MSG);
                }
                boolean forceSingleFile = false;
                sLogger.info(OpenRemotePanel.ADDING_FILES_MSG);
                sVisWindow.getResourceToolkitDialog().getResourceToolkitPanel().addRemoteFiles(localFiles, forceSingleFile);
                sVisWindow.getResourceToolkitDialog().getResourceToolkitPanel().forceLoadNewFiles();
            }
        } catch (Exception exc) {
            sLogger.error(exc.getMessage());
            JOptionPane errorPane = new JOptionPane(exc.getMessage(), JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
            JDialog errorDialog = errorPane.createDialog(null, "Error at startup");
            errorDialog.show();
            errorDialog.dispose();
            return;
        }
    }

    public static void main(String[] args) {
        sSplashWindow.setVisible(false);
        sSplashWindow.dispose();
        sVisWindow.setVisible(true);
        sUniverse.makeLive();
        sUniverse.addArrow();
        sUniverse.addArrow();
        if (args != null) {
            handleApplicationArguments(args);
        }
    }
}
