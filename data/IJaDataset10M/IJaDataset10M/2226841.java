package org.jamesq.core.common;

import java.io.File;
import org.jamesq.core.handler.message.JobMessageHandler;
import org.jamesq.core.handler.rms.JobCommandActor;
import org.jamesq.core.archiver.JobArchiver;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;
import org.jamesq.util.PropertyUtility;

/**
 * Static method for system and environment setup for JAMESQ.
 * 
 * @author Hurng-Chun Lee <hurngchunlee@gmail.com>
 */
public class SystemLoader {

    private static JobArchiver jmsgArchiver = null;

    private static Logger logger = Logger.getLogger(SystemLoader.class.getPackage().getName());

    /**
     * loads the default system properties for jamesq platform.
     * 
     * @throws java.io.IOException
     */
    public static void loadDefaultPropertis() throws IOException {
        Properties properties = new Properties();
        properties.load(ClassLoader.getSystemResourceAsStream("META-INF/jamesq.properties"));
        for (Enumeration e = properties.propertyNames(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            System.setProperty(key, PropertyUtility.getAbsolutePropertyValue(properties.getProperty(key)));
        }
    }

    /**
     * loads user properties for jamesq platform.
     *
     * The user level properties can be presented in two parts:
     *  <ul>
     *  <li/> a property file
     *  <li/> a {@link Properties} object
     *  </ul>
     *
     * if both are given the values defined in {@link Properties} take precedence and will override the
     * values given in the property file.
     *
     * @param jamesqPropertyFile the user-level property file
     * @param jamesqProperties the user-level {@link Properties} object
     *
     * @throws java.io.IOException
     */
    public static void loadUserPropertis(File jamesqPropertyFile, Properties jamesqProperties) throws IOException {
        if (jamesqPropertyFile.exists()) {
            Properties properties = PropertyUtility.loadProperties(jamesqPropertyFile);
            for (Enumeration e = properties.propertyNames(); e.hasMoreElements(); ) {
                String key = (String) e.nextElement();
                System.setProperty(key, PropertyUtility.getAbsolutePropertyValue(properties.getProperty(key)));
            }
        }
        for (Enumeration e = jamesqProperties.propertyNames(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            System.setProperty(key, PropertyUtility.getAbsolutePropertyValue(jamesqProperties.getProperty(key)));
        }
    }

    /**
     * prepares the jamesq runtime environment.
     * 
     * The method needs to be called once in a single JVM instance before running
     * jamesq client and service.
     * 
     * @throws java.io.IOException
     */
    public static void load() throws IOException {
        SystemLoader.loadDefaultPropertis();
    }

    /**
     * gets the loaded {@link JobCommandActor} object. 
     * 
     * @return the {@link JobCommandActor} object; <code>null</code> if actor not properly loaded.
     * 
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.NoSuchMethodException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException
     * @throws java.lang.reflect.InvocationTargetException
     * 
     */
    public static JobCommandActor getJobCommandActor() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object obj = null;
        Class c = Class.forName(System.getProperty("jf.jcmd.actor"));
        Object[] argObjects = new Object[] {};
        Class[] argClasses = new Class[] {};
        obj = c.getConstructor(argClasses).newInstance(argObjects);
        return (JobCommandActor) obj;
    }

    /**
     * gets the loaded {@link JobMessageHandler} object.
     * 
     * @return the {@link JobMessageHandler} object; <code>null</code> if handler not properly loaded.
     * 
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.NoSuchMethodException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public static JobMessageHandler getJobMessageHandler() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object obj = null;
        Class c = Class.forName(System.getProperty("jf.jmsg.handler"));
        Object[] argObjects = new Object[] {};
        Class[] argClasses = new Class[] {};
        obj = c.getConstructor(argClasses).newInstance(argObjects);
        return (JobMessageHandler) obj;
    }

    /**
     * gets the {@link JobArchiver} object. 
     * 
     * @return the {@link JobArchiver} object; <code>null</code> if archiver not properly loaded.
     * 
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.NoSuchMethodException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public static JobArchiver getJobArchiver() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (SystemLoader.jmsgArchiver == null) {
            Object obj = null;
            Class c = Class.forName(System.getProperty("jf.jmsg.archiver"));
            Object[] argObjects = new Object[] {};
            Class[] argClasses = new Class[] {};
            obj = c.getConstructor(argClasses).newInstance(argObjects);
            SystemLoader.jmsgArchiver = (JobArchiver) obj;
        }
        return SystemLoader.jmsgArchiver;
    }
}
