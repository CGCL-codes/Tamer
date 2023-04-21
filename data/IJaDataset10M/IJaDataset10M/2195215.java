package com.w20e.socrates.data;

import com.w20e.socrates.data.MaxLength;
import junit.framework.TestCase;

/**
 * @author dokter
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestMaxLength extends TestCase {

    private MaxLength max;

    protected void setUp() throws Exception {
        super.setUp();
        this.max = new MaxLength(5);
    }

    public void testEval() {
        assertTrue(this.max.eval(""));
        assertTrue(this.max.eval("xxxx"));
        assertTrue(this.max.eval("xxxxx"));
        assertFalse(this.max.eval("xxxxxx"));
    }
}
