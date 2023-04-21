package org.jbeanmapper.configurator;

import org.apache.commons.digester.Digester;
import org.jbeanmapper.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Pugh
 */
public class DefaultConfigurator implements Configurator {

    private boolean validating = false;

    public List getMappings(InputSource inputSource) throws BeanMappingException {
        try {
            Digester digester = getDigester();
            digester.addRule("bean-mappings", new BeanMappingsRule());
            digester.addRule("*/bean-mapping", new BeanMappingRule());
            digester.addRule("*/property-mapping", new PropertyMappingRule());
            digester.addRule("*/default-property-mappings", new DefaultBeanMappingsRule());
            return (List) digester.parse(inputSource);
        } catch (Exception e) {
            throw new BeanMappingException(e);
        }
    }

    /**
     * Get the digester for this configurator.
     *
     * @return The digester for this configurator.
     * @throws SAXException If the configuration for the xml parser for the digester fails.
     */
    protected Digester getDigester() throws SAXException {
        Digester digester = null;
        if (validating) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(true);
            SAXParser parser = null;
            try {
                factory.setFeature("http://apache.org/xml/features/validation/schema", true);
                factory.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
                parser = factory.newSAXParser();
                parser.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", getClass().getResource("bean-mappings.xsd").toString());
            } catch (ParserConfigurationException e) {
                throw new SAXException(e);
            }
            digester = new Digester(parser);
        } else {
            digester = new Digester();
        }
        digester.setErrorHandler(new ErrorHandler() {

            public void error(SAXParseException exception) throws SAXException {
                throw new SAXException(exception);
            }

            public void fatalError(SAXParseException exception) throws SAXException {
                throw new SAXException(exception);
            }

            public void warning(SAXParseException exception) throws SAXException {
                throw new SAXException(exception);
            }
        });
        return digester;
    }

    public BeanMapping getMapping(Class clazz, Class targetClass) throws BeanMappingException {
        try {
            BeanMapping beanMapping = new BeanMapping();
            beanMapping.setTargetClass(targetClass);
            beanMapping.setErrorOnUnMappedProperty(true);
            beanMapping.setPropertyMappings(getPropertyMappings(clazz, targetClass));
            beanMapping.setSourceClass(clazz);
            return beanMapping;
        } catch (IntrospectionException e) {
            throw new BeanMappingException(e);
        }
    }

    /**
     * Get the list of property mappings from <code>srcClass</code> to <code>targetClass</code>.
     *
     * @param srcClass    The source class.
     * @param targetClass The target class.
     * @return The list of property mappings from <code>srcClass</code> to <code>targetClass</code>.
     * @throws IntrospectionException If introspection fails.
     */
    protected List getPropertyMappings(Class srcClass, Class targetClass) throws IntrospectionException {
        List mappings = new ArrayList();
        BeanInfo srcBeanInfo = Introspector.getBeanInfo(srcClass);
        BeanInfo targetBeanInfo = Introspector.getBeanInfo(targetClass);
        PropertyDescriptor[] propertyDescriptors = srcBeanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor srcPropertyDescriptor = propertyDescriptors[i];
            String name = srcPropertyDescriptor.getName();
            if (!name.equals("class")) {
                PropertyDescriptor targetPropertyDescriptor = BeanUtil.findPropertyDescriptor(targetBeanInfo, name);
                if (targetPropertyDescriptor == null) {
                    throw new IntrospectionException("unable to map property [" + name + "] to class " + targetClass);
                }
                mappings.add(createPropertyMapping(srcPropertyDescriptor, targetPropertyDescriptor));
            }
        }
        return mappings;
    }

    /**
     * Create a property mapping from <code>srcPropertyDescriptor</code> to <code>targetPropertyDescriptor</code>.
     *
     * @param srcPropertyDescriptor    The source property descriptor.
     * @param targetPropertyDescriptor The target property descriptor.
     * @return A property mapping from <code>srcPropertyDescriptor</code> to <code>targetPropertyDescriptor</code>.
     * @throws IntrospectionException If introspection fails.
     */
    protected PropertyMapping createPropertyMapping(PropertyDescriptor srcPropertyDescriptor, PropertyDescriptor targetPropertyDescriptor) throws IntrospectionException {
        PropertyMapping mapping = new PropertyMapping();
        mapping.setSrcProperty(srcPropertyDescriptor.getName());
        mapping.setTargetProperty(targetPropertyDescriptor.getName());
        mapping.setPropertyMapper(new DefaultPropertyMapper());
        mapping.setConverter(new DefaultBeanConverter());
        return mapping;
    }

    /**
     * @return <code>true</code> if this configurator validates its xml.  <code>false</code> otherwise.
     */
    public boolean isValidating() {
        return validating;
    }

    /**
     * @param validating <code>true</code> to indicate that this configurator validates its xml.
     *                   <code>false</code> otherwise.
     */
    public void setValidating(boolean validating) {
        this.validating = validating;
    }
}
