package org.nexopenframework.business.support;

import org.nexopenframework.business.BusinessService;
import org.nexopenframework.core.factory.AbstractComponentDefinitionRegistry;
import org.nexopenframework.core.factory.ComponentDefinitionRegistry;

/**
 * <p>NexTReT Open Framework</p>
 * 
 * <p>Implementation of {@link ComponentDefinitionRegistry} for dealing 
 * with {@link BusinessService} classes.</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class BusinessDefinitionRegistry extends AbstractComponentDefinitionRegistry implements ComponentDefinitionRegistry {

    /**
	 * 
	 * @see org.nexopenframework.deployment.factory.ComponentDefinitionRegistry#supports(java.lang.Class)
	 */
    public boolean supports(Class clazz) {
        return !clazz.isInterface() && BusinessService.class.isAssignableFrom(clazz);
    }
}
