package org.pescuma.jfg;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.pescuma.jfg.AbstractAttribute;
import org.pescuma.jfg.AttributeListener;

public class AbstractAttributeTest {

    private AbstractAttribute attrib;

    @Before
    public void setup() {
        attrib = new AbstractAttribute() {

            public String getName() {
                return null;
            }

            public Object getType() {
                return null;
            }

            public Object getValue() {
                return null;
            }

            public void setValue(Object obj) {
            }
        };
    }

    @Test
    public void testGetValueRange() {
        assertEquals(null, attrib.getValueRange());
    }

    @Test
    public void testCanWrite() {
        assertEquals(true, attrib.canWrite());
    }

    @Test
    public void testCanListen() {
        assertEquals(false, attrib.canListen());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddListener() {
        attrib.addListener(new AttributeListener() {

            public void onChange() {
            }
        });
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveListener() {
        attrib.removeListener(new AttributeListener() {

            public void onChange() {
            }
        });
    }

    @Test
    public void testAsGroup() {
        assertEquals(null, attrib.asGroup());
    }
}
