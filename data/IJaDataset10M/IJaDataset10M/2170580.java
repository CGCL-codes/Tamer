package EcoSpeed;

import java.util.Observable;
import java.util.Observer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author julien.sutter
 */
public class AlarmeTest {

    public class TestObserver implements Observer {

        private boolean statut = false;

        public boolean getStatut() {
            return statut;
        }

        public void update(Observable arg0, Object arg1) {
            statut = true;
        }
    }

    public AlarmeTest() {
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
     * Test of setStatut method, of class Alarme.
     */
    @Test
    public void testSetStatut() {
        System.out.println("setStatut");
        Alarme instance = new Alarme();
        assertEquals(false, instance.getStatut());
        instance.setStatut(true);
        assertEquals(true, instance.getStatut());
    }

    /**
     * Test of getStatut method, of class Alarme.
     */
    @Test
    public void testGetStatut() {
        System.out.println("getStatut");
        Alarme instance = new Alarme();
        assertEquals(false, instance.getStatut());
        instance.setStatut(true);
        assertEquals(true, instance.getStatut());
    }

    /**
     * Test of enable method, of class Alarme.
     */
    @Test
    public void testEnable() {
        System.out.println("enable");
        Alarme instance = new Alarme();
        TestObserver testObserver = new TestObserver();
        instance.addObserver(testObserver);
        instance.enable();
        boolean testStatut = instance.getStatut();
        boolean testNotify = testObserver.getStatut();
        assertTrue(testStatut);
        assertTrue(testNotify);
    }

    /**
     * Test of disable method, of class Alarme.
     */
    @Test
    public void testDisable() {
        System.out.println("disable");
        Alarme instance = new Alarme();
        TestObserver testObserver = new TestObserver();
        instance.addObserver(testObserver);
        instance.disable();
        boolean testStatut = instance.getStatut();
        boolean testNotify = testObserver.getStatut();
        assertFalse(testStatut);
        assertTrue(testNotify);
    }
}
