package jmxcollector;

public class Settings {

    public String nagiosResultFile;

    public int writeInterval;

    public static Settings SettingsInstance;

    public int SessionThreadPoolSize = 10;

    public int QueryThreadPoolSize = 20;

    private Settings() {
    }

    public static synchronized Settings getInstance() {
        if (SettingsInstance == null) {
            SettingsInstance = new Settings();
        }
        return SettingsInstance;
    }
}
