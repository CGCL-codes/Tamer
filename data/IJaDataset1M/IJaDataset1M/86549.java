package com.toyi.util;

import java.io.FileNotFoundException;

public class Profiler {

    private static final String PROPERTY_FILE_SEP_KEY = "file.separator";

    private static final String PROPERTY_FILE_EXTENSION = ".properties";

    private static Profiler _instance = new Profiler();

    private String _configPath = null;

    /**
	 * Creates a Profiler object.
	 * 
	 */
    private Profiler() {
    }

    /**
	 * Gets the configurations as defined by configType. Profiler will
	 * try to locate the file in the configuration path as defined inside
	 * jndi.properties. All configuration files MUST end with the extension
	 * <EM>properties</EM>. The profiler will construct the filename as such
	 * <configType>.properties.
	 * <P>Example
	 * <PRE>
	 *	 getConfiguration("tli");
	 * </PRE>
	 * Profiler will attempt to look for the file <EM>tli.properties</EM> and
	 * load the configurations inside that file.
	 * @param configType the string used for getting the configuration file.
	 *				   This is usually the application code.
	 * @throws FileNotFoundException if any error occurs while getting the config file.
	 * @return The configurations inside the file.
	 */
    public Configuration getConfiguration(String configType) throws FileNotFoundException {
        Configuration config = null;
        try {
            config = new Configuration(configType, PROPERTY_FILE_EXTENSION);
        } catch (FileNotFoundException fe) {
            throw fe;
        } catch (Exception exc) {
            throw new FileNotFoundException(exc.toString());
        }
        return config;
    }

    /**
	 * Gets an instance of a Profiler object.
	 * @return The profiler object.
	 * @throws FileNotFoundException if the file jndi.properties cannot be found in
	 *							   the CLASSPATH. This exception will only be thrown
	 *							   if the class is NOT running inside the VM of the
	 *							   App Server.
	 */
    public static Profiler getInstance() {
        return _instance;
    }
}
