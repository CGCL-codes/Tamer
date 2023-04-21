package com.maptales.mobile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Hashtable;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class Settings {

    String username = null;

    String pwd = null;

    String rootURL = "http://www.maptales.com";

    String gpsAdress = "none";

    String gpsName = "unknown GPS";

    int currentStory = -1;

    int lastMarker = -1;

    int loggingIntervall = 10;

    String serverLogin = "";

    String serverPassword = "";

    boolean doBeep = true;

    boolean debug = false;

    public Settings() {
    }

    public boolean load() {
        System.out.println("loading settings");
        try {
            if (System.getProperty("com.maptales.mobile.Debug") != null) {
                System.out.println("debug: " + System.getProperty("com.maptales.mobile.Debug"));
                this.debug = System.getProperty("com.maptales.mobile.Debug").equals("true");
            }
            if (System.getProperty("com.maptales.mobile.Username") != null) {
                this.username = (String) System.getProperty("com.maptales.mobile.Username");
            }
            if (System.getProperty("com.maptales.mobile.Password") != null) {
                this.pwd = (String) System.getProperty("com.maptales.mobile.Password");
            }
            if (System.getProperty("com.maptales.mobile.LoggingIntervall") != null) {
                String intervall = (String) System.getProperty("com.maptales.mobile.LoggingIntervall");
                this.loggingIntervall = Integer.parseInt(intervall);
            }
            if (System.getProperty("com.maptales.mobile.gpsAdress") != null) {
                this.gpsAdress = (String) System.getProperty("com.maptales.mobile.gpsAdress");
            }
            if (System.getProperty("com.maptales.mobile.ServerURL") != null) {
                this.rootURL = (String) System.getProperty("com.maptales.mobile.ServerURL");
            }
            if (System.getProperty("com.maptales.mobile.ServerLogin") != null) {
                this.serverLogin = (String) System.getProperty("com.maptales.mobile.ServerLogin");
            }
            if (System.getProperty("com.maptales.mobile.ServerPassword") != null) {
                this.serverPassword = (String) System.getProperty("com.maptales.mobile.ServerPassword");
            }
            if (System.getProperty("com.maptales.mobile.CurrentStory") != null) {
                String story = (String) System.getProperty("com.maptales.mobile.CurrentStory");
                this.currentStory = Integer.parseInt(story);
            }
            if (System.getProperty("com.maptales.mobile.LastMarker") != null) {
                String marker = (String) System.getProperty("com.maptales.mobile.LastMarker");
                this.lastMarker = Integer.parseInt(marker);
            }
            RecordStore rs = RecordStore.openRecordStore("MaptalesSettings", true);
            if (rs.getNumRecords() < 1) {
                rs.closeRecordStore();
                return false;
            }
            RecordEnumeration recordenum = rs.enumerateRecords(null, null, false);
            byte[] data = recordenum.nextRecord();
            ByteArrayInputStream bin = new ByteArrayInputStream(data);
            DataInputStream in = new DataInputStream(bin);
            Hashtable settings = new Hashtable();
            try {
                while (in.available() > 0) {
                    String key = in.readUTF();
                    String value = in.readUTF();
                    settings.put(key, value);
                }
            } catch (Exception ioex) {
                ioex.printStackTrace();
            }
            in.close();
            bin.close();
            rs.closeRecordStore();
            if (settings.get("com.maptales.mobile.Username") != null) {
                this.username = (String) settings.get("com.maptales.mobile.Username");
            }
            if (settings.get("com.maptales.mobile.Password") != null) {
                this.pwd = (String) settings.get("com.maptales.mobile.Password");
            }
            if (settings.get("com.maptales.mobile.LoggingIntervall") != null) {
                String intervall = (String) settings.get("com.maptales.mobile.LoggingIntervall");
                this.loggingIntervall = Integer.parseInt(intervall);
            }
            if (settings.get("com.maptales.mobile.gpsAdress") != null) {
                this.gpsAdress = (String) settings.get("com.maptales.mobile.gpsAdress");
            }
            if (settings.get("com.maptales.mobile.ServerLogin") != null) {
                this.serverLogin = (String) settings.get("com.maptales.mobile.ServerLogin");
            }
            if (settings.get("com.maptales.mobile.ServerPassword") != null) {
                this.serverPassword = (String) settings.get("com.maptales.mobile.ServerPassword");
            }
            if (settings.get("com.maptales.mobile.CurrentStory") != null) {
                String story = (String) settings.get("com.maptales.mobile.CurrentStory");
                this.currentStory = Integer.parseInt(story);
            }
            if (settings.get("com.maptales.mobile.LastMarker") != null) {
                String marker = (String) settings.get("com.maptales.mobile.LastMarker");
                this.lastMarker = Integer.parseInt(marker);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void storeToRMS() {
        RecordStore rs = null;
        try {
            try {
                RecordStore.deleteRecordStore("MaptalesSettings");
            } catch (Exception e) {
            }
            rs = RecordStore.openRecordStore("MaptalesSettings", true);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bout);
            out.writeUTF("com.maptales.mobile.Username");
            out.writeUTF(this.username);
            out.writeUTF("com.maptales.mobile.Password");
            out.writeUTF(this.pwd);
            out.writeUTF("com.maptales.mobile.LoggingIntervall");
            out.writeUTF(Integer.toString(this.loggingIntervall));
            out.writeUTF("com.maptales.mobile.gpsAdress");
            out.writeUTF(this.gpsAdress);
            out.writeUTF("com.maptales.mobile.ServerURL");
            out.writeUTF(this.rootURL);
            out.writeUTF("com.maptales.mobile.ServerLogin");
            out.writeUTF(this.serverLogin);
            out.writeUTF("com.maptales.mobile.ServerPassword");
            out.writeUTF(this.serverPassword);
            out.writeUTF("com.maptales.mobile.CurrentStory");
            out.writeUTF(Integer.toString(this.currentStory));
            out.writeUTF("com.maptales.mobile.LastMarker");
            out.writeUTF(Integer.toString(this.lastMarker));
            byte[] data = bout.toByteArray();
            rs.addRecord(data, 0, data.length);
            out.close();
            bout.close();
            rs.closeRecordStore();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs == null) rs.closeRecordStore();
            } catch (RecordStoreNotOpenException e) {
                e.printStackTrace();
            } catch (RecordStoreException e) {
                e.printStackTrace();
            }
        }
    }

    public String getRootURL() {
        return rootURL;
    }

    public void setRootURL(String rootURL) {
        this.rootURL = rootURL;
    }

    public void setGpsAdress(String gpsAdress) {
        this.gpsAdress = gpsAdress;
    }

    public void setGpsName(String gpsName) {
        this.gpsName = gpsName;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGpsAdress() {
        return gpsAdress;
    }

    public String getGpsName() {
        return gpsName;
    }

    public String getPwd() {
        return pwd;
    }

    public String getUsername() {
        return username;
    }

    public int getCurrentStory() {
        return currentStory;
    }

    public void setCurrentStory(int currentStory) {
        this.currentStory = currentStory;
    }

    public int getLastMarker() {
        return lastMarker;
    }

    public void setLastMarker(int lastMarker) {
        this.lastMarker = lastMarker;
    }

    public boolean isDoBeep() {
        return doBeep;
    }

    public void setDoBeep(boolean beep) {
        doBeep = beep;
    }

    public boolean isDebug() {
        return debug;
    }

    public int getLoggingIntervall() {
        return loggingIntervall;
    }

    public void setLoggingIntervall(int loggingIntervall) {
        this.loggingIntervall = loggingIntervall;
    }

    public String getServerLogin() {
        return serverLogin;
    }

    public void setServerLogin(String serverLogin) {
        this.serverLogin = serverLogin;
    }

    public String getServerPassword() {
        return serverPassword;
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }
}
