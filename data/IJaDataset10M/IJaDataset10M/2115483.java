package oss.jthinker.views;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit-tests for MasterView class.
 *  
 * @author iappel
 */
public class MasterViewTest {

    public MasterViewTest() {
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
     * Test of invoking saveCurrent() on empty view.
     */
    @Test
    public void emptyViewSave() {
        MasterView master = new MasterView(null);
        master.saveCurrent(false);
    }
}
