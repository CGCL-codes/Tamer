package org.nexopenframework.web.dwr.support;

import org.nexopenframework.core.factory.AbstractComponentDefinitionRegistry;
import org.nexopenframework.core.factory.ComponentDefinitionRegistry;
import org.nexopenframework.web.dwr.AjaxEventExecutor;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class AjaxEventExecutorDefintionRegistry extends AbstractComponentDefinitionRegistry implements ComponentDefinitionRegistry {

    /**
	 * 
	 * @see org.nexopenframework.core.factory.ComponentDefinitionRegistry#supports(java.lang.Class)
	 */
    public boolean supports(Class clazz) {
        return !clazz.isInterface() && AjaxEventExecutor.class.isAssignableFrom(clazz);
    }
}
