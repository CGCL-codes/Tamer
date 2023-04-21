package com.amazon.carbonado.cursor;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.amazon.carbonado.filter.Filter;
import com.amazon.carbonado.stored.*;

/**
 * 
 *
 * @author Brian S O'Neill
 */
public class TestShortCircuitOptimizer extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestShortCircuitOptimizer.class);
    }

    public TestShortCircuitOptimizer(String name) {
        super(name);
    }

    protected void setUp() {
    }

    protected void tearDown() {
    }

    public void testBasic() throws Exception {
        {
            Filter<Order> filter = Filter.getOpenFilter(Order.class);
            Filter<Order> optimized = ShortCircuitOptimizer.optimize(filter);
            assertEquals(filter, optimized);
        }
        {
            Filter<Order> filter = Filter.getClosedFilter(Order.class);
            Filter<Order> optimized = ShortCircuitOptimizer.optimize(filter);
            assertEquals(filter, optimized);
        }
        {
            Filter<Order> filter = Filter.filterFor(Order.class, "orderNumber = ?");
            Filter<Order> optimized = ShortCircuitOptimizer.optimize(filter);
            assertEquals(filter, optimized);
        }
    }

    public void testNoJoins() throws Exception {
        {
            Filter<Order> filter = Filter.filterFor(Order.class, "orderNumber = ? & orderTotal = ?");
            Filter<Order> optimized = ShortCircuitOptimizer.optimize(filter);
            assertEquals(filter, optimized);
        }
        {
            Filter<Order> filter = Filter.filterFor(Order.class, "orderNumber = ? | orderTotal = ?");
            Filter<Order> optimized = ShortCircuitOptimizer.optimize(filter);
            assertEquals(filter, optimized);
        }
        {
            Filter<Order> filter = Filter.filterFor(Order.class, "(orderNumber = ? | orderTotal = ?) & addressID != ?");
            Filter<Order> optimized = ShortCircuitOptimizer.optimize(filter);
            assertEquals(filter, optimized);
        }
    }

    public void testManyToOneJoins() throws Exception {
        {
            Filter<Order> filter = Filter.filterFor(Order.class, "orderNumber = ? & address.addressState = ?");
            Filter<Order> optimized = ShortCircuitOptimizer.optimize(filter);
            assertEquals(filter, optimized);
        }
        {
            Filter<Order> filter = Filter.filterFor(Order.class, "address.addressState = ? & orderNumber = ?");
            Filter<Order> optimized = ShortCircuitOptimizer.optimize(filter);
            assertEquals(Filter.filterFor(Order.class, "orderNumber = ? & address.addressState = ?"), optimized);
        }
        {
            Filter<Order> filter = Filter.filterFor(Order.class, "(address.addressState = ? & orderNumber = ?) | orderTotal < ?");
            Filter<Order> optimized = ShortCircuitOptimizer.optimize(filter);
            assertEquals(Filter.filterFor(Order.class, "orderTotal < ? | (orderNumber = ? & address.addressState = ?)"), optimized);
        }
        {
            Filter<Shipment> filter = Filter.filterFor(Shipment.class, "order.address.addressState = ? | shipper.shipperName = ?");
            Filter<Shipment> optimized = ShortCircuitOptimizer.optimize(filter);
            assertEquals(Filter.filterFor(Shipment.class, "shipper.shipperName = ? | order.address.addressState = ?"), optimized);
        }
        {
            Filter<Shipment> filter = Filter.filterFor(Shipment.class, "order.address.addressState = ? | shipper.address.addressCity = ?");
            Filter<Shipment> optimized = ShortCircuitOptimizer.optimize(filter);
            assertEquals(filter, optimized);
        }
    }

    public void testOneToManyJoins() throws Exception {
        {
            Filter<Order> filter = Filter.filterFor(Order.class, "orderNumber = ? & orderItems(itemPrice = ?)");
            Filter<Order> optimized = ShortCircuitOptimizer.optimize(filter);
            assertEquals(filter, optimized);
        }
        {
            Filter<Order> filter = Filter.filterFor(Order.class, "orderItems(itemPrice = ?) & orderNumber = ?");
            Filter<Order> optimized = ShortCircuitOptimizer.optimize(filter);
            assertEquals(Filter.filterFor(Order.class, "orderNumber = ? & orderItems(itemPrice = ?)"), optimized);
        }
        {
            Filter<Order> filter = Filter.filterFor(Order.class, "(orderItems(itemPrice = ?) & orderNumber = ?) | orderTotal < ?");
            Filter<Order> optimized = ShortCircuitOptimizer.optimize(filter);
            assertEquals(Filter.filterFor(Order.class, "orderTotal < ? | (orderNumber = ? & orderItems(itemPrice = ?))"), optimized);
        }
        {
            Filter<Order> filter = Filter.filterFor(Order.class, "orderItems(shipment.shipmentDate > ?) | shipments(shipmentDate < ?)");
            Filter<Order> optimized = ShortCircuitOptimizer.optimize(filter);
            assertEquals(filter, optimized);
        }
        {
            Filter<Order> filter = Filter.filterFor(Order.class, "orderItems(shipment.shipmentDate > ?) | shipments(shipper.shipperName != ?)");
            Filter<Order> optimized = ShortCircuitOptimizer.optimize(filter);
            assertEquals(filter, optimized);
        }
    }
}
