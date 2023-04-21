package com.hp.hpl.mars.portal.component;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.mars.portal.Portal;
import com.hp.hpl.mars.portal.PortalConfigurationException;
import com.hp.hpl.mars.portal.PortalException;
import com.hp.hpl.mars.portal.util.RDFUtil;
import com.hp.hpl.mars.portal.widget.Widget;

/**
 * Manages components for a request.
 * 
 * <p>
 * A component manager is used to create components for a request. It ensures
 * that each component in a request has a unique name and organising the
 * connecting of dependent components together.
 * </p>
 * 
 * <p>
 * <b>Contract</b>
 * </p>
 * 
 * <ol>
 * 
 * </ol>
 * 
 */
public class ComponentManager implements LifecycleManager {

    private final Log log = LogFactory.getLog(ComponentManager.class);

    private ComponentFactoryCatalog factoryCatalog;

    protected Portal portal;

    protected LifecycleManagerHelper lifecycleManager = new LifecycleManagerHelper();

    protected HttpServletRequest servletRequest;

    protected HttpServletResponse servletResponse;

    protected Map requestParameters;

    private Map<Resource, Object> cache = new HashMap<Resource, Object>();

    private Map<String, String> interactionStates = new HashMap<String, String>();

    protected Map<String, Object> nameTable = new HashMap<String, Object>();

    /**
	 * @param factoryCatalog
	 *            A catalog of factories.
	 */
    public ComponentManager(Portal portal, ComponentFactoryCatalog factoryCatalog) {
        this.portal = portal;
        this.factoryCatalog = factoryCatalog;
    }

    public ComponentFactoryCatalog getFactoryCatalog() {
        return factoryCatalog;
    }

    /**
	 * Get a component from an RDF component description.
	 * 
	 * <p>
	 * Multiple calls with the same argument will return the same (==)
	 * component.
	 * </p>
	 * 
	 * @param r
	 *            the RDF description of the component.
	 * @return the requested component.
	 * @throws PortalConfigurationException
	 *             if factory for the resource cannot be found
	 */
    public Object getStaticComponent(Resource r) {
        Object result = null;
        if (cache.containsKey(r)) {
            result = cache.get(r);
        } else {
            Object c = createNewObject(r, null);
            cache.put(r, c);
            result = c;
        }
        return result;
    }

    /**
	 * Return a previously created component.
	 * 
	 * <p>
	 * <b>Contract</b>
	 * </p>
	 * 
	 * <ol>
	 * <li> Returns the same (==) component object as was previously returned
	 * when the component created with a call to getComponent. Throws a
	 * <code>PortalException</code> if the component was not previously
	 * created.</li>
	 * </ol>
	 * 
	 * @param r
	 *            the RDF description of the component to be returned.
	 * @return the required component.
	 */
    public Object getExistingComponent(Resource r) {
        if (cache.containsKey(r)) {
            return cache.get(r);
        }
        throw new PortalException("Attempt to access a component that has not been created: " + r);
    }

    /**
	 * Return a cloned component given a resource description. Does not affect
	 * cache
	 * 
	 * <p>
	 * <b>Contract</b>
	 * <p>
	 * 
	 * <ol>
	 * <li>Returns a component as described by <code>resource</code></li>
	 * <li>Returns a new instance of the component each time it is called.</li>
	 * </ol>
	 * 
	 * @param resource
	 *            a description of the component to be cloned.
	 * @param id
	 *            an id for the component - must not be null
	 * @return the newly minted component.
	 */
    public Component getDynamicComponent(Resource resource, String id) {
        if (id == null) {
            throw new PortalException("Could not initialize dynamic component for resource " + resource + ", id should not be null");
        }
        Component result = (Component) createNewObject(resource, id);
        return result;
    }

    private Object createNewObject(Resource resource, String id) {
        Resource rType = RDFUtil.getComponentType(resource, factoryCatalog);
        ComponentFactory f = factoryCatalog.getComponentFactory(rType.getURI());
        if (f == null) {
            throw new PortalConfigurationException("Catalog misconfiguration: no factory for resource type " + rType);
        }
        Object c = f.getComponent(resource, this, id);
        if (c instanceof Component) {
            registerComponent((Component) c);
        }
        return c;
    }

    /**
	 * Set the manager reference for the component, update nameTable, load
	 * interactionState
	 * 
	 * @param c
	 */
    public void registerComponent(Component c) {
        c.setComponentManager(this);
        String cId = c.getId();
        if (nameTable.containsKey(cId)) {
            throw new PortalException("Using duplicate id: " + cId + " for component of type " + c.getClass().getName());
        } else {
            nameTable.put(cId, c);
        }
        if (servletRequest.getParameterMap().containsKey(cId)) {
            c.setInteractionState(servletRequest.getParameter(cId));
        }
        if (c instanceof LifecycleEventListener) {
            addLifecycleEventListener((LifecycleEventListener) c);
        }
        if (c instanceof Widget) {
            ((Widget) c).afterInteractionState();
        }
    }

    public void registerComponentIfNew(Component c) {
        if (!nameTable.containsKey(c.getId())) {
            registerComponent(c);
        }
    }

    /**
	 * Add the given component as interested in the lifecycle events
	 */
    public void addLifecycleEventListener(LifecycleEventListener listener) {
        lifecycleManager.addLifecycleEventListener(listener);
    }

    /**
	 * Remove the given component from lifecycle event listening
	 */
    public void removeLifecycleEventListener(LifecycleEventListener listener) {
        lifecycleManager.removeLifecycleEventListener(listener);
    }

    /**
	 * Send the specified kind of event to all the listeners
	 * 
	 * @param kind
	 */
    public void announce(LifecycleEvent.Kind kind) {
        lifecycleManager.announce(this, kind);
    }

    /**
	 * Get the portal attribute.
	 * 
	 * @return the portal attribute.
	 */
    public Portal getPortal() {
        return portal;
    }

    /**
	 * @return the servletRequest
	 */
    public HttpServletRequest getServletRequest() {
        return servletRequest;
    }

    /**
	 * @param servletRequest
	 *            the servletRequest to set
	 */
    public void setServletRequest(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    /**
	 * @return the servletResponse
	 */
    public HttpServletResponse getServletResponse() {
        return servletResponse;
    }

    /**
	 * @param servletResponse
	 *            the servletResponse to set
	 */
    public void setServletResponse(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }

    /**
	 * Get the values for a named request parameter.
	 * 
	 * <p>
	 * This is a convenience method. It is equivalent to
	 * <code>this.getServletRequest().getParameterValues()</code>. The
	 * contract for this method is the same as that of getParameterValues method
	 * of ServletRequest.
	 * </p>
	 * 
	 * <p>
	 * <b>Contract</b>
	 * </p>
	 * 
	 * <ol>
	 * <li>If there is no parameter with the given name the value returned is
	 * <code>null</code>.</li>
	 * <li>If there is one or more parameters with the given name the value
	 * returned is an array containing all of those values.</li>
	 * </ol>
	 * 
	 * @paramName the name of the parameter to get.
	 * @return the values of the named parameter.
	 */
    public String[] getParameter(String paramName) {
        return servletRequest.getParameterValues(paramName);
    }

    /**
	 * For arguments "pageSetId", "linkObject" and list ["p1","v1","p2","v2"]
	 * produce the following:<br />
	 * &lt;a href="pageSetId?p1=v1&p2=v2"&gt;linkObject&lt;/a&gt; The parameter
	 * names "p1", "p2", etc. are always strings. The respective values ("v1",
	 * "v2", etc.) may be any type that was passed from Velocity template; they
	 * are converted to strings - first with some datatype-dependent SPARQL
	 * escaping and then URL-encoded.
	 * 
	 * @param pageSetId
	 *            indicates the pageset we are linking to
	 * @param linkObject
	 *            the string (HTML construct), which is surrounded by
	 *            anchor(&lt;a&gt;) tags
	 * @param parameters
	 *            a list of even length, containing parameters and their values
	 *            (not yet URLEncoded)
	 * @param actionComponent
	 *            a component ID on which an action is to be performed
	 * @param action
	 *            the action to be performed by the actionComponent
	 * @return HTML construct enclosed in anchor tags.
	 */
    public String getLinkToPageSet(String pageSetId, String linkObject, List parameters, String actionComponent, String action) {
        String url = getUrlToPageSet(pageSetId, parameters, actionComponent, action);
        return "<a href=\"" + url + "\">" + linkObject + "</a>";
    }

    /**
	 * See getLinkToPageSet() for explanation of parameters
	 */
    public String getUrlToPageSet(String pageSetId, List parameters, String actionComponent, String action) {
        String url = pageSetId;
        Iterator iter = parameters.iterator();
        String sep = "?";
        if (actionComponent != null) {
            url = url + sep + "_ac=" + encodeAsUtf(actionComponent);
            sep = "&amp;";
            if (action != null) {
                url = url + sep + "_aa=" + encodeAsUtf(action);
            }
        }
        while (iter.hasNext()) {
            String pName = (String) iter.next();
            String pValue = null;
            if (iter.hasNext()) {
                Object val = iter.next();
                pValue = "" + val;
            }
            url = url + sep + encodeAsUtf(pName) + "=" + encodeAsUtf(pValue);
            sep = "&amp;";
        }
        for (String key : interactionStates.keySet()) {
            url = url + sep + encodeAsUtf(key) + "=" + encodeAsUtf(interactionStates.get(key));
            sep = "&amp;";
        }
        return url;
    }

    private String encodeAsUtf(String arg) {
        if (arg == null) {
            log.warn("Argument to encode should not be null");
            return "null";
        }
        try {
            String result = URLEncoder.encode(arg, "UTF-8");
            return result;
        } catch (Exception e) {
            log.error("Unexpected: Bad encoding UTF-8", e);
            throw new PortalException("Unexpected: Bad encoding UTF-8");
        }
    }

    /**
	 * Find a component, which needs action to be performed
	 */
    public void doAction() {
        String componentName = servletRequest.getParameter("_ac");
        String action = servletRequest.getParameter("_aa");
        Component component = (Component) nameTable.get(componentName);
        if (component != null) {
            component.doAction(action);
        } else {
            log.warn("Action '" + action + "' on component '" + componentName + "' ignored; component does not exist");
        }
    }

    /**
	 * Register a component's interaction state, if it is not null; the map of
	 * interaction states is used to generate links, e.g. in getUrlToPageSet()
	 * method
	 * 
	 * @param component
	 *            the component whose state is being registered.
	 * @param state
	 *            a serialization of the component's interaction state
	 */
    public void registerInteractionState(Component component, String state) {
        if (state != null) {
            interactionStates.put(component.getId(), state);
        }
    }
}
