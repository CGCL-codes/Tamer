package com.mephi.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import org.apache.log4j.Logger;

/**
 * 
 * Class is helpful by using its method as for example universal toString method
 * @author GM Mephisto
 * @since 2011-04-09
 */
public class AuxClass {

    /**
	 * Class prints informations to log file if log level is set to INFO 
	 * @param o an instance of class which will be described as result
	 * @param foreseenCapacity the capacity which is reserver for string of characters
	 * @return string which describes an instance given by x
	 */
    public static String toString(Object o, int foreseenCapacity) {
        Class<?> cl = o.getClass();
        StringBuffer desc = new StringBuffer(cl.getName());
        desc.ensureCapacity(foreseenCapacity);
        do {
            desc.append("[");
            Field[] fields = cl.getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);
            for (int i = 0, length = fields.length; i < length; i++) {
                Field p = fields[i];
                desc.append(p.getName());
                desc.append("=");
                try {
                    Object obj = p.get(o);
                    if (obj == null) desc.append("null"); else desc.append(obj.toString());
                } catch (Exception ex) {
                    logger.error(ex);
                }
                if (i < length - 1) desc.append(",");
            }
            desc.append("]");
            cl = cl.getSuperclass();
        } while (cl != Object.class);
        logger.info("name=" + cl.getName() + ", length=" + desc.length() + ", capacity=" + desc.capacity());
        return desc.toString();
    }

    public static final boolean isSerializationAndReflectionAttackPossible(String className) {
        try {
            Constructor<?> c1 = Class.forName(className).getDeclaredConstructors()[0];
            c1.setAccessible(true);
            Class<?> cl = (Class<?>) c1.newInstance(0);
        } catch (ClassNotFoundException ex) {
            logger.error(ex);
            return false;
        } catch (InstantiationException ex) {
            logger.error(ex);
            return false;
        } catch (IllegalAccessException ex) {
            logger.error(ex);
            return false;
        } catch (IllegalArgumentException ex) {
            logger.error(ex);
            return false;
        } catch (InvocationTargetException ex) {
            logger.error(ex);
            return false;
        }
        return true;
    }

    /**
     * Checks if arg is null. If it is null throws an exception {@code e} given by a reference.  
     * 
     * @param arg the arg to test if it is null
     * @param e the runtime exception to throw if arg is null
     */
    public static void checkArgument(Object arg, RuntimeException e) throws RuntimeException {
        if (arg == null) throw e;
    }

    /**
     * Checks if arg is null. If it is null throws an exception {@code e} given by a reference.  
     * 
     * @param arg the arg to test if it is null
     * @param e the Throwable exception to throw if arg is null
     */
    public static void checkArgument(Object arg, Throwable e) throws Throwable {
        if (arg == null) throw e;
    }

    private static Logger logger = Logger.getLogger("LOG");
}
