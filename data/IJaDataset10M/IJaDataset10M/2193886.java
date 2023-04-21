package org.jgap.ext;

import junit.framework.*;

/**
 * Test suite for all tests of package org.jgap.ext
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class AllExtTests extends TestSuite {

    /** String containing the CVS revision. Read out via reflection!*/
    private static final String CVS_REVISION = "$Revision: 1.6 $";

    public static Test suite() {
        TestSuite suite = new TestSuite("AllExtTests");
        return suite;
    }
}
