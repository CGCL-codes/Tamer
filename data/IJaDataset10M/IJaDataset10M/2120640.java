package grocerylister;

import junit.framework.*;
import java.util.Comparator;

/**
 *
 * @author STAN SAKL
 */
public class PastPurchaseTest extends TestCase {

    public PastPurchaseTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public void testNonDefaultConstructor() {
        PastPurchase instance = new PastPurchase("item", "desc", "brand", "size");
        assertEquals("item", instance.getItem());
        assertEquals("desc", instance.getDescription());
        assertEquals("brand", instance.getBrand());
        assertEquals("size", instance.getSize());
    }

    public void testGetItem() {
        PastPurchase instance = new PastPurchase();
        assertNotNull(instance.getItem());
    }

    public void testSetItem() {
        PastPurchase instance = new PastPurchase();
        instance.setItem("peanut butter");
        assertEquals("peanut butter", instance.getItem());
    }

    public void testGetDescription() {
        PastPurchase instance = new PastPurchase();
        assertNotNull(instance.getDescription());
    }

    public void testSetDescription() {
        PastPurchase instance = new PastPurchase();
        instance.setDescription("creamy");
        assertEquals("creamy", instance.getDescription());
    }

    public void testGetBrand() {
        PastPurchase instance = new PastPurchase();
        assertNotNull(instance.getDescription());
    }

    public void testSetBrand() {
        PastPurchase instance = new PastPurchase();
        instance.setBrand("brand");
        assertEquals("brand", instance.getBrand());
    }

    public void testGetSize() {
        PastPurchase instance = new PastPurchase();
        assertNotNull(instance.getDescription());
    }

    public void testSetSize() {
        PastPurchase instance = new PastPurchase();
        instance.setSize("size");
        assertEquals("size", instance.getSize());
    }

    /**
     * Test of getId method, of class grocerylister.PastPurchase.
     */
    public void testGetId() {
        System.out.println("getId");
        PastPurchase instance = new PastPurchase();
        instance.setId(42);
        int expResult = 42;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class grocerylister.PastPurchase.
     */
    public void testSetId() {
        System.out.println("setId");
        int id = 99;
        PastPurchase instance = new PastPurchase();
        instance.setId(id);
        assertEquals(id, instance.getId());
    }

    public void testCompareTo() {
        PastPurchase first = new PastPurchase("Peanut Butter", "Creamy", "Skippy", "64 oz.");
        PastPurchase second = new PastPurchase("Peanut Butter", "Creamy", "Skippy", "64 oz.");
        PastPurchase third = new PastPurchase("Peanut Butter", "Chunky", "Skippy", "64 oz.");
        PastPurchase fourth = new PastPurchase("Peanut Butter", "Creamy", "Jif", "64 oz.");
        boolean expected = true;
        assertEquals(0, first.compareTo(second));
        assertTrue(first.compareTo(third) != 0);
        assertTrue(first.compareTo(fourth) != 0);
    }
}
