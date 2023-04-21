package org.piccolo2d.extras.nodes;

import org.piccolo2d.extras.nodes.PLens;
import junit.framework.TestCase;

/**
 * Unit test for PLens.
 */
public class PLensTest extends TestCase {

    public void testClone() {
        PLens lens = new PLens();
        assertTrue(lens.getInputEventListeners().length > 0);
        PLens cloned = (PLens) lens.clone();
        assertNotNull(cloned);
    }
}
