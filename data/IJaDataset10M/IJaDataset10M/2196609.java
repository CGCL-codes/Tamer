package org.unitmetrics.internal;

import java.util.Set;
import org.unitmetrics.IFlavor;
import org.unitmetrics.junit.TestCase;

/**
 * @author Martin Kersten
 */
public class FlavorRegistryTest extends TestCase {

    IFlavorRegistry registry = new FlavorRegistry();

    IFlavor flavor = createNewDummy(IFlavor.class);

    IFlavor flavor2 = createNewDummy(IFlavor.class);

    public void testAddAndRecieveExperts() {
        assertNull("No flavor should be registered", registry.getFlavor("flavor"));
        assertEmpty("No flavor should be registered", registry.getAll());
        registry.register("flavor", flavor);
        assertSame("Flavor should be registered", flavor, registry.getFlavor("flavor"));
        Set<IFlavor> flavors = registry.getAll();
        assertSize("One flavor should be registered", 1, flavors);
        assertContainsSame("Flavor should be part of the registered flavors", flavor, flavors);
        registry.register("flavor2", flavor2);
        assertSame("Flavor should be registered", flavor, registry.getFlavor("flavor"));
        assertSame("Flavor should be registered", flavor2, registry.getFlavor("flavor2"));
        flavors = registry.getAll();
        assertSize("Two flavors should be registered", 2, flavors);
        assertContainsSame("Flavor should be part of the registered flavors", flavor, flavors);
        assertContainsSame("Flavor2 should be part of the registered flavors", flavor2, flavors);
    }
}
