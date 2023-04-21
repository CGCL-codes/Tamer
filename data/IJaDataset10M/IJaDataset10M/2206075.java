package bexee.ant;

/**
 * @version $Revision: 1.1 $, $Date: 2004/12/15 14:18:17 $
 * @author Patric Fornasier
 * @author Pawel Kowalski
 */
public class DeployTaskTest extends BuildFileTest {

    public DeployTaskTest(String name) {
        super(name);
    }

    public void setUp() {
        configureProject("src/test/deployTask.xml");
    }

    /**
     * This test has deliberately been disabled. It has been enabled only during
     * development for debugging purposes, because it has too many dependencies
     * (Admin, Manager, Axis, ...) to be run as a JUnit test.
     */
    public void _testDeploy() {
        executeTarget("deploy");
    }
}
