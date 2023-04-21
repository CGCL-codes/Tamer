package de.altitude.cimuelle;

import de.altitudecustomcommands.Player;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import javax.naming.ConfigurationException;
import java.io.*;
import java.util.*;

/**
 * User: cybernaut
 * Date: 12.06.11
 * Time: 01:01
 */
public class Config {

    private PropFile userConfig;

    private final List<PropFile> toReload = new ArrayList<PropFile>();

    private static final Logger logger = Logger.getLogger("Config");

    private static Config instance;

    private Thread configMonitor;

    private boolean configMonitorRunning = true;

    public long configMonitorSleepTime = 5000;

    private String launcherConfig;

    private ResourceBundle messages;

    private Config(String configFile) {
        userConfig = new PropFile(configFile);
        toReload.add(userConfig);
        startConfigMonitor();
    }

    private Config(String configFile, String userConfigFile) {
        PropFile config = new PropFile(configFile);
        userConfig = new PropFile(userConfigFile, config.getProperties());
        toReload.add(config);
        toReload.add(userConfig);
        startConfigMonitor();
    }

    public Config() {
    }

    public static synchronized Config getInstance(String configFile) {
        if (Config.instance == null) {
            Config.instance = new Config(configFile);
        }
        return Config.instance;
    }

    public static synchronized Config getInstance(String configFile, String userConfigFile) {
        if (Config.instance == null) {
            Config.instance = new Config(configFile, userConfigFile);
        }
        return Config.instance;
    }

    public static synchronized Config getInstance() {
        if (Config.instance == null) {
            try {
                throw new ConfigurationException();
            } catch (ConfigurationException e) {
                e.printStackTrace();
                logger.error(e, e);
            }
        }
        return Config.instance;
    }

    void startConfigMonitor() {
        if (configMonitor != null && configMonitor.isAlive()) {
            return;
        }
        configMonitor = new Thread() {

            @Override
            public void run() {
                while (configMonitorRunning) {
                    reloadConfig();
                    try {
                        sleep(configMonitorSleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        readLog4jConfiguration();
    }

    private void readLog4jConfiguration() {
        configMonitor.start();
        String log4jFilename = getConfig("log4j", "");
        if (!"".equals(log4jFilename) && new File(log4jFilename).exists()) {
            LogManager.resetConfiguration();
            PropertyConfigurator.configureAndWatch(log4jFilename);
            logger.info("Logger configuration reset. Using file: " + log4jFilename);
        }
    }

    public void stopConfigMonitor() {
        configMonitorRunning = false;
        configMonitor.interrupt();
    }

    void reloadConfig() {
        for (PropFile propFile : toReload) {
            propFile.checkAndLoadConfigFile();
        }
    }

    public synchronized String getConfig(String prop) {
        return userConfig.getProperties().getProperty(prop, "");
    }

    public synchronized String getConfig(String prop, String defaultProp) {
        return userConfig.getProperties().getProperty(prop, defaultProp);
    }

    public String getLauncherConfig() {
        return fileExistenceCheck("launcherConfig", "launcher_config.xml");
    }

    String getServersPath() {
        return getConfig("serversPath", "../servers/");
    }

    public boolean isDebug() {
        return "true".equals(getConfig("debug", "true"));
    }

    public boolean isDebugVerbose() {
        return "true".equals(getConfig("debugVerbose", "false"));
    }

    public String getCommandFile() {
        return fileExistenceCheck("commandfile", "command.txt");
    }

    private String fileExistenceCheck(String prop, String alternative) {
        String filename = getConfig(prop, alternative);
        if (!new File(filename).exists()) {
            if (!"".equals(filename) && new File(getServersPath(), filename).exists()) {
                String server = getServersPath() + filename;
                return server;
            }
            logger.error("File not found " + filename + " or " + getServersPath() + filename + ".");
        }
        return filename;
    }

    public boolean isGetConfigByPort(String port, String property, String alternative) {
        return "true".equals(getConfigByPort(port, property, alternative));
    }

    public String getConfigByPort(String port, String property, String alternative) {
        return getConfig(port + "." + property, getConfig(property, alternative));
    }

    public boolean isChatProperty(String port, String chatString) {
        return !"".equals(getConfigByPort(port, "chat_" + chatString.toLowerCase(), ""));
    }

    public String getChatMessage(String port, String chatString) {
        return getConfigByPort(port, "chat_" + chatString.toLowerCase(), "");
    }

    public boolean isCoopActiveByPort(String port) {
        return isGetConfigByPort(port, "coopActive", "true");
    }

    public Properties getCoopConfig(String port, String mapName) throws FileNotFoundException {
        String coopConfigPath = getConfigByPort(port, "coopConfigPath", "");
        if (!"".equals(coopConfigPath) && new File(coopConfigPath).exists()) {
            File coopConfig = new File(coopConfigPath, mapName + ".properties");
            if (coopConfig.exists()) {
                Properties coopProperty = new Properties();
                FileReader fr = new FileReader(coopConfig);
                try {
                    coopProperty.load(fr);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return coopProperty;
            }
        }
        return null;
    }

    public boolean isCoopRankingActiveByPort(String port) {
        return isGetConfigByPort(port, "coopRankingActive", "false");
    }

    public ResourceBundle getMessages() {
        return messages;
    }

    public String getRessourceBundleFile() {
        return getConfig("languageFile", "");
    }

    public boolean isChatCommandEnabledByPort(String port) {
        return "true".equals(getConfigByPort(port, "chatCommands", "false"));
    }

    public boolean isPlayerCoopAdmin(Player player) {
        StringTokenizer coopAdmins = new StringTokenizer(getConfigByPort(player.getPort(), "coopAdmin", ""));
        while (coopAdmins.hasMoreTokens()) {
            String coopAdmin = coopAdmins.nextToken();
            if (player.getVapor().equals(coopAdmin)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPlayerCoopManageGameGroup(Player player) {
        return (checkCoopAdminGroupProp(player, "manageGames"));
    }

    public boolean isPlayerCoopManagePointsGroup(Player player) {
        return (checkCoopAdminGroupProp(player, "managePoints"));
    }

    public boolean isPlayerCoopManagePlayersGroup(Player player) {
        return (checkCoopAdminGroupProp(player, "managePlayers"));
    }

    private boolean checkCoopAdminGroupProp(Player player, String group) {
        StringTokenizer groups = new StringTokenizer(getConfigByPort(player.getPort(), player.getVapor(), ""), ",");
        while (groups.hasMoreElements()) {
            String playerGroup = groups.nextToken();
            if (group.equals(playerGroup) || "all".equals(playerGroup)) {
                return true;
            }
        }
        return false;
    }

    public String getLogFile(String filename, String alternative) {
        return fileExistenceCheck(filename, alternative);
    }

    private class PropFile {

        String filename = null;

        long modifyTime;

        Properties properties;

        public PropFile(String userConfigFile) {
            this.filename = userConfigFile;
            File f = new File(filename);
            this.modifyTime = f.lastModified();
            properties = new Properties();
            loadConfig(f);
        }

        public PropFile(String userConfigFile, Properties properties) {
            this.filename = userConfigFile;
            this.properties = new Properties(properties);
            if (filename != null) {
                File f = new File(filename);
                this.modifyTime = f.lastModified();
                BufferedInputStream stream = null;
                try {
                    stream = new BufferedInputStream(new FileInputStream(f));
                    properties.load(stream);
                    logger.info("Config " + f.getName() + " loaded.");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (stream != null) {
                            stream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private synchronized void loadConfig(File f) {
            BufferedInputStream stream = null;
            try {
                properties.clear();
                stream = new BufferedInputStream(new FileInputStream(f));
                properties.load(stream);
                logger.info("Config " + f.getName() + " loaded.");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void checkAndLoadConfigFile() {
            if (filename != null) {
                File f = new File(filename);
                if (f.exists() && f.lastModified() != modifyTime) {
                    logger.info("Config reloaded  " + filename);
                    modifyTime = f.lastModified();
                    loadConfig(f);
                }
            } else {
                logger.error("Error in Config Thread.");
            }
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public long getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(long modifyTime) {
            this.modifyTime = modifyTime;
        }

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }
    }
}
