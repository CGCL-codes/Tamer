package org.sg.common.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Semyon Goryachkin
 */
public class ApplicationContextProvider implements ApplicationContextAware {

    /** 
     * Wiring the ApplicationContext into a static method
     *  
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        ApplicationContextHolder.setApplicationContext(ctx);
    }
}
