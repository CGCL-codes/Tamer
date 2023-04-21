package net;

import org.apache.log4j.Category;
import java.util.*;
import java.awt.Color;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import util.*;

/**
 * @author Jesper Nordenberg
 * @version $Revision: 1.4 $ $Date: 2002/06/11 07:32:38 $
 */
public class Settings {

    private static final Category logger = Category.getInstance(Settings.class);

    private static final Settings instance = new Settings();

    private static final String CONFIG_FILE = "data/settings.txt";

    private int usedUploadSlots = 0;

    private int usedDownloadSlots = 0;

    private LocalUser myself = new LocalUser();

    private DataProperties properties = new DataPropertiesRoot();

    private Settings() {
        load();
    }

    public static Settings getInstance() {
        return instance;
    }

    public User getMyself() {
        return myself;
    }

    public void setCopyEnabled(boolean enabled) {
        properties.setBoolean("copyEnabled", enabled);
    }

    public void setMoveEnabled(boolean enabled) {
        properties.setBoolean("moveEnabled", enabled);
    }

    public boolean isCopyEnabled() {
        return properties.getBoolean("copyEnabled", false);
    }

    public boolean isMoveEnabled() {
        return properties.getBoolean("moveEnabled", false);
    }

    public void setCopyDir(String v) {
        properties.set("copyDir", v);
    }

    public String getCopyDir() {
        return properties.get("copyDir", "");
    }

    public void setMoveDir(String v) {
        properties.set("moveDir", v);
    }

    public String getMoveDir() {
        return properties.get("moveDir", "");
    }

    public void setProxyVersion(String v) {
        properties.set("proxy.verison", v);
    }

    public String getProxyVersion() {
        return properties.get("proxy.version", "SOCKS4");
    }

    public void setProxyPort(int p) {
        properties.setInt("proxy.port", p);
    }

    public boolean proxyUsing() {
        return properties.getBoolean("proxy.using", false);
    }

    public boolean showIcons() {
        return properties.getBoolean("gui.showicons", true);
    }

    public void setShowIcons(boolean b) {
        properties.setBoolean("gui.showicons", b);
    }

    public void setProxyUsing(boolean b) {
        properties.setBoolean("proxy.using", b);
    }

    public boolean isNewInterface() {
        return properties.getBoolean("gui.newInterface", true);
    }

    public void setNewInterface(boolean b) {
        properties.setBoolean("gui.newInterface", b);
    }

    public int getProxyPort() {
        return properties.getInt("proxy.port", 1080);
    }

    public String getProxy() {
        return properties.get("proxy.address", "");
    }

    public void setProxy(String p) {
        properties.set("proxy.address", p);
    }

    public String[] getUploadDirs() {
        return properties.getArray("uploadDirs");
    }

    public void setUploadDirs(ArrayList dirs) {
        properties.setArray("uploadDirs", (String[]) dirs.toArray(new String[dirs.size()]));
        ShareManager.getInstance().update();
    }

    public String[] getListUrls() {
        return properties.getArray("listUrls");
    }

    public void setListUrls(ArrayList dirs) {
        properties.setArray("listUrls", (String[]) dirs.toArray(new String[dirs.size()]));
    }

    public void setListUrls(String[] dirs) {
        properties.setArray("listUrls", (String[]) dirs);
    }

    public void setListUrls(Set dirs) {
        properties.setArray("listUrls", (String[]) dirs.toArray());
    }

    public String getDownloadDir() {
        return properties.get("downloadDir", "");
    }

    public File getDownloadDirectory() {
        return new File(properties.get("downloadDir", ""));
    }

    public void setDownloadDir(String dir) {
        properties.set("downloadDir", (new File(dir)).getPath() + File.separatorChar);
    }

    public String getClientVersion() {
        return properties.get("clientVersion", "1.2");
    }

    public boolean isActive() {
        return properties.getBoolean("connection.active", false);
    }

    public void setActive(boolean b) {
        properties.set("connection.active", "" + b);
    }

    public int getUploadSlots() {
        return properties.getInt("uploadSlots", 3);
    }

    public void setUploadSlots(int slots) {
        properties.set("uploadSlots", "" + slots);
    }

    public int getDownloadSlots() {
        return properties.getInt("downloadSlots", 10);
    }

    public void setDownloadSlots(int slots) {
        properties.set("downloadSlots", "" + slots);
    }

    public int getPort() {
        return properties.getInt("connection.port", 412);
    }

    public void setPort(String port) {
        properties.set("connection.port", port);
    }

    public String getIP() {
        try {
            return properties.get("connection.IP", InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            logger.error(e);
            return "0.0.0.0";
        }
    }

    public void setIP(String ip) {
        properties.set("connection.IP", ip);
    }

    public DataProperties getProperties(String prefix) {
        return properties.getChild(prefix);
    }

    public int getFreeUploadSlotCount() {
        return getUploadSlots() - usedUploadSlots;
    }

    public void releaseUploadSlot() {
        if (usedUploadSlots < getUploadSlots()) usedUploadSlots--;
    }

    public synchronized boolean reserveUploadSlot() {
        if (usedUploadSlots < getUploadSlots()) {
            usedUploadSlots++;
            return true;
        }
        return false;
    }

    public int getFreeDownloadSlotCount() {
        return getDownloadSlots() - usedDownloadSlots;
    }

    public void releaseDownloadSlot() {
        if (usedDownloadSlots < getDownloadSlots()) usedDownloadSlots--;
    }

    public synchronized boolean reserveDownloadSlot() {
        if (usedDownloadSlots < getDownloadSlots()) {
            usedDownloadSlots++;
            return true;
        }
        return false;
    }

    public synchronized void load() {
        try {
            properties.read(new FileInputStream(CONFIG_FILE));
            myself.read(properties.getChild("user"));
        } catch (IOException ioe) {
            try {
                File localFile = new File(CONFIG_FILE);
                localFile.createNewFile();
                save();
                load();
            } catch (Exception ex) {
                logger.error(ex);
            }
            logger.error(ioe);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public synchronized void save() {
        try {
            myself.write(properties.getChild("user"));
            File localFile = new File(CONFIG_FILE);
            localFile.createNewFile();
            properties.write(new FileOutputStream(localFile), "JavaDC Config");
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
