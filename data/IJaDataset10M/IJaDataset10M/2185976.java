package org.dctmutils.common.email.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.dctmutils.common.email.exception.EmailException;
import org.dctmutils.common.exception.DctmUtilsException;
import org.dctmutils.common.test.DctmUtilsTestCase;

/**
 * Unit tests for the DctmUtilsException.
 * 
 * @author <a href="mailto:luther@dctmutils.org">Luther E. Birdzell</a>
 */
public class EmailExceptionTest extends DctmUtilsTestCase {

    /**
     * Set up the TestSuite.
     * 
     * @return a <code>Test</code> value
     */
    public static Test suite() {
        return new TestSuite(EmailExceptionTest.class);
    }

    /**
     * Test DctmUtilsException
     */
    public void testDctmUtilsException() {
        DctmUtilsException mpe = new DctmUtilsException();
        assertNotNull(mpe);
    }

    /**
     * Test EmailException
     */
    public void testEmailExceptionString() {
        String message = "junitTestVar is required";
        EmailException mpe = new EmailException(message);
        assertTrue(mpe.getMessage().equals(message));
    }

    /**
     * Test EmailException
     */
    public void testEmailException() {
        String message = "throwable message";
        Exception e = new Exception(message);
        EmailException mpe = new EmailException(e);
        assertFalse(mpe.getMessage().equals(message));
    }

    /**
     * Test EmailException
     */
    public void testEmailExceptionMessageAndThrowable() {
        String throwableMessage = "throwable message";
        Exception e = new Exception(throwableMessage);
        String myMessage = "my message";
        EmailException mpe = new EmailException(myMessage, e);
        assertTrue(mpe.getMessage().equals(myMessage));
    }
}
