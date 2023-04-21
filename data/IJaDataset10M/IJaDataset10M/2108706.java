package org.personalsmartspace.recommendersystem.broker.impl;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.osgi.service.component.ComponentContext;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.onm.api.pss3p.ICallbackListener;
import org.personalsmartspace.onm.api.pss3p.ONMException;
import org.personalsmartspace.onm.api.pss3p.XMLConverter;
import org.personalsmartspace.pss_sm_api.impl.PssService;
import org.personalsmartspace.recommendersystem.api.pss3p.ISuggestions;
import org.personalsmartspace.recommendersystem.api.pss3p.ServiceItem;
import org.personalsmartspace.spm.identity.api.platform.DigitalPersonalIdentifier;
import org.personalsmartspace.spm.identity.api.platform.MalformedDigitialPersonalIdentifierException;
import org.personalsmartspace.spm.trust.api.platform.IMetaTrust;
import org.personalsmartspace.spm.trust.api.platform.ITrust;
import org.personalsmartspace.spm.trust.api.pss2pss.TrustEvaluationException;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;
import org.personalsmartspace.sre.api.pss3p.PssServiceIdentifier;

/**
 * Aggregating callback. A call back that can be used to aggregate the results
 * from multiple messages. The results are sorted, and the top n are kept. The
 * maximum size of the returned list is specified in the constructor.
 * 
 * @author mcrotty@users.sourceforge.net
 * 
 */
public class AggregatingCallback implements ICallbackListener, ISuggestions {

    private PSSLog logger = null;

    private SortedSet<ServiceItem> results = null;

    private int maxSize = 0;

    private int expectedCalls = 1;

    private ITrust trustManager = null;

    private AvailableServices services = null;

    private ComponentContext context = null;

    private long initialTimestamp;

    private long completeTimestamp;

    /**
     * Constructor which accepts the maximum bound to accumulate
     * 
     * @param bound
     *            - Upper bound on the number of results
     * @param mTM
     *            - A trust manager to calculate how trustworthy the source is.
     * @param ccontext
     *            - The component context
     */
    public AggregatingCallback(int bound, ITrust mTM, ComponentContext ccontext) {
        logger = new PSSLog(this);
        results = Collections.synchronizedSortedSet(new TreeSet<ServiceItem>());
        maxSize = bound;
        expectedCalls = 1;
        trustManager = mTM;
        context = ccontext;
        initialTimestamp = 0;
        completeTimestamp = 0;
    }

    /**
     * Constructor which accepts the maximum bound of results to accumulate. And
     * the number of expected batches of results.
     * 
     * @param bound
     *            - Upper bound of the maximum number of results
     * @param noOfCalls
     *            - Expected number of calls
     * @param mTM
     *            - The Trust manager to use
     * @param ccontext
     *            - The component context
     */
    public AggregatingCallback(int bound, int noOfCalls, ITrust mTM, ComponentContext ccontext) {
        logger = new PSSLog(this);
        results = Collections.synchronizedSortedSet(new TreeSet<ServiceItem>());
        maxSize = bound;
        expectedCalls = noOfCalls;
        trustManager = mTM;
        context = ccontext;
        initialTimestamp = 0;
        completeTimestamp = 0;
    }

    @Override
    public void handleCallbackObject(Object object) {
        List<ServiceItem> partial = null;
        logger.info("Synchronous results received");
        if (object != null) {
            if (object instanceof List) {
                partial = (List<ServiceItem>) object;
                addAll(partial);
            } else {
                logger.debug("Invalid results received. Expected a List and got " + object.getClass());
            }
        } else {
            logger.debug("Invalid results received. Expected a List and got (null)");
        }
        expectedCallMade();
    }

    @Override
    public void handleCallbackString(String message) {
        List<ServiceItem> partial = new ArrayList<ServiceItem>();
        logger.info("Asynchronous results received");
        try {
            Object object = XMLConverter.xmlToObject(message, partial.getClass().getCanonicalName(), context.getUsingBundle());
            if ((object != null) && (object instanceof List)) {
                partial = (List<ServiceItem>) object;
                addAll(partial);
            } else {
                logger.debug("Invalid results received");
            }
        } catch (ONMException e) {
            logger.error("Error when accessing recommended services.", e);
        }
        expectedCallMade();
    }

    @Override
    public void handleErrorMessage(String message) {
        logger.error(message);
        expectedCallMade();
    }

    /**
     * Add a service item to the results set. An item is added only if it is
     * better than the worst <code>ServiceItem</code>
     * 
     * @param item
     *            - Service Item to add.
     * @return true - if the item was added.
     */
    public boolean add(ServiceItem item) {
        if (initialTimestamp == 0) {
            initialTimestamp = System.currentTimeMillis();
        }
        if (results.size() < maxSize) {
            return results.add(item);
        } else {
            if (results.last().compareTo(item) > 0) {
                results.remove(results.last());
                return results.add(item);
            }
            return false;
        }
    }

    /**
     * Add all the service items in the Collection
     * 
     * @param allItems
     *            - A collection of service Items
     * @return true - if some items were added.
     */
    public boolean addAll(Collection<? extends ServiceItem> allItems) {
        if ((allItems != null) && (!allItems.isEmpty())) {
            for (Iterator<? extends ServiceItem> i = allItems.iterator(); i.hasNext(); ) {
                ServiceItem si = i.next();
                adjustWithDetail(si);
                add(si);
            }
            logger.debug("Merged " + allItems.size() + " results");
            return true;
        }
        return false;
    }

    private PssService getService(ServiceItem si) {
        if (services != null) {
            IServiceIdentifier id = new PssServiceIdentifier(si.getServiceId());
            for (PssService i : services.getAvailableServices()) {
                if (i.getServiceId().equals(id)) {
                    return i;
                }
            }
        }
        return null;
    }

    private void adjustWithDetail(ServiceItem si) {
        try {
            PssService ps = getService(si);
            if (ps != null) {
                si.setName(ps.getServiceName());
                if (trustManager != null) {
                    IDigitalPersonalIdentifier provider = DigitalPersonalIdentifier.fromString(ps.getServiceId().getOperatorId());
                    IMetaTrust mt = trustManager.evaluate(provider, ps.getServiceType());
                    return;
                }
            }
        } catch (IllegalArgumentException ia) {
            logger.warn("Ignoring malformed service identifier");
        } catch (MalformedDigitialPersonalIdentifierException mdpi) {
            logger.warn("Ignoring malformed DPI", mdpi);
        } catch (TrustEvaluationException te) {
            logger.warn("Ignoring trust calculation exception", te);
        }
    }

    /**
     * Convert the results into a list.
     * 
     * @return List - A list of service items
     */
    public List<ServiceItem> toList() {
        return new ArrayList<ServiceItem>(results);
    }

    private synchronized void expectedCallMade() {
        expectedCalls--;
        if (expectedCalls < 1) {
            completeTimestamp = System.currentTimeMillis();
            notifyAll();
        }
        logger.debug("Waiting for " + expectedCalls + " more results.");
    }

    @Override
    public synchronized boolean isComplete() {
        return (expectedCalls <= 0);
    }

    @Override
    public synchronized List<ServiceItem> getSuggestions(int timeout) {
        if (!isComplete()) {
            try {
                if (timeout == 0) timeout = 1;
                wait(timeout);
            } catch (InterruptedException e) {
            }
        }
        return new ArrayList<ServiceItem>(results);
    }

    @Override
    public boolean isEmpty() {
        return results.isEmpty();
    }

    public void show(PrintStream ps) {
        ps.print("{");
        for (Iterator<ServiceItem> i = results.iterator(); i.hasNext(); ) {
            ps.print(i.next().getServiceId() + ",");
        }
        ps.println("}");
    }

    /**
     * Sets the list of available services. This is used from the addName method
     * 
     * @param services
     *            the services to set
     */
    public void setAvailableServices(AvailableServices services) {
        this.services = services;
    }

    /**
     * @return the initialTimestamp
     */
    public long getInitialTimestamp() {
        return initialTimestamp;
    }

    /**
     * @return the completeTimestamp
     */
    public long getCompletedTimestamp() {
        return completeTimestamp;
    }
}
