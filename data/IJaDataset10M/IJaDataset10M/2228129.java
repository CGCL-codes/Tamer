package net.sourceforge.cathcart.typedefs;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PropertyTypeTest {

    PropertyType pt;

    @Before
    public void setUp() throws Exception {
        pt = new PropertyType();
    }

    @After
    public void tearDown() throws Exception {
        pt = null;
    }

    @Test
    public void shouldBeSuccessful() {
        pt.setName(PropertyType.ALWAYS_SUCCESSFUL);
        assertTrue(pt.alwaysSuccessful);
    }

    @Test
    public void shouldNotBeSuccessful() {
        pt.setName("aType");
        assertFalse(pt.alwaysSuccessful);
    }

    @Test
    public void shouldBeAbsolute() {
        pt.setAbsolute(true);
        assertTrue(pt.isAbsolute());
    }

    @Test
    public void shouldntBeAbsolute() {
        assertFalse(pt.isAbsolute());
    }
}
