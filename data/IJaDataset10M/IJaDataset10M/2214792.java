package org.unitmetrics.internal.resultstore;

import org.eclipse.core.resources.IResource;
import org.jmock.Mock;
import org.unitmetrics.IMetric;
import org.unitmetrics.IUnit;
import org.unitmetrics.core.Unit;
import org.unitmetrics.internal.resultstore.PerUnitCollection;
import org.unitmetrics.junit.TestCase;

/**
 * @author Martin Kersten
 */
public class PerUnitCollectionTest extends TestCase {

    IMetric metric = createNewDummy(IMetric.class);

    IMetric metric2 = createNewDummy(IMetric.class);

    Mock resourceMock;

    IResource resource;

    IUnit unit;

    IUnit unit2;

    PerUnitCollection<Object> collection = new PerUnitCollection<Object>() {
    };

    Object object = new Object();

    Object object2 = new Object();

    protected void setUp() throws Exception {
        super.setUp();
        resourceMock = mock(IResource.class);
        resource = (IResource) resourceMock.proxy();
        unit = new Unit(resource, resource, "type", "myResource", 0);
        unit2 = new Unit(resource, resource, "type", "mySecondResource", 0);
    }

    public void testAddingAndRemoving() throws Exception {
        assertNull("No object should be stored", collection.get(unit, metric));
        collection.set(unit, metric, object);
        assertSame("Object should be stored", object, collection.get(unit, metric));
        collection.set(unit, metric, object2);
        assertSame("Object2 should be stored", object2, collection.get(unit, metric));
        collection.remove(unit, metric);
        assertNull("No object should be stored anymore", collection.get(unit, metric));
    }

    public void testAddRemoveTwoObjects() throws Exception {
        assertNull("No object should be stored for metric1", collection.get(unit, metric));
        assertNull("No object should be stored for metric2", collection.get(unit, metric2));
        collection.set(unit, metric, object);
        assertSame("Object should be stored for metric1", object, collection.get(unit, metric));
        assertNull("No object should be stored for metric2", collection.get(unit, metric2));
        collection.set(unit, metric2, object2);
        assertSame("Object should be stored for metric1", object, collection.get(unit, metric));
        assertSame("Object2 should be stored for metric2", object2, collection.get(unit, metric2));
        collection.remove(unit, metric);
        assertNull("No object should be stored for metric", collection.get(unit, metric));
        assertSame("Object2 should be stored for metric2", object2, collection.get(unit, metric2));
        collection.remove(unit, metric2);
        assertNull("No object should be stored for metric1", collection.get(unit, metric));
        assertNull("No object should be stored for metric2", collection.get(unit, metric2));
    }

    public void testGetUnits() {
        collection.set(unit, metric, object);
        collection.set(unit2, metric, object2);
        IUnit[] units = collection.getUnits(resource, metric);
        assertSize("Two units should be a result stored for", 2, units);
        assertContains("For unit a result should be stored for", unit, units);
        assertContains("For unit2 a result should be stored for", unit2, units);
    }
}
