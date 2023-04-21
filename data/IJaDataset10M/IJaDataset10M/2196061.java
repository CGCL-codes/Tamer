package android.test.suitebuilder.examples.smoke;

import android.test.suitebuilder.annotation.Smoke;
import junit.framework.TestCase;

@Smoke
public class SmokeTest extends TestCase {

    public void testSmoke() throws Exception {
        assertTrue(true);
    }
}
