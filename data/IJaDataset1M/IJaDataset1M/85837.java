package net.sf.springlayout.web.validator;

import net.sf.springlayout.AbstractTestBase;
import net.sf.springlayout.model.MockModelObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

/**
 * @author Rob Monie
 */
public class DecimalValidatorTest extends AbstractTestBase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFailingValidationWithoutSettingDecimalPlaces() {
        DecimalValidator decimalValidator = new DecimalValidator();
        MockHttpServletRequest request = this.createMockHttpServletRequest("POST", "");
        MockModelObject mockObject = new MockModelObject();
        mockObject.setTestDoubleField(new Double(1.1));
        Errors errors = new BindException(mockObject, "mockObject");
        decimalValidator.validate(request, mockObject, errors, "testDoubleField");
        assertTrue(errors.hasErrors());
    }

    public void testPassingValidationWithSettingDecimalPlaces() {
        DecimalValidator decimalValidator = new DecimalValidator();
        decimalValidator.setMaxDecimalPlaces(2);
        MockHttpServletRequest request = this.createMockHttpServletRequest("POST", "");
        MockModelObject mockObject = new MockModelObject();
        mockObject.setTestDoubleField(new Double(1.11));
        Errors errors = new BindException(mockObject, "mockObject");
        decimalValidator.validate(request, mockObject, errors, "testDoubleField");
        assertFalse(errors.hasErrors());
    }

    public void testFailingValidationExceedingDecimalPlaces() {
        DecimalValidator decimalValidator = new DecimalValidator();
        decimalValidator.setMaxDecimalPlaces(2);
        MockHttpServletRequest request = this.createMockHttpServletRequest("POST", "");
        MockModelObject mockObject = new MockModelObject();
        mockObject.setTestDoubleField(new Double(1.111));
        Errors errors = new BindException(mockObject, "mockObject");
        decimalValidator.validate(request, mockObject, errors, "testDoubleField");
        assertTrue(errors.hasErrors());
    }

    public void testValidationJavascript() {
        DecimalValidator decimalValidator = new DecimalValidator();
        decimalValidator.setMaxDecimalPlaces(2);
        assertTrue(StringUtils.contains(decimalValidator.getJavascript(), "validateDecimal(field,2,alerts)"));
    }
}
