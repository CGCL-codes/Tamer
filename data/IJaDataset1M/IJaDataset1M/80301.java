package experiments;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * Test class used for testing Jumble's use of the <code>suite()</code> method.
 * 
 * @author Tin Pavlinic
 * @version $Revision: 414 $
 */
public class JumblerExperimentSillySuiteTest extends TestCase {

    public static Test suite() {
        return JumblerExperimentTest.suite();
    }
}
