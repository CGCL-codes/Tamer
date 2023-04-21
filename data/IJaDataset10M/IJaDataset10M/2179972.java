package com.simpleftp.ftp.server.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import sun.security.util.Resources;

/**
 * The logger class for Ftp Server
 * @author sajil
 *
 */
public class FtpLogger {

    static Logger logger = Logger.getLogger("FtpServerLogger");

    public static boolean debug = false;

    static {
        InputStream in = ClassLoader.getSystemResourceAsStream("com/simpleftp/ftp/server/properties/log4j.properties");
        Properties logProperties = new Properties();
        try {
            logProperties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PropertyConfigurator.configure(logProperties);
        if (logger.isDebugEnabled()) {
            debug = true;
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}
