package com.cosylab.vdct.utils;

import java.awt.Color;
import java.awt.Shape;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.cosylab.logging.DebugLogger;
import com.cosylab.vdct.visual.widget.custom.CustomWidgetFactory;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * @author ikriznar
 *
 */
public final class ConfigurationHelper {

    public static final String DEFAULT_PROPERTIES_FILE = "configuration.properties";

    public static final String SAVED_PROPERTIES_FILE = "saved.properties";

    public static final String CONFIGURATION_FOLDER = ".VisualDCT";

    private static File defaults;

    private static File configFolder;

    private static String configDir;

    private static final Logger debug = DebugLogger.getLogger(ConfigurationHelper.class.getName(), Level.INFO);

    private ConfigurationHelper() {
    }

    public static final Properties loadConfiguration() {
        Properties p = new Properties();
        try {
            p.load(ConfigurationHelper.class.getClassLoader().getResourceAsStream(DEFAULT_PROPERTIES_FILE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File f = new File(getSavedDefaultsFile().getParentFile(), DEFAULT_PROPERTIES_FILE);
            if (!f.exists()) {
                InputStream s = ConfigurationHelper.class.getClassLoader().getResourceAsStream(DEFAULT_PROPERTIES_FILE);
                copyFromJarToConfigFolderAndComment(s, DEFAULT_PROPERTIES_FILE);
            }
            if (f.exists()) {
                InputStream r = new FileInputStream(f);
                p.load(r);
                r.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            p.putAll(System.getProperties());
        } catch (SecurityException e) {
        }
        return p;
    }

    public static final File getConfigurationFolder() throws IOException {
        if (configFolder == null) {
            try {
                configFolder = new File(System.getProperty("user.home"));
            } catch (SecurityException e) {
            }
            configFolder = new File(configFolder, CONFIGURATION_FOLDER);
            debug.info("Using default configuration folder: " + configFolder.getPath());
            if (!configFolder.exists()) {
                debug.config("Default configuration folder not found: " + configFolder.getPath() + ", creating new.");
                configFolder.mkdirs();
            }
        }
        return configFolder;
    }

    public static final File getSavedDefaultsFile() throws IOException {
        if (defaults == null) {
            defaults = getConfigurationFolder();
            File config = new File(defaults, DEFAULT_PROPERTIES_FILE);
            defaults = new File(defaults, SAVED_PROPERTIES_FILE);
            if (!defaults.exists()) {
                defaults.createNewFile();
            }
            if (!config.exists()) {
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return defaults;
    }

    public static final Properties loadSavedDefaults() {
        Properties p = new Properties();
        try {
            File defaults = getSavedDefaultsFile();
            if (defaults.exists()) {
                p.load(new FileInputStream(defaults));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return p;
    }

    /**
	 * Helps get array of non-empty sting out of configuration string with ',' as separtor.. 
	 * @param s
	 * @return array with non-empty string or null
	 */
    public static final String[] splitAndTrim(String s) {
        if (s == null) {
            return null;
        }
        s = s.trim();
        String[] ss = s.split(",");
        if (ss.length == 0) {
            return null;
        }
        List<String> l = new ArrayList<String>(ss.length);
        for (int i = 0; i < ss.length; i++) {
            String sss = ss[i].trim();
            if (sss.length() > 0) {
                l.add(sss);
            }
        }
        if (l.size() == 0) {
            return null;
        }
        return l.toArray(new String[l.size()]);
    }

    /**
	 * This method checks the input </code>String</code> for any system variables of
	 * the form </code>${variable}</code> and replaces the content with corresponding
	 * values. If the system variable does not exist, it returns the input 
	 * </code>String</code>.
	 * @param input the input </code>String</code>
	 * @return a </code>String</code> where all names of system variables have
	 * been replaced by their values
	 */
    public static String checkForSystemVariables(String input) {
        if (input == null) return null;
        String changed = input;
        String unchanged;
        do {
            unchanged = changed;
            changed = replaceFirstSysVariable(unchanged);
        } while (!unchanged.equals(changed));
        return changed;
    }

    private static String replaceFirstSysVariable(String input) {
        int start = input.indexOf("${");
        if (start < 0) return input;
        int end = input.indexOf("}");
        if (end < 0 || start + 2 >= end) return input;
        String variableName = input.substring(start + 2, end);
        String variable = System.getProperty(variableName);
        if (variable == null) return input;
        return input.substring(0, start) + variable + input.substring(end + 1);
    }

    public static void saveDefaults(Properties prop) {
        try {
            File defaults = getSavedDefaultsFile();
            FileOutputStream str = new FileOutputStream(defaults);
            prop.store(str, "Defaults");
            str.flush();
            str.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static File findFile(String fileName) throws IOException {
        File result = null;
        File file = new File(fileName);
        if (file.exists()) {
            return file;
        }
        result = new File(getConfigurationFolder(), file.getPath());
        if (result.exists()) {
            return result;
        }
        result = new File(getConfigurationFolder(), file.getName());
        if (result.exists()) {
            return result;
        }
        URL url = ConfigurationHelper.class.getClassLoader().getResource(fileName);
        if (url != null) {
            try {
                result = new File(url.toURI());
                if (result.exists()) {
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            copyFromJarToConfigFolder(ConfigurationHelper.class.getClassLoader().getResourceAsStream(fileName), fileName);
        }
        url = ClassLoader.getSystemClassLoader().getResource(fileName);
        if (url != null) {
            try {
                result = new File(url.toURI());
                if (result.exists()) {
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            copyFromJarToConfigFolder(ClassLoader.getSystemClassLoader().getResourceAsStream(fileName), fileName);
        }
        result = new File(getConfigurationFolder(), file.getPath());
        if (result.exists()) {
            return result;
        }
        result = new File(getConfigurationFolder(), file.getName());
        if (result.exists()) {
            return result;
        }
        return null;
    }

    public static void copyFromJarToConfigFolder(InputStream file, String name) throws IOException {
        System.out.println("Extracting file '" + name + "' from jar to folder '" + getConfigurationFolder() + "'.");
        BufferedInputStream in = new BufferedInputStream(file, 1024);
        File f = new File(getConfigurationFolder(), name);
        OutputStream out = new BufferedOutputStream(new FileOutputStream(f), 1024);
        byte[] b = new byte[1024];
        int len = Math.min(in.available(), 1024);
        while (len > 0) {
            in.read(b, 0, len);
            out.write(b, 0, len);
            len = Math.min(in.available(), 1024);
        }
        out.flush();
        out.close();
        in.close();
    }

    public static void copyFromJarToConfigFolderAndComment(InputStream file, String name) throws IOException {
        System.out.println("Extracting file '" + name + "' from jar to folder '" + getConfigurationFolder() + "'.");
        BufferedReader in = new BufferedReader(new InputStreamReader(file));
        File f = new File(getConfigurationFolder(), name);
        PrintWriter out = new PrintWriter(new FileOutputStream(f));
        out.println("###");
        out.println("#");
        out.println("# This is configuration file for VisualDCT.");
        out.println("# Lines commented with '!' sign contain configuration parameters with default values.");
        out.println("# To modify parameter copy commented line, modify value and remove comment sign '!'.");
        out.println("# To reset all configuration parameters to default values delete or rename this file.");
        out.println("#");
        out.println("###");
        out.println();
        out.println();
        String line;
        while (in.ready()) {
            line = in.readLine();
            if (line.trim().length() == 0 || line.startsWith("#")) {
                out.println(line);
            } else {
                out.print('!');
                out.println(line);
            }
        }
        out.flush();
        out.close();
        in.close();
    }

    public static void saveDefaults(Properties prop, String name) {
        try {
            File defaults = getSavedDefaultsFile(name);
            try {
                FileOutputStream str = new FileOutputStream(defaults);
                prop.store(str, "Defaults");
                str.flush();
                str.close();
            } catch (SecurityException e) {
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static File getSavedDefaultsFile(String name) throws IOException {
        File file = getConfigurationFolder();
        file = new File(file, SAVED_PROPERTIES_FILE + "." + name);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (SecurityException e) {
        }
        return file;
    }

    public static Properties loadSavedDefaults(String name) {
        Properties p = new Properties();
        try {
            File defaults = getSavedDefaultsFile(name);
            try {
                if (defaults.exists()) {
                    p.load(new FileInputStream(defaults));
                }
            } catch (SecurityException e) {
                p.load(ConfigurationHelper.class.getResourceAsStream("/" + configDir + "/" + SAVED_PROPERTIES_FILE + "." + name));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return p;
    }

    public static String serialize(Color background) {
        if (background == null) return null;
        StringBuffer sb = new StringBuffer();
        sb.append(background.getRGB());
        return sb.toString();
    }

    public static String serialize(Shape frameShape) {
        if (frameShape == null) return null;
        if (frameShape == CustomWidgetFactory.RECTANGLE) {
            return "rect";
        } else if (frameShape == CustomWidgetFactory.ROUND_RECTANGLE) {
            return "roundrect";
        } else if (frameShape == CustomWidgetFactory.ELLIPSE) {
            return "ellipse";
        }
        return null;
    }

    public static Color deserializeColor(String property) {
        if (property != null) {
            return new Color(Integer.parseInt(property));
        }
        return null;
    }

    public static String serialize(Point point) {
        return new String(point.x + "," + point.y);
    }

    public static Point deserializePoint(String property) {
        String[] split = property.split(",");
        if (split.length == 2) {
            try {
                return new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            } catch (Exception ex) {
                throw new IllegalArgumentException("cannot deserialize Point from " + property);
            }
        } else {
            throw new IllegalArgumentException("cannot deserialize Point from " + property);
        }
    }

    public static String serialize(Rectangle rec) {
        return new String(rec.x + "," + rec.y + "," + rec.width + "," + rec.height);
    }

    public static Rectangle deserializeRectangle(String property) {
        String[] split = property.split(",");
        if (split.length == 4) {
            try {
                return new Rectangle(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
            } catch (Exception ex) {
                throw new IllegalArgumentException("cannot deserialize Rectangle from " + property);
            }
        } else {
            throw new IllegalArgumentException("cannot deserialize Rectangle from " + property);
        }
    }

    public static Shape deserializeShape(String property) {
        if ("rect".equals(property)) {
            return CustomWidgetFactory.RECTANGLE;
        }
        if ("ellipse".equals(property)) {
            return CustomWidgetFactory.ELLIPSE;
        }
        if ("roundrect".equals(property)) {
            return CustomWidgetFactory.ROUND_RECTANGLE;
        }
        return null;
    }

    public static void setConfigurationFolder(String configDir) {
        debug.fine("Setting configuration folder: " + configDir);
        ConfigurationHelper.configDir = configDir;
        configFolder = new File(configDir);
        try {
            if (!configFolder.exists()) {
                debug.warning("Configuration folder not found: " + configFolder.getAbsolutePath() + ", using default.");
                configFolder = null;
            }
        } catch (SecurityException e) {
        }
    }
}
