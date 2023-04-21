package date.factoryMonth.factoryJulianGregorianMonth;

import date.month.julianGregorian.November;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ACER 3614
 */
public class FactoryNovemberTest {

    FactoryNovember f;

    public FactoryNovemberTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        f = new FactoryNovember();
    }

    @After
    public void tearDown() {
        f = null;
    }

    /**
     * Test of createMonth method, of class FactoryAugust.
     */
    @Test
    public void testCreateMonth() {
        System.out.println("createMonth");
        assertEquals(f.createMonth(), November.instance());
    }
}
