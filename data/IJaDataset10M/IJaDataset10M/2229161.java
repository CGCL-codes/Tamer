package net.sf.springlayout.web.validator;

import junit.framework.TestCase;
import net.sf.springlayout.model.MockModelObject;
import net.sf.springlayout.web.validator.RegularExpressionValidator;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

/**
 * @author adam boas
 */
public class RegularExpressionValidatorTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFailingValidation() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockModelObject mockObject = new MockModelObject();
        mockObject.setTestStringFieldClean("xyz");
        Errors errors = new BindException(mockObject, "commandName");
        RegularExpressionValidator validator = new RegularExpressionValidator();
        validator.setRegularExpression("^abc$");
        validator.validate(request, mockObject, errors, "testStringFieldClean");
        assertEquals(1, errors.getErrorCount());
    }

    public void testPassingValidation() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockModelObject mockObject = new MockModelObject();
        mockObject.setTestStringFieldClean("abc");
        Errors errors = new BindException(mockObject, "commandName");
        RegularExpressionValidator validator = new RegularExpressionValidator();
        validator.setRegularExpression("^abc$");
        validator.validate(request, mockObject, errors, "testStringFieldClean");
        assertEquals(0, errors.getErrorCount());
    }

    public void testValidationWithNullPath() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockModelObject mockObject = new MockModelObject();
        Errors errors = new BindException(mockObject, "commandName");
        RegularExpressionValidator validator = new RegularExpressionValidator();
        validator.validate(request, mockObject, errors, "testStringFieldClean");
        assertEquals(0, errors.getErrorCount());
    }
}
