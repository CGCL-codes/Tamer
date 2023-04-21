package org.nexopenframework.core.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.Ordered;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Generic contract for inner components in the NexOpen Framework 
 *    for dealing with registering in the Spring Factory at bootstraping
 *    during the <code>Component Definition Registry Phase</code>.
 *    Basically, NexOpen application contexts perform a scanning of classes
 *    in the application (skipping the framework ones) and pass the controls
 *    to thess interfaces.</p>
 * 
 * @see Ordered
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public interface ComponentDefinitionRegistry extends Ordered {

    /**
	 * @param clazz
	 * @return
	 */
    boolean supports(Class clazz);

    /**
	 * @param clazz
	 * @param registry
	 * @throws BeansException
	 */
    void register(Class clazz, BeanDefinitionRegistry registry) throws BeansException;
}
