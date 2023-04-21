package com.diccionarioderimas;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import com.diccionarioderimas.gui.dialogs.AboutDialog;

public class UserPreferences {

    private long uniqueId = new Random().nextLong();

    private Date lastUpdated;

    private String theme = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";

    private boolean yEquivalence = true;

    private boolean zEquivalence = false;

    private boolean ceEquivalence = false;

    private boolean liEquivalence = false;

    private boolean niEquivalence = false;

    private boolean viewCounter = false;

    private boolean stayOnTop = false;

    private boolean proxy = false;

    private String proxyHTTP = "12.47.164.114";

    private int proxyPort = 8888;

    private String proxyUser = "Opcional";

    private String proxyPassword = "Opcional";

    private String basePath;

    public UserPreferences(String basePath) {
        this.basePath = basePath;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            lastUpdated = df.parse(AboutDialog.DATE);
        } catch (ParseException e1) {
        }
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(basePath + "preferences.properties"));
            String id = prop.getProperty("uniqueId");
            String lastUp = prop.getProperty("lastUpdated");
            String the = prop.getProperty("theme");
            String yEquival = prop.getProperty("yEquivalence");
            String zEquival = prop.getProperty("zEquivalence");
            String ceEquival = prop.getProperty("ceEquivalence");
            String liEquival = prop.getProperty("liEquivalence");
            String niEquival = prop.getProperty("niEquivalence");
            String counter = prop.getProperty("viewCounter");
            String top = prop.getProperty("stayOnTop");
            String proxy = prop.getProperty("proxy");
            String proxyHTTP = prop.getProperty("proxyHTTP");
            String proxyPort = prop.getProperty("proxyPort");
            String proxyUser = prop.getProperty("proxyUser");
            String proxyPassword = prop.getProperty("proxyPassword");
            if (id != null) uniqueId = Long.parseLong(id);
            if (lastUp != null) lastUpdated = new Date(Long.parseLong(lastUp));
            if (the != null) theme = the;
            if (yEquival != null) yEquivalence = Boolean.parseBoolean(yEquival);
            if (zEquival != null) zEquivalence = Boolean.parseBoolean(zEquival);
            if (ceEquival != null) ceEquivalence = Boolean.parseBoolean(ceEquival);
            if (liEquival != null) liEquivalence = Boolean.parseBoolean(liEquival);
            if (ceEquival != null) niEquivalence = Boolean.parseBoolean(niEquival);
            if (counter != null) viewCounter = Boolean.parseBoolean(counter);
            if (top != null) stayOnTop = Boolean.parseBoolean(top);
            if (proxy != null) this.proxy = Boolean.parseBoolean(proxy);
            if (proxyHTTP != null) this.proxyHTTP = proxyHTTP;
            if (proxyPort != null) this.proxyPort = Integer.parseInt(proxyPort);
            if (proxyUser != null) this.proxyUser = proxyUser;
            if (proxyPassword != null) this.proxyPassword = proxyPassword;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        Properties prop = new Properties();
        try {
            prop.setProperty("uniqueId", uniqueId + "");
            prop.setProperty("lastUpdated", lastUpdated.getTime() + "");
            prop.setProperty("theme", theme);
            prop.setProperty("yEquivalence", yEquivalence + "");
            prop.setProperty("zEquivalence", zEquivalence + "");
            prop.setProperty("ceEquivalence", ceEquivalence + "");
            prop.setProperty("liEquivalence", liEquivalence + "");
            prop.setProperty("niEquivalence", niEquivalence + "");
            prop.setProperty("viewCounter", viewCounter + "");
            prop.setProperty("stayOnTop", stayOnTop + "");
            prop.setProperty("proxy", proxy + "");
            prop.setProperty("proxyHTTP", proxyHTTP);
            prop.setProperty("proxyPort", proxyPort + "");
            prop.setProperty("proxyUser", proxyUser);
            prop.setProperty("proxyPassword", proxyPassword);
            prop.store(new FileOutputStream(basePath + "preferences.properties"), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean isYEquivalence() {
        return yEquivalence;
    }

    public void setYEquivalence(boolean equivalence) {
        yEquivalence = equivalence;
    }

    public boolean isZEquivalence() {
        return zEquivalence;
    }

    public void setZEquivalence(boolean equivalence) {
        zEquivalence = equivalence;
    }

    public boolean isCeEquivalence() {
        return ceEquivalence;
    }

    public void setCeEquivalence(boolean ceEquivalence) {
        this.ceEquivalence = ceEquivalence;
    }

    public String getBasePath() {
        return basePath;
    }

    public boolean isLiEquivalence() {
        return liEquivalence;
    }

    public void setLiEquivalence(boolean liEquivalence) {
        this.liEquivalence = liEquivalence;
    }

    public boolean isNiEquivalence() {
        return niEquivalence;
    }

    public void setNiEquivalence(boolean niEquivalence) {
        this.niEquivalence = niEquivalence;
    }

    public boolean isViewCounter() {
        return viewCounter;
    }

    public void setViewCounter(boolean viewCounter) {
        this.viewCounter = viewCounter;
    }

    public boolean isStayOnTop() {
        return stayOnTop;
    }

    public void setStayOnTop(boolean stayOnTop) {
        this.stayOnTop = stayOnTop;
    }

    public boolean isProxy() {
        return proxy;
    }

    public void setProxy(boolean proxy) {
        this.proxy = proxy;
    }

    public String getProxyHTTP() {
        return proxyHTTP;
    }

    public void setProxyHTTP(String proxyHTTP) {
        this.proxyHTTP = proxyHTTP;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }
}
