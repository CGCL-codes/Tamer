package cz.cvut.fel.mvod.common;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Murko
 */
public class networkAddressRangeTest {

    public networkAddressRangeTest() {
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
     * Test of isOnLAN method, of class networkAddressRange.
     */
    @Test
    public void testIsOnLAN() {
        System.out.println("isOnLAN");
        int[] remote = { 192, 168, 2, 1 };
        boolean expResult = true;
        boolean result = networkAddressRange.isOnLAN(remote);
        assertEquals(expResult, result);
    }

    /**
     * Test of isAllowed method, of class networkAddressRange.
     */
    @Test
    public void testIsAllowed() {
        System.out.println("isAllowed");
        int[] address = { 192, 168, 1, 1 };
        boolean isSecured = true;
        networkAddressRange instance = null;
        try {
            instance = new networkAddressRange(new int[] { 192, 168, 1, 2 }, new int[] { 255, 255, 255, 0 }, networkAddressRange.DENY_ACCESS);
        } catch (Exception ex) {
            Logger.getLogger(networkAddressRangeTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        int expResult = -1;
        int result = instance.isAllowed(address, isSecured);
        assertEquals(expResult, result);
    }

    /**
     * Test of getNetworkForHumans method, of class networkAddressRange.
     */
    @Test
    public void testGetNetworkForHumans() {
        System.out.println("getNetworkForHumans");
        networkAddressRange instance = null;
        try {
            instance = new networkAddressRange(new int[] { 192, 168, 1, 1 }, new int[] { 255, 255, 255, 0 }, networkAddressRange.ALLOW_ANY);
        } catch (Exception ex) {
            Logger.getLogger(networkAddressRangeTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        String expResult = "192.168.1.1";
        String result = instance.getNetworkForHumans();
        assertEquals(expResult, result);
    }

    /**
     * Test of getShortMask method, of class networkAddressRange.
     */
    @Test
    public void testGetShortMask() {
        System.out.println("getShortMask");
        networkAddressRange instance = null;
        try {
            instance = new networkAddressRange(new int[] { 192, 168, 1, 1 }, new int[] { 255, 255, 255, 0 }, networkAddressRange.ALLOW_ANY);
        } catch (Exception ex) {
            Logger.getLogger(networkAddressRangeTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        int expResult = 24;
        int result = instance.getShortMask();
        assertEquals(expResult, result);
    }
}
