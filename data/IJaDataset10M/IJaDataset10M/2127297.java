package org.szegedi.spring.beans.factory;

import java.util.Map;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * An extension to the beans factory utils in Spring.
 * @author Attila Szegedi
 * @version $Id: BeanFactoryUtilsEx.java 79 2007-09-17 11:24:06Z szegedia $
 */
public class BeanFactoryUtilsEx {

    /**
     * Returns a bean of specified type in the specified bean factory or its 
     * ancestors. In contrast with Spring's BeanFactoryUtils, doesn't throw an
     * exception if no bean is found, but rather returns null.
     * @param lbf the bean factory to look for the bean
     * @param type the expected type of the bean
     * @return the bean - if exactly one is defined. null - if none is defined
     * @throws NoSuchBeanDefinitionException if more than one bean is defined
     */
    public static Object beanOfTypeIncludingAncestors(ListableBeanFactory lbf, Class type) {
        Map map = org.springframework.beans.factory.BeanFactoryUtils.beansOfTypeIncludingAncestors(lbf, type);
        switch(map.size()) {
            case 0:
                {
                    return null;
                }
            case 1:
                {
                    return map.values().iterator().next();
                }
            default:
                {
                    throw new NoSuchBeanDefinitionException("More than one bean of type " + type.getName() + " found: " + map.keySet());
                }
        }
    }
}
