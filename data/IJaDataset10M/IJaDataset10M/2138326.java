package net.sf.jmp3renamer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.FileHandler;
import javax.swing.ImageIcon;
import net.sf.jmp3renamer.gui.MainGUI;
import net.sf.jmp3renamer.gui.SplashScreen;
import net.sf.jmp3renamer.logging.DebugConsoleHandler;
import net.sf.jmp3renamer.logging.ErrorPopupHandler;
import net.sf.jmp3renamer.osgi.DataPluginTracker;
import net.sf.jmp3renamer.osgi.FilePluginTracker;
import net.sf.jmp3renamer.osgi.GeneralPluginTracker;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The backbone of JMP3Renamer.
 */
public class Main implements BundleActivator {

    public static final String VERSION = "2.3.1";

    private final String PLUGINMANAGER_URL = "http://www.hampelratte.org/maven/repository.xml";

    public static final String CONFIG_DIR = System.getProperty("user.home") + File.separator + ".jmp3renamer";

    public static final String CONFIG_FILE = CONFIG_DIR + File.separator + "jmp3renamer.props";

    private static transient Logger logger = LoggerFactory.getLogger(Main.class);

    private SplashScreen splash;

    /**
     * Properties hashtable, which holds all used Strings according to the selected language.
     * 
     * @see Main#getTranslation(String word).
     * @see Main#loadLanguage().
     */
    private static Properties lang = new Properties();

    /**
     * ArrayList, which defines the supported languages.
     * 
     * @see Main#loadLanguage().
     */
    private static List<String> supportedLanguages = new ArrayList<String>();

    /**
     * Properties hashtable, which holds all configuration settings concerning the core of jmp3renamer (without plugins,
     * they have thier own conf).
     */
    private static Properties props = new Properties();

    DebugConsoleHandler debugConsoleHandler;

    ErrorPopupHandler errorPopupHandler;

    FileHandler fileHandler;

    /**
     * The GUI of JMP3Renamer.
     */
    private static MainGUI mainGUI;

    private ServiceTracker filePluginTracker = null;

    private ServiceTracker dataPluginTracker = null;

    private ServiceTracker generalPluginTracker = null;

    private BundleContext ctx;

    private void init(BundleContext ctx) {
        ImageIcon icon = ImageLoader.getInstance().loadImage("/images/about_background.jpg");
        splash = new SplashScreen(icon);
        splash.setVisible(true);
        splash.setStatus("Loading config...");
        loadConfig();
        splash.setStatus("Initializing logging...");
        initLogger();
        splash.setStatus("Loading language...");
        loadLanguage();
        splash.setStatus(I18N.translate("splash.gui"));
        initGUI(ctx);
        splash.setStatus(I18N.translate("splash.plugins"));
        PluginLoader loader = new PluginLoader(ctx);
        loader.load();
        ctx.addBundleListener(loader);
        splash.setStatus(I18N.translate("splash.ready"));
        splash.dispose();
        mainGUI.setVisible(true);
        FileManager.getInstance().createTrackers(ctx);
    }

    /**
     * Called, if the user wants to exit JMP3Renamer. If "save on exit" is selected, the last configuration and window
     * state will be saved.
     */
    public void exit() {
        logger.info("Exitting...");
        try {
            ctx.getBundle(0).stop();
        } catch (BundleException e) {
            logger.error("Couldn't shutdown OSGi framework", e);
            System.exit(1);
        }
    }

    /**
     * Sets default settings and tries to load the configuration from a file. This is the first thing jmp3renamer does
     * after startup.
     * 
     * @see Main#props
     */
    private void loadConfig() {
        props.setProperty("filename", "%artist - %track - %title");
        props.setProperty("filenameCompilation", "Various Artists - %track - %artist - %title");
        props.setProperty("logging", "2");
        props.setProperty("logfile", "");
        props.setProperty("last", System.getProperty("user.home"));
        props.setProperty("extendedState", "0");
        props.setProperty("width", "800");
        props.setProperty("height", "600");
        props.setProperty("x", "0");
        props.setProperty("y", "0");
        props.setProperty("plugin_repos", "JMP3Renamer Plugins," + PLUGINMANAGER_URL);
        props.setProperty("leading_0s", "1");
        props.setProperty("words", "1");
        props.setProperty("directory", "");
        props.setProperty("directoryCompilation", "");
        props.setProperty("version", "");
        props.setProperty("proxyHost", "");
        props.setProperty("proxyPort", "");
        props.setProperty("proxyAuth", "");
        props.setProperty("http.proxyUser", "");
        props.setProperty("http.proxyPassword", "");
        props.setProperty("laf", "com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        props.setProperty("theme", "SkyBlue");
        props.setProperty("supportedLanguages", "en,de,es,fr");
        try {
            File confdir = new File(System.getProperty("user.home") + System.getProperty("file.separator") + ".jmp3renamer");
            if (!confdir.exists()) {
                confdir.mkdir();
            }
            File conffile = new File(CONFIG_FILE);
            if (!conffile.exists()) {
                conffile.createNewFile();
            }
            java.io.FileInputStream fin = new FileInputStream(CONFIG_FILE);
            props.load(fin);
        } catch (Exception e) {
            System.err.println("Couldn't load settings: " + e);
        }
        String sl = props.getProperty("supportedLanguages");
        String[] langs = sl.split(",");
        for (String lang2 : langs) {
            supportedLanguages.add(lang2);
        }
        String host = props.getProperty("proxyHost");
        if (!host.equals("")) {
            logger.info("Setting proxy server: " + props.getProperty("proxyHost") + ":" + props.getProperty("proxyPort"));
            System.setProperty("proxySet", "true");
            System.setProperty("proxyHost", host);
            System.setProperty("proxyPort", props.getProperty("proxyPort"));
            if (props.getProperty("proxyAuth").equals("true")) {
                System.setProperty("http.proxyUser", props.getProperty("http.proxyUser"));
                System.setProperty("http.proxyPassword", props.getProperty("http.proxyPassword"));
            }
        }
    }

    /**
     * Saves the current configuration.
     * 
     * @see Main#props
     */
    public void saveConfig() {
        StringBuffer sb = new StringBuffer();
        for (Iterator<String> iter = supportedLanguages.iterator(); iter.hasNext(); ) {
            String element = iter.next();
            sb.append(element);
            if (iter.hasNext()) {
                sb.append(",");
            }
        }
        props.setProperty("supportedLanguages", sb.toString());
        try {
            java.io.FileOutputStream fout = new FileOutputStream(CONFIG_FILE);
            props.store(fout, "JMP3Renamer Properties");
            logger.info("Settings saved");
        } catch (Exception e) {
            logger.error(I18N.translate("error.save_config"), e);
        }
    }

    /**
     * Loads the language. First, this method will initialize the language hashtable with english. Then we try to load a
     * language file according to Locale.getDefault();
     * 
     * @see Main#lang
     * @see java.util.Locale#getDefault()
     */
    private void loadLanguage() {
        logger.info("Loading language file");
        Properties english = new Properties();
        try {
            loadLanguageFromJar(english, Locale.ENGLISH.getLanguage());
        } catch (IOException e1) {
            logger.error(I18N.translate("error.load_language", new String[] { Locale.ENGLISH.getLanguage() }), e1);
            System.exit(1);
        }
        String language = props.getProperty("lang");
        if (language == null) {
            language = Locale.getDefault().getLanguage();
        }
        if (!supportedLanguages.contains(language)) {
            logger.error(I18N.translate("error.lang_not_supported"));
            language = "en";
        }
        boolean notInJar = false;
        try {
            loadLanguageFromJar(lang, language);
        } catch (Exception e) {
            notInJar = true;
            logger.warn("Couldn't load language from jar for JMP3Renamer", e);
        }
        try {
            loadlLanguageFromFile(lang, language);
        } catch (IOException e) {
            logger.warn("Couldn't load language from file for JMP3Renamer", e);
            if (notInJar) {
                language = "en";
                logger.warn("Falling back to default language (en)");
            }
        }
        props.setProperty("lang", language);
        I18N.registerLanguageTable(lang, language);
        I18N.registerLanguageTable(english, "en");
    }

    /**
     * Initializes the logging of JMP3Renamer.
     * 
     * @see Logger
     */
    public void initLogger() {
        java.util.logging.Logger root = java.util.logging.Logger.getLogger("");
        if (debugConsoleHandler == null) {
            debugConsoleHandler = new DebugConsoleHandler();
            root.addHandler(debugConsoleHandler);
        }
        if (errorPopupHandler == null) {
            errorPopupHandler = new ErrorPopupHandler();
            root.addHandler(errorPopupHandler);
        }
    }

    /**
     * Initliazes the GUI.
     * @param ctx
     * 
     * @see net.sf.jmp3renamer.gui.MainGUI
     */
    private void initGUI(BundleContext ctx) {
        logger.info("Initializing GUI");
        mainGUI = new MainGUI(this, ctx);
    }

    /**
     * Returns a List with all supported languages.
     * 
     * @return a List with all supported languages
     * @see Main#supportedLanguages
     */
    public static List<String> getSupportedLanguages() {
        return supportedLanguages;
    }

    /**
     * Sets a configuration setting. This method only sets settings of the core, not settings of plugins.
     * 
     * @param key
     *            the name of the setting
     * @param value
     *            the value of the setting
     * @see Main#props
     * @see Main#getProperty(String key)
     */
    public static void setProperty(String key, String value) {
        props.setProperty(key, value);
    }

    /**
     * Gets a configuration setting. This method only returns settings of the core, not settings of plugins.
     * 
     * @param key
     *            the name of the setting
     * @return the value of the setting
     */
    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    /**
     * Returns the GUI of JMP3Renamer.
     * 
     * @return the GUI of JMP3Renamer
     */
    public static MainGUI getGUI() {
        return mainGUI;
    }

    /**
     * @see Translatable#getLanguageTable(String)
     */
    public static Properties getLanguageTable(String locale) throws IOException {
        Properties lang = new Properties();
        loadLanguageFromJar(lang, locale);
        loadlLanguageFromFile(lang, locale);
        return lang;
    }

    /**
     * Loads the translations for the given locale in the lang table
     * 
     * @see #lang
     * @param locale
     * @throws IOException
     */
    public static void loadLanguageFromJar(Properties lang, String locale) throws IOException {
        logger.debug("Trying to load language file from jar for JMP3Renamer for locale " + locale);
        lang.load(Main.class.getResourceAsStream("/lang/jmp3renamer_" + locale + ".properties"));
    }

    public static void loadlLanguageFromFile(Properties lang, String locale) throws IOException {
        String dirString = System.getProperty("user.home") + System.getProperty("file.separator") + ".jmp3renamer" + System.getProperty("file.separator") + "lang";
        String name = "jmp3renamer_" + locale + ".properties";
        File file = new File(dirString, name);
        if (file.exists()) {
            logger.debug("Trying to load language file from file for JMP3Renamer for locale " + locale);
            FileInputStream fis = new FileInputStream(file);
            lang.load(fis);
        }
    }

    public static Properties getProperties() {
        return props;
    }

    public void start(BundleContext ctx) throws Exception {
        this.ctx = ctx;
        init(ctx);
        filePluginTracker = new FilePluginTracker(ctx, FilePlugin.class.getName(), null);
        filePluginTracker.open();
        dataPluginTracker = new DataPluginTracker(ctx, DataPlugin.class.getName(), null);
        dataPluginTracker.open();
        generalPluginTracker = new GeneralPluginTracker(ctx, GeneralPlugin.class.getName(), null);
        generalPluginTracker.open();
    }

    public void stop(BundleContext ctx) throws Exception {
        logger.info("Saving config...");
        props.setProperty("extendedState", new Integer(mainGUI.getExtendedState()).toString());
        double width = mainGUI.getSize().getWidth();
        double height = mainGUI.getSize().getHeight();
        props.setProperty("width", new Double(width).toString());
        props.setProperty("height", new Double(height).toString());
        double x = mainGUI.getLocation().getX();
        double y = mainGUI.getLocation().getY();
        props.setProperty("x", new Double(x).toString());
        props.setProperty("y", new Double(y).toString());
        saveConfig();
    }

    public void renameFiles() {
        FileManager.getInstance().resetStates();
        Thread thread = new Thread() {

            @Override
            public void run() {
                FileManager.getInstance().renameFiles();
            }
        };
        thread.start();
    }
}
