package org.statefive.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractTypeWrapperTest {

    private static final Class WRAPPER_CLASS = String.class;

    private WrapperSubclass wrapper;

    private class WrapperSubclass extends AbstractTypeWrapper {

        public WrapperSubclass() {
            super(WRAPPER_CLASS);
        }

        public Object wrap() {
            return "";
        }
    }

    @Before
    public void setUp() throws Exception {
        wrapper = new WrapperSubclass();
    }

    @Test
    public void testSetGetValue() {
        assertNull(wrapper.getValue());
        wrapper.setValue("123");
        assertEquals("123", wrapper.getValue());
    }

    @Test
    public void testGetType() {
        assertEquals(String.class, wrapper.getType());
    }
}
