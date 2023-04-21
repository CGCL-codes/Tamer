package net.sf.jimo.modules.core;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import net.sf.jimo.api.BundleService;
import net.sf.jimo.api.FrameworkException;
import net.sf.jimo.modules.core.util.PersistenceXmlHelper;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationAdmin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConfigurationPersistenceXmlImpl extends PersistenceXmlHelper {

    public static final String ELEMENT_CONFIGURATIONS = "configurations";

    public static final String ELEMENT_CONFIGURATION = "configuration";

    public static final String ATTRIBUTE_PID = "pid";

    private static final String TYPE_NONE = "none";

    private static final String TYPE_STRING = "string";

    private static final String TYPE_STRINGARRAY = "stringarray";

    private static final String TYPE_INTEGER = "int";

    private static final String TYPE_INTEGERARRAY = "intarray";

    private static final String TYPE_BOOLEAN = "boolean";

    private static final String TYPE_FRAGMENT = "fragment";

    private static final String TYPE_LONG = "long";

    private static final String TYPE_LONGARRAY = "longarray";

    private static final String TYPE_FLOAT = "float";

    private static final String TYPE_DOUBLE = "double";

    private static final String TYPE_BYTEARRAY = "bytearray";

    private static final String ATTRIBUTE_FACTORYPID = "factoryPid";

    private static final String ATTRIBUTE_LOCATION = "location";

    private static final String ELEMENT_PROPERTIES = "properties";

    private static final String ELEMENT_PROPERTY = "property";

    private static final String ATTRIBUTE_TYPE = "type";

    private static final String ELEMENT_KEY = "key";

    private static final String ELEMENT_VALUE = "value";

    public ConfigurationPersistenceXmlImpl() throws FrameworkException {
        super(Core.INSTANCE.getBundleContext().getDataFile(Core.getConfig().getProperty(CoreConstants.KEY_CONFIGURATIONSTORE)), ELEMENT_CONFIGURATIONS, Core.INSTANCE.getBundleContext());
        log.info("Created ConfigurationPersistenceXmlImpl");
        BundleContext bundleContext = Core.INSTANCE.getBundleContext();
        ServiceReference serviceReference = bundleContext.getServiceReference(BundleService.class.getName());
        BundleService service = (BundleService) bundleContext.getService(serviceReference);
        service.addIdleEventListener(new IdleListener());
    }

    public Dictionary findItem(String pid) throws IOException {
        log.debug("findItem " + pid);
        Document configurationStore = getStorage();
        synchronized (configurationStore) {
            Element configElement = super.findElement(pid, ELEMENT_CONFIGURATION, ATTRIBUTE_PID, configurationStore);
            if (configElement == null) {
                log.debug("findItem " + pid + " : Not found.");
                return null;
            }
            log.debug("findItem " + pid + " : Found.");
            return createDictionary(configElement);
        }
    }

    public Dictionary[] findItems(String factoryPid) throws IOException {
        log.debug("findItems " + factoryPid);
        Document configurationStore = getStorage();
        synchronized (configurationStore) {
            NodeList configurationsList = configurationStore.getElementsByTagName(ELEMENT_CONFIGURATIONS);
            Element configurationsElement;
            Set res = new HashSet();
            if (configurationsList.getLength() > 0) {
                configurationsElement = (Element) configurationsList.item(0);
                NodeList configList = configurationsElement.getElementsByTagName(ELEMENT_CONFIGURATION);
                for (int i = 0; i < configList.getLength(); i++) {
                    Element configElement = (Element) configList.item(i);
                    String nextPid = configElement.getAttribute(ATTRIBUTE_FACTORYPID);
                    if (factoryPid.equals(nextPid)) {
                        log.debug("findItems " + factoryPid + " found " + configElement.getAttribute(ATTRIBUTE_PID));
                        res.add(createDictionary(configElement));
                    }
                }
            }
            return (Dictionary[]) res.toArray(new Dictionary[res.size()]);
        }
    }

    public synchronized void storeItem(Dictionary dictionary) throws IOException {
        Document configurationStore = getStorage();
        synchronized (configurationStore) {
            log.debug("storeItem " + dictionary.get(Constants.SERVICE_PID));
            Element configuration = findConfigurationElement((String) dictionary.get(Constants.SERVICE_PID), configurationStore);
            if (configuration != null) {
                configuration.getParentNode().removeChild(configuration);
            }
            NodeList configurationsList = configurationStore.getElementsByTagName(ELEMENT_CONFIGURATIONS);
            if (configurationsList.getLength() > 0) {
                Element configurationsElement = (Element) configurationsList.item(0);
                Element configElement = createConfigurationElement(dictionary, configurationStore);
                configurationsElement.appendChild(configElement);
            } else {
                log.warn("storeItem: No configurations element found.");
            }
            writeStorage();
        }
    }

    public synchronized void removeItem(String pid) throws IOException {
        Document configurationStore = getStorage();
        synchronized (configurationStore) {
            log.debug("removeItem " + pid);
            Element configuration = findConfigurationElement(pid, configurationStore);
            if (configuration == null) {
                log.debug("removeItem " + pid + " : Not found.");
                return;
            }
            configuration.getParentNode().removeChild(configuration);
            writeStorage();
        }
    }

    private Dictionary createDictionary(Element configElement) {
        Dictionary dict = new Hashtable();
        NodeList propertiesList = configElement.getElementsByTagName(ELEMENT_PROPERTIES);
        if (propertiesList.getLength() != 0) {
            Element propertiesElement = (Element) propertiesList.item(0);
            NodeList propList = propertiesElement.getElementsByTagName(ELEMENT_PROPERTY);
            for (int i = 0; i < propList.getLength(); i++) {
                Element propElement = (Element) propList.item(i);
                setPropertyValue(propElement, dict);
            }
        }
        return dict;
    }

    private void setPropertyValue(Element propElement, Dictionary dict) {
        NodeList keyList = propElement.getElementsByTagName(ELEMENT_KEY);
        String key = null;
        if (keyList.getLength() != 0) {
            Element keyElement = (Element) keyList.item(0);
            Node keyText = keyElement.getFirstChild();
            key = keyText.getNodeValue();
        }
        NodeList valueList = propElement.getElementsByTagName(ELEMENT_VALUE);
        String value = null;
        String type = TYPE_NONE;
        if (valueList.getLength() != 0) {
            Element valueElement = (Element) valueList.item(0);
            type = valueElement.getAttribute(ATTRIBUTE_TYPE);
            Node valueText = valueElement.getFirstChild();
            if (valueText != null) value = valueText.getNodeValue();
        }
        if (value == null) return;
        if (TYPE_STRING.equals(type) || TYPE_FRAGMENT.equals(type)) {
            dict.put(key, value);
        } else if (TYPE_STRINGARRAY.equals(type)) {
            String[] values = value.split(",");
            dict.put(key, values);
        } else if (TYPE_INTEGER.equals(type)) {
            dict.put(key, Integer.valueOf(value));
        } else if (TYPE_INTEGERARRAY.equals(type)) {
            String[] values = value.split(",");
            Integer[] intValues = new Integer[values.length];
            for (int i = 0; i < values.length; i++) {
                intValues[i] = Integer.valueOf(values[i]);
            }
            dict.put(key, intValues);
        } else if (TYPE_BOOLEAN.equals(type)) {
            dict.put(key, Boolean.valueOf(value));
        } else if (TYPE_LONG.equals(type)) {
            dict.put(key, Long.valueOf(value));
        } else if (TYPE_LONGARRAY.equals(type)) {
            String[] values = value.split(",");
            Long[] longValues = new Long[values.length];
            for (int i = 0; i < values.length; i++) {
                longValues[i] = Long.valueOf(values[i]);
            }
            dict.put(key, longValues);
        } else if (TYPE_FLOAT.equals(type)) {
            dict.put(key, Float.valueOf(value));
        } else if (TYPE_DOUBLE.equals(type)) {
            dict.put(key, Double.valueOf(value));
        } else if (TYPE_BYTEARRAY.equals(type)) {
            String s = value;
            Byte[] byteValues = new Byte[s.length() / 2];
            int n = 0;
            while (s.length() > 0) {
                String ss = s.substring(0, 2);
                Byte b = Byte.valueOf(ss);
                byteValues[n++] = b;
            }
            dict.put(key, byteValues);
        } else {
            dict.put(key, value);
        }
    }

    private Element createConfigurationElement(Dictionary dictionary, Document document) {
        Element configElement = document.createElement(ELEMENT_CONFIGURATION);
        Enumeration enumeration = dictionary.keys();
        String pid = (String) dictionary.get(Constants.SERVICE_PID);
        configElement.setAttribute(ATTRIBUTE_PID, pid);
        String factoryPid = (String) dictionary.get(ConfigurationAdmin.SERVICE_FACTORYPID);
        if (factoryPid != null) configElement.setAttribute(ATTRIBUTE_FACTORYPID, factoryPid);
        String location = (String) dictionary.get(ConfigurationAdmin.SERVICE_BUNDLELOCATION);
        if (location != null) configElement.setAttribute(ATTRIBUTE_LOCATION, location);
        Element propertiesElement = document.createElement(ELEMENT_PROPERTIES);
        configElement.appendChild(propertiesElement);
        while (enumeration.hasMoreElements()) {
            Element propertyElement = document.createElement(ELEMENT_PROPERTY);
            propertiesElement.appendChild(propertyElement);
            Element keyElement = document.createElement(ELEMENT_KEY);
            propertyElement.appendChild(keyElement);
            Element valueElement = document.createElement(ELEMENT_VALUE);
            propertyElement.appendChild(valueElement);
            String key = (String) enumeration.nextElement();
            Node keyText = document.createTextNode(key);
            keyElement.appendChild(keyText);
            Object obj = dictionary.get(key);
            String type = TYPE_NONE;
            Node valueNode = null;
            if (obj instanceof String) {
                type = TYPE_STRING;
                valueNode = document.createTextNode("" + obj);
            } else if (obj instanceof String[]) {
                type = TYPE_STRINGARRAY;
                String[] ar = (String[]) obj;
                String value = "";
                for (int i = 0; i < ar.length; i++) {
                    value += ar[i];
                    if (i < ar.length - 1) value += ",";
                }
                valueNode = document.createTextNode(value);
            } else if (obj instanceof Integer) {
                type = TYPE_INTEGER;
                valueNode = document.createTextNode("" + obj);
            } else if (obj instanceof Integer[]) {
                type = TYPE_INTEGERARRAY;
                Integer[] ar = (Integer[]) obj;
                String value = "";
                for (int i = 0; i < ar.length; i++) {
                    value += ar[i];
                    if (i < ar.length - 1) value += ",";
                }
                valueNode = document.createTextNode(value);
            } else valueNode = document.createTextNode("" + obj);
            valueElement.setAttribute(ATTRIBUTE_TYPE, type);
            valueElement.appendChild(valueNode);
        }
        return configElement;
    }

    private Element findConfigurationElement(String pid, Document document) {
        if (document == null || pid == null) return null;
        NodeList configurationsList = document.getElementsByTagName(ELEMENT_CONFIGURATIONS);
        if (configurationsList.getLength() > 0) {
            Element e = (Element) configurationsList.item(0);
            NodeList configurationElements = e.getElementsByTagName(ELEMENT_CONFIGURATION);
            if (configurationElements.getLength() > 0) {
                for (int i = 0; i < configurationElements.getLength(); i++) {
                    Element element = (Element) configurationElements.item(i);
                    String elementPid = element.getAttribute(ATTRIBUTE_PID);
                    if (pid.equals(elementPid)) return element;
                }
            }
        }
        return null;
    }
}
