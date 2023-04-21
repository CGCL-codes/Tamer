package org.unitmetrics.flavors;

import org.unitmetrics.junit.TestCase;

/**
 * @author Martin Kersten
 */
public class FalseFlavorTest extends TestCase {

    public void testFlavor() {
        assertFalse("Flavor should always be true", new FalseFlavor().isOfFlavor(null));
    }
}
