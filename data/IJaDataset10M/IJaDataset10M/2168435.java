package system;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Micke
 */
public class MovieTest {

    public MovieTest() {
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
     * Test of getTitle method, of class Movie.
     */
    @Test
    public void testGetTitle() {
        System.out.println("getTitle");
        Movie instance = new Movie(1, "Up", 2009, 96, 54677, "hej", "hej", "hej", "hej", "hej", "hej", "hej");
        String expResult = "Up";
        String result = instance.getTitle();
        assertEquals(expResult, result);
    }

    public void testSearchOnYouTube() {
        System.out.println("searchOnYouTube");
        Movie instance = new Movie(1, "Up", 2009, 96, 54677, "hej", "hej", "hej", "hej", "hej", "hej", "hej");
        instance.searchOnYouTube();
    }

    public void testSearchImgOnGoogle() {
        System.out.println("searchImgOnGoogle");
        Movie instance = new Movie(1, "Up and away", 2009, 96, 54677, "hej", "hej", "hej", "hej", "hej", "hej", "hej");
        instance.searchImgOnGoogle();
    }
}
