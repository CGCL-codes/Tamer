package net.sf.beanlib.spi;

import java.lang.reflect.Method;

/** 
 * Used to find a method from a JavaBean.
 * 
 *  @author Joe D. Velopar
 */
public interface BeanMethodFinder {

    /**
     * @param propertyName property name related to the method to be found. 
     * @param target JavaBean instance.
     * @return the method found or null if not found.
     */
    public Method find(String propertyName, Object target);
}
