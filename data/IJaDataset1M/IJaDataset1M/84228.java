package org.nexopenframework.core.framework.config;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nexopenframework.core.CoreException;
import org.nexopenframework.core.framework.Lifecycle;
import org.nexopenframework.core.framework.State;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Extension of {@link BeanPostProcessor} for registering all beans which implements 
 * {@link Lifecycle} contract API.</p>
 * 
 * <p><b>07/01/2010</b< REVISION 3257 : Added check of state of {@link Lifecycle} component for avoiding more than one starting</p>
 * 
 * @see org.springframework.beans.factory.config.BeanPostProcessor
 * @see org.springframework.context.Lifecycle
 * @author Francesc Xavier Magdaleno
 * @since 2.0.0.GA
 * @version $Revision 3257$,$Date: 2009-10-05 20:48:44 +0100 $
 */
@Order(100)
public class LifecycleBeanPostProcessor implements BeanPostProcessor, ApplicationListener<ApplicationContextEvent> {

    /**name of this bean in Spring container*/
    public static final String BEAN_NAME = "openfrwk.core.lifecycle_processor";

    /**Jakarta Commons Logging (JCL) facility */
    private static final Log logger = LogFactory.getLog(LifecycleBeanPostProcessor.class);

    /**NexOpen {@link Lifecycle} implementations*/
    private final List<Lifecycle> lifecycles = new ArrayList<Lifecycle>();

    /**
	 * 
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
	 */
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        return bean;
    }

    /**
	 * <p>We check if bean</p>
	 * 
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
	 */
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
        if (bean instanceof Lifecycle) {
            lifecycles.add((Lifecycle) bean);
        }
        return bean;
    }

    public void onApplicationEvent(final ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            start();
        } else if (event instanceof ContextClosedEvent) {
            stop();
        }
    }

    protected void start() {
        for (final Lifecycle lifecycle : this.lifecycles) {
            if (lifecycle.isStarted()) {
                continue;
            }
            try {
                lifecycle.create();
                lifecycle.start();
            } catch (final Exception e) {
                logger.error("Problem during create or start Lifecycle", e);
                throw new CoreException("Problem during create or start Lifecycle", e);
            }
        }
    }

    protected void stop() {
        for (final Lifecycle lifecycle : this.lifecycles) {
            if (State.DESTROYED.equals(lifecycle.getState())) {
                continue;
            }
            lifecycle.stop();
            lifecycle.destroy();
        }
    }
}
