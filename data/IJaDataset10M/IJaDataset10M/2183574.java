package com.continuent.tungsten.router.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;
import com.continuent.tungsten.commons.config.TungstenProperties;

public class Configuration {

    /**
     * Logger
     */
    private static Logger logger = Logger.getLogger(Configuration.class);

    /**
     * The source of the properties for this configuration.
     */
    public TungstenProperties props = null;

    /**
     * Creates a new <code>ServiceConfiguration</code> object
     * 
     * @param configFileName
     * @throws ConfigurationException
     */
    public Configuration(String configFileName) throws ConfigurationException {
        load(configFileName);
    }

    /**
     * Loads a service configuration from a file located on the classpath.
     * 
     * @param configFileName
     * @throws ConfigurationException
     */
    public void load(String configFileName) throws ConfigurationException {
        props = new TungstenProperties();
        InputStream is = null;
        String clusterHome = System.getProperty("cluster.home");
        if (clusterHome == null) {
            logger.info("Property cluster.home not set; loading config file from class path: " + configFileName);
            is = getClass().getResourceAsStream(configFileName);
            if (is == null) throw new ConfigurationException("Unable to find configuration file in classpath: " + configFileName);
        } else {
            File configFile = new File(clusterHome, configFileName);
            logger.info("Property cluster.home is set; loading config file from file: " + configFile.getAbsolutePath());
            if (!configFile.exists() || !configFile.canRead()) {
                throw new ConfigurationException("Configuration file does not exist or is not readable: " + configFile.getAbsolutePath());
            }
            try {
                is = new FileInputStream(configFile);
            } catch (FileNotFoundException f) {
                throw new ConfigurationException(String.format("Cannot create an input stream for file '%s', reason=%s", configFile.getAbsolutePath(), f));
            }
        }
        try {
            props.load(is);
        } catch (IOException e) {
            throw new ConfigurationException("Unable to load configuration file:" + configFileName + ", reason=" + e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        props.applyProperties(this, true);
    }

    /**
     * deletes a specific file
     * 
     * @param delFileName
     * @throws ConfigurationException
     */
    public void delFile(String delFileName) throws ConfigurationException {
        File delFile = new File(delFileName);
        if (delFile.exists() && delFile.canWrite()) {
            delFile.delete();
        } else {
            throw new ConfigurationException("Can't delete file because it is not writeable. File=" + delFileName);
        }
    }

    /**
     * Validates that a directory exists.
     * 
     * @param dirName
     * @throws ConfigurationException
     */
    public File getDir(String dirName) throws ConfigurationException {
        File dir = new File(dirName);
        if (!dir.isDirectory()) {
            throw new ConfigurationException("The path indicated by the servicesHome property, '" + dirName + "', must be a directory.");
        }
        return dir;
    }

    /**
     * Stores a configuration file in a specific output file.
     * 
     * @param props
     * @param outFileName
     * @throws ConfigurationException
     */
    public void storeConfig(TungstenProperties props, String outFileName) throws ConfigurationException {
        try {
            File checkFile = new File(outFileName);
            File backupFile = new File(outFileName + ".bak");
            if (checkFile.exists()) {
                if (backupFile.exists()) {
                    backupFile.delete();
                }
                checkFile.renameTo(new File(outFileName + ".bak"));
            }
            FileOutputStream fout = new FileOutputStream(outFileName);
            props.store(fout);
            fout.flush();
            fout.close();
        } catch (FileNotFoundException f) {
            throw new ConfigurationException("Unable to process a file when configuring resources:" + f);
        } catch (IOException i) {
            throw new ConfigurationException("Error while storing properties:" + i);
        }
    }

    /**
     * Apply the properties from this configuration to another instance.
     * 
     * @param o
     */
    public void applyProperties(Object o) {
        this.props.applyProperties(o);
    }
}
