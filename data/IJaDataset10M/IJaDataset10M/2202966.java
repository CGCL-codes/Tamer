package org.dwgsoftware.raistlin.activation.impl.test;

import org.dwgsoftware.raistlin.activation.impl.test.components.TestService;
import org.dwgsoftware.raistlin.composition.model.DeploymentModel;
import org.dwgsoftware.raistlin.util.exception.ExceptionHelper;

public class CodeSecurityDisabledTestCase extends AbstractTestCase {

    public CodeSecurityDisabledTestCase() {
        this("secure");
    }

    public CodeSecurityDisabledTestCase(String name) {
        super(name, false);
    }

    DeploymentModel m_test;

    /**
    * Setup the model using a source balock in the conf 
    * directory.
    * @exception Exception if things don't work out
    */
    public void setUp() throws Exception {
        super.setUp("secure.xml");
        m_model.assemble();
        m_model.commission();
        m_test = m_model.getModel("/Component1/test");
    }

    public void tearDown() {
        m_model.decommission();
    }

    private TestService getTestService() throws Exception {
        return (TestService) m_test.resolve();
    }

    private void releaseTestService(TestService service) {
        m_test.release(service);
    }

    /**
    * Create, assembly, deploy and decommission the block 
    * defined by getPath().
    */
    public void testCodeSecurity() throws Exception {
        TestService test = getTestService();
        assertNotNull("test", test);
        try {
            test.createDirectory();
        } catch (Throwable e) {
            releaseTestService(test);
            final String error = "CodeSecurityTest primary failure.";
            final String message = ExceptionHelper.packException(error, e, true);
            getLogger().error(message);
            throw new Exception(message);
        }
        try {
            test.deleteDirectory();
        } catch (Throwable e) {
            releaseTestService(test);
            final String error = "CodeSecurityTest secondary failure.";
            final String message = ExceptionHelper.packException(error, e, true);
            getLogger().error(message);
            throw new Exception(message);
        }
        try {
            String ver = test.getJavaVersion();
        } catch (Throwable e) {
            releaseTestService(test);
            final String error = "CodeSecurityTest secondary failure.";
            final String message = ExceptionHelper.packException(error, e, true);
            getLogger().error(message);
            throw new Exception(message);
        }
        try {
            test.setJavaVersion("1.0.2");
        } catch (Throwable e) {
            releaseTestService(test);
            final String error = "CodeSecurityTest primary failure.";
            final String message = ExceptionHelper.packException(error, e, true);
            getLogger().error(message);
            throw new Exception(message);
        }
        releaseTestService(test);
    }
}
