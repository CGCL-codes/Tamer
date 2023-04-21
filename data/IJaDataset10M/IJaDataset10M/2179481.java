package util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author TecHunter
 */
public class CommonWordsTest {

    public CommonWordsTest() {
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
     * Test of isCommon method, of class CommonWords.
     */
    @Test
    public void testIsCommon() {
        System.out.println("isCommon");
        assertEquals(true, CommonWords.isCommon("en"));
    }
}
