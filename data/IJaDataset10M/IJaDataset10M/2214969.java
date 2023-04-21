package be.fomp.jeve.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy.Type;
import java.util.Properties;
import be.fomp.jeve.core.exceptions.JEveConfigurationException;
import be.fomp.jeve.core.exceptions.JEveConnectionException;
import be.fomp.jeve.core.util.Proxy;

/**
 * <pre>
 *  ______   ______    
 * /\__  __\/\ \___\ 
 * \/_/\ \_/\ \ \__/    __   __     __       
 *    \ \ \  \ \ \__\  /\ \ /\ \  /'__`\  
 *    _\_\ \  \ \ \_/_ \ \ \\_\ \/\  __/       
 *   /\____/   \ \_____\\ \_____/\ \____\   
 *   \_/__/     \/_____/ \/____/  \/____/                   
 *</pre>
 *	This file is part of the JEVE core API.<br />
 *	<br />
 * 
 * Class containing configuration data for the EVE-Online API.
 * 
 * @author Sven Meys
 *
 */
public class Configuration implements CoreConfiguration {

    /**
	 * Instance of the configuration..Only one object is needed
	 */
    private static Configuration instance = null;

    /**
	 * The properties object containing all the config properties
	 */
    private static final Properties properties = new Properties();

    /**
	 * File where all configuration options are saved
	 */
    private static String config_file_path;

    /**
	 * Getter for the configuration object
	 * @return An instance of the configuration
	 * @throws JEveConfigurationException
	 */
    public static final Configuration getInstance() throws JEveConfigurationException {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    /**
	 * Private constructor. This class is a singleton.
	 * @throws JEveConfigurationException
	 */
    private Configuration() throws JEveConfigurationException {
        if (config_file_path == null) throw new JEveConfigurationException("Configuration File path was not set");
        File configFile = new File(config_file_path);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException ioe) {
                throw new JEveConfigurationException("Unable to create configuration file");
            }
        }
        loadConfiguration();
    }

    /**
	 * Load the configuration from a file.
	 * @throws JEveConfigurationException
	 */
    public final void loadConfiguration() throws JEveConfigurationException {
        if (config_file_path == null) throw new JEveConfigurationException("Configuration File path was not set");
        InputStream in = null;
        try {
            in = new FileInputStream(config_file_path);
            properties.load(in);
        } catch (IOException ioe) {
            throw new JEveConfigurationException("Unable to load config.properties");
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e) {
                System.err.println("Unable to close inputstream");
            }
        }
    }

    /**
	 * Stores the current configuration to a file
	 * @throws JEveConfigurationException
	 */
    public final void storeConfiguration() throws JEveConfigurationException {
        if (config_file_path == null) throw new JEveConfigurationException("Configuration File path was not set");
        OutputStream out = null;
        try {
            out = new FileOutputStream(config_file_path);
            properties.store(out, COMMENT);
        } catch (IOException ioe) {
            throw new JEveConfigurationException("Unable to store configuration");
        } finally {
            try {
                if (out != null) out.close();
            } catch (Exception e) {
                System.err.println("Unable to close outputstream");
            }
        }
    }

    /**
	 * Getter for properties. This way this class can be accessed in a generic way.
	 * @return Properties stored in the config file
	 */
    public final Properties getConfigurationProperties() {
        return properties;
    }

    /**
	 * Fetches the proxy configuration options from the config file
	 * and returns it as a ProxyConfiguration Object
	 * @return An object containing the proxy settings
	 */
    public final Proxy getProxyConfiguration() {
        Proxy returnValue = null;
        String useProxy = properties.getProperty("useProxy", "false");
        String proxyType = properties.getProperty("proxyType", "DIRECT");
        String proxyHost = properties.getProperty("proxyHost", "");
        String proxyPort = properties.getProperty("proxyPort", "80");
        String useProxyAuth = properties.getProperty("useProxyAuth", "false");
        String username = properties.getProperty("proxyUser", "");
        String password = properties.getProperty("proxyPass", "");
        if (useProxy == null || !"true".equals(useProxy)) useProxy = "false";
        if (proxyType == null || (!"DIRECT".equals(proxyType) && !"HTTP".equals(proxyType) && !"SOCKS".equals(proxyType))) proxyType = "DIRECT";
        if (proxyHost == null) proxyHost = "";
        if (proxyPort == null || !proxyPort.matches("[0123456789]+")) proxyPort = "80";
        if (useProxyAuth == null || !"true".equals(useProxyAuth)) useProxyAuth = "false";
        if (username == null) username = "";
        if (password == null) password = "";
        if ("true".equals(useProxy)) {
            if ("true".equals(useProxyAuth)) {
                returnValue = new Proxy(Type.valueOf(proxyType), proxyHost, Integer.parseInt(proxyPort), username, password);
            } else {
                returnValue = new Proxy(Type.valueOf(proxyType), proxyHost, Integer.parseInt(proxyPort));
            }
        }
        return returnValue;
    }

    /**
	 * Sets the proxy configuration parameters in the properties.
	 * 
	 * @param useProxy true|false whether a proxy is defined
	 * @param proxyType DIRECT|HTTP|SOCKS defines the proxy type
	 * @param proxyHost the proxy host
	 * @param proxyPort the proxy port
	 * @param useProxyAuth true|false defines whether authentication is used
	 * @param proxyUser the proxy username
	 * @param proxyPass the proxy password
	 * @throws JEveConnectionException
	 */
    public final void setProxyconfiguration(boolean useProxy, Type proxyType, String proxyHost, int proxyPort, boolean useProxyAuth, String proxyUser, String proxyPass) throws JEveConnectionException {
        if (proxyType == null) proxyType = Type.DIRECT;
        if (proxyHost == null) proxyHost = "";
        if (proxyPort <= 0) proxyPort = 0;
        if (proxyUser == null) proxyUser = "";
        if (proxyPass == null) proxyPass = "";
        properties.setProperty("useProxy", "" + useProxy);
        properties.setProperty("proxyType", proxyType.toString());
        properties.setProperty("proxyHost", proxyHost);
        properties.setProperty("proxyPort", "" + proxyPort);
        properties.setProperty("useProxyAuth", "" + useProxyAuth);
        properties.setProperty("proxyUser", proxyUser);
        properties.setProperty("proxyPass", proxyPass);
    }

    /**
	 * Sets the configuration file path<br>
	 * MUST BE CALLED BEFORE TRYING TO BUILD ANY CONNECTION
	 * 
	 * @throws JEveConfigurationException
	 */
    public static void setConfigurationFilePath(String configFilePath) {
        config_file_path = configFilePath;
    }
}
