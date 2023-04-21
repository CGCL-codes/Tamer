package org.ikasan.framework.component.transformation;

import junit.framework.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Test class for ExceptionThrowingErrorListener
 * 
 * @author Ikasan Development Team
 * 
 */
public class ExceptionThrowingErrorHandlerTest {

    /**
     * An exception to handle
     */
    private SAXParseException saxParseException = new SAXParseException("saxParseException", null);

    /**
     * Tests that with default constructor arguments, error method rethrows same
     * exception
     */
    @Test
    public void testError_WithDefautSettings() {
        ExceptionThrowingErrorHandler errorHandler = new ExceptionThrowingErrorHandler();
        SAXException thrownException = null;
        try {
            errorHandler.error(saxParseException);
            Assert.fail("Exception should have been thrown");
        } catch (SAXException e) {
            thrownException = e;
        }
        Assert.assertEquals("Thrown exception should have been the exception passed to listener", saxParseException, thrownException);
    }

    /**
     * Tests that with default constructor arguments, fatalError method rethrows
     * same exception
     */
    @Test
    public void testFatalError_WithDefautSettings() {
        ExceptionThrowingErrorHandler errorHandler = new ExceptionThrowingErrorHandler();
        SAXException thrownException = null;
        try {
            errorHandler.fatalError(saxParseException);
            Assert.fail("Exception should have been thrown");
        } catch (SAXException e) {
            thrownException = e;
        }
        Assert.assertEquals("Thrown exception should have been the exception passed to listener", saxParseException, thrownException);
    }

    /**
     * Tests that with default constructor arguments, warning method rethrows
     * same exception
     */
    @Test
    public void testWarning_WithDefautSettings() {
        ExceptionThrowingErrorHandler errorHandler = new ExceptionThrowingErrorHandler();
        SAXException thrownException = null;
        try {
            errorHandler.warning(saxParseException);
            Assert.fail("Exception should have been thrown");
        } catch (SAXException e) {
            thrownException = e;
        }
        Assert.assertEquals("Thrown exception should have been the exception passed to listener", saxParseException, thrownException);
    }

    /**
     * Tests that with constructor argument set to false, error method does not
     * rethrow exception
     * @throws SAXException -
     */
    @Test
    public void testError_WithRethrowingDisabled() throws SAXException {
        ExceptionThrowingErrorHandler errorHandler = new ExceptionThrowingErrorHandler(false, true, true);
        errorHandler.error(saxParseException);
    }

    /**
     * Tests that with constructor argument set to false, fatalError method does
     * not rethrow exception
     * 
     * @throws SAXException - 
     */
    @Test
    public void testFatalError_WithRethrowingDisabled() throws SAXException {
        ExceptionThrowingErrorHandler errorHandler = new ExceptionThrowingErrorHandler(true, false, true);
        errorHandler.fatalError(saxParseException);
    }

    /**
     * Tests that with constructor argument set to false, fatalError method does
     * not rethrow exception
     * 
     * @throws SAXException - 
     */
    @Test
    public void testWarning_WithRethrowingDisabled() throws SAXException {
        ExceptionThrowingErrorHandler errorHandler = new ExceptionThrowingErrorHandler(true, true, false);
        errorHandler.warning(saxParseException);
    }
}
