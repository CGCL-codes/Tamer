package org.armedbear.j;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

public final class Preferences {

    private Properties properties = new Properties();

    private ArrayList listeners;

    public static final File getPreferencesFile() {
        return File.getInstance(Directories.getEditorDirectory(), "prefs");
    }

    public static void editPrefs() {
        File prefs = getPreferencesFile();
        if (prefs == null) return;
        final Editor editor = Editor.currentEditor();
        Buffer buf = editor.openFile(prefs);
        if (buf != null) editor.activate(buf);
    }

    public synchronized void reload() {
        reloadInternal();
        firePreferencesChanged();
    }

    private void reloadInternal() {
        File file = getPreferencesFile();
        if (file == null || !file.isFile()) {
            properties = new Properties();
            return;
        }
        Properties temp = new Properties();
        try {
            InputStream in = file.getInputStream();
            temp.load(in);
            in.close();
        } catch (IOException e) {
            Log.error(e);
        }
        temp = canonicalize(temp);
        String themeName = temp.getProperty(Property.THEME.key());
        if (themeName == null || themeName.length() == 0) {
            properties = temp;
            return;
        }
        String themePath = temp.getProperty(Property.THEME_PATH.key());
        properties = loadTheme(themeName, themePath);
        properties.putAll(temp);
    }

    private static Properties canonicalize(Properties properties) {
        Properties newProperties = new Properties();
        for (Enumeration e = properties.keys(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            newProperties.put(key.toLowerCase(), properties.get(key));
        }
        return newProperties;
    }

    public synchronized void killTheme() {
        Iterator it = properties.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (key.startsWith("color.")) it.remove(); else if (key.indexOf(".color.") >= 0) it.remove(); else if (key.startsWith("style.")) it.remove(); else if (key.indexOf(".style.") >= 0) it.remove();
        }
    }

    private static Properties loadTheme(String themeName, String themePath) {
        Properties properties = new Properties();
        File file = getThemeFile(themeName, themePath);
        if (file != null && file.isFile()) {
            try {
                InputStream in = file.getInputStream();
                properties.load(in);
                in.close();
            } catch (IOException e) {
                Log.error(e);
            }
        }
        return canonicalize(properties);
    }

    private static File getThemeFile(String themeName, String themePath) {
        if (themeName == null) return null;
        themeName = stripQuotes(themeName);
        if (Utilities.isFilenameAbsolute(themeName)) return File.getInstance(themeName);
        if (themePath != null) {
            Path path = new Path(stripQuotes(themePath));
            String[] array = path.list();
            if (array != null) {
                for (int i = 0; i < array.length; i++) {
                    File dir = File.getInstance(array[i]);
                    if (dir != null && dir.isDirectory()) {
                        File themeFile = File.getInstance(dir, themeName);
                        if (themeFile != null && themeFile.isFile()) return themeFile;
                    }
                }
            }
        }
        String classPath = System.getProperty("java.class.path");
        if (classPath != null) {
            Path path = new Path(classPath);
            String[] array = path.list();
            if (array == null) return null;
            final File userDir = File.getInstance(System.getProperty("user.dir"));
            for (int i = 0; i < array.length; i++) {
                String pathComponent = array[i];
                if (pathComponent.endsWith("src")) {
                    File srcDir = File.getInstance(pathComponent);
                    if (srcDir != null && srcDir.isDirectory()) {
                        File parentDir = srcDir.getParentFile();
                        if (parentDir != null && parentDir.isDirectory()) {
                            File themeDir = File.getInstance(parentDir, "themes");
                            if (themeDir != null && themeDir.isDirectory()) {
                                File themeFile = File.getInstance(themeDir, themeName);
                                if (themeFile != null && themeFile.isFile()) return themeFile;
                            }
                        }
                    }
                } else {
                    String suffix = "j.jar";
                    if (pathComponent.endsWith(suffix)) {
                        String prefix = pathComponent.substring(0, pathComponent.length() - suffix.length());
                        File prefixDir;
                        if (prefix.length() == 0) {
                            prefixDir = userDir;
                        } else {
                            prefixDir = File.getInstance(userDir, prefix);
                        }
                        if (prefixDir != null && prefixDir.isDirectory()) {
                            File themeDir = File.getInstance(prefixDir, "themes");
                            if (themeDir != null && themeDir.isDirectory()) {
                                File themeFile = File.getInstance(themeDir, themeName);
                                if (themeFile != null && themeFile.isFile()) return themeFile;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public synchronized void setProperty(Property property, String value) {
        properties.setProperty(property.key(), value);
    }

    public synchronized void setProperty(Property property, int value) {
        properties.setProperty(property.key(), String.valueOf(value));
    }

    public synchronized void setProperty(String key, String value) {
        properties.setProperty(key.toLowerCase(), value);
    }

    public synchronized void removeProperty(String key) {
        properties.remove(key.toLowerCase());
    }

    public synchronized String getStringProperty(Property property) {
        String value = getProperty(property.key());
        if (value != null) return stripQuotes(value); else return (String) property.getDefaultValue();
    }

    public synchronized String getStringProperty(String key) {
        String value = getProperty(key);
        if (value != null) return stripQuotes(value); else return null;
    }

    public synchronized boolean getBooleanProperty(Property property) {
        String value = getProperty(property.key());
        if (value != null) {
            value = value.trim();
            if (value.equals("true") || value.equals("1")) return true;
            if (value.equals("false") || value.equals("0")) return false;
        }
        return ((Boolean) property.getDefaultValue()).booleanValue();
    }

    public synchronized boolean getBooleanProperty(Property property, boolean defaultValue) {
        return getBooleanProperty(property.key(), defaultValue);
    }

    public synchronized boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            value = value.trim();
            if (value.equals("true") || value.equals("1")) return true;
            if (value.equals("false") || value.equals("0")) return false;
        }
        return defaultValue;
    }

    public synchronized int getIntegerProperty(Property property) {
        String value = getProperty(property.key());
        if (value != null) {
            value = value.trim();
            if (value.length() > 0) {
                if (value.charAt(0) == '+') value = value.substring(1).trim();
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                }
            }
        }
        return ((Integer) property.getDefaultValue()).intValue();
    }

    public synchronized Color getColorProperty(String key) {
        String value = getStringProperty(key);
        if (value != null) return Utilities.getColor(value);
        return null;
    }

    private String getProperty(String key) {
        return properties.getProperty(key.toLowerCase());
    }

    private static String stripQuotes(String s) {
        final int length = s.length();
        if (length >= 2) {
            if (s.charAt(0) == '"' && s.charAt(length - 1) == '"') return s.substring(1, length - 1); else if (s.charAt(0) == '\'' && s.charAt(length - 1) == '\'') return s.substring(1, length - 1);
        }
        return s.trim();
    }

    public synchronized void addPreferencesChangeListener(PreferencesChangeListener listener) {
        if (listeners == null) listeners = new ArrayList();
        listeners.add(listener);
    }

    public synchronized void firePreferencesChanged() {
        if (listeners != null) for (int i = 0; i < listeners.size(); i++) ((PreferencesChangeListener) listeners.get(i)).preferencesChanged();
    }
}
