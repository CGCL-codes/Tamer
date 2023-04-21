package ba_leipzig_lending_and_service_control_system.conroller;

import java.util.prefs.Preferences;

/**
 * Controller-Class to handle the registry
 *
 * @author Chris Hagen
 */
public class ctrlRegistry {

    /**
     * Gets the registry value with the given name.
     *
     * @param name  Name of the registry value
     * @return value
     */
    public String getRegistryValue(String name) {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        String val = prefs.get(name, "");
        if (name.equals("host") && val.equals("")) val = "localhost";
        if (name.equals("port") && val.equals("")) val = "1521";
        if (name.equals("language") && val.equals("")) val = "EN";
        return val;
    }

    /**
     * Sets the registry value with the given name.
     *
     * @param name  Name of the registry value
     * @param value Value
     */
    public void setRegistryValue(String name, String value) {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        prefs.put(name, value.trim());
    }
}
