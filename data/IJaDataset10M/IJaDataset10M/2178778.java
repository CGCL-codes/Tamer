package com.germinus.xpression.content_editor.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.struts.util.MessageResources;
import junit.framework.TestCase;

public class OurBeanUtilsTest extends TestCase {

    public void testPopulateWithAutoCreate() throws Exception {
        OurBeanUtils ourBeanUtils = new OurBeanUtils();
        Map properties = new HashMap();
        properties.put("property1.property2.property3", "value");
        LazyDynaBean bean = new LazyDynaBean();
        ourBeanUtils.populate(bean, properties);
        assertNotNull(bean.get("property1"));
        DynaBean property1 = (DynaBean) bean.get("property1");
        assertNotNull(property1.get("property2"));
        DynaBean property2 = (DynaBean) property1.get("property2");
        assertNotNull(property2.get("property3"));
        assertEquals("value", property2.get("property3"));
        assertEquals("value", PropertyUtils.getNestedProperty(bean, "property1.property2.property3"));
    }

    public void testPopulateIndexedWithAutoCreate() throws Exception {
        OurBeanUtils ourBeanUtils = new OurBeanUtils();
        Map properties = new HashMap();
        properties.put("property1[0].text", "value");
        properties.put("property1[0].subproperty", "subValue");
        properties.put("property1[1].subproperty", "subValue2");
        LazyDynaBean bean = new LazyDynaBean();
        ourBeanUtils.populate(bean, properties);
        assertNotNull(bean.get("property1"));
        assertTrue(Collection.class.isAssignableFrom(bean.get("property1").getClass()));
        Collection property1Collection = (Collection) bean.get("property1");
        assertEquals(2, property1Collection.size());
        Iterator it = property1Collection.iterator();
        Object firstElement = it.next();
        assertNotNull(firstElement);
        assertEquals("value", PropertyUtils.getNestedProperty(firstElement, "text"));
        assertEquals("subValue", PropertyUtils.getNestedProperty(firstElement, "subproperty"));
        Object secondElement = it.next();
        assertNotNull(secondElement);
        assertEquals("subValue2", PropertyUtils.getNestedProperty(secondElement, "subproperty"));
    }
}
