package org.ikasan.framework.exception;

import junit.framework.JUnit4TestAdapter;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * This test class supports the <code>IkasanExceptionActionType</code> concrete
 * implementation class.
 * 
 * @author Ikasan Development Team
 */
public class IkasanExceptionActionTypeTest {

    /**
     * The logger instance.
     */
    private static Logger logger = Logger.getLogger(IkasanExceptionActionTypeTest.class);

    /**
     * Setup runs before each test
     */
    @Before
    public void setUp() {
    }

    /**
     * Test param based Constructor
     */
    @Test
    public void testIkasanExceptionActionTypeGetters() {
        IkasanExceptionActionType type = IkasanExceptionActionType.ROLLBACK_RETRY;
        String currentText = "Operation will rollback and retry.";
        String actionTypeText = type.getDescription();
        Assert.assertTrue(actionTypeText.equals(currentText));
    }

    /**
     * Teardown after each test
     */
    @After
    public void tearDown() {
        logger.info("tearDown");
    }

    /**
     * The suite is this class
     * 
     * @return JUnit Test class
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(IkasanExceptionActionTypeTest.class);
    }
}
