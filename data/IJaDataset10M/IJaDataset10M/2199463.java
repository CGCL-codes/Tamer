package org.mitre.midiki.impl.mitre;

import org.mitre.midiki.state.*;
import java.util.*;
import java.util.logging.*;

/**
 * Provides an implementation of <code>Mediator</code> for testing.
 *
 * @author <a href="mailto:cburke@mitre.org">Carl Burke</a>
 * @version 1.0
 * @since 1.0
 */
public class DummyMediator implements Mediator {

    private static Logger logger = Logger.getLogger("org.mitre.midiki.impl.mitre.DummyMediator");

    /**
     * Local storage of data distribution agent name.
     */
    private String myAgentName;

    private HashMap serviceHandlers;

    private boolean isNative = false;

    /**
     * Returns the tag by which this Mediator is known.
     */
    public String publicName() {
        return "default";
    }

    /**
     * Describe <code>agentName</code> method here.
     *
     */
    public String agentName() {
        return myAgentName;
    }

    /**
     * Set configuration data for this mediator from properties.
     *
     * @param props a <code>PropertyTree</code> value
     * @return a <code>boolean</code> value
     */
    public boolean configure(PropertyTree props) {
        PropertyTree pmode = props.child("mode");
        if (pmode != null) {
            isNative = ((String) (pmode.value())).equalsIgnoreCase("native");
        }
        return true;
    }

    /**
     * Returns <code>true</code> if this mediator follows Midiki
     * protocols for data exchange, or <code>false</code> if only
     * the underlying framework is to be used. Enables/disables
     * additional processing in cells.
     *
     * @return a <code>boolean</code> value
     */
    public boolean usesMidikiProtocol() {
        return !isNative;
    }

    /**
     * Register the agent with the mediator. There should be
     * only one InfoState connected to this mediator.
     *
     */
    public boolean register(String asName) {
        logger.logp(Level.FINER, "org.mitre.midiki.impl.mitre.DummyMediator", "register", asName);
        myAgentName = asName;
        return true;
    }

    /**
     * Describe <code>declareServices</code> method here.
     *
     */
    public boolean declareServices(Object services, Object requests) {
        logger.entering("org.mitre.midiki.impl.mitre.DummyMediator", "declareServices", services);
        return true;
    }

    /**
     * Describe <code>appendServiceDeclaration</code> method here.
     *
     */
    public Object appendServiceDeclaration(String name, Object parameters, Object serviceList) {
        ArrayList decl = new ArrayList();
        decl.add(name);
        decl.add(((Contract) parameters).name());
        ((List) serviceList).add(decl);
        logger.entering("org.mitre.midiki.impl.mitre.DummyMediator", "appendServiceDeclaration", decl);
        String serviceTag = ((Contract) parameters).name() + name;
        tag_id++;
        return serviceTag;
    }

    /**
     * Describe <code>appendQueryDeclaration</code> method here.
     *
     */
    public Object appendQueryDeclaration(String cell, String query, boolean isnative, Object parameters, Object serviceList) {
        ArrayList decl = new ArrayList();
        decl.add(cell);
        decl.add(query);
        decl.add(new Boolean(isnative));
        decl.add(parameters);
        ((List) serviceList).add(decl);
        logger.entering("org.mitre.midiki.impl.mitre.DummyMediator", "appendQueryDeclaration", decl);
        String serviceTag = "query$" + cell + "$" + query;
        tag_id++;
        return serviceTag;
    }

    /**
     * Describe <code>appendActionDeclaration</code> method here.
     *
     */
    public Object appendActionDeclaration(String cell, String action, boolean isnative, Object parameters, Object serviceList) {
        ArrayList decl = new ArrayList();
        decl.add(cell);
        decl.add(action);
        decl.add(new Boolean(isnative));
        decl.add(parameters);
        ((List) serviceList).add(decl);
        logger.entering("org.mitre.midiki.impl.mitre.DummyMediator", "appendActionDeclaration", decl);
        String serviceTag = "method$" + cell + "$" + action;
        tag_id++;
        return serviceTag;
    }

    /**
     * Describe <code>appendServiceSpecification</code> method here.
     * Declarations handle the 'subscribe' side of the interface.
     * Some interfaces also require specification of the 'publish' side.
     * That's what the specifications are for.
     *
     */
    public Object appendServiceSpecification(String name, Object parameters, Object serviceList) {
        return null;
    }

    /**
     * Describe <code>appendQuerySpecification</code> method here.
     *
     */
    public Object appendQuerySpecification(String cell, String query, boolean isnative, Object parameters, Object serviceList) {
        return null;
    }

    /**
     * Describe <code>appendActionSpecification</code> method here.
     *
     */
    public Object appendActionSpecification(String cell, String action, boolean isnative, Object parameters, Object serviceList) {
        return null;
    }

    /**
     * Describe <code>registerServiceHandler</code> method here.
     *
     */
    public void registerServiceHandler(Object service, ServiceHandler handler) {
        logger.entering("org.mitre.midiki.impl.mitre.DummyMediator", "registerServiceHandler", service);
        if (serviceHandlers.put(service, handler) != null) {
            logger.logp(Level.FINER, "org.mitre.midiki.impl.mitre.DummyMediator", "registerServiceHandler", "replacing previous handler");
        }
    }

    /**
     * Describe <code>assertReadiness</code> method here.
     *
     */
    public void assertReadiness() {
        logger.entering("org.mitre.midiki.impl.mitre.DummyMediator", "assertReadiness");
    }

    /**
     * Describe <code>useService</code> method here.
     *
     */
    public boolean useService(Object request, Object parameters, Object results) {
        logger.entering("org.mitre.midiki.impl.mitre.DummyMediator", "useService", request.toString());
        boolean success = false;
        String rqst = (String) request;
        if (rqst.charAt(0) == ('$')) {
            if (parameters instanceof CellInstance) {
                rqst = ((CellInstance) parameters).instanceType.name() + rqst;
            } else {
                rqst = ((List) parameters).get(0) + rqst;
            }
        }
        ServiceHandler svc = (ServiceHandler) serviceHandlers.get(rqst);
        if (svc == null) {
            logger.logp(Level.WARNING, "org.mitre.midiki.impl.mitre.DummyMediator", "useService", request.toString() + " failed");
        } else {
            if (parameters instanceof CellInstance) {
                LinkedList params = new LinkedList();
                params.add(((CellInstance) parameters).instanceId);
                params.addAll(((CellInstance) parameters).instanceData);
                success = svc.handleEvent(rqst, params, (Collection) results);
            } else {
                success = svc.handleEvent(rqst, (List) parameters, (Collection) results);
            }
        }
        logger.exiting("org.mitre.midiki.impl.mitre.DummyMediator", "useService", request.toString());
        logger.logp(Level.FINEST, "org.mitre.midiki.impl.mitre.DummyMediator", "useService", (success ? "succeeded" : "failed"));
        return success;
    }

    /**
     * Describe <code>requestService</code> method here.
     *
     */
    public boolean requestService(Object request, Object parameters, Object results) {
        logger.entering("org.mitre.midiki.impl.mitre.DummyMediator", "requestService", request.toString());
        return false;
    }

    /**
     * Broadcasts a request to all listeners. Since this version of the
     * Mediator only serves a single local InfoState, we don't need to
     * do anything fancy. Just use the same code used for 'use'.
     *
     */
    public boolean broadcastServiceRequest(Object request, Object parameters, Object results) {
        logger.entering("org.mitre.midiki.impl.mitre.DummyMediator", "broadcastServiceRequest", request.toString());
        boolean success = false;
        String rqst = (String) request;
        if (rqst.charAt(0) == ('$')) {
            rqst = ((List) parameters).get(0) + rqst;
        }
        ServiceHandler svc = (ServiceHandler) serviceHandlers.get(rqst);
        if (svc == null) {
            logger.logp(Level.WARNING, "org.mitre.midiki.impl.mitre.DummyMediator", "broadcastServiceRequest", request.toString(), "failed");
        } else {
            success = svc.handleEvent(rqst, (List) parameters, (Collection) results);
        }
        logger.exiting("org.mitre.midiki.impl.mitre.DummyMediator", "broadcastServiceRequest", request.toString());
        logger.logp(Level.FINEST, "org.mitre.midiki.impl.mitre.DummyMediator", "broadcastServiceRequest", (success ? "succeeded" : "failed"));
        return success;
    }

    /**
     * Describe <code>getData</code> method here.
     *
     */
    public boolean getData(Object cellName, Object cellInstance, Object results) {
        logger.logp(Level.FINER, "org.mitre.midiki.impl.mitre.DummyMediator", "getData", cellName.toString(), cellInstance);
        return false;
    }

    /**
     * Describe <code>putData</code> method here.
     *
     */
    public boolean putData(Object request) {
        logger.entering("org.mitre.midiki.impl.mitre.DummyMediator", "putData", request);
        return false;
    }

    /**
     * Describe <code>replaceData</code> method here.
     *
     */
    public boolean replaceData(Object oldData, Object newData) {
        logger.logp(Level.FINER, "org.mitre.midiki.impl.mitre.DummyMediator", "replaceData", "old-data", oldData);
        logger.logp(Level.FINER, "org.mitre.midiki.impl.mitre.DummyMediator", "replaceData", "new-data", newData);
        return false;
    }

    /**
     * Describe <code>pauseInteraction</code> method here.
     *
     */
    public Object pauseInteraction(String key, Object param) {
        logger.entering("org.mitre.midiki.impl.mitre.DummyMediator", "pauseInteraction", key);
        return null;
    }

    /**
     * Describe <code>resumeInteraction</code> method here.
     *
     */
    public Object resumeInteraction(String key, Object param) {
        logger.entering("org.mitre.midiki.impl.mitre.DummyMediator", "resumeInteraction", key);
        return null;
    }

    public DummyMediator() {
        serviceHandlers = new HashMap();
    }

    static int tag_id = 0;
}
