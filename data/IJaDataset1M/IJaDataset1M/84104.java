package my.jutils.string;

import junit.framework.TestCase;

public class HumanReadableFormatTest extends TestCase {

    private HumanReadableFormat hrf;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        hrf = new HumanReadableFormat();
    }

    public void testFormat() {
        assertEquals("   0.00B", hrf.format(0));
        assertEquals("1023.00B", hrf.format(1023));
        assertEquals("   1.00K", hrf.format(1024));
        assertEquals("   1.00K", hrf.format(1025));
    }
}
