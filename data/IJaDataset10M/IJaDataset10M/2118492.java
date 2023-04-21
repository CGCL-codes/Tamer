package test.com.ivis.xprocess.framework;

import java.util.logging.Level;
import java.util.logging.Logger;
import test.com.ivis.xprocess.XprocessTest;

public class TestLogging extends XprocessTest {

    private static final Logger logger = Logger.getLogger(TestLogging.class.getName());

    public void testLogging() throws Exception {
        logger.log(Level.SEVERE, "First try");
        logger.log(Level.INFO, "Second try");
        Exception re = new Exception("Ooops");
        logger.log(Level.FINER, "Just about to throw");
        logger.throwing(this.getClass().getName(), "testLogging", re);
        try {
            throw re;
        } catch (Exception e) {
        }
        assertTrue("no tests here, just examples", true);
    }

    @Override
    public String getTestRootDir() {
        return getClass().getSimpleName();
    }
}
