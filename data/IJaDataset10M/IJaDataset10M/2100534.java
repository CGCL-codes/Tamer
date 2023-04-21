package org.objectwiz.metadata.criteria;

import junit.framework.TestCase;

/**
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class OrderTest extends TestCase {

    public void testInstanciate() {
        Order order;
        order = new Order("ascending", true);
        assertEquals("ascending", order.getTargetName());
        assertTrue(order.isAscending());
        order = new Order("descending", false);
        assertEquals("descending", order.getTargetName());
        assertFalse(order.isAscending());
    }
}
