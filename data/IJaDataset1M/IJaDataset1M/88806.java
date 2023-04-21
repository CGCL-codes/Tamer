package thresholdscheme;

import ThresholdScheme.LagrangeMethod.Joining;
import ThresholdScheme.Shares;
import ThresholdScheme.LagrangeMethod.Sharing;
import java.math.BigInteger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class SharingTest {

    public SharingTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getShares method, of class Sharing.
     */
    @Test
    public void testGetShares() throws Exception {
        System.out.println("getShares");
        String m = "12323249384858459084908885908459048908449999999";
        BigInteger message = new BigInteger(m);
        BigInteger prime = message.nextProbablePrime();
        System.out.println(m.length());
        int threshold = 25;
        int sharesCount = 25;
        Sharing instance = new Sharing();
        Shares expResult = null;
        Shares result = instance.getShares(message, prime, threshold, sharesCount);
        Joining j = new Joining();
        BigInteger b = j.getMessage(result, threshold, prime);
        assertEquals(message, b);
    }
}
