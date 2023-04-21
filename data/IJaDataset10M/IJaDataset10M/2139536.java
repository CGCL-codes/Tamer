package org.radeox.filter;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.radeox.filter.NewlineFilter;

public class NewlineFilterTest extends FilterTestSupport {

    protected void setUp() throws Exception {
        filter = new NewlineFilter();
        super.setUp();
    }

    public static Test suite() {
        return new TestSuite(NewlineFilterTest.class);
    }

    public void testNewline() {
        assertEquals("Test<br/>Text", filter.filter("Test\\\\Text", context));
    }
}
