package org.settings4j.config;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.settings4j.Connector;
import org.settings4j.ContentResolver;
import org.settings4j.Filter;
import org.settings4j.ObjectResolver;
import org.settings4j.Settings4jInstance;
import org.settings4j.Settings4jRepository;
import org.settings4j.connector.CachedConnectorWrapper;
import org.settings4j.connector.FilteredConnectorWrapper;
import org.settings4j.connector.SystemPropertyConnector;
import org.settings4j.contentresolver.ClasspathContentResolver;
import org.settings4j.contentresolver.FilteredContentResolverWrapper;
import org.settings4j.objectresolver.AbstractObjectResolver;
import org.settings4j.objectresolver.FilteredObjectResolverWrapper;
import org.settings4j.settings.DefaultFilter;
import org.settings4j.util.ELConnectorWrapper;
import org.settings4j.util.ExpressionLanguageUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Harald.Brabenetz
 */
public class DOMConfigurator {

    /** General Logger for this Class. */
    private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(DOMConfigurator.class);

    private static final Class[] NO_PARAM = new Class[] {};

    private static final String CONFIGURATION_TAG = "settings4j:configuration";

    private static final String CONNECTOR_TAG = "connector";

    private static final String CONNECTOR_REF_TAG = "connector-ref";

    private static final String OBJECT_RESOLVER_TAG = "objectResolver";

    private static final String OBJECT_RESOLVER_REF_TAG = "objectResolver-ref";

    private static final String CONTENT_RESOLVER_TAG = "contentResolver";

    private static final String CONTENT_RESOLVER_REF_TAG = "contentResolver-ref";

    private static final String MAPPING_TAG = "mapping";

    private static final String FILTER_TAG = "filter";

    private static final String EXCLUDE_TAG = "exclude";

    private static final String INCLUDE_TAG = "include";

    private static final String ENTRY_TAG = "entry";

    private static final String ENTRY_KEY_ATTR = "key";

    private static final String ENTRY_REFKEY_ATTR = "ref-key";

    private static final String PARAM_TAG = "param";

    private static final String NAME_ATTR = "name";

    private static final String CLASS_ATTR = "class";

    private static final String PATTERN_ATTR = "pattern";

    private static final String CACHED_ATTR = "cached";

    private static final String VALUE_ATTR = "value";

    private static final String REF_ATTR = "ref";

    private static final String DOCUMENT_BUILDER_FACTORY_KEY = "javax.xml.parsers.DocumentBuilderFactory";

    private static final float JAVA_VERSION_1_5 = 1.5f;

    private final Map connectorBag;

    private final Map contentResolverBag;

    private final Map objectResolverBag;

    private final Settings4jRepository repository;

    private final Map expressionAttributes = new HashMap();

    /**
     * Configure the given Settings4jRepository with an XMl-configuration (see settings4j.dtd).
     * @param repository The Repository to configure.
     */
    public DOMConfigurator(final Settings4jRepository repository) {
        super();
        this.repository = repository;
        this.connectorBag = new HashMap();
        this.contentResolverBag = new HashMap();
        this.objectResolverBag = new HashMap();
    }

    /**
     * Sets a parameter based from configuration file content.
     * 
     * @param elem param element, may not be null.
     * @param propSetter property setter, may not be null.
     * @param props properties
     */
    private void setParameter(final Element elem, final Object bean, final Connector[] connectors) {
        final String name = elem.getAttribute(NAME_ATTR);
        final String valueStr = (elem.getAttribute(VALUE_ATTR));
        try {
            final PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(bean, name);
            final Method setter = PropertyUtils.getWriteMethod(propertyDescriptor);
            Object value;
            if (connectors != null) {
                value = subst(valueStr, connectors, setter.getParameterTypes()[0]);
            } else {
                value = subst(valueStr, null, setter.getParameterTypes()[0]);
            }
            PropertyUtils.setProperty(bean, name, value);
        } catch (final IllegalAccessException e) {
            LOG.warn("Cannnot set Property: " + name, e);
        } catch (final InvocationTargetException e) {
            LOG.warn("Cannnot set Property: " + name, e);
        } catch (final NoSuchMethodException e) {
            LOG.warn("Cannnot set Property: " + name, e);
        }
    }

    /**
     * A static version of {@link #doConfigure(URL)}.
     * 
     * @param url The location of the configuration file.
     * @param repository the Repository to configure.
     * @throws FactoryConfigurationError if {@link DocumentBuilderFactory#newInstance()} throws an exception.
     */
    public static void configure(final URL url, final Settings4jRepository repository) throws FactoryConfigurationError {
        new DOMConfigurator(repository).doConfigure(url);
    }

    /**
     * @param url The location of the configuration file.
     */
    public void doConfigure(final URL url) {
        final ParseAction action = new ParseAction() {

            public Document parse(final DocumentBuilder parser) throws SAXException, IOException {
                return parser.parse(url.toString());
            }

            public String toString() {
                return "url [" + url.toString() + "]";
            }
        };
        doConfigure(action);
    }

    private void doConfigure(final ParseAction action) throws FactoryConfigurationError {
        DocumentBuilderFactory dbf = null;
        try {
            LOG.debug("System property is :" + new SystemPropertyConnector().getString(DOCUMENT_BUILDER_FACTORY_KEY));
            dbf = DocumentBuilderFactory.newInstance();
            LOG.debug("Standard DocumentBuilderFactory search succeded.");
            LOG.debug("DocumentBuilderFactory is: " + dbf.getClass().getName());
        } catch (final FactoryConfigurationError fce) {
            final Exception e = fce.getException();
            LOG.debug("Could not instantiate a DocumentBuilderFactory.", e);
            throw fce;
        }
        try {
            dbf.setValidating(true);
            final DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            docBuilder.setErrorHandler(new SAXErrorHandler());
            docBuilder.setEntityResolver(new Settings4jEntityResolver());
            final Document doc = action.parse(docBuilder);
            parse(doc.getDocumentElement());
        } catch (final Exception e) {
            LOG.error("Could not parse " + action.toString() + ".", e);
        }
    }

    /**
     * Used internally to configure the settings4j framework by parsing a DOM tree of XML elements based on <a
     * href="doc-files/settings4j.dtd">settings4j.dtd</a>.
     * 
     * @param element The XML {@link #CONFIGURATION_TAG} Element.
     */
    protected void parse(final Element element) {
        final String rootElementName = element.getTagName();
        if (!rootElementName.equals(CONFIGURATION_TAG)) {
            LOG.error("DOM element is - not a <" + CONFIGURATION_TAG + "> element.");
            return;
        }
        final Settings4jInstance root = this.repository.getSettings();
        synchronized (root) {
            parseChildrenOfSettingsElement(element, root);
        }
    }

    /**
     * Used internally to parse an Filter element.
     * 
     * @param filterElement the XML-Element for Filter.
     * @return the Filter-Object
     */
    protected Filter parseFilter(final Element filterElement) {
        Filter filter;
        String className = filterElement.getAttribute(CLASS_ATTR);
        if (StringUtils.isEmpty(className)) {
            className = DefaultFilter.class.getName();
        }
        LOG.debug("Desired Connector class: [" + className + "]");
        try {
            final Class clazz = loadClass(className);
            final Constructor constructor = clazz.getConstructor(NO_PARAM);
            filter = (Filter) constructor.newInstance(null);
        } catch (final Exception oops) {
            LOG.error("Could not retrieve connector [filter: " + className + "]. Reported error follows.", oops);
            return null;
        }
        final NodeList children = filterElement.getChildNodes();
        final int length = children.getLength();
        for (int loop = 0; loop < length; loop++) {
            final Node currentNode = children.item(loop);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element currentElement = (Element) currentNode;
                final String tagName = currentElement.getTagName();
                if (tagName.equals(INCLUDE_TAG)) {
                    final Element includeTag = (Element) currentNode;
                    final String patteren = includeTag.getAttribute(PATTERN_ATTR);
                    filter.addInclude(patteren);
                } else if (tagName.equals(EXCLUDE_TAG)) {
                    final Element excludeTag = (Element) currentNode;
                    final String patteren = excludeTag.getAttribute(PATTERN_ATTR);
                    filter.addExclude(patteren);
                } else {
                    quietParseUnrecognizedElement(filter, currentElement);
                }
            }
        }
        return filter;
    }

    /**
     * Used internally to parse an connector element.
     * 
     * @param connectorElement The XML Connector Element
     * @return the Connector Object.
     */
    protected Connector parseConnector(final Element connectorElement) {
        final String connectorName = connectorElement.getAttribute(NAME_ATTR);
        Connector connector;
        final String className = connectorElement.getAttribute(CLASS_ATTR);
        LOG.debug("Desired Connector class: [" + className + ']');
        try {
            final Class clazz = loadClass(className);
            final Constructor constructor = clazz.getConstructor(NO_PARAM);
            connector = (Connector) constructor.newInstance(null);
        } catch (final Exception oops) {
            LOG.error("Could not retrieve connector [" + connectorName + "]. Reported error follows.", oops);
            return null;
        }
        connector.setName(connectorName);
        final Connector[] subConnectors = getConnectors(connectorElement);
        for (int i = 0; i < subConnectors.length; i++) {
            connector.addConnector(subConnectors[i]);
        }
        Filter filter = null;
        synchronized (connector) {
            final NodeList children = connectorElement.getChildNodes();
            final int length = children.getLength();
            for (int loop = 0; loop < length; loop++) {
                final Node currentNode = children.item(loop);
                if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                    final Element currentElement = (Element) currentNode;
                    final String tagName = currentElement.getTagName();
                    if (tagName.equals(CONTENT_RESOLVER_REF_TAG)) {
                        final Element contentResolverRef = (Element) currentNode;
                        final ContentResolver contentResolver = findContentResolverByReference(contentResolverRef);
                        connector.setContentResolver(contentResolver);
                    } else if (tagName.equals(OBJECT_RESOLVER_REF_TAG)) {
                        final Element objectResolverRef = (Element) currentNode;
                        final ObjectResolver objectResolver = findObjectResolverByReference(objectResolverRef);
                        connector.setObjectResolver(objectResolver);
                    } else if (tagName.equals(FILTER_TAG)) {
                        final Element filterElement = (Element) currentNode;
                        filter = parseFilter(filterElement);
                    } else if (tagName.equals(PARAM_TAG)) {
                        setParameter(currentElement, connector, subConnectors);
                    } else {
                        quietParseUnrecognizedElement(connector, currentElement);
                    }
                }
            }
            final Boolean cached = (Boolean) subst(connectorElement.getAttribute(CACHED_ATTR), subConnectors, Boolean.class);
            if (cached != null && cached.booleanValue()) {
                connector = new CachedConnectorWrapper(connector);
            }
            if (filter != null) {
                connector = new FilteredConnectorWrapper(connector, filter);
            }
            connector.init();
        }
        return connector;
    }

    /**
     * Only logs out unrecognized Elements.
     * 
     * @param instance instance, may be null.
     * @param element element, may not be null.
     */
    private static void quietParseUnrecognizedElement(final Object instance, final Element element) {
        String elementName = "UNKNOWN";
        String instanceClassName = "UNKNOWN";
        try {
            elementName = element.getNodeName();
            instanceClassName = instance.getClass().getName();
        } catch (final Exception e) {
            LOG.warn("Error in quietParseUnrecognizedElement(): " + e.getMessage());
            if (LOG.isDebugEnabled()) {
                LOG.debug(e.getMessage(), e);
            }
        }
        LOG.warn("Unrecognized Element will be ignored: " + elementName + " for Instance: " + instanceClassName);
    }

    /**
     * Return all referenced connectors from Child-Nodes {@link #CONNECTOR_REF_TAG}.
     * 
     * @param connectorsElement The XML Connectors Element
     * @return the Connectors Objects as Array.
     */
    protected Connector[] getConnectors(final Element connectorsElement) {
        final List connectors = new ArrayList();
        final NodeList children = connectorsElement.getChildNodes();
        final int length = children.getLength();
        for (int loop = 0; loop < length; loop++) {
            final Node currentNode = children.item(loop);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element currentElement = (Element) currentNode;
                final String tagName = currentElement.getTagName();
                if (tagName.equals(CONNECTOR_REF_TAG)) {
                    final Element connectorRef = (Element) currentNode;
                    final Connector connector = findConnectorByReference(connectorRef);
                    connectors.add(connector);
                }
            }
        }
        return (Connector[]) connectors.toArray(new Connector[connectors.size()]);
    }

    /**
     * Used internally to parse the children of a settings element.
     * 
     * @param settingsElement The XML Settings Element
     * @param settings _The Settings Object do apply the values.
     */
    protected void parseChildrenOfSettingsElement(final Element settingsElement, final Settings4jInstance settings) {
        Node currentNode = null;
        Element currentElement = null;
        String tagName = null;
        settings.removeAllConnectors();
        final NodeList connectorElements = settingsElement.getElementsByTagName(CONNECTOR_TAG);
        int length = connectorElements.getLength();
        for (int i = 0; i < length; i++) {
            currentNode = connectorElements.item(i);
            currentElement = (Element) currentNode;
            final Connector connector = parseConnector(currentElement);
            if (connector != null) {
                this.connectorBag.put(connector.getName(), connector);
                settings.addConnector(connector);
            }
        }
        final List list = settings.getConnectors();
        final Connector[] connectors = (Connector[]) list.toArray(new Connector[list.size()]);
        final NodeList children = settingsElement.getChildNodes();
        length = children.getLength();
        for (int i = 0; i < length; i++) {
            currentNode = children.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                currentElement = (Element) currentNode;
                tagName = currentElement.getTagName();
                if (tagName.equals(MAPPING_TAG)) {
                    final Map mapping = parseMapping(currentElement);
                    if (mapping != null) {
                        settings.setMapping(mapping);
                    }
                } else if (tagName.equals(PARAM_TAG)) {
                    setParameter(currentElement, settings, connectors);
                } else if (tagName.equals(CONNECTOR_TAG)) {
                    LOG.trace("CONNECTOR_TAG already parsed");
                } else if (tagName.equals(CONTENT_RESOLVER_TAG)) {
                    LOG.trace("CONTENT_RESOLVER_TAG will be parsed on the maned");
                } else if (tagName.equals(OBJECT_RESOLVER_TAG)) {
                    LOG.trace("OBJECT_RESOLVER_TAG will be parsed on the maned");
                } else {
                    quietParseUnrecognizedElement(settings, currentElement);
                }
            }
        }
    }

    /**
     * Used internally to parse connectors by IDREF name.
     * 
     * @param doc The whole XML configuration.
     * @param connectorName The Connector-Name, to search for.
     * @return the Connector instance.
     */
    protected Connector findConnectorByName(final Document doc, final String connectorName) {
        Connector connector = (Connector) this.connectorBag.get(connectorName);
        if (connector != null) {
            return connector;
        }
        final Element element = getElementByNameAttr(doc, connectorName, "connector");
        if (element == null) {
            LOG.error("No connector named [" + connectorName + "] could be found.");
            return null;
        }
        connector = parseConnector(element);
        this.connectorBag.put(connectorName, connector);
        return connector;
    }

    /**
     * Used internally to parse connectors by IDREF element.
     * 
     * @param connectorRef The Element with the {@link #REF_ATTR} - Attribute.
     * @return the Connector instance.
     */
    protected Connector findConnectorByReference(final Element connectorRef) {
        final String connectorName = connectorRef.getAttribute(REF_ATTR);
        final Document doc = connectorRef.getOwnerDocument();
        return findConnectorByName(doc, connectorName);
    }

    /**
     * Used internally to parse an objectResolver element.
     * 
     * @param objectResolverElement The XML Object resolver Element.
     * @return The ObjectResolver instance.
     */
    protected ObjectResolver parseObjectResolver(final Element objectResolverElement) {
        final String objectResolverName = objectResolverElement.getAttribute(NAME_ATTR);
        ObjectResolver objectResolver;
        final String className = objectResolverElement.getAttribute(CLASS_ATTR);
        LOG.debug("Desired ObjectResolver class: [" + className + ']');
        try {
            final Class clazz = loadClass(className);
            final Constructor constructor = clazz.getConstructor(NO_PARAM);
            objectResolver = (ObjectResolver) constructor.newInstance(null);
        } catch (final Exception oops) {
            LOG.error("Could not retrieve objectResolver [" + objectResolverName + "]. Reported error follows.", oops);
            return null;
        } catch (final NoClassDefFoundError e) {
            LOG.warn("The ObjectResolver '" + objectResolverName + "' cannot be created. There are not all required Libraries inside the Classpath: " + e.getMessage(), e);
            return null;
        }
        final Connector[] connectors = getConnectors(objectResolverElement);
        Filter filter = null;
        synchronized (objectResolver) {
            final NodeList children = objectResolverElement.getChildNodes();
            final int length = children.getLength();
            for (int loop = 0; loop < length; loop++) {
                final Node currentNode = children.item(loop);
                if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                    final Element currentElement = (Element) currentNode;
                    final String tagName = currentElement.getTagName();
                    if (tagName.equals(OBJECT_RESOLVER_REF_TAG)) {
                        final Element objectResolverRef = (Element) currentNode;
                        final ObjectResolver subObjectResolver = findObjectResolverByReference(objectResolverRef);
                        objectResolver.addObjectResolver(subObjectResolver);
                    } else if (tagName.equals(PARAM_TAG)) {
                        setParameter(currentElement, objectResolver, connectors);
                    } else if (tagName.equals(FILTER_TAG)) {
                        final Element filterElement = (Element) currentNode;
                        filter = parseFilter(filterElement);
                    } else {
                        quietParseUnrecognizedElement(objectResolver, currentElement);
                    }
                }
            }
            String isCachedValue = objectResolverElement.getAttribute(CACHED_ATTR);
            final Boolean isCached = (Boolean) subst(isCachedValue, null, Boolean.class);
            if (BooleanUtils.isTrue(isCached)) {
                if (objectResolver instanceof AbstractObjectResolver) {
                    ((AbstractObjectResolver) objectResolver).setCached(isCached.booleanValue());
                } else {
                    LOG.warn("Only AbstractObjectResolver can use the attribute cached=\"true\" ");
                }
            }
            if (filter != null) {
                objectResolver = new FilteredObjectResolverWrapper(objectResolver, filter);
            }
        }
        return objectResolver;
    }

    /**
     * Used internally to parse an mapping element.
     * 
     * @param mappingElement The XML Mapping Element.
     * @return the Map instance (Key = String; Value = String).
     */
    protected Map parseMapping(final Element mappingElement) {
        final String mappingName = mappingElement.getAttribute(NAME_ATTR);
        Map mapping;
        String className = mappingElement.getAttribute(CLASS_ATTR);
        if (StringUtils.isEmpty(className)) {
            className = "java.util.HashMap";
        }
        LOG.debug("Desired Map class: [" + className + ']');
        try {
            final Class clazz = loadClass(className);
            final Constructor constructor = clazz.getConstructor(NO_PARAM);
            mapping = (Map) constructor.newInstance(null);
        } catch (final Exception oops) {
            LOG.error("Could not retrieve mapping [" + mappingName + "]. Reported error follows.", oops);
            return null;
        } catch (final NoClassDefFoundError e) {
            LOG.warn("The Mapping '" + mappingName + "' cannot be created. There are not all required Libraries inside the Classpath: " + e.getMessage(), e);
            return null;
        }
        synchronized (mapping) {
            final NodeList children = mappingElement.getChildNodes();
            final int length = children.getLength();
            for (int loop = 0; loop < length; loop++) {
                final Node currentNode = children.item(loop);
                if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                    final Element currentElement = (Element) currentNode;
                    final String tagName = currentElement.getTagName();
                    if (tagName.equals(ENTRY_TAG)) {
                        final String key = currentElement.getAttribute(ENTRY_KEY_ATTR);
                        final String keyRef = currentElement.getAttribute(ENTRY_REFKEY_ATTR);
                        mapping.put(key, keyRef);
                    } else {
                        quietParseUnrecognizedElement(mapping, currentElement);
                    }
                }
            }
        }
        return mapping;
    }

    /**
     * Used internally to parse an contentResolver element.
     * 
     * @param contentResolverElement The ContentResolver Element.
     * @return the ContentResolver instance.
     */
    protected ContentResolver parseContentResolver(final Element contentResolverElement) {
        final String contentResolverName = contentResolverElement.getAttribute(NAME_ATTR);
        ContentResolver contentResolver;
        final String className = contentResolverElement.getAttribute(CLASS_ATTR);
        LOG.debug("Desired ContentResolver class: [" + className + ']');
        try {
            final Class clazz = loadClass(className);
            final Constructor constructor = clazz.getConstructor(NO_PARAM);
            contentResolver = (ContentResolver) constructor.newInstance(null);
        } catch (final Exception e) {
            LOG.error("Could not retrieve contentResolver [" + contentResolverName + "]. " + "Reported error follows.", e);
            return null;
        } catch (final NoClassDefFoundError e) {
            LOG.warn("The ContentResolver '" + contentResolverName + "' cannot be created. " + "There are not all required Libraries inside the Classpath: " + e.getMessage(), e);
            return null;
        }
        final Connector[] connectors = getConnectors(contentResolverElement);
        Filter filter = null;
        synchronized (contentResolver) {
            final NodeList children = contentResolverElement.getChildNodes();
            final int length = children.getLength();
            for (int loop = 0; loop < length; loop++) {
                final Node currentNode = children.item(loop);
                if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                    final Element currentElement = (Element) currentNode;
                    final String tagName = currentElement.getTagName();
                    if (tagName.equals(CONTENT_RESOLVER_REF_TAG)) {
                        final Element contentResolverRef = (Element) currentNode;
                        final ContentResolver subContentResolver = findContentResolverByReference(contentResolverRef);
                        contentResolver.addContentResolver(subContentResolver);
                    } else if (tagName.equals(PARAM_TAG)) {
                        setParameter(currentElement, contentResolver, connectors);
                    } else if (tagName.equals(FILTER_TAG)) {
                        final Element filterElement = (Element) currentNode;
                        filter = parseFilter(filterElement);
                    } else {
                        quietParseUnrecognizedElement(contentResolver, currentElement);
                    }
                }
            }
            if (filter != null) {
                contentResolver = new FilteredContentResolverWrapper(contentResolver, filter);
            }
        }
        return contentResolver;
    }

    /**
     * Used internally to parse contentResolvers by IDREF name.
     * 
     * @param doc XML Document of the whole settings4j.xml.
     * @param contentResolverName the contentResolver Name to search for.
     * @return the ContentResolver instance.
     */
    protected ContentResolver findContentResolverByName(final Document doc, final String contentResolverName) {
        ContentResolver contentResolver = (ContentResolver) this.contentResolverBag.get(contentResolverName);
        if (contentResolver != null) {
            return contentResolver;
        }
        final Element element = getElementByNameAttr(doc, contentResolverName, CONTENT_RESOLVER_TAG);
        if (element == null) {
            LOG.error("No contentResolver named [" + contentResolverName + "] could be found.");
            return null;
        }
        contentResolver = parseContentResolver(element);
        this.contentResolverBag.put(contentResolverName, contentResolver);
        return contentResolver;
    }

    /**
     * Used internally to parse objectResolvers by IDREF name.
     * 
     * @param doc XML Document of the whole settings4j.xml.
     * @param objectResolverName the ObjectResolver Name to search for.
     * @return the ObjectResolver instance.
     */
    protected ObjectResolver findObjectResolverByName(final Document doc, final String objectResolverName) {
        ObjectResolver objectResolver = (ObjectResolver) this.objectResolverBag.get(objectResolverName);
        if (objectResolver != null) {
            return objectResolver;
        }
        final Element element = getElementByNameAttr(doc, objectResolverName, OBJECT_RESOLVER_TAG);
        if (element == null) {
            LOG.error("No objectResolver named [" + objectResolverName + "] could be found.");
            return null;
        }
        objectResolver = parseObjectResolver(element);
        this.objectResolverBag.put(objectResolverName, objectResolver);
        return objectResolver;
    }

    /**
     * Used internally to parse objectResolvers by IDREF element.
     * 
     * @param objectResolverRef The Element with the {@link #REF_ATTR}.
     * @return the ObjectResolver instance.
     */
    protected ObjectResolver findObjectResolverByReference(final Element objectResolverRef) {
        final String objectResolverName = objectResolverRef.getAttribute(REF_ATTR);
        final Document doc = objectResolverRef.getOwnerDocument();
        return findObjectResolverByName(doc, objectResolverName);
    }

    /**
     * Used internally to parse contentResolvers by IDREF element.
     * 
     * @param contentResolverRef The Element with the {@link #REF_ATTR}.
     * @return the ContentResolver instance.
     */
    protected ContentResolver findContentResolverByReference(final Element contentResolverRef) {
        final String contentResolverName = contentResolverRef.getAttribute(REF_ATTR);
        final Document doc = contentResolverRef.getOwnerDocument();
        return findContentResolverByName(doc, contentResolverName);
    }

    private static Element getElementByNameAttr(final Document doc, final String nameAttrValue, final String elementTagName) {
        Element element = null;
        final NodeList list = doc.getElementsByTagName(elementTagName);
        for (int t = 0; t < list.getLength(); t++) {
            final Node node = list.item(t);
            final NamedNodeMap map = node.getAttributes();
            final Node attrNode = map.getNamedItem("name");
            if (nameAttrValue.equals(attrNode.getNodeValue())) {
                element = (Element) node;
                break;
            }
        }
        return element;
    }

    /**
     * @author Harald.Brabenetz
     */
    private interface ParseAction {

        Document parse(final DocumentBuilder parser) throws SAXException, IOException;
    }

    /**
     * This function will replace expressions like ${connectors.string['']}.
     * 
     * @param value The value or Expression.
     * @param connectors required for validating Expression like ${connectors.string['']}
     * @return the String-Value of the given value or Expression
     */
    protected String subst(final String value, final Connector[] connectors) {
        return (String) subst(value, connectors, String.class);
    }

    /**
     * This function will replace expressions like ${connectors.object['']} or simply ${true}.
     * 
     * @param value The value or Expression.
     * @param clazz The expected {@link Class}.
     * @param connectors required for validating Expression like ${connectors.string['']}
     * @return the Object-Value of the given value or Expression.
     */
    protected Object subst(final String value, final Connector[] connectors, final Class clazz) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        if (value.indexOf("${") >= 0) {
            try {
                final Map context = new HashMap(this.expressionAttributes);
                if (connectors != null) {
                    context.put("connectors", new ELConnectorWrapper(connectors));
                    final Map connectorMap = new HashMap();
                    for (int i = 0; i < connectors.length; i++) {
                        final Connector connector = connectors[i];
                        connectorMap.put(connector.getName(), new ELConnectorWrapper(new Connector[] { connector }));
                    }
                    context.put("connector", connectorMap);
                }
                if (SystemUtils.isJavaVersionAtLeast(JAVA_VERSION_1_5)) {
                    context.put("env", System.getenv());
                }
                final Object result = ExpressionLanguageUtil.evaluateExpressionLanguage(value, context, clazz);
                return result;
            } catch (final Exception e) {
                LOG.error(e.getMessage(), e);
                return null;
            }
        }
        if (clazz.equals(String.class)) {
            return value.trim();
        } else if (clazz.equals(Boolean.class)) {
            return BooleanUtils.toBooleanObject(value.trim());
        } else {
            throw new UnsupportedOperationException("The following Type is not supported now: " + clazz + "; found value: " + value);
        }
    }

    /**
     * Add a ExpressionAttribute like ('ContextPath', 'myApp').
     * <p>
     * A settings4j.xml Value like value="c:/settings/${contextPath}" will be evaluated as 
     * "c:/settings/myApp".
     * 
     * @param key The Key as String.
     * @param value The value as Object.
     */
    public void addExpressionAttribute(final String key, final Object value) {
        this.expressionAttributes.put(key, value);
    }

    private static Class loadClass(final String clazz) throws ClassNotFoundException {
        return ClasspathContentResolver.getClassLoader().loadClass(clazz);
    }
}
