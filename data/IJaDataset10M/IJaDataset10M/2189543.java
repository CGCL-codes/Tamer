package org.pustefixframework.config.contextxmlservice;

import org.springframework.beans.factory.config.BeanReference;
import de.schlund.pfixcore.workflow.ContextInterceptor;

/**
 * Holds a context interceptor step object.  
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public interface ContextInterceptorHolder {

    /**
     * Returns the context interceptor object. The returned object must either 
     * implement the {@link ContextInterceptor} interface or be a {@link BeanReference} 
     * to a bean that implements this interface.
     * 
     * @return page flow step
     */
    Object getContextInterceptorObject();
}
