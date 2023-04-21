package org.qedeq.kernel.test;

import junit.framework.Test;
import org.qedeq.base.test.QedeqTestSuite;
import org.qedeq.kernel.dto.module.KernelDtoModuleTestSuite;

/**
 * Run all tests for the project.
 * 
 * @version $Revision: 1.1 $
 * @author Michael Meyling
 */
public class KernelSeTestSuite extends QedeqTestSuite {

    /**
     * Get a new <code>KernelTestSuite</code>.
     * 
     * @return Test.
     */
    public static Test suite() {
        return new KernelSeTestSuite();
    }

    /**
     * Constructor.
     */
    protected KernelSeTestSuite() {
        this(true, false);
    }

    /**
     * Constructor.
     *
     * @param   withTest    Execute test methods.      
     * @param   withPest    Execute pest methods.
     */
    public KernelSeTestSuite(final boolean withTest, final boolean withPest) {
        super(withTest, withPest);
        addTest(KernelDtoModuleTestSuite.suite());
    }
}
