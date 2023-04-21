package javax.persistence.dataModel;

import bbd.BBDBeanArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @param B 
 * @param L 
 * @author James Gamber
 */
public class BBDJavaClassBrokerTest<B extends JavaClassBean, L extends BBDBeanArrayList<B>> {

    public BBDJavaClassBrokerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of select method, of class BBDJavaClassBroker.
     */
    @Test
    public void selectAll() {
        System.out.println("select");
        BBDJavaClassBroker instance = new BBDJavaClassBroker();
        instance.setPrincipal("bbd", "bbd");
        L result = (L) instance.selectAll();
        assertTrue(result.size() > 0);
        boolean found = false;
        for (JavaClassBean jcb : result) {
            if (jcb.getJavaClassName().contains(".String")) found = true;
        }
        assertTrue("didnt find the string class", found);
    }

    /**
     * Test of update method, of class BBDJavaClassBroker.
     */
    @Ignore
    public void update() {
        System.out.println("update");
        B row = null;
        BBDJavaClassBroker instance = new BBDJavaClassBroker();
        int expResult = 0;
        int result = instance.update(row);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPrincipal method, of class BBDJavaClassBroker.
     */
    @Test
    public void setPrincipal() {
        System.out.println("setPrincipal");
        String name = "test";
        String password = "test";
        BBDJavaClassBroker instance = new BBDJavaClassBroker();
        instance.setPrincipal(name, password);
        assertEquals(instance.userAccess.getName(), name);
    }
}
