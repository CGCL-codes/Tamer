package general;

import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Jort's test klasse.
 * @author Simon Hiemstra
 * @version 2012-01-24
 */
public class Jort {

    @Before
    public void beforeUnitTest() {
    }

    @Test
    public void testSampleMethod() {
    }

    @After
    public void afterUnitTest() {
        fail("Fail.");
    }

    @Ignore
    public void negeerMethode() {
    }
}
