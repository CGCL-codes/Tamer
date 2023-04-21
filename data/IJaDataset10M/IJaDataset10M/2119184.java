package net.frontlinesms;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import net.frontlinesms.resources.ClasspathPropertySet;

/**
 * Loads and reads a properties file from the classpath.  This properties file is created at build time.
 * @author Alex
 */
public final class BuildProperties extends ClasspathPropertySet {

    /** Property key: application build version */
    private static final String PROPERTY_VERSION = "Version";

    /** Singleton instance of this class. */
    private static BuildProperties instance;

    /**
	 * Creates the singleton instance of this class by loading the properties from the commandline.
	 * @throws IOException If the class
	 */
    private BuildProperties() throws IOException {
        super("/net/frontlinesms/build.properties");
    }

    /** @return the version of FrontlineSMS that we are running. */
    public String getVersion() {
        return super.getProperty(PROPERTY_VERSION);
    }

    /**
	 * Lazy getter for {@link #instance}
	 * @return The singleton instance of this class
	 */
    public static synchronized BuildProperties getInstance() {
        if (instance == null) {
            try {
                instance = new BuildProperties();
            } catch (Exception ex) {
                throw new IllegalStateException("Could not load build properties!", ex);
            }
        }
        return instance;
    }
}
