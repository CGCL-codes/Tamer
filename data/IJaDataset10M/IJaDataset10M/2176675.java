package org.chefxp.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Rectangle;

public class ConfigCommander {

    public static final Properties config;

    public static final Properties languageConfig;

    public static final Properties defaultConfig;

    public static final Properties defaultLanguageConfig;

    public static final Logger log = Logger.getLogger(ConfigCommander.class);

    static {
        defaultConfig = new Properties();
        defaultConfig.put("Language.File", "english.lang");
        defaultConfig.put("Window.Size.Width", "1024");
        defaultConfig.put("Window.Size.Height", "768");
        defaultConfig.put("Window.Size.X", "0");
        defaultConfig.put("Window.Size.Y", "0");
        defaultConfig.put("SiteManager.Window.Size.Width", "1024");
        defaultConfig.put("SiteManager.Window.Size.Height", "768");
        defaultConfig.put("SiteManager.Window.Size.X", "0");
        defaultConfig.put("SiteManager.Window.Size.Y", "0");
        defaultConfig.put("QuickConnect.Window.Size.Width", "400");
        defaultConfig.put("QuickConnect.Window.Size.Height", "255");
        defaultConfig.put("QuickConnect.Window.Size.X", "0");
        defaultConfig.put("QuickConnect.Window.Size.Y", "0");
        defaultConfig.put("Home.Directory", System.getProperty("user.home"));
        defaultConfig.put("SiteManager.Directory", "sites");
    }

    static {
        defaultLanguageConfig = new Properties();
        defaultLanguageConfig.put("Window.Title", "CHEFXP: You can't flash better");
        defaultLanguageConfig.put("FTPConnection.Connect", "Connect to server");
        defaultLanguageConfig.put("FTPConnection.Stop", "Stop");
        defaultLanguageConfig.put("Menu.SiteManager", "Site Manager");
        defaultLanguageConfig.put("Menu.Session", "Session");
        defaultLanguageConfig.put("Menu.Quick.Connect", "Quick Connect");
        defaultLanguageConfig.put("Menu.Save.Path", "Save Path");
        defaultLanguageConfig.put("Menu.Local.Path", "Local Path");
        defaultLanguageConfig.put("Menu.Remote.Path", "Remote Path");
        defaultLanguageConfig.put("Menu.Quick.Connect", "Quick Connect");
        defaultLanguageConfig.put("Menu.Transfer.Mode", "Transfer Mode");
        defaultLanguageConfig.put("Menu.Transfer.Mode.ASCII", "ASCII");
        defaultLanguageConfig.put("Menu.Transfer.Mode.Binary", "Binary");
        defaultLanguageConfig.put("Menu.Transfer.Mode.Auto", "Auto");
        defaultLanguageConfig.put("SiteManager.Load", "Load Site");
        defaultLanguageConfig.put("SiteManager.Close", "Close Site Manager");
        defaultLanguageConfig.put("SiteManager.Name", "Site Manager");
        defaultLanguageConfig.put("SiteManager.Delete", "Delete");
        defaultLanguageConfig.put("QuickConnect.Host", "Host: ");
        defaultLanguageConfig.put("QuickConnect.Port", "Port: ");
        defaultLanguageConfig.put("QuickConnect.Username", "Login: ");
        defaultLanguageConfig.put("QuickConnect.Password", "Pasword: ");
        defaultLanguageConfig.put("QuickConnect.Passive", "Passive");
        defaultLanguageConfig.put("QuickConnect.Upload.Bandwidth", "Upload Bandwidth: ");
        defaultLanguageConfig.put("QuickConnect.Download.Bandwidth", "Download Bandwidth: ");
        defaultLanguageConfig.put("QuickConnect.Security", "Security: ");
        defaultLanguageConfig.put("QuickConnect.AuthTLS", "AUTH TLS");
        defaultLanguageConfig.put("QuickConnect.AuthSSL", "AUTH SSL");
        defaultLanguageConfig.put("QuickConnect.Name", "AUTH SSL");
        defaultLanguageConfig.put("QuickConnect.Connect", "Connect ...");
        defaultLanguageConfig.put("QuickConnect.Abort", "Cancel");
        defaultLanguageConfig.put("QuickConnect.NormalFtpConnection", "No encryption");
    }

    static {
        config = new Properties();
        languageConfig = new Properties();
        File configFile = new File("config" + File.separator + "mainconfig.properties");
        try {
            config.load(new FileInputStream(configFile));
            upgradeConfig(config, defaultConfig);
            config.store(new FileOutputStream(configFile), "chefxp");
        } catch (Exception e) {
            log.fatal("Couldn't load the mainconfig.properties from " + configFile.getAbsolutePath());
            log.fatal(e, e);
            log.info("I will generate a standard config file!");
            new File(configFile.getParent()).mkdirs();
            try {
                defaultConfig.store(new FileOutputStream(configFile), "chefxp");
                config.clear();
                config.putAll(defaultConfig);
            } catch (Exception fatal) {
                log.fatal("Couldn't generate standard config file in: " + configFile.getAbsolutePath());
                log.fatal("Please check permissions and disk freespace.");
                log.fatal("I will exit now");
                log.fatal(fatal, fatal);
                System.exit(-1);
            }
        }
        File languageFile = new File("language" + File.separator + config.getProperty("Language.File"));
        try {
            languageConfig.load(new FileInputStream(languageFile));
            upgradeConfig(languageConfig, defaultLanguageConfig);
            languageConfig.store(new FileOutputStream(languageFile), "chefxp");
        } catch (Exception e) {
            log.fatal("Couldn't load language file " + config.getProperty("Language.File") + " from " + languageFile.getAbsolutePath());
            log.fatal(e, e);
            log.info("I will generate a standard language file!");
            new File(languageFile.getParent()).mkdirs();
            config.put("Language.File", "english.lang");
            try {
                defaultLanguageConfig.store(new FileOutputStream(languageFile), "chefxp");
                languageConfig.clear();
                languageConfig.putAll(defaultLanguageConfig);
            } catch (Exception fatal) {
                log.fatal("Couldn't generate standard language file in: " + configFile.getAbsolutePath());
                log.fatal("Please check permissions and disk freespace.");
                log.fatal("I will exit now");
                log.fatal(fatal, fatal);
                System.exit(-1);
            }
        }
    }

    public static void upgradeConfig(Properties config, Properties defaultConfig) {
        if (config.size() != defaultConfig.size()) {
            for (Iterator it = defaultConfig.keySet().iterator(); it.hasNext(); ) {
                Object key = it.next();
                if (!config.containsKey(key)) {
                    log.debug("Upgrading Config: Key: " + key + " Value: " + defaultConfig.get(key));
                    config.put(key, defaultConfig.get(key));
                }
            }
        }
    }

    public static String getLanguageEntry(String component) {
        return languageConfig.getProperty(component);
    }

    public static String getString(String key) {
        return config.getProperty(key);
    }

    public static int getInteger(String key) {
        return Integer.parseInt(config.getProperty(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.getBoolean(config.getProperty(key));
    }

    public static double getDouble(String key) {
        return Double.parseDouble(config.getProperty(key));
    }

    public static Rectangle getBounds(String window) {
        return new Rectangle(getInteger(window + ".Size.X"), getInteger(window + ".Size.Y"), getInteger(window + ".Size.Width"), getInteger(window + ".Size.Height"));
    }
}
