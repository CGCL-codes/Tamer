package org.homeunix.thecave.buddi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import net.java.dev.SwingWorker;
import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.impl.DocumentImpl;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.BuddiPluginFactory;
import org.homeunix.thecave.buddi.plugin.api.BuddiRunnablePlugin;
import org.homeunix.thecave.buddi.plugin.api.exception.DataModelProblemException;
import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.BuddiCryptoFactory;
import org.homeunix.thecave.buddi.util.FileFunctions;
import org.homeunix.thecave.buddi.util.OperationCancelledException;
import org.homeunix.thecave.buddi.view.MainFrame;
import org.homeunix.thecave.buddi.view.dialogs.BuddiPasswordDialog;
import org.homeunix.thecave.buddi.view.menu.bars.FramelessMenuBar;
import org.homeunix.thecave.buddi.view.menu.items.EditPreferences;
import org.homeunix.thecave.buddi.view.menu.items.FileOpen;
import org.homeunix.thecave.buddi.view.menu.items.FileQuit;
import org.homeunix.thecave.buddi.view.menu.items.HelpAbout;
import ca.digitalcave.moss.application.document.exception.DocumentLoadException;
import ca.digitalcave.moss.application.document.exception.DocumentSaveException;
import ca.digitalcave.moss.common.LogUtil;
import ca.digitalcave.moss.common.OperatingSystemUtil;
import ca.digitalcave.moss.common.ParseCommands;
import ca.digitalcave.moss.common.StreamUtil;
import ca.digitalcave.moss.common.Version;
import ca.digitalcave.moss.common.ParseCommands.ParseResults;
import ca.digitalcave.moss.common.ParseCommands.ParseVariable;
import ca.digitalcave.moss.crypto.IncorrectPasswordException;
import ca.digitalcave.moss.osx.Application;
import ca.digitalcave.moss.osx.ApplicationAdapter;
import ca.digitalcave.moss.osx.ApplicationEvent;
import ca.digitalcave.moss.swing.ApplicationModel;
import ca.digitalcave.moss.swing.LookAndFeelUtil;
import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.swing.exception.WindowOpenException;
import edu.stanford.ejalbert.BrowserLauncher;

/**
 * The main class, containing the launch methods for Buddi.  This class 
 * is responsible for setting up the environment (including reading
 * command line options), loading the preferences and languages, 
 * warning the user (if using experimental code), etc.  It also initializes
 * and contains access methods for several environment-type methods,
 * such as what OS we are running on, and what the working directory is.
 * 
 * @author wyatt
 * @author jdidion - Split the LNF dialog into its own class for better maintainability
 */
public class Buddi {

    private static String pluginsFolder;

    private static String languagesFolder;

    private static String reportsFolder;

    private static List<File> filesToLoad;

    private static Boolean noAutoSave = false;

    private static Boolean debian = false;

    private static Boolean windowsInstaller = false;

    private static Boolean genericUnix = false;

    private static Boolean slackware = false;

    private static Boolean redhat = false;

    private static File logFile = null;

    private static Version version = null;

    public static Version getVersion() {
        if (version == null) version = Version.getVersionResource("version.txt");
        return version;
    }

    /** 
	 * @return True if running on generic Unix, false otherwise.  This is 
	 * obtained through the startup flag --unix, so it can be 
	 * faked if desired.  This is used to determine which download
	 * to use on Linux - if the flag is set, we will download
	 * a .deb when a new version is released, otherwise we will
	 * download a .jar.
	 */
    public static boolean isUnix() {
        if (genericUnix == null) genericUnix = false;
        return genericUnix;
    }

    /** 
	 * @return True if running on Slackware, false otherwise.  This is 
	 * obtained through the startup flag --slackware, so it can be 
	 * faked if desired.  This is used to determine which download
	 * to use on Linux - if the flag is set, we will download
	 * a .deb when a new version is released, otherwise we will
	 * download a .jar.
	 */
    public static boolean isSlackware() {
        if (slackware == null) slackware = false;
        return slackware;
    }

    /**
	 * @return True if running from a Windows Installer installed .exe, false
	 * otherwise.  This is obtained through the startup flag --windows-installer, so it can be 
	 * faked if desired.  This is used to determine which download
	 * to use on Windows - if the flag is set, we will download
	 * the installer version when a new version is released, otherwise we will
	 * download the standalone .exe.
	 */
    public static boolean isWindowsInstaller() {
        if (windowsInstaller == null) windowsInstaller = false;
        return windowsInstaller;
    }

    /** 
	 * @return True if running on Redhat, false otherwise.  This is 
	 * obtained through the startup flag --debian, so it can be 
	 * faked if desired.  This is used to determine which download
	 * to use on Linux - if the flag is set, we will download
	 * a .deb when a new version is released, otherwise we will
	 * download a .jar.
	 */
    public static boolean isRedhat() {
        if (redhat == null) redhat = false;
        return redhat;
    }

    /** 
	 * @return True if running on Debian, false otherwise.  This is 
	 * obtained through the startup flag --debian, so it can be 
	 * faked if desired.  This is used to determine which download
	 * to use on Linux - if the flag is set, we will download
	 * a .deb when a new version is released, otherwise we will
	 * download a .jar.
	 */
    public static boolean isDebian() {
        if (debian == null) debian = false;
        return debian;
    }

    private static boolean isAutoSave() {
        if (noAutoSave == null) noAutoSave = false;
        return !noAutoSave;
    }

    public static File getPluginsFolder() {
        if (pluginsFolder == null) pluginsFolder = OperatingSystemUtil.getUserFolder("Buddi") + File.separator + Const.PLUGIN_FOLDER;
        return new File(pluginsFolder);
    }

    public static File getReportsFolder() {
        if (reportsFolder == null) reportsFolder = OperatingSystemUtil.getUserFolder("Buddi") + File.separator + Const.REPORT_FOLDER;
        return new File(reportsFolder);
    }

    public static File getLanguagesFolder() {
        if (languagesFolder == null) languagesFolder = OperatingSystemUtil.getUserFile("Buddi", Const.LANGUAGE_FOLDER).getAbsolutePath();
        Logger.getLogger(Buddi.class.getName()).finest(languagesFolder);
        return new File(languagesFolder);
    }

    /**
	 * Method to start the GUI.  Should be run from the AWT Dispatch thread.
	 */
    private static void launchGUI() {
        if (OperatingSystemUtil.isMac()) {
            Application.getApplication().setFramessMenuBar(new FramelessMenuBar());
        }
        if (filesToLoad.size() > 0) {
            for (File f : filesToLoad) {
                openFile(f);
            }
        }
        if (PrefsModel.getInstance().isShowUpdateNotifications() && PrefsModel.getInstance().getAvailableVersion() != null) {
            Version availableVersion = new Version(PrefsModel.getInstance().getAvailableVersion());
            Version thisVersion = getVersion();
            if (thisVersion.compareTo(availableVersion) < 0) {
                String[] buttons = new String[2];
                buttons[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_DOWNLOAD);
                buttons[1] = TextFormatter.getTranslation(ButtonKeys.BUTTON_CANCEL);
                int reply = JOptionPane.showOptionDialog(null, TextFormatter.getTranslation(BuddiKeys.NEW_VERSION_MESSAGE) + " " + PrefsModel.getInstance().getAvailableVersion() + "\n" + TextFormatter.getTranslation(BuddiKeys.NEW_VERSION_MESSAGE_2), TextFormatter.getTranslation(BuddiKeys.NEW_VERSION), JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
                if (reply == JOptionPane.YES_OPTION) {
                    String fileLocation;
                    if (Const.BRANCH.equals(Const.STABLE)) fileLocation = Const.DOWNLOAD_URL_STABLE; else fileLocation = Const.DOWNLOAD_URL_UNSTABLE;
                    if (OperatingSystemUtil.isMac()) fileLocation += Const.DOWNLOAD_TYPE_OSX; else if (OperatingSystemUtil.isWindows()) {
                        if (isWindowsInstaller()) fileLocation += Const.DOWNLOAD_TYPE_WINDOWS_INSTALLER; else fileLocation += Const.DOWNLOAD_TYPE_WINDOWS;
                    } else {
                        if (Buddi.isDebian()) fileLocation += Const.DOWNLOAD_TYPE_DEBIAN; else if (Buddi.isSlackware()) fileLocation += Const.DOWNLOAD_TYPE_SLACKWARE; else if (Buddi.isRedhat()) fileLocation += Const.DOWNLOAD_TYPE_REDHAT; else if (Buddi.isUnix()) fileLocation += Const.DOWNLOAD_TYPE_UNIX; else fileLocation += Const.DOWNLOAD_TYPE_GENERIC;
                    }
                    try {
                        BrowserLauncher bl = new BrowserLauncher(null);
                        bl.openURLinBrowser(fileLocation);
                    } catch (Exception e) {
                        Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Unknown Exception", e);
                    }
                }
            }
        }
        if (PrefsModel.getInstance().isShowPromptAtStartup() && ApplicationModel.getInstance().getOpenFrames().size() == 0) {
            new FileOpen(null).doClick();
        }
        if (PrefsModel.getInstance().getLastDataFiles() != null && ApplicationModel.getInstance().getOpenFrames().size() == 0) {
            for (File f : PrefsModel.getInstance().getLastDataFiles()) {
                openFile(f);
            }
        }
        if (ApplicationModel.getInstance().getOpenFrames().size() == 0) {
            try {
                Document model = ModelFactory.createDocument();
                MainFrame mainWndow = new MainFrame(model);
                try {
                    mainWndow.openWindow(PrefsModel.getInstance().getWindowSize(model.getFile() + ""), PrefsModel.getInstance().getWindowLocation(model.getFile() + ""));
                } catch (WindowOpenException woe) {
                }
            } catch (ModelException me) {
                Logger.getLogger(Buddi.class.getName()).log(Level.SEVERE, "Model Exception", me);
            }
        }
        MainFrame mainFrame = null;
        for (MossFrame mossFrame : ApplicationModel.getInstance().getOpenFrames()) {
            if (mossFrame instanceof MainFrame) {
                mainFrame = (MainFrame) mossFrame;
                break;
            }
        }
        if (mainFrame == null) Logger.getLogger(Buddi.class.getName()).severe("No Buddi main windows were able to open!");
        startVersionCheck(mainFrame);
        startUpdateCheck(mainFrame);
        if (isAutoSave()) {
            Timer t = new Timer();
            t.schedule(new TimerTask() {

                @Override
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            for (MossFrame frame : ApplicationModel.getInstance().getOpenFrames()) {
                                if (frame instanceof MainFrame) {
                                    MainFrame mainFrame = (MainFrame) frame;
                                    if (mainFrame.getDocument().isChanged()) {
                                        File autoSaveLocation = ModelFactory.getAutoSaveLocation(mainFrame.getDocument().getFile());
                                        try {
                                            ((DocumentImpl) mainFrame.getDocument()).saveAuto(autoSaveLocation);
                                            Logger.getLogger(this.getClass().getName()).finest("Auto saved file to " + autoSaveLocation);
                                        } catch (DocumentSaveException dse) {
                                            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error saving autosave file", dse);
                                        }
                                    } else {
                                        Logger.getLogger(this.getClass().getName()).finest("Did not autosave, as there are no changes to the data file " + mainFrame.getDocument().getFile());
                                    }
                                }
                            }
                        }

                        ;
                    });
                }
            }, PrefsModel.getInstance().getAutosaveDelay() * 1000, PrefsModel.getInstance().getAutosaveDelay() * 1000);
        }
    }

    /**
	 * Opens the given file.  Must be run in the event dispatch thread. 
	 * @param f
	 */
    private static void openFile(File f) {
        if (f.getName().endsWith(Const.DATA_FILE_EXTENSION)) {
            try {
                Document model;
                model = ModelFactory.createDocument(f);
                MainFrame mainWndow = new MainFrame(model);
                try {
                    mainWndow.openWindow(PrefsModel.getInstance().getWindowSize(model.getFile() + ""), PrefsModel.getInstance().getWindowLocation(model.getFile() + ""));
                } catch (WindowOpenException woe) {
                }
            } catch (DocumentLoadException lme) {
                Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Error loading document", lme);
            } catch (OperationCancelledException oce) {
            }
        } else if (f.getName().endsWith(Const.PLUGIN_EXTENSION)) {
            Logger.getLogger(Buddi.class.getName()).info("Trying to copy " + f.getAbsolutePath() + " to " + Buddi.getPluginsFolder() + File.separator + f.getName());
            if (!Buddi.getPluginsFolder().exists()) {
                if (!Buddi.getPluginsFolder().mkdirs()) {
                    Logger.getLogger(Buddi.class.getName()).warning("Error creating Plugins directory!");
                }
            }
            try {
                File dest = new File(Buddi.getPluginsFolder().getAbsolutePath() + File.separator + f.getName());
                if (f.getAbsolutePath().equals(dest.getAbsolutePath())) throw new IOException("Cannot copy to the same file.");
                FileFunctions.copyFile(f, dest);
                String[] options = new String[1];
                options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);
                JOptionPane.showOptionDialog(null, TextFormatter.getTranslation(BuddiKeys.MESSAGE_PLUGIN_ADDED_RESTART_NEEDED), TextFormatter.getTranslation(BuddiKeys.MESSAGE_PLUGIN_ADDED_RESTART_NEEDED_TITLE), JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            } catch (IOException ioe) {
                Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Error copying plugin to Plugins directory", ioe);
            }
        } else if (f.getName().endsWith(Const.LANGUAGE_EXTENSION)) {
            Logger.getLogger(Buddi.class.getName()).info("Trying to copy " + f.getAbsolutePath() + " to " + Buddi.getLanguagesFolder() + File.separator + f.getName());
            if (!Buddi.getLanguagesFolder().exists()) {
                if (!Buddi.getLanguagesFolder().mkdirs()) {
                    Logger.getLogger(Buddi.class.getName()).warning("Error creating Languages directory!");
                }
            }
            try {
                File dest = new File(Buddi.getLanguagesFolder().getAbsolutePath() + File.separator + f.getName());
                if (f.getAbsolutePath().equals(dest.getAbsolutePath())) throw new IOException("Cannot copy to the same file.");
                FileFunctions.copyFile(f, dest);
                String[] options = new String[1];
                options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);
                JOptionPane.showOptionDialog(null, TextFormatter.getTranslation(BuddiKeys.MESSAGE_LANGUAGE_ADDED_RESTART_NEEDED), TextFormatter.getTranslation(BuddiKeys.MESSAGE_LANGUAGE_ADDED_RESTART_NEEDED_TITLE), JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                PrefsModel.getInstance().setLanguage(f.getName().replaceAll(Const.LANGUAGE_EXTENSION, ""));
                PrefsModel.getInstance().save();
            } catch (IOException ioe) {
                Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Error copying translation to Languages directory", ioe);
            }
        }
    }

    /**
	 * Main method for Buddi.  Can pass certain command line options
	 * in.  Use --help flag to see complete list.
	 * @param args
	 */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        UIManager.put("OptionPane.maxCharactersPerLineCount", Integer.MAX_VALUE);
        UIManager.put("ComboBox.maximumRowCount", Integer.valueOf(10));
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", Const.PROJECT_NAME);
        String help = "USAGE: java -jar Buddi.jar <options> <data file>, where options include:\n" + "--usb\t\tRun on a USB key: put preferences, languages, and plugins in working dir.\n" + "--prefs\tFilename\tPath and name of Preference File (Default varies by platform)\n" + "--verbosity\t0-5\tVerbosity Level (0 = Emergency, 7 = Verbose)\n" + "--languages\tFolder\tFolder to store custom languages (should be writable; default varies by platform)\n" + "--plugins\tFolder\tFolder to store plugins (should be writable; default varies by platform)\n" + "--nosplash\t\tDon't show splash screen on startup\n" + "--lnf\tclassname\tTry to use the given LnF; you must manually include the LnF jar in the classpath\n" + "--extract\tData File\tExtracts the XML contents of the given file, to Filename.xml.  Used for debugging.\n" + "--log\tlogFile\tLocation to store logs, or 'stdout' / 'stderr' (default varies by platform)\n";
        List<ParseVariable> variables = new LinkedList<ParseVariable>();
        variables.add(new ParseVariable("--usb", Boolean.class, false));
        variables.add(new ParseVariable("--prefs", String.class, false));
        variables.add(new ParseVariable("--verbosity", Integer.class, false));
        variables.add(new ParseVariable("--plugins", String.class, false));
        variables.add(new ParseVariable("--languages", String.class, false));
        variables.add(new ParseVariable("--log", String.class, false));
        variables.add(new ParseVariable("--nosplash", Boolean.class, false));
        variables.add(new ParseVariable("--noautosave", Boolean.class, false));
        variables.add(new ParseVariable("--extract", String.class, false));
        variables.add(new ParseVariable("--reports", String.class, false));
        variables.add(new ParseVariable("--lnf", String.class, false));
        variables.add(new ParseVariable("--font", String.class, false));
        variables.add(new ParseVariable("--debian", Boolean.class, false));
        variables.add(new ParseVariable("--redhat", Boolean.class, false));
        variables.add(new ParseVariable("--slackware", Boolean.class, false));
        variables.add(new ParseVariable("--unix", Boolean.class, false));
        variables.add(new ParseVariable("--windows-installer", Boolean.class, false));
        ParseResults results = ParseCommands.parse(args, help, variables);
        if (results.getString("--extract") != null) {
            extractFile(new File(results.getString("--extract")));
            System.exit(0);
        }
        LookAndFeelUtil.setLookAndFeel(results.getString("--lnf"));
        splash: if (!OperatingSystemUtil.isMac()) {
            for (String string : args) {
                if (string.equals("--nosplash")) break splash;
            }
        }
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            public void uncaughtException(Thread arg0, Throwable arg1) {
                Logger.getLogger(Buddi.class.getName()).log(Level.SEVERE, "Uncaught exception", arg1);
                if (arg1 instanceof DataModelProblemException) sendBugReport(((DataModelProblemException) arg1).getDataModel()); else sendBugReport();
            }
        });
        if (OperatingSystemUtil.isMac()) {
            Application application = Application.getApplication();
            application.addAboutMenuItem();
            application.addPreferencesMenuItem();
            application.setEnabledAboutMenu(true);
            application.setEnabledPreferencesMenu(true);
            application.addApplicationListener(new ApplicationAdapter() {

                @Override
                public void handleAbout(ApplicationEvent arg0) {
                    new HelpAbout(null).doClick();
                    arg0.setHandled(true);
                }

                @Override
                public void handleOpenFile(final ApplicationEvent arg0) {
                    arg0.setHandled(true);
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            openFile(new File(arg0.getFilename()));
                        }
                    });
                }

                @Override
                public void handleReOpenApplication(ApplicationEvent arg0) {
                    arg0.setHandled(true);
                    if (ApplicationModel.getInstance().getOpenFrames().size() == 0) {
                        try {
                            Document model = ModelFactory.createDocument();
                            MainFrame mainWndow = new MainFrame(model);
                            mainWndow.openWindow(PrefsModel.getInstance().getWindowSize(model.getFile() + ""), PrefsModel.getInstance().getWindowLocation(model.getFile() + ""));
                        } catch (ModelException me) {
                            Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Model Exception", me);
                        } catch (WindowOpenException woe) {
                        }
                    }
                }

                @Override
                public void handlePreferences(ApplicationEvent arg0) {
                    new EditPreferences(null).doClick();
                    arg0.setHandled(true);
                }

                @Override
                public void handleQuit(ApplicationEvent arg0) {
                    arg0.setHandled(false);
                    new FileQuit(null).doClick();
                }
            });
        }
        try {
            PrintStream logStream = System.err;
            if (results.getString("--log") != null) {
                if (results.getString("--log").equals("stdout")) logStream = System.out; else if (results.getString("--log").equals("stderr")) logStream = System.err; else {
                    logFile = new File(results.getString("--log"));
                }
            } else logFile = OperatingSystemUtil.getLogFile("Buddi", Const.LOG_FILE);
            if (logFile != null) {
                if (!logFile.getParentFile().exists()) logFile.getParentFile().mkdirs();
                if (logFile.getParentFile().exists()) logStream = new PrintStream(logFile);
            }
            Logger logger = Logger.getLogger("org.homeunix.thecave");
            Handler streamHandler = new StreamHandler(logStream, new SimpleFormatter());
            logger.addHandler(streamHandler);
        } catch (FileNotFoundException fnfe) {
            Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Cannot open log file", fnfe);
        }
        Integer verbosity = results.getInteger("--verbosity");
        if (verbosity != null) {
            Level level;
            switch(verbosity) {
                case 0:
                    level = Level.SEVERE;
                    break;
                case 1:
                    level = Level.WARNING;
                    break;
                case 2:
                    level = Level.WARNING;
                    break;
                case 3:
                    level = Level.INFO;
                    break;
                case 4:
                    level = Level.INFO;
                    break;
                case 5:
                    level = Level.FINE;
                    break;
                case 6:
                    level = Level.FINER;
                    break;
                case 7:
                    level = Level.FINEST;
                    break;
                default:
                    level = Level.INFO;
                    break;
            }
            LogUtil.setLogLevel(level.toString());
            Logger.getLogger(Buddi.class.getName()).finest("Setting log level to " + level);
        } else {
            if (Const.DEVEL) Logger.getLogger("org.homeunix.thecave").setLevel(Level.FINEST); else Logger.getLogger(Buddi.class.getName()).setLevel(Level.INFO);
        }
        Logger.getLogger(Buddi.class.getName()).info("Buddi version: " + getVersion());
        Logger.getLogger(Buddi.class.getName()).info("Buddi command line arguments: " + Arrays.toString(args));
        Logger.getLogger(Buddi.class.getName()).info("Operating System: " + System.getProperty("os.name") + ", " + System.getProperty("os.arch"));
        Logger.getLogger(Buddi.class.getName()).info("Java VM version: " + System.getProperty("java.version"));
        Boolean usbKey = results.getBoolean("--usb");
        String prefsLocation = results.getString("--prefs");
        String font = results.getString("--font");
        languagesFolder = results.getString("--languages");
        pluginsFolder = results.getString("--plugins");
        noAutoSave = results.getBoolean("--noautosave");
        windowsInstaller = results.getBoolean("--windows-installer");
        slackware = results.getBoolean("--slackware");
        debian = results.getBoolean("--debian");
        redhat = results.getBoolean("--redhat");
        genericUnix = results.getBoolean("--unix");
        reportsFolder = results.getString("--reports");
        filesToLoad = new LinkedList<File>();
        for (String s : results.getCommands()) {
            filesToLoad.add(new File(s));
        }
        Logger.getLogger(Buddi.class.getName()).finest("Files to load: " + filesToLoad);
        String currentWorkingDir = System.getProperty("user.dir") + File.separator;
        if (usbKey != null && usbKey) {
            if (languagesFolder == null) languagesFolder = currentWorkingDir + Const.LANGUAGE_FOLDER;
            if (pluginsFolder == null) pluginsFolder = currentWorkingDir + Const.PLUGIN_FOLDER;
            if (prefsLocation == null) prefsLocation = currentWorkingDir + Const.PREFERENCE_FILE_NAME;
        }
        if (prefsLocation != null) {
            PrefsModel.setPrefsFile(new File(prefsLocation));
        }
        if (font != null) {
            Enumeration<Object> keys = UIManager.getDefaults().keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                if (value instanceof FontUIResource) {
                    FontUIResource f = (FontUIResource) value;
                    UIManager.put(key, new FontUIResource(font, f.getStyle(), f.getSize()));
                }
            }
        }
        Logger.getLogger(Buddi.class.getName()).info("Working directory: " + currentWorkingDir);
        for (BuddiRunnablePlugin plugin : (List<BuddiRunnablePlugin>) BuddiPluginFactory.getPlugins(BuddiRunnablePlugin.class)) {
            try {
                plugin.run();
            } catch (RuntimeException re) {
                Logger.getLogger(Buddi.class.getName()).log(Level.SEVERE, "Runtime Exception encountered while starting plugin", re);
                JOptionPane.showMessageDialog(null, "There was a problem starting the plugin " + plugin.getClass() + ".  Please send a copy of Buddi.log to the plugin author for debugging.");
            } catch (Exception e) {
                Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Exception encountered while starting plugin", e);
                JOptionPane.showMessageDialog(null, "There was a problem starting the plugin " + plugin.getClass() + ".  Please send a copy of Buddi.log to the plugin author for debugging.");
            }
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                launchGUI();
            }
        });
    }

    /**
	 * Display any information you need to see on first version, such as 
	 * donation requests, etc. 
	 */
    private static void startVersionCheck(final MossFrame frame) {
        String[] warningButtons = new String[2];
        warningButtons[0] = "Continue";
        warningButtons[1] = "Quit";
        if (PrefsModel.getInstance().getLastVersion() != null && PrefsModel.getInstance().getLastVersion().isSamePatch(new Version(3, 4, 0, 8))) {
            PrefsModel.getInstance().setShowCurrentBudget(true);
        }
        if (PrefsModel.getInstance().getLastVersion() == null || !PrefsModel.getInstance().getLastVersion().equals(getVersion())) {
            PrefsModel.getInstance().updateVersion();
            String[] buttons = new String[2];
            buttons[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_DONATE);
            buttons[1] = TextFormatter.getTranslation(ButtonKeys.BUTTON_NOT_NOW);
            int reply = JOptionPane.showOptionDialog(frame, TextFormatter.getTranslation(BuddiKeys.MESSAGE_ASK_FOR_DONATION), TextFormatter.getTranslation(BuddiKeys.MESSAGE_ASK_FOR_DONATION_TITLE), JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
            if (reply == JOptionPane.YES_OPTION) {
                try {
                    BrowserLauncher bl = new BrowserLauncher(null);
                    bl.openURLinBrowser(Const.DONATE_URL);
                } catch (Exception e) {
                    Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Unknown Exception", e);
                }
            }
        }
    }

    /**
	 * Starts a thread which checks the Internet for any new versions.
	 */
    public static void startUpdateCheck(final MossFrame frame) {
        if (PrefsModel.getInstance().isShowUpdateNotifications()) {
            Thread updateThread = new Thread("Update Thread") {

                public void run() {
                    try {
                        Proxy p;
                        if (PrefsModel.getInstance().isShowProxySettings()) {
                            InetAddress proxyAddress = InetAddress.getByName(PrefsModel.getInstance().getProxyServer());
                            InetSocketAddress socketAddress = new InetSocketAddress(proxyAddress, PrefsModel.getInstance().getPort());
                            p = new Proxy(Proxy.Type.DIRECT, socketAddress);
                        } else {
                            p = Proxy.NO_PROXY;
                        }
                        URL mostRecentVersion = new URL(Const.PROJECT_URL + Const.VERSION_FILE + "?current=" + Buddi.getVersion().toString());
                        URLConnection connection = mostRecentVersion.openConnection(p);
                        InputStream is = connection.getInputStream();
                        Properties versions = new Properties();
                        versions.load(is);
                        PrefsModel.getInstance().setAvailableVersion(versions.getProperty(Const.BRANCH));
                    } catch (IOException ioe) {
                        Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Unknown Exception", ioe);
                    }
                }

                ;
            };
            updateThread.start();
        }
    }

    /**
	 * Checks for new updates, and notifies user if they are found.
	 */
    public static void doUpdateCheck(final MossFrame frame) {
        SwingWorker updateWorker = new SwingWorker() {

            @Override
            public Object construct() {
                try {
                    Proxy p;
                    if (PrefsModel.getInstance().isShowProxySettings()) {
                        InetAddress proxyAddress = InetAddress.getByName(PrefsModel.getInstance().getProxyServer());
                        InetSocketAddress socketAddress = new InetSocketAddress(proxyAddress, PrefsModel.getInstance().getPort());
                        p = new Proxy(Proxy.Type.DIRECT, socketAddress);
                    } else {
                        p = Proxy.NO_PROXY;
                    }
                    URL mostRecentVersion = new URL(Const.PROJECT_URL + Const.VERSION_FILE);
                    URLConnection connection = mostRecentVersion.openConnection(p);
                    InputStream is = connection.getInputStream();
                    Properties versions = new Properties();
                    versions.load(is);
                    if (versions.get(Const.BRANCH) == null) return getVersion();
                    Version availableVersion = new Version(versions.get(Const.BRANCH).toString());
                    Version thisVersion = getVersion();
                    Logger.getLogger(Buddi.class.getName()).finest("This version: " + thisVersion);
                    Logger.getLogger(Buddi.class.getName()).finest("Available version: " + availableVersion);
                    if (thisVersion.compareTo(availableVersion) < 0) return availableVersion;
                } catch (IOException ioe) {
                    Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Unknown Exception", ioe);
                    String[] options = new String[1];
                    options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);
                    JOptionPane.showOptionDialog(frame, TextFormatter.getTranslation(BuddiKeys.MESSAGE_ERROR_CHECKING_FOR_UPDATES), TextFormatter.getTranslation(BuddiKeys.ERROR), JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                }
                return null;
            }

            @Override
            public void finished() {
                if (get() != null) {
                    String[] buttons = new String[2];
                    buttons[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_DOWNLOAD);
                    buttons[1] = TextFormatter.getTranslation(ButtonKeys.BUTTON_CANCEL);
                    int reply = JOptionPane.showOptionDialog(frame, TextFormatter.getTranslation(BuddiKeys.NEW_VERSION_MESSAGE) + " " + get() + "\n" + TextFormatter.getTranslation(BuddiKeys.NEW_VERSION_MESSAGE_2), TextFormatter.getTranslation(BuddiKeys.NEW_VERSION), JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
                    if (reply == JOptionPane.YES_OPTION) {
                        String fileLocation;
                        if (Const.BRANCH.equals(Const.STABLE)) fileLocation = Const.DOWNLOAD_URL_STABLE; else fileLocation = Const.DOWNLOAD_URL_UNSTABLE;
                        if (OperatingSystemUtil.isMac()) fileLocation += Const.DOWNLOAD_TYPE_OSX; else if (OperatingSystemUtil.isWindows()) {
                            if (isWindowsInstaller()) fileLocation += Const.DOWNLOAD_TYPE_WINDOWS_INSTALLER; else fileLocation += Const.DOWNLOAD_TYPE_WINDOWS;
                        } else {
                            if (Buddi.isDebian()) fileLocation += Const.DOWNLOAD_TYPE_DEBIAN; else if (Buddi.isSlackware()) fileLocation += Const.DOWNLOAD_TYPE_SLACKWARE; else if (Buddi.isRedhat()) fileLocation += Const.DOWNLOAD_TYPE_REDHAT; else if (Buddi.isUnix()) fileLocation += Const.DOWNLOAD_TYPE_UNIX; else fileLocation += Const.DOWNLOAD_TYPE_GENERIC;
                        }
                        try {
                            BrowserLauncher bl = new BrowserLauncher(null);
                            bl.openURLinBrowser(fileLocation);
                        } catch (Exception e) {
                            Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Unknown Exception", e);
                        }
                    }
                } else {
                    String[] options = new String[1];
                    options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);
                    JOptionPane.showOptionDialog(frame, TextFormatter.getTranslation(BuddiKeys.MESSAGE_NO_NEW_VERSION), TextFormatter.getTranslation(BuddiKeys.MESSAGE_NO_NEW_VERSION_TITLE), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                }
                super.finished();
            }
        };
        if (Const.DEVEL) Logger.getLogger(Buddi.class.getName()).finest("Starting update checking...");
        updateWorker.start();
    }

    public static void sendBugReport(Document... models) {
    }

    private static void extractFile(File fileToLoad) {
        try {
            InputStream is;
            BuddiCryptoFactory factory = new BuddiCryptoFactory();
            char[] password = null;
            while (true) {
                try {
                    is = factory.getDecryptedStream(new FileInputStream(fileToLoad), password);
                    OutputStream os = new FileOutputStream(new File(fileToLoad.getAbsolutePath() + ".xml"));
                    StreamUtil.copyStream(is, os);
                    os.flush();
                    os.close();
                    is.close();
                    System.exit(0);
                } catch (IncorrectPasswordException ipe) {
                    BuddiPasswordDialog passwordDialog = new BuddiPasswordDialog();
                    password = passwordDialog.askForPassword(false, true);
                    if (password == null) System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }
}
