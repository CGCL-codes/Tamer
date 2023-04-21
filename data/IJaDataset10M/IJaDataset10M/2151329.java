package net.sf.staccatocommons.instrument.context.internal;

import javassist.ClassPool;
import net.sf.staccatocommons.testing.junit.jmock.JUnit4MockObjectTestCase;
import org.junit.Before;
import org.slf4j.Logger;

/**
 * @author flbulgarelli
 * 
 */
public abstract class AbstractAnnotationContextUnitTest extends JUnit4MockObjectTestCase {

    private Logger logger;

    private ClassPool pool = ClassPool.getDefault();

    /**
   * @throws java.lang.Exception
   */
    @Before
    public void setUp() throws Exception {
        logger = mock(Logger.class);
    }

    protected Logger getLogger() {
        return logger;
    }

    protected ClassPool getPool() {
        return pool;
    }
}
