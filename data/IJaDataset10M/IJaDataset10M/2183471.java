package org.granite.tide.jcdi;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.event.Notify;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

/**
 * TideEvents override to intercept JCDI events handling
 * 
 * @author William DRAI
 */
public class TideEvents {

    private static final long serialVersionUID = -5395975397632138270L;

    @Inject
    private BeanManager manager;

    public void processEvent(@Observes(notifyObserver = Notify.ALWAYS) @Any Object event) {
        try {
            @SuppressWarnings("unchecked") Bean<JCDIServiceContext> scBean = (Bean<JCDIServiceContext>) manager.getBeans(JCDIServiceContext.class).iterator().next();
            JCDIServiceContext serviceContext = (JCDIServiceContext) manager.getReference(scBean, JCDIServiceContext.class, manager.createCreationalContext(scBean));
            if (serviceContext != null) serviceContext.processEvent(event);
        } catch (ContextNotActiveException e) {
        }
    }
}
