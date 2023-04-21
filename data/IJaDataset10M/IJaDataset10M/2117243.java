package org.personalsmartspace.cm.broker.impl.remote;

import java.util.HashMap;
import java.util.Map;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.personalsmartspace.cm.api.pss3p.ContextBrokerException;
import org.personalsmartspace.cm.api.pss3p.ContextException;
import org.personalsmartspace.onm.api.pss3p.ONMException;
import com.thoughtworks.xstream.XStream;

/**
 * This class provide static methods for serialising and de-serialising POJOs
 * (or an array of POJOs) to/from XML string(s).
 * 
 * @author Christopher Viana Lima
 */
public class LocalXMLConverter {

    private static XStream xstream = new XStream();

    public static final String DB_MANAGER_INTERFACE = "org.personalsmartspace.cm.db.api.platform.ICtxDBManager";

    public static final String PRIV_PREF_MANAGER_INTERFACE = "org.personalsmartspace.spm.preference.api.platform.IPrivacyPreferenceManager";

    public static final String REASONING_MANAGER_INTERFACE = "org.personalsmartspace.cm.reasoning.api.platform.IReasoningManager";

    public static final String RULE_INTERFACE = "org.personalsmartspace.lm.api.IRule";

    private static Map<String, String> frameworkClasses = null;

    static {
        frameworkClasses = new HashMap<String, String>();
        frameworkClasses.put("org.personalsmartspace.spm.preference.api.platform.PPNPrivacyPreferenceTreeModel", PRIV_PREF_MANAGER_INTERFACE);
        frameworkClasses.put("org.personalsmartspace.cm.reasoning.proximity.impl.ProximityRule", RULE_INTERFACE);
        frameworkClasses.put("org.personalsmartspace.lm.bayesian.rule.BayesianRule", "org.personalsmartspace.lm.bayesian.rule.BayesianRule");
    }

    ;

    /**
     * To serialise an POJO to XML
     * 
     * @param obj
     *            object to be serialised.
     * @return String serialised XML
     */
    public static String objectToXml(Object obj) {
        return xstream.toXML(obj);
    }

    /**
     * To serialise an array of POJOs to XML
     * 
     * @param objs
     *            objects to be serialised.
     * @return String[] an array of serialised XML
     */
    public static String[] objectsToXml(Object[] objs) {
        String[] objectsAsXML = new String[objs.length];
        for (int i = 0; objs.length > i; i++) {
            objectsAsXML[i] = xstream.toXML(objs[i]);
        }
        return objectsAsXML;
    }

    /**
     * To de-serialise an POJO from XML
     * 
     * @param xml
     *            serialised XML
     * @param className
     *            base class name of serialised object
     * @param bundle
     *            the local bundle
     * @return Object
     */
    public static Object xmlToObject(String xml, String className, Bundle bundle) throws ContextException {
        try {
            if (null != bundle) {
                Class<?> objectClass = bundle.loadClass(className);
                if (objectClass.getClassLoader() != null) {
                    xstream.setClassLoader(objectClass.getClassLoader());
                }
            }
            return xstream.fromXML(xml);
        } catch (ClassNotFoundException e) {
            throw new ContextBrokerException("Could not load class: " + className, e);
        }
    }

    /**
     * To de-serialise an array of POJOs from XML
     * 
     * @param xmlArray
     *            an array of serialised POJOs
     * @param classNameArray
     *            an array of base class names of the serialised objects
     * @param bundle
     *            the local bundle
     * @return Object array
     * @throws ONMException
     */
    public static Object[] xmlToObjects(String[] xmlArray, String[] classNameArray, Bundle bundle) throws ContextException {
        Object[] objs = new Object[xmlArray.length];
        for (int i = 0; xmlArray.length > i; i++) {
            objs[i] = xmlToObject(xmlArray[i], classNameArray[i], bundle);
        }
        return objs;
    }

    /**
     * To de-serialise an POJO from XML
     * 
     * @param xml
     *            serialised XML
     * @param objectClass
     *            base class of serialised object
     */
    public static Object xmlToObject(String xml, Class<?> objectClass) {
        if (objectClass.getClassLoader() != null) {
            xstream.setClassLoader(objectClass.getClassLoader());
        }
        return xstream.fromXML(xml);
    }

    /**
     * To de-serialise a POJO from XML.
     * 
     * Workaround for trac #685: get classpaths for de-serialisation of
     * framework objects that are stored as blob context attributes.
     * 
     * @param xml serialised XML
     * @param bc  bundle context
     * 
     * @author      <a href="mailto:phdn@users.sourceforge.net">phdn</a>
     * @since 0.5.2
     */
    public static Object xmlToObject(String xml, BundleContext bc) throws ContextException {
        Throwable lastThrowable = null;
        Object obj = null;
        for (String fwClass : frameworkClasses.keySet()) {
            try {
                obj = LocalXMLConverter.xmlToObject(xml, fwClass, getBundleFromInterface(frameworkClasses.get(fwClass), bc));
                break;
            } catch (Throwable t) {
                lastThrowable = t;
            }
        }
        if (null == obj) {
            if (null == lastThrowable) {
                throw new ContextBrokerException("could not de-serialise");
            } else {
                throw new ContextBrokerException("could not de-serialise", lastThrowable);
            }
        }
        return obj;
    }

    /**
     * De-serialise a POJO from XML.
     * 
     * @param xml               The XML implementation of the POJO.
     * @param classLoader       The classloader to use during de-serialisation.
     * @return                  The de-serialised object.
     */
    public static Object xmlToObject(String xml, ClassLoader classLoader) {
        if (null != classLoader) {
            xstream.setClassLoader(classLoader);
        }
        return xstream.fromXML(xml);
    }

    /**
     * To de-serialise an array of POJOs from XML
     * 
     * @param xmlArray
     *            an array of serialised POJOs
     * @param objectClassArray
     *            an array of base classes of the serialised objects
     */
    public static Object[] xmlToObjects(String[] xmlArray, Class<?>[] objectClassArray) {
        Object[] objs = new Object[xmlArray.length];
        for (int i = 0; xmlArray.length > i; i++) {
            objs[i] = xmlToObject(xmlArray[i], objectClassArray[i]);
        }
        return objs;
    }

    /**
     * Gets the bundle for the specified interface.
     *
     * @param iface     The name of the interface.
     * @param bc        The bundle context.
     * @return          The bundle that contains the interface or null.
     * 
     * @author      <a href="mailto:phdn@users.sourceforge.net">phdn</a>
     * @since 0.5.2
     */
    private static Bundle getBundleFromInterface(String iface, BundleContext bc) {
        Bundle bundle = null;
        if (null != bc) {
            final ServiceReference serviceReference = bc.getServiceReference(iface);
            if (null != serviceReference) {
                bundle = serviceReference.getBundle();
            }
        }
        return bundle;
    }
}
