package org.jude.client;

import java.io.*;
import java.util.*;

/**
 * <p> A library of static function to call if you want them. 
 *   
 *  
 * @author $Author: zanibonim $  
 * @version $Revision: 1.2 $   
 *  
 **/
public class StaticFunctionLibrary {

    public static String fromFileToString(File file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while ((line = in.readLine()) != null) {
                buffer.append(line);
                buffer.append('\n');
            }
            in.close();
            return buffer.toString();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * @return a extensive explaniation of  
     *   Java configuration and run time properties. 
     **/
    public static String getJavaConfiguration() {
        String message = " ";
        message += "\nSystem Properties:\n";
        try {
            Properties properties = System.getProperties();
            Enumeration keys = properties.propertyNames();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = properties.getProperty(key);
                message += "\n" + key + ": " + value;
            }
        } catch (SecurityException ex) {
            message += "hide from security system";
        }
        return message;
    }

    /**
     * @return a description of class loader hiearchy 
     */
    public static String getClassLoaderDescription(Class myClass) {
        String result = "";
        return result;
    }

    /**
     * Show detail of a ClassLoader 
     */
    class MyClassLoader extends ClassLoader {

        public MyClassLoader(ClassLoader loader) {
            super(loader);
        }

        public String getPackagesDescription() {
            String result = "";
            return result;
        }
    }
}
