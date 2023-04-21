package org.obe.server.j2ee.service;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import org.apache.commons.logging.Log;
import org.obe.client.api.repository.RepositoryException;
import org.obe.client.api.repository.EventTypeMetaData;
import org.obe.event.AbstractApplicationEventBroker;
import org.obe.event.BasicApplicationEventBroker;
import org.obe.server.j2ee.ejb.EventSubscriptionLocal;
import org.obe.server.j2ee.repository.EJBLocalHelper;
import org.obe.server.util.ObjectUtil;
import org.obe.spi.event.ApplicationEvent;
import org.obe.spi.service.ServiceManager;
import org.obe.xpdl.model.misc.Duration;

/**
 * Provides a persistent application event broker service.
 *
 * @author Adrian Price
 */
public class EJBApplicationEventBroker extends AbstractApplicationEventBroker {

    protected static final Log _logger = getLog(EJBApplicationEventBroker.class);

    public EJBApplicationEventBroker(ServiceManager svcMgr) {
        super(svcMgr);
    }

    protected String getConfigurationFileName() {
        String filename = BasicApplicationEventBroker.class.getName();
        return filename.substring(filename.lastIndexOf('.') + 1) + ".xml";
    }

    protected Log getLogger() {
        return _logger;
    }

    protected ApplicationEventSubscription createSubscription(String eventType, Object[] eventKeys, String predicate, Date effective, Date expiry, int count, String[] correlationKeys) throws RepositoryException {
        try {
            return EJBLocalHelper.getEventSubscriptionHome().create(eventType, eventKeys, predicate, effective, expiry, count, correlationKeys);
        } catch (CreateException e) {
            throw new RepositoryException(e);
        }
    }

    protected TemporalEventSubscription createSubscription(String eventType, Date effective, Date expiry, int count, Duration interval, String calendar, boolean recoverable, String[] correlationKeys) throws RepositoryException {
        try {
            return EJBLocalHelper.getEventSubscriptionHome().create(eventType, effective, expiry, count, interval == null ? null : interval.toString(), calendar, recoverable, correlationKeys);
        } catch (CreateException e) {
            throw new RepositoryException(e);
        }
    }

    public void unsubscribe(String[] correlationKeys, boolean exact) throws RepositoryException {
        try {
            Collection subs = EJBLocalHelper.getEventSubscriptionHome().xfindByCorrelationKeys(correlationKeys, exact);
            for (Iterator i = subs.iterator(); i.hasNext(); ) {
                EventSubscriptionLocal sub = (EventSubscriptionLocal) i.next();
                sub.remove();
            }
        } catch (FinderException e) {
            throw new RepositoryException(e);
        } catch (RemoveException e) {
            throw new RepositoryException(e);
        }
    }

    protected Collection findSubscriptions(ApplicationEvent event, EventTypeMetaData metaData) throws RepositoryException {
        try {
            Object[] keys = event.getKeys();
            return EJBLocalHelper.getEventSubscriptionHome().xfindByEvent(event.getEventType(), keys, ObjectUtil.hashcode(keys, metaData.getInputParameterCount()));
        } catch (FinderException e) {
            throw new RepositoryException(e);
        }
    }

    protected void storeEvent(ApplicationEvent event, EventTypeMetaData metaData) throws RepositoryException {
        try {
            EJBLocalHelper.getApplicationEventHome().create(event, ObjectUtil.hashcode(event.getKeys(), metaData.getInputParameterCount()));
        } catch (CreateException e) {
            throw new RepositoryException(e);
        }
    }

    protected Collection findStoredEvents(String eventType, Object[] subscriptionKeys) throws RepositoryException {
        try {
            return EJBLocalHelper.getApplicationEventHome().xfindByEvent(eventType, subscriptionKeys);
        } catch (FinderException e) {
            throw new RepositoryException(e);
        }
    }
}
