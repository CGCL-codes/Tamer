package org.hl7.types.impl;

import junit.framework.TestCase;

/**
 * @author Jere Krischel
 */
public class NullFlavorImplTest extends TestCase {

    public void testDisplayNameMethodWithInvalidFlavor() {
    }

    public void testDisplayNameMethodWithNpFlavor() {
        NullFlavorImpl nullFlavorImpl = NullFlavorImpl.NP;
        assertEquals(STjlStringAdapter.valueOf("not present"), nullFlavorImpl.displayName());
    }

    public void testDisplayNameMethodWithNotANullFlavor() {
        NullFlavorImpl nullFlavorImpl = NullFlavorImpl.NOT_A_NULL_FLAVOR;
        assertEquals(STjlStringAdapter.valueOf("not a null flavor"), nullFlavorImpl.displayName());
    }

    public void testCodeMethodWithInvalidFlavor() {
    }

    public void testCodeMethodWithNpFlavor() {
        NullFlavorImpl nullFlavorImpl = NullFlavorImpl.NP;
        assertEquals(STjlStringAdapter.valueOf("NP"), nullFlavorImpl.code());
    }

    public void testCodeMethodWithNotANullFlavor() {
        NullFlavorImpl nullFlavorImpl = NullFlavorImpl.NOT_A_NULL_FLAVOR;
        assertEquals(STnull.NA.hashCode(), nullFlavorImpl.code().hashCode());
    }

    public void testConstructingWithZero() {
    }

    public void testConstructingWithNonZero() {
    }
}
