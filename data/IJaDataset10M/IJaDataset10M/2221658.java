package fileHandling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import com.jme.util.CloneImportExport;
import logic.nodes.nodeSettings.Settings;

/**
 * Provides static methods to load and save {@link Settings}.
 * 
 * @author Wasserleiche
 */
public class SettingsLoader extends Loader {

    /**
	 * Returns the loaded {@link Settings} of the given texture-path. This method shall only be called if there is a 
	 * {@link Settings} stored in the {@link HashMap}!.
	 * @param settingsPath The path of the {@link Settings} to be retrieved.
	 * @return The {@link Settings}.
	 */
    protected static Settings getLoadedSettings(String settingsPath) {
        assert (settingsPath != null);
        return (Settings) loadedObjects.get(settingsPath).loadClone();
    }

    /**
	 * Creates a new {@link CloneImportExport}-object out of the given {@link Settings} and stores it in the 
	 * loadedObjects-{@link HashMap} with the path.
	 * @param modelPath The path that will become the key in the {@link HashMap}.
	 * @param model The {@link Settings} that has to be saved in a {@link CloneImportExport}-object.
	 */
    protected static void putLoadedSettings(String settingsPath, Settings settings) {
        assert (settingsPath != null && settings != null);
        CloneImportExport clone = new CloneImportExport();
        clone.saveClone(settings);
        loadedObjects.put(settingsPath, clone);
    }

    /**
	 * Creates a new {@link Settings}-Object by reading the given file.
	 * @param settingsFile The file to be read.
	 * @return A new {@link Settings}-Object. It contains either all information from the file or
	 * an error-message, if an error occurred.
	 */
    public static Settings loadSettings(String settingsFile) {
        if (objectIsLoaded(settingsFile)) return getLoadedSettings(settingsFile);
        Settings settings = new Settings();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(settingsFile));
            String newLine = reader.readLine();
            while (newLine != null) {
                String[] parts = newLine.split(":");
                String optionName = parts[0];
                String value = parts[1];
                String[] valueParts = value.split(";");
                value = valueParts[0];
                for (int i = 1; i < valueParts.length; i++) {
                    value += "\n" + valueParts[i];
                }
                settings.addOption(optionName, value);
                newLine = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            settings.setLastErrorMessage(e.getMessage());
        }
        return settings;
    }

    /**
	 * Writes all information of the given {@link Settings}-Object into the given file. If there is any error 
	 * while writing the file, the {@link Settings}-Object will contain an error-message. 
	 * @param settingsFile The file to write the information to.
	 * @param settings The {@link Settings}-Object whose information shall be writen.
	 */
    public static void saveSettings(String settingsFile, Settings settings) {
        settings.clearLastErrorMessage();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(settingsFile));
            for (String option : settings.getKeys()) {
                writer.write(option + ":" + settings.getValueOf(option));
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            settings.setLastErrorMessage(e.getMessage());
        }
    }
}
