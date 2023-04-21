package de.eversync.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import de.eversync.logging.Logger;

public class Config {

    static String cCONFIG_FILE = "config.conf";

    private Properties mProperties;

    public Config() {
        this.setProperties(new Properties());
        this.setDefaultProperties();
    }

    public void setWatchPath(String aWatchPath) {
        this.getProperties().setProperty("watch_path", aWatchPath);
    }

    public String getWatchPath() {
        if (this.getProperties().getProperty("watch_path") == null) return "";
        return this.getProperties().getProperty("watch_path");
    }

    public void setUpdateIntervall(String aIntervall) {
        this.getProperties().setProperty("update_intervall", aIntervall);
    }

    public int getUpdateIntervall() {
        if (this.getProperties().getProperty("update_intervall") == null) return 20;
        return Integer.parseInt(this.getProperties().getProperty("update_intervall"));
    }

    public void setUserName(String aUserName) {
        this.getProperties().setProperty("username", aUserName);
    }

    public String getUserName() {
        if (this.getProperties().getProperty("username") == null) return "";
        return this.getProperties().getProperty("username");
    }

    public void setPassword(String aPassword) {
        this.getProperties().setProperty("password", aPassword);
    }

    public String getPassword() {
        if (this.getProperties().getProperty("password") == null) return "";
        return this.getProperties().getProperty("password");
    }

    public void setServerUrl(String aUrl) {
        this.getProperties().setProperty("server_url", aUrl);
    }

    public String getServerUrl() {
        if (this.getProperties().getProperty("server_url") == null) return "";
        return this.getProperties().getProperty("server_url");
    }

    public void setServerPort(String aPort) {
        this.getProperties().setProperty("server_port", aPort);
    }

    public int getServerPort() {
        if (this.getProperties().getProperty("server_port") == null) return -1;
        return Integer.parseInt(this.getProperties().getProperty("server_port"));
    }

    public boolean isConnectionConfigured() {
        if (this.getServerUrl().isEmpty() || this.getServerPort() == 0 || this.getUserName().isEmpty() || this.getPassword().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void setAutoSync(String aAutoSync) {
        this.getProperties().setProperty("autosync", aAutoSync);
    }

    public String getAutoSync() {
        if (this.getProperties().getProperty("autosync") == null) return "off";
        return this.getProperties().getProperty("autosync");
    }

    public void saveConfig() {
        FileOutputStream OutputStream;
        File file = new File(Config.cCONFIG_FILE);
        try {
            if (file.createNewFile()) System.out.println("Created config file: " + Config.cCONFIG_FILE);
            OutputStream = new FileOutputStream(file);
            this.getProperties().store(OutputStream, "EverSync Config");
        } catch (FileNotFoundException e) {
            Logger.getInstance().add("Can't save config");
            e.printStackTrace();
        } catch (IOException e) {
            Logger.getInstance().add("Can't save config");
            e.printStackTrace();
        }
    }

    public boolean loadConfig() {
        File file = new File(Config.cCONFIG_FILE);
        if (file.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(Config.cCONFIG_FILE);
                this.getProperties().load(inputStream);
                return true;
            } catch (FileNotFoundException e) {
                Logger.getInstance().add("Can't find the config file...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void setDefaultProperties() {
        this.getProperties().setProperty("watch_path", "defaultWatchFolder");
        this.getProperties().setProperty("update_intervall", "20");
    }

    private void setProperties(Properties aProp) {
        this.mProperties = aProp;
    }

    private Properties getProperties() {
        return this.mProperties;
    }
}
