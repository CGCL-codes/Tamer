package com.blindeye.util;

import java.io.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Utils {

    private static PropertiesConfiguration properties;

    static {
        try {
            properties = new PropertiesConfiguration(new File("blindeyes.properties"));
            System.out.println("Loading properties from: " + new File("").getAbsolutePath());
            properties.setProperty("blindeyes.encryption.algorithm", "SSL");
            properties.setProperty("blindeyes.encryption.key_type", "SunX509");
            properties.setProperty("blindeyes.encryption.key_store_type", "JKS");
            properties.setProperty("blindeyes.encryption.keyname", "/data/backups/blindeyes.key");
            properties.setProperty("blindeyes.encryption.passphrase", "d0n0tte3stm3");
            properties.setProperty("blindeyes.controller.ip", "127.0.0.1");
            properties.setProperty("blindeyes.controller.host", "localhost");
            properties.setProperty("blindeyes.controller.port", new Integer(7001));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static PropertiesConfiguration getProperties() {
        return properties;
    }
}
