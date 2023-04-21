package com.googlecode.beanfiles.translators;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.googlecode.beanfiles.BeanWithNestedBeans;
import com.googlecode.beanfiles.TranslatorMapUtils;

public class TestTranslatorMapUtils extends TestCase {

    protected static Log log() {
        return LogFactory.getLog(TranslatorMapUtils.class);
    }

    public static Test suite() {
        return new TestSuite(TestTranslatorMapUtils.class);
    }

    /**
     * buildPropertiesMap function still undetermined
     */
    public void testBuildNestedPropertiesMap() {
        Map<String, String> properties = TranslatorMapUtils.buildNestedPropertiesMap(Arrays.asList(new String[] { "property1", "property2.prop1", "property2.prop2" }));
        assertTrue(properties.keySet().contains("property1"));
        assertEquals("property1", properties.get("property1"));
        assertTrue(properties.keySet().contains("property2.prop1"));
        assertEquals("property2", properties.get("property2.prop1"));
        assertTrue(properties.keySet().contains("property2.prop2"));
        assertEquals("property2", properties.get("property2.prop2"));
    }

    public void testBuildeNestedPropertiesMapFromClass() {
        List<String> properties = TranslatorMapUtils.buildNestedPropertiesList(BeanWithNestedBeans.class);
        Iterator<String> keys = properties.iterator();
        String key1 = keys.next();
        String key2 = keys.next();
        String key3 = keys.next();
        String key4 = keys.next();
        String key5 = keys.next();
        String key6 = keys.next();
        String key7 = keys.next();
        assertEquals("listBean.integers", key1);
        assertEquals("listBean.strings", key2);
        assertEquals("simpleBean.property1", key3);
        assertEquals("simpleBean.property2", key4);
        assertEquals("simpleBean.property3", key5);
        assertEquals("simpleBean.property4", key6);
        assertEquals("simpleBean.property5", key7);
    }
}
