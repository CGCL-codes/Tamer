package util;

import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

/**
 * Manages the custom settings made by the user on this machine.
 */
public class Settings {

    public static final String FRAME_LOCATION = "Frame Location", FRAME_SIZE = "Frame Size", LOCAL_LIBRARY = "Local Library";

    public static final char DELIM = '_';

    private final HashMap<String, String> values = new HashMap<String, String>();

    private final File settingsFile;

    private Settings(File settingsFile) {
        this.settingsFile = settingsFile;
        if (settingsFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(settingsFile));
                String s = reader.readLine();
                while (s != null) {
                    int split = s.indexOf(DELIM);
                    values.put(s.substring(0, split), s.substring(split + 1, s.length()));
                    s = reader.readLine();
                }
                reader.close();
                return;
            } catch (Exception e) {
                System.out.println("Settings file is corrupted, loading the default settings.");
            }
        } else {
            System.out.println("No settings file found...putting default settings.");
        }
        putDefaultSettings();
    }

    /**
	 * Stores this key-value pair in the settings.
	 * 
	 * @param key
	 *            The type of settings.
	 * @param value
	 *            The value for the setting.
	 */
    public void putSetting(String key, String value) {
        values.put(key, value);
    }

    /**
	 * Gets the setting which corresponds to the input.
	 * 
	 * @param key
	 *            The type of setting to retrieve.
	 * @return The settings which corresponds to the input type.
	 */
    public String getSetting(String key) {
        return values.get(key);
    }

    /**
	 * Gets the setting which corresponds to the input.
	 * 
	 * @param key
	 *            The type of setting to retrieve.
	 * @return The settings which corresponds to the input type.
	 */
    public boolean getSettingAsBoolean(String key) {
        return "true".equalsIgnoreCase(values.get(key));
    }

    /**
	 * Gets the setting which corresponds to the input.
	 * 
	 * @param type
	 *            The type of setting to retrieve.
	 * @return The settings which corresponds to the input type. If there is no
	 *         such settings, -1 is returned.
	 */
    public int getSettingAsInt(String key) {
        String s = values.get(key);
        if (s == null) return -1;
        return Integer.valueOf(s);
    }

    /**
	 * Gets the setting which corresponds to the input.
	 * 
	 * @param type
	 *            The type of setting to retrieve.
	 * @return The settings which corresponds to the input type.
	 */
    public Point getSettingAsPoint(String key) {
        String s = values.get(key);
        if (s == null) return null;
        int split = s.indexOf(' ');
        return new Point(Integer.valueOf(s.substring(0, split)), Integer.valueOf(s.substring(split + 1, s.length())));
    }

    /**
	 * Gets the setting which corresponds to the input.
	 * 
	 * @param type
	 *            The type of setting to retrieve.
	 * @return The settings which corresponds to the input type.
	 */
    public Dimension getSettingAsDimension(String key) {
        String s = values.get(key);
        if (s == null) return null;
        int split = s.indexOf(' ');
        return new Dimension(Integer.valueOf(s.substring(0, split)), Integer.valueOf(s.substring(split + 1, s.length())));
    }

    /**
	 * Changes all the settings to their defaults.
	 */
    public void putDefaultSettings() {
        values.clear();
        putSetting(FRAME_LOCATION, "0 0");
        putSetting(FRAME_SIZE, "800 600");
        putSetting(LOCAL_LIBRARY, FileSystem.getDefaultLibraryPath());
    }

    /**
	 * Writes all of the current settings into a file.
	 */
    public void saveSettings() throws IOException {
        PrintStream stream = new PrintStream(settingsFile);
        for (String key : values.keySet()) {
            String value = values.get(key);
            stream.print(key);
            stream.print(DELIM);
            stream.println(value);
        }
        stream.close();
    }

    private static Settings commonSettings = null;

    public static Settings get() {
        if (commonSettings == null) {
            commonSettings = new Settings(FileSystem.virtualChild(FileSystem.getApplicationDataFolder(), "settings"));
        }
        return commonSettings;
    }
}
