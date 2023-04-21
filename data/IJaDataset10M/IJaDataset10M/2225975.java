package com.hitao.codegen.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Callable;

/**
 * Class that loads the system.properties file into the Sytem.getProperties().<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: SystemPropertiesLoader.java 31 2011-03-01 10:24:54Z guest $
 */
public class SystemPropertiesLoader implements Callable<Void> {

    /**System property used to keep track if the system.properties file has been loaded or not*/
    public static final String SYSTEM_PROPERTY_FLAG = "system.properties.isLoaded";

    /**system property used to override default location of system.properties file*/
    public static final String FILE_PROPERTY_LOCATION_PROP = "system.properties.location";

    /**System property used to keep track of how the system.properties file was loaded*/
    public static final String SYSTEM_PROPERTY_LOCATION_TYPE = "system.properties.location.type";

    /**default name of system.properties file*/
    public static final String SYSTEM_PROPERTY_FILE = "system.properties";

    /**the text to place at the beginning of a line to include another file (using the same {@link LocationType}) */
    public static final String INCLUDE_TAG = "#!include";

    private static final String CONFIG_PATH = "codegen.config.path";

    private final String propertiesFile_;

    private LocationType locationType_;

    /**
   * Constructor
   */
    public SystemPropertiesLoader() {
        this(LocationType.FILESYSTEM, System.getProperty(FILE_PROPERTY_LOCATION_PROP));
    }

    /**
   * Constructor
   *
   * @param argPropertiesFileLocation location of properties file to load
   */
    public SystemPropertiesLoader(String argPropertiesFileLocation) {
        this(LocationType.FILESYSTEM, argPropertiesFileLocation);
    }

    /**
   * Constructor
   *
   * @param argLocationType  how the system.properties should be loaded
   */
    public SystemPropertiesLoader(LocationType argLocationType) {
        this(argLocationType, System.getProperty(FILE_PROPERTY_LOCATION_PROP));
    }

    /**
   * Constructor
   *
   * @param argLocationType  how the system.properties should be loaded
   * @param argPropertiesFileLocation location of properties file to load
   */
    public SystemPropertiesLoader(LocationType argLocationType, String argPropertiesFileLocation) {
        propertiesFile_ = argPropertiesFileLocation;
        locationType_ = argLocationType;
    }

    /**
   * Loads the properties file into {@link System#getProperties()}.
   *
   * @return <code>null</code>
   * @throws IOException if unable to load the properties file
   */
    public Void call() throws IOException {
        loadSystemProperties();
        return null;
    }

    /**
   * Loads the properties file into {@link System#getProperties()}.
   * @throws IOException if unable to load the properties file
   */
    public void loadSystemProperties() throws IOException {
        if (Boolean.getBoolean(SYSTEM_PROPERTY_FLAG)) {
            return;
        }
        boolean loaded = false;
        try {
            Properties appProps = load(new Properties(), new HashSet<String>(), getSystemPropertiesLocation());
            Properties sysProps = System.getProperties();
            for (Enumeration<?> e = appProps.propertyNames(); e.hasMoreElements(); ) {
                String name = (String) e.nextElement();
                if (sysProps.getProperty(name) == null) {
                    sysProps.setProperty(name, appProps.getProperty(name));
                }
            }
            setConfigPathProperty(sysProps);
            loaded = true;
            sysProps.setProperty(SYSTEM_PROPERTY_FLAG, Boolean.TRUE.toString());
            sysProps.setProperty(SYSTEM_PROPERTY_LOCATION_TYPE, locationType_.name());
        } finally {
            if (!loaded) {
                System.err.println("DTV system properties were NOT loaded.");
            }
        }
    }

    private void setConfigPathProperty(Properties argProperties) {
        SortedMap<Integer, String> configPathParts = new TreeMap<Integer, String>();
        List<String> toRemove = new ArrayList<String>();
        for (Enumeration<?> e = argProperties.propertyNames(); e.hasMoreElements(); ) {
            String name = (String) e.nextElement();
            if (name.startsWith(CONFIG_PATH + ".")) {
                configPathParts.put(Integer.valueOf(name.substring(CONFIG_PATH.length() + 1)), argProperties.getProperty(name));
                toRemove.add(name);
            }
        }
        for (String s : toRemove) {
            argProperties.remove(s);
        }
        String configPath = argProperties.getProperty(CONFIG_PATH, "");
        for (String part : configPathParts.values()) {
            configPath += part;
        }
        argProperties.setProperty(CONFIG_PATH, configPath);
    }

    private Properties load(Properties argProperties, Set<String> argLoadedFiles, ISystemPropertiesLocation argLocation) throws IOException {
        final String absolutePath = argLocation.getName();
        if (argLoadedFiles.contains(absolutePath)) {
            System.err.println("already loaded " + absolutePath);
        } else {
            argLoadedFiles.add(absolutePath);
            BufferedReader reader = argLocation.getReader();
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    handleLine(argProperties, argLoadedFiles, line);
                }
            } finally {
                try {
                    reader.close();
                } catch (Exception ex) {
                }
            }
        }
        return argProperties;
    }

    private void handleLine(Properties argNewProps, Set<String> argLoadedFiles, String argLine) throws IOException {
        if (argLine.startsWith(INCLUDE_TAG)) {
            String includeFileName = argLine.substring(INCLUDE_TAG.length());
            handleInclude(argNewProps, argLoadedFiles, includeFileName);
        } else if (!argLine.startsWith("#")) {
            argNewProps.load(new ByteArrayInputStream((argLine + "\n").getBytes()));
        }
    }

    private void handleInclude(Properties argProperties, Set<String> argLoadedFiles, String argLine) {
        try {
            final String name = argLine.trim();
            ISystemPropertiesLocation loc;
            if (locationType_ == LocationType.CLASSPATH) {
                loc = new URLSystemPropertiesLocation(new URL(name));
            } else {
                File file = new File(name);
                loc = new FileSystemPropertiesLocation(file);
            }
            load(argProperties, argLoadedFiles, loc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ISystemPropertiesLocation getSystemPropertiesLocation() {
        ISystemPropertiesLocation loc;
        try {
            if (locationType_ == LocationType.FILESYSTEM) {
                loc = getSystemPropertiesFromFile();
            } else {
                loc = getSystemPropertiesFromClassPath();
            }
        } catch (Throwable ex) {
            try {
                if (locationType_ == LocationType.FILESYSTEM) {
                    loc = getSystemPropertiesFromClassPath();
                    locationType_ = LocationType.CLASSPATH;
                } else {
                    loc = getSystemPropertiesFromFile();
                    locationType_ = LocationType.FILESYSTEM;
                }
            } catch (Throwable ex2) {
                ex.printStackTrace();
                ex2.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
        System.setProperty(FILE_PROPERTY_LOCATION_PROP, loc.getName());
        return loc;
    }

    private ISystemPropertiesLocation getSystemPropertiesFromFile() throws FileNotFoundException {
        final String filePath;
        if (propertiesFile_ == null) {
            filePath = SYSTEM_PROPERTY_FILE;
        } else {
            filePath = propertiesFile_;
        }
        File f = new File(filePath);
        if (!f.exists()) {
            f = new File(SYSTEM_PROPERTY_FILE);
        }
        if (!f.exists()) {
            throw new FileNotFoundException(filePath);
        }
        return new FileSystemPropertiesLocation(f);
    }

    private ISystemPropertiesLocation getSystemPropertiesFromClassPath() {
        URL url = null;
        if (propertiesFile_ != null) {
            try {
                url = ClassPathUtils.getResource(propertiesFile_);
            } catch (Exception ee) {
                System.err.println("Failed attempt at loading system.properties: " + propertiesFile_ + " " + ee);
                ee.printStackTrace();
            }
        }
        if (url == null) {
            url = ClassPathUtils.getResource(SYSTEM_PROPERTY_FILE);
            if (url == null) {
                System.err.println("Failed to load system properties " + "resource from classpath " + SYSTEM_PROPERTY_FILE);
                return null;
            }
        }
        return new URLSystemPropertiesLocation(url);
    }

    /**
   * Type of location that system.properties is loaded
   */
    public static enum LocationType {

        /**
     * loaded using the classpath
     */
        CLASSPATH, /**
     * loaded from the file system
     */
        FILESYSTEM
    }

    private static interface ISystemPropertiesLocation {

        /**
     * Gets the type of location.
     *
     * @return the type of location
     */
        public LocationType getLocationType();

        /**
     * Gets a unique name for the location.
     * This name is used to handle circular includes.
     *
     * @return Returns the name.
     */
        public String getName();

        /**
     * Gets a reader for the location.
     *
     * @return a new reader for the location
     * @throws IOException if unable to access the location
     */
        public BufferedReader getReader() throws IOException;
    }

    private static class URLSystemPropertiesLocation implements ISystemPropertiesLocation {

        private final URL url_;

        URLSystemPropertiesLocation(URL argUrl) {
            if (argUrl == null) {
                throw new NullPointerException();
            }
            url_ = argUrl;
        }

        /**{@inheritDoc}*/
        public String getName() {
            return url_.toExternalForm();
        }

        /**{@inheritDoc}*/
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(url_.openStream()));
        }

        /**{@inheritDoc}*/
        public LocationType getLocationType() {
            return LocationType.CLASSPATH;
        }
    }

    private static class FileSystemPropertiesLocation implements ISystemPropertiesLocation {

        private final File file_;

        FileSystemPropertiesLocation(File argFile) {
            file_ = argFile;
        }

        /**{@inheritDoc}*/
        public String getName() {
            return file_.getAbsolutePath();
        }

        /**{@inheritDoc}*/
        public BufferedReader getReader() throws IOException {
            return FileUtils.getFileReader(file_);
        }

        /**{@inheritDoc}*/
        public LocationType getLocationType() {
            return LocationType.FILESYSTEM;
        }
    }
}
