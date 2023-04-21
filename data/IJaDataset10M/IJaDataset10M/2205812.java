package ca.sandstorm.luminance.test.input;

import android.test.AndroidTestCase;
import ca.sandstorm.luminance.input.InputXY;

public class InputXYTest extends AndroidTestCase {

    InputXY _ixy;

    protected void setUp() throws Exception {
        _ixy = new InputXY();
        super.setUp();
    }

    public void testInputXY() throws Exception {
        assertNotNull(_ixy);
    }
}
