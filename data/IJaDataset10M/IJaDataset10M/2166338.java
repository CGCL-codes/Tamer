package DBTables;

import java.sql.PreparedStatement;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Иван
 */
public class TTreeOfFileStructureTest {

    public TTreeOfFileStructureTest() {
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
     * Test of SetValues method, of class TTreeOfFileStructure.
     */
    @Test
    public void testSetValues() throws Exception {
        System.out.println("SetValues");
        PreparedStatement prepareStatement = null;
        TTreeOfFileStructure instance = null;
        instance.SetValues(prepareStatement);
        fail("The test case is a prototype.");
    }
}
