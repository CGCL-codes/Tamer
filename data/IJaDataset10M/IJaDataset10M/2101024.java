package org.jbeanmapper.configurator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.apache.commons.digester.Digester;
import org.jbeanmapper.BeanMapping;
import org.jbeanmapper.BeanMappingException;
import org.jbeanmapper.DefaultPropertyMapper;
import org.jbeanmapper.BeanConverter;
import org.jbeanmapper.MappingContext;
import org.jbeanmapper.PropertyMapper;
import org.jbeanmapper.PropertyMapping;
import org.jbeanmapper.PropertyMappingException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author Brian Pugh
 */
public class TestPropertyMappingRule extends TestCase {

    public void testBegin() throws Exception {
        Digester digester = new Digester();
        PropertyMappingRule propertyMappingRule = new PropertyMappingRule();
        propertyMappingRule.setDigester(digester);
        AttributesImpl attributes = new AttributesImpl();
        String srcProperty = "myproperty";
        attributes.addAttribute("", "src", "src", "", srcProperty);
        String targetProperty = "yourproperty";
        attributes.addAttribute("", "target", "target", "", targetProperty);
        attributes.addAttribute("", "mapping-class", "mapping-class", "", "org.jbeanmapper.DefaultPropertyMapper");
        attributes.addAttribute("", "converter", "converter", "", "org.jbeanmapper.DefaultBeanConverter");
        propertyMappingRule.begin("", "", attributes);
        Object obj = digester.pop();
        assertTrue(obj instanceof PropertyMapping);
        PropertyMapping propertyMapping = (PropertyMapping) obj;
        assertTrue(propertyMapping.getPropertyMapper() instanceof PropertyMapper);
        assertTrue(propertyMapping.getPropertyMapper() instanceof DefaultPropertyMapper);
        assertTrue(propertyMapping.getConverter() instanceof BeanConverter);
        assertEquals(srcProperty, propertyMapping.getSrcProperty());
        assertEquals(targetProperty, propertyMapping.getTargetProperty());
    }

    public void testEnd() throws Exception {
        Digester digester = new Digester();
        PropertyMappingRule propertyMappingRule = new PropertyMappingRule();
        propertyMappingRule.setDigester(digester);
        BeanMapping beanMapping = new BeanMapping();
        PropertyMapping propertyMapping = new PropertyMapping();
        digester.push(beanMapping);
        digester.push(propertyMapping);
        propertyMappingRule.end("", "");
        Object result = digester.pop();
        assertTrue(result instanceof BeanMapping);
        BeanMapping resultBeanMapping = (BeanMapping) result;
        assertEquals(propertyMapping, resultBeanMapping.getPropertyMappings().get(0));
    }

    public void testBeanUtilsConverter() throws Exception {
        Digester digester = new Digester();
        PropertyMappingRule propertyMappingRule = new PropertyMappingRule();
        propertyMappingRule.setDigester(digester);
        AttributesImpl attributes = new AttributesImpl();
        String srcProperty = "myproperty";
        attributes.addAttribute("", "src", "src", "", srcProperty);
        String targetProperty = "yourproperty";
        attributes.addAttribute("", "target", "target", "", targetProperty);
        attributes.addAttribute("", "mapping-class", "mapping-class", "", "org.jbeanmapper.DefaultPropertyMapper");
        attributes.addAttribute("", "converter", "converter", "", "org.apache.commons.beanutils.converters.IntegerConverter");
        propertyMappingRule.begin("", "", attributes);
        Object obj = digester.pop();
        assertTrue(obj instanceof PropertyMapping);
        PropertyMapping propertyMapping = (PropertyMapping) obj;
        assertTrue(propertyMapping.getConverter() instanceof BeanConverter);
    }

    public void testNullSrc() throws Exception {
        try {
            Digester digester = new Digester();
            PropertyMappingRule propertyMappingRule = new PropertyMappingRule();
            propertyMappingRule.setDigester(digester);
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "target", "target", "", "someprop");
            attributes.addAttribute("", "mapping-class", "mapping-class", "", "org.jbeanmapper.DefaultPropertyMapper");
            propertyMappingRule.begin("", "", attributes);
            fail("expected BeanMappingException");
        } catch (BeanMappingException e) {
        }
    }

    public void testNullTarget() throws Exception {
        try {
            Digester digester = new Digester();
            PropertyMappingRule propertyMappingRule = new PropertyMappingRule();
            propertyMappingRule.setDigester(digester);
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "src", "src", "", "someprop");
            attributes.addAttribute("", "creator", "creator", "", "org.jbeanmapper.creator.DefaultBeanCreator");
            attributes.addAttribute("", "mapping-class", "mapping-class", "", "org.jbeanmapper.DefaultPropertyMapper");
            propertyMappingRule.begin("", "", attributes);
            fail("expected BeanMappingException");
        } catch (BeanMappingException e) {
        }
    }

    public void testInvalidConverterClass() throws Exception {
        try {
            Digester digester = new Digester();
            PropertyMappingRule propertyMappingRule = new PropertyMappingRule();
            propertyMappingRule.setDigester(digester);
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "src", "src", "", "someprop");
            attributes.addAttribute("", "target", "target", "", "someprop");
            attributes.addAttribute("", "converter", "converter", "", "org.jbeanmapper.converterNotHere");
            propertyMappingRule.begin("", "", attributes);
            fail("expeted BeanMappingException");
        } catch (BeanMappingException e) {
            assertTrue(e.getCause() instanceof ClassNotFoundException);
        }
    }

    public void testConverterNotImplementingInterface() throws Exception {
        try {
            Digester digester = new Digester();
            PropertyMappingRule propertyMappingRule = new PropertyMappingRule();
            propertyMappingRule.setDigester(digester);
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "src", "src", "", "someprop");
            attributes.addAttribute("", "target", "target", "", "someprop");
            attributes.addAttribute("", "converter", "converter", "", "org.jbeanmapper.PersonBean1");
            propertyMappingRule.begin("", "", attributes);
            fail("expeted BeanMappingException");
        } catch (BeanMappingException e) {
        }
    }

    public void testConverterIllegalAccessException() throws Exception {
        try {
            Digester digester = new Digester();
            PropertyMappingRule propertyMappingRule = new PropertyMappingRule();
            propertyMappingRule.setDigester(digester);
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "src", "src", "", "someprop");
            attributes.addAttribute("", "target", "target", "", "someprop");
            attributes.addAttribute("", "converter", "converter", "", "org.jbeanmapper.configurator.TestPropertyMappingRule$MockPropertyMapper");
            propertyMappingRule.begin("", "", attributes);
            fail("expeted BeanMappingException");
        } catch (BeanMappingException e) {
            assertTrue("expected IllegalAccessException, got " + e.getCause(), e.getCause() instanceof IllegalAccessException);
        }
    }

    public void testConverterInstantiationException() throws Exception {
        try {
            Digester digester = new Digester();
            PropertyMappingRule propertyMappingRule = new PropertyMappingRule();
            propertyMappingRule.setDigester(digester);
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "src", "src", "", "org.jbeanmapper.PersonBean1");
            attributes.addAttribute("", "target", "target", "", "org.jbeanmapper.PersonBean2");
            attributes.addAttribute("", "mapping-class", "mapping-class", "", "org.jbeanmapper.configurator.TestPropertyMappingRule$MockPropertyMapper2");
            propertyMappingRule.begin("", "", attributes);
            fail("expeted BeanMappingException");
        } catch (BeanMappingException e) {
            assertTrue("expected InstantiationException, got " + e.getCause(), e.getCause() instanceof InstantiationException);
        }
    }

    public void testInvalidPropertyMapperClass() throws Exception {
        try {
            Digester digester = new Digester();
            PropertyMappingRule propertyMappingRule = new PropertyMappingRule();
            propertyMappingRule.setDigester(digester);
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "src", "src", "", "someprop");
            attributes.addAttribute("", "target", "target", "", "someprop");
            attributes.addAttribute("", "mapping-class", "mapping-class", "", "org.jbeanmapper.MapperNotHere");
            propertyMappingRule.begin("", "", attributes);
            fail("expeted BeanMappingException");
        } catch (BeanMappingException e) {
            assertTrue(e.getCause() instanceof ClassNotFoundException);
        }
    }

    public void testPropertyMapperNotImplementingInterface() throws Exception {
        try {
            Digester digester = new Digester();
            PropertyMappingRule propertyMappingRule = new PropertyMappingRule();
            propertyMappingRule.setDigester(digester);
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "src", "src", "", "someprop");
            attributes.addAttribute("", "target", "target", "", "someprop");
            attributes.addAttribute("", "mapping-class", "mapping-class", "", "org.jbeanmapper.PersonBean1");
            propertyMappingRule.begin("", "", attributes);
            fail("expeted BeanMappingException");
        } catch (BeanMappingException e) {
        }
    }

    public void testPropertyMapperIllegalAccessException() throws Exception {
        try {
            Digester digester = new Digester();
            PropertyMappingRule propertyMappingRule = new PropertyMappingRule();
            propertyMappingRule.setDigester(digester);
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "src", "src", "", "someprop");
            attributes.addAttribute("", "target", "target", "", "someprop");
            attributes.addAttribute("", "mapping-class", "mapping-class", "", "org.jbeanmapper.configurator.TestPropertyMappingRule$MockPropertyMapper");
            propertyMappingRule.begin("", "", attributes);
            fail("expeted BeanMappingException");
        } catch (BeanMappingException e) {
            assertTrue("expected IllegalAccessException, got " + e.getCause(), e.getCause() instanceof IllegalAccessException);
        }
    }

    public void testPropertyMapperInstantiationException() throws Exception {
        try {
            Digester digester = new Digester();
            PropertyMappingRule propertyMappingRule = new PropertyMappingRule();
            propertyMappingRule.setDigester(digester);
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "src", "src", "", "org.jbeanmapper.PersonBean1");
            attributes.addAttribute("", "target", "target", "", "org.jbeanmapper.PersonBean2");
            attributes.addAttribute("", "mapping-class", "mapping-class", "", "org.jbeanmapper.configurator.TestPropertyMappingRule$MockPropertyMapper2");
            propertyMappingRule.begin("", "", attributes);
            fail("expeted BeanMappingException");
        } catch (BeanMappingException e) {
            assertTrue("expected InstantiationException, got " + e.getCause(), e.getCause() instanceof InstantiationException);
        }
    }

    private static class MockPropertyMapper implements PropertyMapper {

        public void mapProperty(Object srcObject, String srcProperty, Object targetObject, String targetProperty, MappingContext context, BeanConverter converter) throws PropertyMappingException, BeanMappingException {
        }
    }

    public class MockPropertyMapper2 implements PropertyMapper {

        public void mapProperty(Object srcObject, String srcProperty, Object targetObject, String targetProperty, MappingContext context, BeanConverter converter) throws PropertyMappingException, BeanMappingException {
        }
    }

    public static Test suite() {
        return new TestSuite(TestPropertyMappingRule.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }
}
