package datadog.db.platforms;

import datadog.exceptions.DatadogRuntimeException;
import datadog.services.ClassService;
import datadog.sessions.SessionServer;
import java.util.HashMap;

/**
 * The built-in Platforms will register themselves. If you add a platform
 * manually, you'll need to register it yourself at runtime.
 *
 * @author Justin Tomich
 * @version $Id: PlatformManager.java 203 2006-06-08 05:30:21Z tomichj $
 */
public class PlatformManager {

    private static final HashMap driversToPlatforms = new HashMap();

    static {
        register(new HsqldbPlatform());
        register(new MysqlPlatform());
    }

    /**
     * Register a platform. Platforms are indexed by driver class. Only one
     * Platform may be present for a given driver class; registering a Platform
     * will replace any Platform previously registered for a driver class.
     * 
     * If you use a 3rd party Platform, you must register it at runtime.
     *
     * @param platform
     */
    public static void register(Platform platform) {
        driversToPlatforms.put(platform.getDriverClass(), platform);
    }

    public static Platform getPlatform(ClassService classService) {
        SessionServer server = classService.getSessionServer();
        return server.getPlatform();
    }

    public static Platform getPlatform(String driver) {
        Platform platform = (Platform) driversToPlatforms.get(driver);
        if (platform == null) {
            throw new DatadogRuntimeException("Driver not part of a supported" + " platform: " + driver);
        }
        return platform;
    }
}
