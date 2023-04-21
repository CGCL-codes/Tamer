package net.sourceforge.argval.internal.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import net.sourceforge.argval.utils.ILocateFile;
import net.sourceforge.argval.utils.IPropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This PropertiesLoader provides a standard way for loading (test) configuration(s). It provides
 * a static method to load the configuration as a {@link Properties} object.
 * <p>
 * The configuration is loaded as follows:
 * <ul>
 *  <li>The properties are loaded from the configuration file, but any of these properties may be
 *  overruled by system properties.
 *  <li>The configuration file is located as follows
 *  <ul>
 *    <li>If the system property <code>property-key-name-configuration-file</code> is set, that 
 *        file is used.
 *    <li>Otherwise, if there exist a file 'default-properties-filename' in the current working 
 *        directory that file is used
 *    <li>Otherwise, if there exist an application configuration directory (under the users home 
 *        directory '${user.home}/configuration-path'), containing a default configuration file,
 *        that file is used.
 *    <li>Otherwise, the loading will fail with a runtime exception with a descriptive message.
 *  </ul>
 * </ul>
 * 
 * An example of using the PropertiesLoader:
 * <iframe  src="{@docRoot}/example/net/sourceforge/argval/example/Application.html" 
 *   width="100%"  height="400">
 * <pre>
 *     public Application() {
 *         String propKeyConfigurationFile = "application-configuration"; 
 *         String configDirName = ".application"; 
 *         String defaultFileName = "application.properties";
 *         propertiesLoader = new PropertiesLoaderImpl(propKeyConfigurationFile, configDirName, defaultFileName);
 *     }
 *
 *     public Properties getConfiguration() throws ApplicationException {
 *         try {
 *             return propertiesLoader.loadConfiguration();
 *         } 
 *         catch (IOException ioe) {
 *             try {
 *             throw new ApplicationException("Unable to load configuration file " 
 *                     + propertiesLoader.getPropertiesFile(), ioe);
 *             } 
 *             catch (FileNotFoundException fnfe) {
 *             throw new ApplicationException(fnfe);
 *             }
 *         }
 *     }
 * </pre>
 * </iframe>
 *   
 * @author T. Verhagen
 */
public class PropertiesLoader implements IPropertiesLoader {

    /** The logging instance. */
    private static final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

    /** The location of the properties file. */
    private final ILocateFile locateFile;

    /**
     * Creates a <code>PropertiesLoader</code> specific for an application / utility.
     * <p>
     * Specify the <code>propKeyConfigurationFile</code> as a property key. This key can be used 
     * to specify the location of the configuration file, as system property / environment variable.
     * As in:
     * <pre>java  -Dmy-application-name.configuration.path=<the location of the properties file>
     *   org.organisation.application.Start</pre>
     * The <code>folderName</code> is optional, use it when the application has a default configuration folder 
     * in the users home directory. Like <code>${user.home/.my-application-config/</code>. 
     * The <code>defaultFileName</code> required, used to find load the default configuration file, from the 
     * configuration application configuration directory 
     * (<code>${user.home}/.my-application-config/defaultFileName</code>). Or the current working directory
     * (<code>${user.dir}/defaultFileName</code>).
     * 
     * @param propKeyConfigurationFile - the system property key, for locating the configuration file (not <code>null</code>)
     * @param configDirName - the applications configuration directory (when it has one) (can be <code>null</code>)
     * @param defaultFileName - the default configuration file name (not <code>null</code>).
     */
    public PropertiesLoader(String propKeyConfigurationFile, String configDirName, String defaultFileName) {
        locateFile = new PropertyLocatedFile(propKeyConfigurationFile, configDirName, defaultFileName, logger);
    }

    public Properties loadConfiguration() throws IOException {
        return loadConfiguration(Boolean.TRUE);
    }

    public Properties loadConfiguration(Boolean includeSystemProperties) throws IOException {
        final File file = getPropertiesFile();
        logger.info("Reading configuration from '" + file.getAbsolutePath() + "'");
        Properties configProp = new Properties();
        configProp.load(new FileInputStream(file));
        logger.debug("includeSystemProperties " + includeSystemProperties);
        if (includeSystemProperties != null && includeSystemProperties == Boolean.TRUE) {
            configProp.putAll(System.getProperties());
        }
        return configProp;
    }

    public File getPropertiesFile() throws FileNotFoundException {
        return locateFile.getFile();
    }

    public String getPropertiesFileMessage() {
        return locateFile.getFileMessage();
    }
}
