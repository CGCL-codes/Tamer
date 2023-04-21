package org.sd_network.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;

/**
 * A Configuration of an application.
 *
 * <p> $Id$
 *
 * @author Masatoshi Sato
 */
public class Config extends Properties {

    /** Default Logger. */
    private static final Logger _log = Logger.getLogger(Config.class.getName());

    /** The instance of this class. */
    private static Config _instance = null;

    /**
     * Return the instance of this class.
     * You should be call init method before call this method.
     */
    public static final Config getInstance() {
        if (_instance == null) throw new IllegalStateException("You must call load(propertyFile) method " + "before get instance.");
        return _instance;
    }

    /**
     * Load property from the specified file path by <tt>propertyFilePaht</tt>.
     *
     * @param propertyFilePath  Path to property file that is defined various
     *                          properties.
     *
     * @return  Instance of Config it was included new properties.
     */
    public static final Config load(String propertyFilePath) {
        if (propertyFilePath == null) throw new NullPointerException("propertyFilePath");
        if (_instance == null) _instance = new Config();
        _instance.loadProperty(propertyFilePath);
        return _instance;
    }

    /**
     * Load property from specified file.
     */
    private final void loadProperty(String filePath) {
        if (filePath == null) throw new NullPointerException("filePath");
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            load(in);
        } catch (IOException e) {
            throw new IllegalStateException("File[" + filePath + "] is not accessible. " + e.getMessage());
        }
    }
}
