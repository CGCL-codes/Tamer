package org.t2framework.commons.util;

import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.t2framework.commons.Constants;
import org.t2framework.commons.util.Reflections.MethodUtil;

public class JavaBeansUtilTest extends TestCase {

    public void testDecapitalize() throws Exception {
        assertEquals("aaaa", JavaBeansUtil.decapitalize("Aaaa"));
    }

    public void testSetMethod() throws Exception {
        assertTrue(JavaBeansUtil.isSetMethod("setHoge"));
        assertFalse(JavaBeansUtil.isSetMethod("sethoge"));
        assertTrue(JavaBeansUtil.isSetMethod("setHOge"));
        Method m = this.getClass().getDeclaredMethod("setHoge", String.class);
        boolean ret = JavaBeansUtil.isSetMethod("setHoge", m);
        assertTrue(ret);
    }

    public void testGetReadMethodFromWriteMethod1() throws Exception {
        Method writeMethod = MethodUtil.getDeclaredMethod(this.getClass(), "setStr", new Class[] { String.class });
        Method readMethod = JavaBeansUtil.getReadMethodFromWriteMethod(this.getClass(), writeMethod);
        assertNotNull(readMethod);
    }

    public void testGetReadMethodFromWriteMethod2() throws Exception {
        Method writeMethod = MethodUtil.getDeclaredMethod(this.getClass(), "setFlag", new Class[] { Boolean.TYPE });
        Method readMethod = JavaBeansUtil.getReadMethodFromWriteMethod(this.getClass(), writeMethod);
        assertNotNull(readMethod);
    }

    public void testGetWriteMethodFromReadMethod1() throws Exception {
        Method readMethod = MethodUtil.getDeclaredMethod(this.getClass(), "getStr", Constants.EMPTY_CLASS_ARRAY);
        Method writeMethod = JavaBeansUtil.getWriteMethodFromReadMethod(this.getClass(), readMethod);
        assertNotNull(writeMethod);
    }

    public void testGetWriteMethodFromReadMethod2() throws Exception {
        Method readMethod = MethodUtil.getDeclaredMethod(this.getClass(), "isFlag", Constants.EMPTY_CLASS_ARRAY);
        Method writeMethod = JavaBeansUtil.getWriteMethodFromReadMethod(this.getClass(), readMethod);
        assertNotNull(writeMethod);
    }

    public void setStr(String s) {
    }

    public String getStr() {
        return "aaa";
    }

    public void setFlag(boolean b) {
    }

    public boolean isFlag() {
        return false;
    }

    public JavaBeansUtilTest setHoge(String hoge) {
        return this;
    }
}
