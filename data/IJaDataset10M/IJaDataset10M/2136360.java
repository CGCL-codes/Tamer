package jpcsp.settings;

/**
 * Simple container for a registered settings listener.
 *
 * See
 *     Settings.registerSettingsListener()
 * for the the registration of settings listeners.
 * 
 * @author gid15
 *
 */
public class SettingsListenerInfo {

    private String name;

    private String key;

    private ISettingsListener listener;

    public SettingsListenerInfo(String name, String key, ISettingsListener listener) {
        this.name = name;
        this.key = key;
        this.listener = listener;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public ISettingsListener getListener() {
        return listener;
    }

    /**
	 * Test if the current object is matching the given name and key values.
	 * A null value matches any value.
	 *
	 * @param name     name, or null to match any name.
	 * @param key      key, or null to match any key.
	 * @return
	 */
    public boolean equals(String name, String key) {
        if (name != null) {
            if (!this.name.equals(name)) {
                return false;
            }
        }
        if (key != null) {
            if (!this.key.equals(key)) {
                return false;
            }
        }
        return true;
    }
}
