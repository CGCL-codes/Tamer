package anima.access;

import java.io.File;
import java.net.URI;
import java.util.Properties;
import anima.config.ConfigManager;

/**
 * Converts relative references (to a file or directory) in absolute references,
 * based on the current location of the application and user data files.
 * 
 * @author Andre Santanche
 */
public class Location {

    private static Exception locationException = null;

    public static URI appURI = null, workURI = null;

    static {
        try {
            File localDirectory = new File(System.getProperty("user.dir"));
            appURI = localDirectory.toURI();
            workURI = appURI;
            Properties systemProperties = ConfigManager.getSystemProperties();
            String appDirectory = systemProperties.getProperty(ConfigManager.DIRECTORY_APPLICATION);
            String workDirectory = systemProperties.getProperty(ConfigManager.DIRECTORY_WORK);
            if (appDirectory != null) try {
                URI aURI = new File(appDirectory).toURI();
                if (aURI != null) appURI = aURI;
            } catch (Exception e) {
            }
            if (workDirectory != null) try {
                URI wURI = new File(workDirectory).toURI();
                if (wURI != null) workURI = wURI;
            } catch (Exception e) {
            }
        } catch (Exception e) {
            locationException = e;
            e.printStackTrace();
        }
    }

    public static boolean locationWorking() {
        return locationException == null && appURI != null && workURI != null;
    }

    public static Exception getLocationException() {
        return locationException;
    }

    /**
     * Transforms a relative reference in an absolute URI considering the application
     * directory as a base path.
     * 
     * @param relativeReference relative reference to be transformed
     * @return absolute reference or null if the relative reference is null or if the location
     *         engine is not working for any reason
     */
    public static URI toAbsoluteAppURI(String relativeReference) {
        return (locationWorking() && relativeReference != null) ? appURI.resolve(relativeReference) : null;
    }

    /**
     * Transforms a relative reference in an absolute URI considering the work
     * directory as a base path.
     * 
     * @param relativeReference relative reference to be transformed
     * @return absolute reference or null if the relative reference is null or if the location
     *         engine is not working for any reason
     */
    public static URI toAbsoluteWorkURI(String relativeReference) {
        return (locationWorking() && relativeReference != null) ? workURI.resolve(relativeReference) : null;
    }
}
