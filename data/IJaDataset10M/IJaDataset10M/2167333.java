package org.nexopenframework.samples.swf.context;

import org.nexopenframework.security.workflow.SecurityActorLocator;
import org.nexopenframework.spring.context.BootstrapEvent;
import org.nexopenframework.spring.context.BootstrapListener;
import org.nexopenframework.workflow.providers.jbpm31.JbpmService;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;

/**
 * <p>simple-workflow using NexOpen</p>
 * 
 * <p>This {@link BootstrapListener} is registered</p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class JbpmServiceListener implements BootstrapListener {

    /**
	 * <p></p>
	 * 
	 * @see org.nexopenframework.spring.context.BootstrapListener#contextDestroyed(org.nexopenframework.spring.context.BootstrapEvent)
	 */
    public void contextDestroyed(final BootstrapEvent event) {
    }

    /**
	 * <p></p>
	 * 
	 * @see org.nexopenframework.spring.context.BootstrapListener#contextInitialized(org.nexopenframework.spring.context.BootstrapEvent)
	 */
    public void contextInitialized(final BootstrapEvent event) {
        final ApplicationContext ac = (ApplicationContext) event.getContext().getOriginal();
        final JbpmService service = (JbpmService) BeanFactoryUtils.beanOfTypeIncludingAncestors(ac, JbpmService.class);
        final SecurityActorLocator actorLocator = new SecurityActorLocator();
        service.setActorLocator(actorLocator);
    }
}
