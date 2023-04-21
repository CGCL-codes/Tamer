package org.qedeq.kernel.bo.test;

import junit.framework.Test;

/**
 * Run all tests inclusive "pest" methods for the project.
 *
 * @version $Revision: 1.1 $
 * @author Michael Meyling
 */
public class KernelBoTestAllSuite extends KernelBoTestSuite {

    /**
     * Get a new <code>KernelTestSuiteWithPest</code>.
     *
     * @return Test.
     */
    public static Test suite() {
        return new KernelBoTestAllSuite();
    }

    /**
     * Constructor.
     */
    public KernelBoTestAllSuite() {
        super(true, true);
    }
}
