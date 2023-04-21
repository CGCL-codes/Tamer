package org.weras.commons.core.to.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.io.Serializable;
import org.junit.Test;
import org.weras.commons.core.to.TO;

public class TOTest {

    @Test
    public void testGetSetModel() {
        MyTO to = new MyTO();
        assertNull(to.getModel());
        MyModel model = new MyModel();
        to.setModel(model);
        assertSame(model, to.getModel());
    }

    @Test
    public void testWiths() {
        MyTO to = new MyTO();
        assertNull(to.getModel());
        MyModel model = new MyModel();
        MyPrimaryKey key = new MyPrimaryKey();
        to.withModel(model).withModelID(key);
        assertSame(model, to.getModel());
        assertSame(key, to.getModelID());
    }

    @Test
    public void testAddAndGetParam() {
        MyTO to = new MyTO();
        to.addParam("my", 9);
        assertNotNull(to.getParam("my"));
        assertEquals(to.getParam("my"), 9);
    }

    @Test
    public void testContainsAndRemoveParam() {
        MyTO to = new MyTO();
        to.addParam("my", 9);
        assertTrue(to.containsParam("my"));
        assertNotNull(to.getParam("my"));
        to.removeParam("my");
        assertNull(to.getParam("my"));
        assertFalse(to.containsParam("my"));
    }

    @Test
    public void testLock() {
        MyTO to = new MyTO();
        to.setLock(true);
        assertTrue(to.isLock());
    }

    @Test
    public void testIntParam() {
        MyTO to = new MyTO();
        int value = 90;
        to.addParam("p", value);
        assertTrue(to.getIntParam("p").intValue() == value);
    }

    @Test
    public void testShortParam() {
        MyTO to = new MyTO();
        short value = 150;
        to.addParam("p", value);
        assertTrue(to.getShortParam("p").shortValue() == value);
    }

    @Test
    public void testFloatParam() {
        MyTO to = new MyTO();
        float value = 110f;
        to.addParam("p", value);
        assertTrue(to.getFloatParam("p").floatValue() == value);
    }

    @Test
    public void testDoubleParam() {
        MyTO to = new MyTO();
        double value = 90f;
        to.addParam("p", value);
        assertTrue(to.getDoubleParam("p").doubleValue() == value);
    }

    @Test
    public void testLongParam() {
        MyTO to = new MyTO();
        long value = 10L;
        to.addParam("p", value);
        assertTrue(to.getLongParam("p").longValue() == value);
    }

    @Test
    public void testCharParam() {
        MyTO to = new MyTO();
        char value = 'c';
        to.addParam("p", value);
        assertTrue(to.getCharParam("p").equals(value));
    }

    @Test
    public void testStringParam() {
        MyTO to = new MyTO();
        String value = "s";
        to.addParam("p", value);
        assertSame(to.getStringParam("p"), value);
    }

    @Test
    public void testBooleanParam() {
        MyTO to = new MyTO();
        Boolean value = false;
        to.addParam("p", value);
        assertSame(to.getBooleanParam("p"), value);
        assertFalse(to.getBooleanParam("p"));
    }

    class MyModel {

        private MyPrimaryKey pk;

        public MyPrimaryKey getPk() {
            return pk;
        }

        public void setPk(MyPrimaryKey id) {
            this.pk = id;
        }
    }

    @SuppressWarnings("serial")
    class MyPrimaryKey implements Serializable {
    }

    @SuppressWarnings("serial")
    class MyTO extends TO<MyModel, MyPrimaryKey> {

        @Override
        public MyPrimaryKey getModelID() {
            return getModel().getPk();
        }

        @Override
        public void setModelID(MyPrimaryKey id) {
            getModel().setPk(id);
        }
    }
}
