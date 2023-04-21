package gov.nasa.gsfc.visbard.model;

import gov.nasa.gsfc.visbard.util.VisbardException;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class SettingsManager {

    static org.apache.log4j.Category sLogger = org.apache.log4j.Category.getInstance(SettingsManager.class.getName());

    private static final String HEADER_TEXT = "ViSBARD Settings File";

    public static final String DEFAULT_SETTINGS_FILE = "ViSBARD.settings";

    private Properties fProperties = new Properties();

    private Vector fHolders = new Vector();

    private String fDefaultSettingsFilename = DEFAULT_SETTINGS_FILE;

    public SettingsManager() {
    }

    public void load() throws VisbardException {
        this.load(fDefaultSettingsFilename);
    }

    public void load(String filename) throws VisbardException {
        String defaultFilename = VisbardMain.getSettingsDir() + File.separator + DEFAULT_SETTINGS_FILE;
        File defaultFile = new File(defaultFilename);
        InputStream settingsInStreamFromFile = null;
        try {
            sLogger.info("Loading settings from : " + defaultFilename);
            settingsInStreamFromFile = new FileInputStream(defaultFile);
        } catch (FileNotFoundException fnf) {
            sLogger.info("Unable to load custom settings from user's settings directory (" + fnf.getMessage() + "); reverting to default settings");
            try {
                InputStream settingsInStreamFromJar = VisbardMain.class.getClassLoader().getResourceAsStream(filename);
                FileOutputStream settingsOutStream = new FileOutputStream(defaultFile);
                int c;
                while ((c = settingsInStreamFromJar.read()) != -1) settingsOutStream.write(c);
                settingsInStreamFromJar.close();
                settingsOutStream.close();
                settingsInStreamFromFile = new FileInputStream(defaultFile);
            } catch (IOException ioe) {
                sLogger.warn("Unable to copy default settings to user's settings directory (" + ioe.getMessage() + "); using default settings from ViSBARD distribution package");
                settingsInStreamFromFile = VisbardMain.class.getClassLoader().getResourceAsStream(filename);
            }
        }
        this.processSettingsFile(settingsInStreamFromFile, filename);
    }

    public void load(File file) throws VisbardException {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            throw new VisbardException("Unable to find properties file " + file);
        }
        this.processSettingsFile(is, file.getPath());
    }

    protected void processSettingsFile(InputStream is, String filename) throws VisbardException {
        try {
            fProperties.load(is);
            is.close();
        } catch (IOException ex) {
            throw new VisbardException("Failed to load properties file " + filename);
        }
        for (int i = 0; i < fHolders.size(); i++) {
            this.setSettings((SettingsHolder) fHolders.get(i));
        }
    }

    public void save(File file) throws VisbardException {
        sLogger.info("Saving settings to : " + file);
        for (int i = 0; i < fHolders.size(); i++) {
            this.getSettings((SettingsHolder) fHolders.get(i));
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
            throw new VisbardException("Unable to open file for output " + file);
        }
        try {
            fProperties.store(os, HEADER_TEXT);
            os.close();
        } catch (IOException ex) {
            throw new VisbardException("Failed to save properties file " + file);
        }
    }

    private boolean containsChar(String source, char c) {
        char[] str = source.toCharArray();
        for (int i = 0; i < str.length; i++) if (str[i] == c) return true;
        return false;
    }

    private void setSettings(SettingsHolder holder) {
        String holderID = holder.getHolderID();
        Iterator sets = fProperties.keySet().iterator();
        HashMap settings = new HashMap();
        while (sets.hasNext()) {
            String key = (String) sets.next();
            if (key.startsWith(holderID + "_")) {
                String prop = fProperties.getProperty(key);
                key = key.substring(holderID.length() + 1, key.length());
                settings.put(key, prop);
            }
        }
        holder.setSettings(settings);
    }

    private void getSettings(SettingsHolder holder) {
        HashMap settings = holder.getSettings();
        Iterator sets = settings.keySet().iterator();
        while (sets.hasNext()) {
            String key = (String) sets.next();
            String value = (String) settings.get(key);
            fProperties.put(holder.getHolderID() + "_" + key, value);
        }
    }

    public Properties getAllSettings() {
        Properties curProps = new Properties();
        for (int i = 0; i < fHolders.size(); i++) {
            HashMap settings = ((SettingsHolder) fHolders.get(i)).getSettings();
            Iterator sets = settings.keySet().iterator();
            while (sets.hasNext()) {
                String key = (String) sets.next();
                String value = (String) settings.get(key);
                curProps.put("OPTIONS_" + ((SettingsHolder) fHolders.get(i)).getHolderID() + "_" + key, value);
            }
        }
        return curProps;
    }

    public void setAllSettings(Properties newProps) {
        for (int i = 0; i < fHolders.size(); i++) {
            String holderID = ((SettingsHolder) fHolders.get(i)).getHolderID();
            Iterator sets = newProps.keySet().iterator();
            HashMap settings = new HashMap();
            while (sets.hasNext()) {
                String key = (String) sets.next();
                if (key.startsWith("OPTIONS_" + holderID + "_")) {
                    String prop = newProps.getProperty(key);
                    key = key.substring(holderID.length() + 1 + 8, key.length());
                    settings.put(key, prop);
                }
            }
            ((SettingsHolder) fHolders.get(i)).setSettings(settings);
        }
    }

    public void registerSettingsHolder(SettingsHolder holder) {
        if (this.containsChar(holder.getHolderID(), '_')) {
            sLogger.warn("Underscore character found in HolderID!");
            return;
        }
        setSettings(holder);
        Vector myVec = (Vector) fHolders.clone();
        if (!myVec.contains(holder)) {
            myVec.add(holder);
            fHolders = myVec;
        }
    }

    public void unregisterSettingsHolder(SettingsHolder holder) {
        getSettings(holder);
        Vector myVec = (Vector) fHolders.clone();
        myVec.remove(holder);
        fHolders = myVec;
    }

    public boolean getStereoEnabledApp() {
        Iterator sets = fProperties.keySet().iterator();
        HashMap settings = new HashMap();
        while (sets.hasNext()) {
            String key = (String) sets.next();
            if (key.equals("Universe_Stereo Enabled Application")) {
                String prop = fProperties.getProperty(key);
                return Boolean.valueOf(prop).booleanValue();
            }
        }
        return false;
    }
}
