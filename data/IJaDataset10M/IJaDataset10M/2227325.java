package hojadetrabajo1;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author JuanFer
 */
public class CarRadioTest {

    public CarRadioTest() {
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
     * Test of energia method, of class CarRadio.
     */
    @Test
    public void testEnergia() {
        System.out.println("energia");
        CarRadio instance = new CarRadio(1);
        instance.energia();
        boolean expResult = true;
        boolean result = instance.estaEncendido();
        assertEquals(expResult, result);
    }

    /**
     * Test of estaEncendido method, of class CarRadio.
     */
    @Test
    public void testEstaEncendido() {
        System.out.println("estaEncendido");
        CarRadio instance = new CarRadio(1);
        boolean expResult = false;
        boolean result = instance.estaEncendido();
        assertEquals(expResult, result);
    }
}
