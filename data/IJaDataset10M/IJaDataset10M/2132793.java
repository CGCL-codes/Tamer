package org.openwms.core.test;

import org.junit.After;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An AbstractMockitoTests can be subclasses to initial Mockito on each test
 * run.
 * 
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version $Revision: $
 * @since 0.1
 */
public abstract class AbstractMockitoTests {

    /**
     * Logger instance can be used by subclasses.
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Setting up some test data.
     */
    @Before
    public void onSuperBefore() {
        doBefore();
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Clean up, clear lists.
     */
    @After
    public void onSuperAfter() {
        doAfter();
    }

    /**
     * Do something before the mock objects are initialized.
     */
    public void doBefore() {
    }

    /**
     * Do something after each test run.
     */
    public void doAfter() {
    }
}
