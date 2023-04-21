package com.bs.xdbms.xmldb.modules;

import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;
import org.xmldb.api.*;
import junit.framework.*;

public class CollectionManagementServiceImplTest extends TestCase {

    public CollectionManagementServiceImplTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new CollectionManagementServiceImplTest("createCollection") {

            protected void runTest() {
                testCreateCollection();
            }
        });
        suite.addTest(new CollectionManagementServiceImplTest("removeCollection") {

            protected void runTest() {
                testRemoveCollection();
            }
        });
        return suite;
    }

    /**
	 * @see TestCase#setUp()
	 */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
	 * @see TestCase#tearDown()
	 */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCreateCollection() {
        try {
            Collection root = getDefaultCollection();
            Assert.assertNotNull("Failed getting a default collection", root);
            CollectionManagementService service1 = (CollectionManagementService) root.getService("CollectionManagementService", "1.0");
            Assert.assertNotNull("Failed creating a collection management service", service1);
            Collection user = service1.createCollection("user");
            Assert.assertNotNull("Failed creating a collection", user);
            CollectionManagementService service2 = (CollectionManagementService) user.getService("CollectionManagementService", "1.0");
            Assert.assertNotNull("Failed creating a collection management service", service2);
            Collection test = service2.createCollection("test");
            Assert.assertNotNull("Failed creating a collection", test);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e);
        }
    }

    public void testRemoveCollection() {
        try {
            Collection root = getDefaultCollection();
            Assert.assertNotNull("Failed getting a default collection", root);
            CollectionManagementService service = (CollectionManagementService) root.getService("CollectionManagementService", "1.0");
            Assert.assertNotNull("Failed creating a collection management service", service);
            service.removeCollection("user");
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e);
        }
    }

    protected Collection getDefaultCollection() throws Exception {
        Collection col = null;
        String driver = "com.bs.xdbms.xmldb.base.Database";
        Class c = Class.forName(driver);
        Database database = (Database) c.newInstance();
        DatabaseManager.registerDatabase(database);
        col = DatabaseManager.getCollection("xmldb:myxdb://localhost/root", "root", "");
        return col;
    }
}
