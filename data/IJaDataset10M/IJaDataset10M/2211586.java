package hambo.config;

/**
 * The <code>ConfigManager</code> manages configuration data for Hambo components and
 * applications. A configuration object is associated to a name. The name shall
 * uniquely identify the component/application. How the name are used are up
 * to implementations of <code>ConfigManager</code>.
 * <br><br>
 * The default implementation is {@link PropertyConfigManager}. It uses the name as
 * a property name prefix.
 * <br><br>
 * Example using the default implementation:
 * <pre>
 * Config config = ConfigManager.getConfig("app.calendar");
 * String name  = config.getName("name");   // Mapped to the property "app.calendar.name"
 * String class = config.getName("class");  // Mapped to the property "app.calendar.class"
 * </pre>
 */
public abstract class ConfigManager {

    private static final String ILLEGAL_STATE_MSG = "The ConfigManager has not been properly initialized";

    /** Self reference - used to implement the Singleton pattern for this class. */
    private static ConfigManager singletonRef = null;

    /** Initialize the singleton instances. */
    static {
        singletonRef = new PropertyConfigManager();
    }

    /**
     * Returns the singleton instance of the configuration manager.
     */
    public static ConfigManager getConfigManager() {
        if (singletonRef == null) {
            throw new IllegalStateException(ILLEGAL_STATE_MSG);
        }
        return singletonRef;
    }

    /**
     * Returns the config object associated with the given <code>name</code>.
     */
    public static Config getConfig(String name) {
        return ConfigManager.getConfigManager().doGetConfig(name);
    }

    /**
     * Returns the config object associated with the given <code>name</code>.. 
     */
    protected abstract Config doGetConfig(String name);
}
