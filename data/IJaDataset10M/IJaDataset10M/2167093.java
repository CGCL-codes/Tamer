package net.sourceforge.processdash;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.processdash.util.FileProperties;
import net.sourceforge.processdash.util.RobustFileWriter;

public class InternalSettings extends Settings {

    private static FileProperties fsettings = null;

    private static String settingsFile = null;

    private static String settingsFileRename = null;

    private static long settingsFileTimestamp;

    private static PropertyChangeSupport propSupport = new PropertyChangeSupport(InternalSettings.class);

    public static final String sep = System.getProperty("file.separator");

    private static boolean dirty;

    private static boolean disableChanges;

    private static final Logger logger = Logger.getLogger(InternalSettings.class.getName());

    public static void initialize(String settingsFilename) throws IOException {
        checkPermission("initialize");
        if (settings != null) return;
        String cwd = System.getProperty("user.dir");
        InputStream in;
        defaults = defaultProperties();
        try {
            in = Settings.class.getResourceAsStream("pspdash.ad");
            if (in != null) {
                Properties systemDefaults = new Properties(defaults);
                systemDefaults.load(in);
                in.close();
                filterOperatingSystemSpecificSettings(systemDefaults);
                addLegacyPrefNames(systemDefaults.getProperty("legacyUserPrefs", ""));
                defaults = systemDefaults;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setReadOnly(readOnly);
        Properties propertyComments = new Properties();
        try {
            propertyComments.load(Settings.class.getResourceAsStream("pspdash.ad-comments"));
        } catch (Exception e0) {
        }
        settings = fsettings = new FileProperties(defaults, propertyComments);
        fsettings.setDateStamping(false);
        dirty = disableChanges = false;
        File settingsFile;
        if (settingsFilename != null && settingsFilename.length() != 0) {
            settingsFile = new File(settingsFilename);
            homedir = settingsFile.getParent();
            if (homedir == null) homedir = cwd;
        } else {
            String filename = checkForOldSettingsFile(cwd, getSettingsFilename());
            if (settingsFileRename != null) dirty = true;
            settingsFilename = cwd + sep + filename;
            settingsFile = new File(settingsFilename);
            homedir = cwd;
        }
        if (!settingsFile.isFile()) {
            dirty = true;
            System.out.println("No user preferences file found; using system-wide defaults.");
        } else {
            try {
                in = new FileInputStream(settingsFile);
                fsettings.load(in);
                in.close();
                settingsFileTimestamp = settingsFile.lastModified();
            } catch (Exception e) {
                IOException ioe = new IOException(settingsFilename);
                ioe.initCause(e);
                throw ioe;
            }
        }
        InternalSettings.settingsFile = settingsFilename;
        fsettings.setHeader(PROPERTIES_FILE_HEADER);
        fsettings.setKeepingStrangeKeys(true);
    }

    private static void filterOperatingSystemSpecificSettings(Properties settings) {
        String os = "os-" + getOSPrefix() + ".";
        Map matchingValues = new HashMap();
        for (Iterator i = settings.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry) i.next();
            String key = (String) e.getKey();
            if (key.startsWith("os-")) {
                if (key.startsWith(os)) matchingValues.put(key.substring(os.length()), e.getValue());
                i.remove();
            }
        }
        settings.putAll(matchingValues);
    }

    static String getOSPrefix() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("windows") != -1) return "windows"; else if (os.startsWith("mac")) return "mac"; else if (os.indexOf("linux") != -1) return "linux"; else return "unix";
    }

    private static void addLegacyPrefNames(String spec) {
        if (spec != null) {
            String[] legacyPrefs = spec.trim().split("\\s+");
            legacyPrefNames.addAll(Arrays.asList(legacyPrefs));
            legacyPrefNames.remove("");
        }
    }

    public static final String getSettingsFilename() {
        return "pspdash.ini";
    }

    /**
   * Historically, we have used ".pspdash" for the settings file on Mac and
   * Unix, and "pspdash.ini" on Windows.  However, this creates problems when
   * a dashboard is shared across platforms.  To resolve this problem, we
   * must standardize on a single cross-platform filename.  However, existing
   * users have historical data with the legacy filename.  If a user has a
   * legacy file, we need to load it and migrate it to the new name.  This
   * method checks for the existence of a legacy settings file, and possibly
   * prepares for a file rename.
   * 
   * @param cwd the directory to look in
   * @param desiredName the name we'd like the file to have
   */
    private static String checkForOldSettingsFile(String cwd, String desiredName) {
        if ("windows".equals(getOSPrefix())) return desiredName;
        File desiredFile = new File(cwd, desiredName);
        if (desiredFile.isFile()) return desiredName;
        File oldFile = new File(cwd, ".pspdash");
        if (oldFile.isFile()) {
            settingsFileRename = desiredFile.getPath();
            return ".pspdash";
        }
        return desiredName;
    }

    public static String getSettingsFileName() {
        checkPermission("getFileName");
        return settingsFile;
    }

    private static final String PROPERTIES_FILE_HEADER = "User preferences for the PSP Dashboard tool " + "(NOTE: When specifying names of files or directories within this " + "file, use a forward slash as a separator.  It will be translated " + "into an appropriate OS-specific directory separator automatically.)";

    public static void set(String name, String value, String comment) {
        set0(name, value, comment);
    }

    public static void set(String name, String value) {
        set0(name, value, null);
    }

    private static synchronized void set0(String name, String value, String comment) {
        checkPermission("write." + name);
        if (disableChanges) return;
        String oldValue = getVal(name);
        boolean needsSave = true;
        if (isPrefName(name)) {
            needsSave = setPrefImpl(name, value);
        } else if (value == null) {
            fsettings.remove(name);
            value = fsettings.getProperty(name);
        } else {
            fsettings.put(name, value);
            if (comment != null) fsettings.setComment(name, comment);
        }
        String propName = SYS_PROP_PREFIX + name;
        if (System.getProperty(propName) != null) System.getProperties().remove(propName);
        if (needsSave) {
            serializable = null;
            dirty = true;
            saveSettings();
        }
        maybeFirePropertyChange(name, oldValue, value);
    }

    private static boolean setPrefImpl(String name, String value) {
        if (name.startsWith(PREFS_PREFIX)) name = name.substring(PREFS_PREFIX.length());
        if (value == null) userPreferences.remove(name); else userPreferences.put(name, value);
        if (fsettings.containsKey(name)) {
            fsettings.remove(name);
            return true;
        } else {
            return false;
        }
    }

    public static void setDefaultValue(String name, String defaultValue) {
        checkPermission("write." + name);
        if (disableChanges) return;
        String oldValue = fsettings.getProperty(name);
        if (defaultValue == null) {
            defaults.remove(name);
        } else {
            defaults.put(name, defaultValue);
        }
        String newValue = fsettings.getProperty(name);
        maybeFirePropertyChange(name, oldValue, newValue);
    }

    public static String getExtendableVal(String name, String sep) {
        String result = getVal(name);
        String extra = getVal("additional." + name);
        if (extra != null) {
            if (result == null) result = extra; else result = result + sep + extra;
            set(name, result);
            set("additional." + name, null);
        }
        return result;
    }

    /**
     * Return an explicit value for the setting (i.e., one that appears in the
     * user settings file). If a system property override has been set or if a
     * default value is in place, those will be ignored by this method.
     * 
     * @param name
     *            the name of a setting
     * @return an explicitly set value of the setting, or null if the value has
     *         not been explicitly set.
     * @since 1.12.1.1
     */
    public static String getExplicitVal(String name) {
        if (settings != null && settings.containsKey(name)) return settings.getProperty(name); else return null;
    }

    /**
     * @return the default value for this setting (i.e., the value that would be
     *         used if the setting does not appear in the user settings file).
     * @since 1.12.1.1
     */
    public static String getDefaultVal(String name) {
        return defaults.getProperty(name);
    }

    static synchronized void saveSettings() {
        if (isReadOnly()) return;
        AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                if (fsettings != null) try {
                    String oldName, destName;
                    if (settingsFileRename == null) {
                        oldName = null;
                        destName = settingsFile;
                    } else {
                        oldName = settingsFile;
                        destName = settingsFileRename;
                    }
                    File destFile = new File(destName);
                    Writer out = new RobustFileWriter(destFile);
                    fsettings.store(out);
                    out.close();
                    if (oldName != null) {
                        new File(oldName).delete();
                        settingsFile = destName;
                        settingsFileRename = null;
                    }
                    settingsFileTimestamp = destFile.lastModified();
                    dirty = false;
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Unable to save settings file.", e);
                }
                return null;
            }
        });
    }

    public static synchronized void maybeReload() {
        checkPermission("reload");
        File srcFile = new File(settingsFile);
        if (!srcFile.isFile()) return;
        long timestamp = srcFile.lastModified();
        if (timestamp == 0 || timestamp == settingsFileTimestamp) return;
        Properties nullProps = new Properties();
        FileProperties newProps = new FileProperties(nullProps, nullProps);
        try {
            FileInputStream in = new FileInputStream(srcFile);
            newProps.load(in);
            in.close();
        } catch (Exception e) {
            return;
        }
        Set<String> keysToDelete = new HashSet(fsettings.keySet());
        for (Map.Entry e : newProps.entrySet()) {
            String key = (String) e.getKey();
            keysToDelete.remove(key);
            if (isSetBySystemProperty(key)) continue;
            String value = (String) e.getValue();
            set(key, value);
        }
        for (String key : keysToDelete) {
            if (isSetBySystemProperty(key) == false) set(key, null);
        }
        settingsFileTimestamp = timestamp;
        dirty = false;
    }

    private static boolean isSetBySystemProperty(String name) {
        String propName = SYS_PROP_PREFIX + name;
        return System.getProperty(propName) != null;
    }

    public static synchronized boolean isDirty() {
        return dirty;
    }

    public static void setReadOnly(boolean ro) {
        checkPermission("setReadOnly");
        Settings.readOnly = ro;
        if (defaults != null) {
            if (ro) defaults.put(READ_ONLY, "true"); else defaults.remove(READ_ONLY);
        }
    }

    public static void setReadOnlyFollowMode() {
        setReadOnly(true);
        Settings.followMode = true;
    }

    static synchronized void setDisableChanges(boolean disable) {
        disableChanges = disable;
        logger.fine("Settings changes " + (disableChanges ? "disabled." : "enabled."));
    }

    public static void addPropertyChangeListener(PropertyChangeListener l) {
        propSupport.addPropertyChangeListener(l);
    }

    public static void addPropertyChangeListener(String propertyName, PropertyChangeListener l) {
        propSupport.addPropertyChangeListener(propertyName, l);
    }

    public static void removePropertyChangeListener(PropertyChangeListener l) {
        propSupport.removePropertyChangeListener(l);
    }

    public static void removePropertyChangeListener(String propertyName, PropertyChangeListener l) {
        propSupport.removePropertyChangeListener(propertyName, l);
    }

    private static void maybeFirePropertyChange(final String propertyName, final String oldValue, final String newValue) {
        if (!eq(oldValue, newValue)) {
            AccessController.doPrivileged(new PrivilegedAction() {

                public Object run() {
                    propSupport.firePropertyChange(propertyName, oldValue, newValue);
                    return null;
                }
            });
        }
    }

    private static boolean eq(String a, String b) {
        if (a == b) return true;
        if (a == null) return false;
        return a.equals(b);
    }

    static void loadLocaleSpecificDefaults(ResourceBundle resources) {
        checkPermission("initialize");
        defaults.put("dateFormat", resources.getString("Date_Format"));
        defaults.put("dateTimeFormat", resources.getString("Date_Time_Format"));
        defaults.put("http.charset", resources.getString("HTTP_charset_"));
        defaults.put("window.title", resources.getString("Window_Title"));
    }

    /** This main method is used to merge settings from one or more external
   * sources into a user's settings file.  It is typically only called by
   * the dashboard installer as an optional post-installation step.
   */
    public static void main(String[] args) {
        if (args.length < 2) return;
        File destDir = new File(args[0]);
        if (!destDir.isDirectory()) return;
        String filename = checkForOldSettingsFile(destDir.getPath(), getSettingsFilename());
        String rename = settingsFileRename;
        File settingsFile = new File(destDir, filename);
        try {
            initialize(settingsFile.getPath());
            settingsFileRename = rename;
            for (int i = args.length; i-- > 1; ) tryToMerge(args[i]);
        } catch (Exception e) {
        }
    }

    private static void tryToMerge(String url) {
        if ("none".equalsIgnoreCase(url)) return;
        Properties nullProps = new Properties();
        FileProperties propsIn = new FileProperties(nullProps, nullProps);
        try {
            propsIn.load(new URL(url).openStream());
        } catch (Exception e) {
        }
        if (propsIn.isEmpty()) return;
        for (Iterator i = propsIn.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry) i.next();
            String propKey = ((String) e.getKey()).trim();
            if (!propKey.startsWith(MERGE_PROP_PREFIX)) continue;
            String settingName = propKey.substring(MERGE_PROP_PREFIX.length());
            if (getVal(settingName) == null) {
                String settingVal = ((String) e.getValue()).trim();
                set(settingName, settingVal);
            }
        }
    }

    private static final String MERGE_PROP_PREFIX = "pdash.";
}
