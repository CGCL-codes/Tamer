package com.orientechnologies.tools.oexplorer;

import com.orientechnologies.jdo.*;
import com.orientechnologies.odbms.tools.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

public class Application {

    public Application() {
        instance = this;
        openSplashWindow();
        shutdowning = false;
        Runtime.getRuntime().addShutdownHook(new ApplicationShutdown());
        IconManager.createInstance();
        DatabaseManager.createInstance();
        TxManager.createInstance();
        QueryManager.createInstance();
        CommandOutputConsumer.getInstance();
        packFrame = false;
        loadConfig();
        setLookAndFeel(getConfigItem("oexplorer.view.look_and_feel", "Metal"));
        mainFrame = MainFrame.createInstance();
        if (packFrame) mainFrame.pack(); else mainFrame.validate();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = mainFrame.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        mainFrame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        mainFrame.setVisible(true);
        splashScreen.close();
        splashScreen = null;
        cfgTool = new DbConfig();
        Application.getInstance().writeLog("Application ready.");
    }

    /**
   * Open splash window
   */
    public void openSplashWindow() {
        splashScreen = new winSplash();
        new Thread(splashScreen).start();
    }

    /**
   * Save current configuration before to exit.
   */
    public void exit() {
        if (shutdowning) return;
        shutdowning = true;
        saveConfig();
        System.exit(0);
    }

    protected void loadConfig() {
        try {
            configFilePath = oUtility.getOrientHome() + CONFIG_FILE;
            config = new Properties();
            File configFile;
            FileInputStream configStream;
            configFile = new File(configFilePath);
            if (configFile.createNewFile()) return;
            configStream = new FileInputStream(configFile);
            config.load(configStream);
        } catch (IOException e) {
            return;
        }
        loadDbConfig();
    }

    protected void loadDbConfig() {
        Hashtable dbs = new Hashtable();
        try {
            DbItem item;
            int i = 0;
            aliases = new DbAlias();
            for (Enumeration keys = aliases.getAliases(); keys.hasMoreElements(); ) {
                item = new DbItem();
                item.alias = (String) keys.nextElement();
                item.id = i++;
                item.url = aliases.getUrl(item.alias);
                item.classpath = aliases.getClassPath(item.alias);
                item.user = aliases.getUser(item.alias);
                dbs.put(item.alias, item);
            }
        } catch (Exception e) {
            Application.getInstance().getMainFrame().showException(e);
        }
        DatabaseManager.getInstance().setDatabases(dbs);
    }

    public void saveConfig() {
        try {
            config.store(new FileOutputStream(configFilePath), CONFIG_HEADER);
        } catch (Exception e) {
        }
    }

    public String getOrientHome() {
        return oUtility.getOrientHome();
    }

    public DbItem addDbItem(String iDbAlias, String iDbUrl, String iDbCP, String iUser) {
        if (!aliases.addAlias(iDbAlias, iDbUrl, iDbCP, iUser)) return null;
        mainFrame.getRootNode().add(new DefaultMutableTreeNode(iDbAlias));
        mainFrame.getTreeObjects().updateUI();
        DbItem item = DatabaseManager.getInstance().newDatabase(iDbAlias, iDbUrl, iDbCP, iUser);
        mainFrame.getViewQuery().fillDatabaseList(DatabaseManager.getInstance().getDatabases());
        return item;
    }

    public void removeDbItem(String iDbAlias) {
        aliases.delAlias(iDbAlias);
        DefaultMutableTreeNode node = Application.getInstance().getMainFrame().getCurrDatabase();
        node.removeFromParent();
        mainFrame.treObjects.updateUI();
        DatabaseManager.getInstance().removeDatabase(iDbAlias);
    }

    public void updateDbItem(String iDbAlias, String iDbUrl, String iDbCP, String iUser) {
        aliases.changeAlias(iDbAlias, iDbUrl, iDbCP, iUser);
    }

    public Hashtable getConfig() {
        return config;
    }

    public String getConfigItem(String iItemName) {
        return config.getProperty(iItemName);
    }

    public String getConfigItem(String iItemName, String iDefItem) {
        return config.getProperty(iItemName, iDefItem);
    }

    public boolean setConfigItem(String iItemName, String iValue) {
        String value = config.getProperty(iItemName);
        if (value != null && value.equals(iValue)) return false;
        config.setProperty(iItemName, iValue);
        return true;
    }

    public void removeConfigItem(String iItemName) {
        config.remove(iItemName);
    }

    public void writeLog(String iMessage) {
        mainFrame.appendLog("\n" + new Date() + " - " + iMessage);
    }

    public void appendLog(String iMessage) {
        mainFrame.appendLog(iMessage);
    }

    public void setLookAndFeel(String iLook) {
        if (iLook == null) return;
        try {
            UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
            for (int i = 0; i < looks.length; ++i) if (looks[i].getName().equals(iLook)) {
                UIManager.setLookAndFeel(looks[i].getClassName());
                break;
            }
        } catch (Exception ex) {
            if (getMainFrame() != null) getMainFrame().showMessage("Configuration error", "Cannot change the look and feel to: " + iLook + " (" + ex + ")", false);
        }
    }

    public DbConfig getCfgTool() {
        return cfgTool;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public static Application getInstance() {
        return instance;
    }

    public static void createInstance() {
        new Application();
    }

    public static void main(String[] args) {
        createInstance();
    }

    public class ApplicationShutdown extends Thread {

        public void run() {
            Application.getInstance().exit();
        }
    }

    private DbConfig cfgTool = null;

    private boolean packFrame;

    private Properties config = null;

    private MainFrame mainFrame = null;

    private DbAlias aliases;

    private String configFilePath;

    private boolean shutdowning;

    private winSplash splashScreen = null;

    private static Application instance = null;

    private static final String DBS_FILE = "/cfg/dbalias.properties";

    private static final String CONFIG_FILE = "/cfg/oexplorer.config";

    private static final String CONFIG_HEADER = "OEXPLORER CONFIG FILE";
}
