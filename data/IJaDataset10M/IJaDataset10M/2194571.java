package org.fluid.commons.util;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides a single static method to get singleton instances 
 * of the specified class. This way singleton classes are not forced to 
 * implement the singleton pattern explicitly.
 * @author linesta
 *
 */
public final class Singletons {

    private static Map<Class<?>, Object> singletons = new HashMap<Class<?>, Object>();

    /**
	 * Return a instance of the specified class. The returned object is 
	 * meant to be a singleton; next calls to this method will return the 
	 * same object.
	 * @param clazz
	 * @return A singleton instance of the class
	 * @throws InstantiationException
	 */
    public static Object instance(Class<?> clazz) throws InstantiationException {
        Object obj = singletons.get(clazz);
        if (obj == null) {
            try {
                obj = clazz.newInstance();
            } catch (IllegalAccessException e) {
                throw new InstantiationException(e.getMessage());
            }
            singletons.put(clazz, obj);
        }
        return obj;
    }

    private Singletons() {
    }
}
