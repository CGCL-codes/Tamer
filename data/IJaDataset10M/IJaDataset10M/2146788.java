package org.exist.xslt;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import org.apache.log4j.Logger;
import org.exist.storage.BrokerPool;

/**
 * Allows the TransformerFactory that is used for XSLT to be
 * chosen through configuration settings in conf.xml
 *
 * Within eXist this class should be used instead of
 * directly calling SAXTransformerFactory.newInstance() directly
 *
 * @author Adam Retter <adam.retter@devon.gov.uk>
 * @author Andrzej Taramina <andrzej@chaeron.com>
 */
public class TransformerFactoryAllocator {

    private static final Logger LOG = Logger.getLogger(TransformerFactoryAllocator.class);

    public static final String CONFIGURATION_ELEMENT_NAME = "transformer";

    public static final String TRANSFORMER_CLASS_ATTRIBUTE = "class";

    public static final String PROPERTY_TRANSFORMER_CLASS = "transformer.class";

    public static final String CONFIGURATION_TRANSFORMER_ATTRIBUTE_ELEMENT_NAME = "attribute";

    public static final String PROPERTY_TRANSFORMER_ATTRIBUTES = "transformer.attributes";

    public static final String TRANSFORMER_CACHING_ATTRIBUTE = "caching";

    public static final String PROPERTY_CACHING_ATTRIBUTE = "transformer.caching";

    public static final String PROPERTY_BROKER_POOL = "transformer.brokerPool";

    private TransformerFactoryAllocator() {
    }

    /**
     * Get the TransformerFactory defined in conf.xml
     * If the class can't be found or the given class doesn't implement
     * the required interface, the default factory is returned.
     *
     * @param pool A database broker pool, used for reading the conf.xml configuration
     *
     * @return  A SAXTransformerFactory, for which newInstance() can then be called
     *
     *
     * Typical usage:
     *
     * Instead of SAXTransformerFactory.newInstance() use
     * TransformerFactoryAllocator.getTransformerFactory(broker).newInstance()
     */
    public static SAXTransformerFactory getTransformerFactory(BrokerPool pool) {
        SAXTransformerFactory factory;
        String transformerFactoryClassName = (String) pool.getConfiguration().getProperty(PROPERTY_TRANSFORMER_CLASS);
        if (transformerFactoryClassName == null) {
            factory = (SAXTransformerFactory) TransformerFactory.newInstance();
        } else {
            try {
                factory = (SAXTransformerFactory) Class.forName(transformerFactoryClassName).newInstance();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Set transformer factory: " + transformerFactoryClassName);
                }
                Hashtable<String, Object> attributes = (Hashtable<String, Object>) pool.getConfiguration().getProperty(PROPERTY_TRANSFORMER_ATTRIBUTES);
                Enumeration<String> attrNames = attributes.keys();
                while (attrNames.hasMoreElements()) {
                    String name = attrNames.nextElement();
                    Object value = attributes.get(name);
                    try {
                        factory.setAttribute(name, value);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Set transformer attribute: " + ", " + "name: " + name + ", value: " + value);
                        }
                    } catch (IllegalArgumentException iae) {
                        LOG.warn("Unable to set attribute for TransformerFactory: '" + transformerFactoryClassName + "', name: " + name + ", value: " + value + ", exception: " + iae.getMessage());
                    }
                }
                try {
                    factory.setAttribute(PROPERTY_BROKER_POOL, pool);
                } catch (Exception e) {
                }
            } catch (ClassNotFoundException cnfe) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Cannot find the requested TrAX factory '" + transformerFactoryClassName + "'. Using default TrAX Transformer Factory instead.");
                }
                factory = (SAXTransformerFactory) TransformerFactory.newInstance();
            } catch (ClassCastException cce) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("The indicated class '" + transformerFactoryClassName + "' is not a TrAX Transformer Factory. Using default TrAX Transformer Factory instead.");
                }
                factory = (SAXTransformerFactory) TransformerFactory.newInstance();
            } catch (Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Error found loading the requested TrAX Transformer Factory '" + transformerFactoryClassName + "'. Using default TrAX Transformer Factory instead: " + e);
                }
                factory = (SAXTransformerFactory) TransformerFactory.newInstance();
            }
        }
        return (factory);
    }
}
