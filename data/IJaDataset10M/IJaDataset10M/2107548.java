package com.filesearch;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Properties;
import com.filesearch.PropertyManager;

public class PropertyManager {

    public static Properties loadProp(String fileName) throws Exception {
        Properties prop = null;
        try {
            prop = new Properties();
            FileInputStream in = new FileInputStream(fileName);
            prop.load(in);
            in.close();
        } catch (java.io.IOException e) {
            throw new Exception("Problem loading properties file " + fileName + ". Error:" + e);
        }
        return prop;
    }

    public static void saveProp(String fileName, Properties prop) throws Exception {
        FileOutputStream out = new FileOutputStream(fileName);
        prop.store(out, null);
        out.flush();
        out.close();
    }
}
