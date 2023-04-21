package net.sf.carmaker.orders.impl;

import org.junit.Assert;
import net.sf.carmaker.orders.IConfigurationItemType;
import net.sf.carmaker.orders.IOrder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Team Gr�n, Marcus Trommen & Matthias Rummel
 * @version 0.01.02
 * @since 0.01
 */
public class TestOrder {

    /** Logger f�r Debug-Ausgaben */
    private static final Log log = LogFactory.getLog(TestOrder.class);

    private static final String configurationString1 = "A4711,RED,nichts,NONE";

    private static final String configurationString2 = "X123S,BLUE,Sportd�mpfer,SPORTABSORBER";

    private IOrder order1;

    private IOrder order2;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        order1 = Order.createOrder(configurationString1);
        order2 = Order.createOrder(configurationString2);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test Methode f�r {@link net.sf.carmaker.orders.impl.Order#Order(java.lang.String)}.
     */
    @Test
    public void testOrder() {
        log.debug("+++ starting: testOrder");
        Assert.assertNotNull("reference to valid object expected", order1);
        Assert.assertNotNull("reference to valid object expected", order2);
    }

    /**
     * Test Methode f�r
     * {@link net.sf.carmaker.orders.impl.Order#hasConfigItem(net.sf.carmaker.orders.IConfigurationItemType)}.
     */
    @Test
    public void testHasConfigItemType() {
        log.debug("+++ starting: testHasConfigItemType");
        Assert.assertTrue(order1.hasConfigItem(IConfigurationItemType.WHEEL));
        Assert.assertTrue(order2.hasConfigItem(IConfigurationItemType.WHEEL));
        Assert.assertFalse(order1.hasConfigItem(IConfigurationItemType.SA_SPORTS_ABSORBER));
        Assert.assertTrue(order2.hasConfigItem(IConfigurationItemType.SA_SPORTS_ABSORBER));
        try {
            Assert.assertTrue(order2.hasConfigItem(null));
            Assert.fail("");
        } catch (IllegalParameterException exception) {
            log.debug("Expected exception, caused of NULL parameter", exception);
        }
    }

    /**
     * Teste das Problem, da� die Anh�ngerkupplung nicht gefunden wird.
     */
    @Test
    public void testTowingHitch() {
        IOrder orderLocal = Order.createOrder("A4711,RED,SA_TOWING_HITCH,TowingHitch");
        Assert.assertTrue(orderLocal.hasConfigItem("TowingHitch"));
    }

    /**
     * Test Methode f�r {@link net.sf.carmaker.orders.impl.Order#createOrder(String)}.
     */
    @Test
    public void testCreateOrder() {
        log.debug("+++ starting: testCreateOrder");
        IOrder orderNull = Order.createOrder(null);
        Assert.assertNull("null-object expected", orderNull);
        orderNull = Order.createOrder("");
        Assert.assertNull("null-object expected", orderNull);
    }

    /**
     * Testet die ToString Methode
     */
    @Test
    public void testToString() {
        log.debug("+++ starting: testCreateOrder");
        String theString = order1.toString();
        Assert.assertTrue(theString.equals("Order: Wheel, Bench, Body, Chassis, Gearbox, Motor, Seat, Steering"));
    }
}
