package elements;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class CompositeEventTest {

    public CompositeEventTest() {
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
   * Test of execEvent method, of class CompositeEvent.
   */
    @Test
    public void testExecEvent() {
        System.out.println("execEvent");
        PlayerPortfolio portfolio = null;
        CompositeEvent instance = new CompositeEvent();
        instance.execEvent(portfolio);
        fail("The test case is a prototype.");
    }

    /**
   * Test of addEvent method, of class CompositeEvent.
   */
    @Test
    public void testAddEvent() {
        System.out.println("addEvent");
        BaseEvent event = null;
        CompositeEvent instance = new CompositeEvent();
        instance.addEvent(event);
        fail("The test case is a prototype.");
    }
}
