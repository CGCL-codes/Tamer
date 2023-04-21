package net.sf.springlayout.web.validator;

import junit.framework.TestCase;
import net.sf.springlayout.model.MockModelObject;
import net.sf.springlayout.web.validator.MinValueValidator;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

/**
 * @author adam boas
 */
public class MinValueValidatorTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFailingValidation() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockModelObject mockObject = new MockModelObject();
        mockObject.setTestIntegerField(new Integer(100));
        Errors errors = new BindException(mockObject, "commandName");
        MinValueValidator validator = new MinValueValidator(101);
        validator.validate(request, mockObject, errors, "testIntegerField");
        assertEquals(1, errors.getErrorCount());
    }

    public void testFailingWithNonNumberValidation() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockModelObject mockObject = new MockModelObject();
        mockObject.setTestStringField("test");
        Errors errors = new BindException(mockObject, "commandName");
        MinValueValidator validator = new MinValueValidator(99);
        validator.validate(request, mockObject, errors, "testStringField");
        assertEquals(1, errors.getErrorCount());
    }

    public void testPassingValidation() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockModelObject mockObject = new MockModelObject();
        mockObject.setTestIntegerField(new Integer(3));
        Errors errors = new BindException(mockObject, "commandName");
        MinValueValidator validator = new MinValueValidator();
        validator.setMinValue(2);
        validator.validate(request, mockObject, errors, "testIntegerField");
        assertEquals(0, errors.getErrorCount());
    }

    public void testValidationWithNullPath() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockModelObject mockObject = new MockModelObject();
        Errors errors = new BindException(mockObject, "commandName");
        MinValueValidator validator = new MinValueValidator(10);
        validator.validate(request, mockObject, errors, "testIntegerField");
        assertEquals(0, errors.getErrorCount());
    }
}
