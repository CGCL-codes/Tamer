package ca.sandstorm.luminance.test.input;

import ca.sandstorm.luminance.input.SingleTouchFilter;
import android.test.AndroidTestCase;

public class SingleTouchFilterTest extends AndroidTestCase {

    SingleTouchFilter _mtf;

    protected void setUp() throws Exception {
        _mtf = new SingleTouchFilter();
        super.setUp();
    }

    public void testMultiTouchFilter() throws Exception {
        assertNotNull(_mtf);
    }
}
