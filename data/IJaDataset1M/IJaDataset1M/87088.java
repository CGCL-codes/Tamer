package net.sf.balm.common.beanutils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BeanUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    public void testInheritedProperty() {
        ChildBean childBean = new ChildBean();
        Assert.assertEquals("setParentName", BeanUtils.getSetterMethod(childBean, "parentName").getName());
        Assert.assertEquals("getParentName", BeanUtils.getGetterMethod(childBean, "parentName").getName());
        Assert.assertEquals(String.class, BeanUtils.getPropertyType(childBean, "parentName"));
        Assert.assertEquals("setChildName", BeanUtils.getSetterMethod(childBean, "childName").getName());
        Assert.assertEquals("getChildName", BeanUtils.getGetterMethod(childBean, "childName").getName());
        Assert.assertEquals(String.class, BeanUtils.getPropertyType(childBean, "childName"));
    }

    public void testGetMapProperty() {
        Map outContainer = new HashMap();
        Map innerContainer = new HashMap();
        outContainer.put("inner", innerContainer);
        Map data = new HashMap();
        ChildBean childBean = new ChildBean();
        childBean.setChildName("child");
        data.put("child", childBean);
        innerContainer.put("data", data);
        try {
            System.out.println(BeanUtils.getPropertyValueIfNecessary(outContainer, "*[0]"));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetListProperty() {
        List container = new ArrayList();
        ChildBean childBean = new ChildBean();
        childBean.setChildName("child");
        container.add(childBean);
        container.add(childBean);
        container.add(childBean);
        container.add(childBean);
        try {
            System.out.println(BeanUtils.getPropertyValueIfNecessary(container, "0"));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
