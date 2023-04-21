package com.google.code.joto.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

/**
 * @author epere4
 * @author liliana.nu
 *
 */
public class IdentitySetUnitTest {

    private Set<String> set;

    @Before
    public void setup() {
        set = new IdentitySet<String>();
    }

    /**
	 * Test method for {@link com.google.code.joto.util.IdentitySet#size()}.
	 */
    @Test
    public void testSize() {
        assertEquals(0, set.size());
        set.add("something");
        assertEquals(1, set.size());
        assertFalse(set.isEmpty());
        set.clear();
        assertEquals(0, set.size());
        assertTrue(set.isEmpty());
    }

    /**
	 * Test method for {@link com.google.code.joto.util.IdentitySet#contains(java.lang.Object)}.
	 */
    @Test
    public void testContainsObject() {
        assertFalse(set.contains("some"));
        set.add("some");
        assertTrue(set.contains("some"));
        assertFalse(set.contains(new String("some")));
    }
}
