package org.javason;

import java.io.Serializable;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import junit.framework.TestCase;

public class TestMisc extends TestCase {

    public class TestBeanA implements Serializable {

        private static final long serialVersionUID = -7910220024063395349L;

        private String _blah;

        public String getBlah() {
            return _blah;
        }

        public void setBlah(String blah) {
            _blah = blah;
        }

        public String toString() {
            return ReflectionToStringBuilder.toString(this);
        }

        public String toString2() {
            return new ToStringBuilder(this).append("_blah", _blah).toString();
        }
    }

    public void testToStringBuilder() {
        System.out.println("ToString = " + (new TestBeanA()).toString());
        System.out.println("ToString = " + (new TestBeanA()).toString2());
        TestBeanA beanA = new TestBeanA();
        assertEquals(beanA.toString(), beanA.toString2());
    }

    public void testSystemMethod() {
        String methodName = "system.describe";
        assertEquals("describe", methodName.replaceFirst("system\\.", ""));
        methodName = "systemDescribe";
        assertEquals(methodName, methodName.replaceFirst("system\\.", ""));
        methodName = "system.";
        assertEquals("", methodName.replaceFirst("system\\.", ""));
        methodName = "system.system.describe";
        assertEquals("system.describe", methodName.replaceFirst("system\\.", ""));
    }

    public void testNullInstanceOf() {
        Integer test = null;
        try {
            assertFalse(test instanceof Integer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
