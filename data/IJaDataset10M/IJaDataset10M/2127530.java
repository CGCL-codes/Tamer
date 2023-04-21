package de.blitzcoder.collide;

import de.blitzcoder.collide.util.Log;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;

public class Config {

    private static final String FILE = "collide.props";

    private static Properties props = null;

    public static void load() {
        Log.log("Loading Config");
        props = new Properties();
        try {
            props.load(new FileInputStream(new File(FILE)));
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load " + FILE + ". CollIDE will now exit");
            System.exit(0);
        }
    }

    public static void save() {
        Log.log("Saving Config");
        setBooleanProperty("system.firstRun", false);
        try {
            props.store(new FileOutputStream(new File(FILE)), "CollIDE Config");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getProperty(String name) {
        return props.getProperty(name, "");
    }

    public static int getIntProperty(String name) {
        return Integer.parseInt(props.getProperty(name, "0"));
    }

    public static int getIntProperty(String name, int standard) {
        return Integer.parseInt(props.getProperty(name, "" + standard));
    }

    public static boolean getBooleanProperty(String name) {
        return Boolean.parseBoolean(props.getProperty(name, "false"));
    }

    public static File getFileProperty(String name) {
        String path = props.getProperty(name);
        return path != null ? new File(path) : null;
    }

    public static Font getFontProperty(String name) {
        return new Font(props.getProperty(name, "Arial"), getIntProperty(name + ".style", 0), getIntProperty(name + ".size", 15));
    }

    public static void setIntProperty(String name, int value) {
        props.setProperty(name, "" + value);
    }

    public static void setProperty(String name, String value) {
        props.setProperty(name, value);
    }

    public static void setBooleanProperty(String name, boolean value) {
        props.setProperty(name, "" + value);
    }

    public static void setFileProperty(String name, File file) {
        props.setProperty(name, file.getAbsolutePath());
    }

    public static void setRectangleProperty(String name, Rectangle rect) {
        props.setProperty(name + ".x", "" + rect.x);
        props.setProperty(name + ".y", "" + rect.y);
        props.setProperty(name + ".width", "" + rect.width);
        props.setProperty(name + ".height", "" + rect.height);
    }

    public static Rectangle getRectangleProperty(String name) {
        int x = getIntProperty(name + ".x");
        int y = getIntProperty(name + ".y");
        int width = getIntProperty(name + ".width");
        int height = getIntProperty(name + ".height");
        return new Rectangle(x, y, width, height);
    }
}
