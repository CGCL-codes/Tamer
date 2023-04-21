package net.sf.beanlib.spi;

import java.lang.reflect.Method;

/**
 * Used to do something to the "from" JavaBean.
 *  
 * @author Joe D. Velopar
 */
public interface BeanSourceHandler {

    public void handleBeanSource(Object fromBean, Method readerMethod, Object propertyValue);
}
