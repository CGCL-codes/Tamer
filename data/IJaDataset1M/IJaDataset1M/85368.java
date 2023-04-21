package org.ofbiz.service.engine;

import java.util.Map;
import java.util.List;
import java.util.Iterator;
import javolution.util.FastMap;
import org.ofbiz.service.ServiceDispatcher;
import org.ofbiz.service.ModelService;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.GenericServiceCallback;
import org.ofbiz.service.config.ServiceConfigUtil;
import org.ofbiz.base.config.GenericConfigException;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilXml;
import org.w3c.dom.Element;

/**
 * Abstract Service Engine
 */
public abstract class AbstractEngine implements GenericEngine {

    public static final String module = AbstractEngine.class.getName();

    protected static Map locationMap = null;

    protected ServiceDispatcher dispatcher = null;

    protected AbstractEngine(ServiceDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        initLocations();
    }

    protected synchronized void initLocations() {
        if (locationMap == null) {
            locationMap = FastMap.newInstance();
            Element root = null;
            try {
                root = ServiceConfigUtil.getXmlRootElement();
            } catch (GenericConfigException e) {
                Debug.logError(e, module);
            }
            if (root != null) {
                List locationElements = UtilXml.childElementList(root, "service-location");
                if (locationElements != null) {
                    Iterator i = locationElements.iterator();
                    while (i.hasNext()) {
                        Element e = (Element) i.next();
                        locationMap.put(e.getAttribute("name"), e.getAttribute("location"));
                    }
                }
            }
            Debug.logInfo("Loaded Service Locations : " + locationMap, module);
        }
    }

    protected String getLocation(ModelService model) {
        if (locationMap.containsKey(model.location)) {
            return (String) locationMap.get(model.location);
        } else {
            return model.location;
        }
    }

    /**
     * @see org.ofbiz.service.engine.GenericEngine#sendCallbacks(org.ofbiz.service.ModelService, java.util.Map, java.lang.Object, int)
     */
    public void sendCallbacks(ModelService model, Map context, Object cbObj, int mode) throws GenericServiceException {
        List callbacks = dispatcher.getCallbacks(model.name);
        if (callbacks != null) {
            Iterator i = callbacks.iterator();
            while (i.hasNext()) {
                GenericServiceCallback gsc = (GenericServiceCallback) i.next();
                if (gsc.isEnabled()) {
                    if (cbObj == null) {
                        gsc.receiveEvent(context);
                    } else if (cbObj instanceof Throwable) {
                        gsc.receiveEvent(context, (Throwable) cbObj);
                    } else if (cbObj instanceof Map) {
                        gsc.receiveEvent(context, (Map) cbObj);
                    } else {
                        throw new GenericServiceException("Callback object is not Throwable or Map");
                    }
                } else {
                    i.remove();
                }
            }
        }
    }
}
