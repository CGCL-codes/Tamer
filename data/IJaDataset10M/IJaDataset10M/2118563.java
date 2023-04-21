package org.antdepo.common;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * ControlTier Software Inc.
 * User: alexh
 * Date: Jul 22, 2005
 * Time: 3:06:31 PM
 */
public class TestObjectContext extends TestCase {

    private static final String ENTITY_DEPOT = "HahaDepot";

    private static final String ENTITY_TYPE = "HahaClass";

    private static final String ENTITY_NAME = "aHaha";

    public TestObjectContext(String name) {
        super(name);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(TestObjectContext.class);
    }

    public void testAll() {
        ObjectDesc obj = ObjectDesc.create(ENTITY_DEPOT, ENTITY_TYPE, ENTITY_NAME);
        assertEquals(obj.getDepot(), ENTITY_DEPOT);
        assertEquals(obj.getEntityType(), ENTITY_TYPE);
        assertEquals(obj.getEntityName(), ENTITY_NAME);
        assertTrue(obj.isDepotContext());
        assertTrue(obj.isTypeContext());
        assertTrue(obj.isObjectContext());
        ObjectDesc obj2 = ObjectDesc.create(obj);
        assertTrue("obj did not equal obj2", obj.equals(obj2));
        assertEquals("obj and obj2 hashcodes did not match. obj:" + obj.hashCode() + ", obj2: " + obj2.hashCode(), obj.hashCode(), obj2.hashCode());
    }
}
