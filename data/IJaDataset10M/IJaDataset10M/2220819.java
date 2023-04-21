package net.sf.echobinding.validation;

import junit.framework.TestCase;
import net.sf.echobinding.binding.OgnlPropertyAdapter;
import net.sf.echobinding.binding.PropertyAdapter;

/**
 *
 */
public class _ValidatorsTest extends TestCase {

    /**
	 * Test method for 'echobinding.validation.Validator.validate(Object, Object)'
	 */
    public void testValidate() {
        String regexPattern = "^\\w*(?=\\w*\\d)(?=\\w*[a-z])(?=\\w*[A-Z])\\w*$";
        Validator validator = new RegexValidator(regexPattern);
        assertFalse("(A1.1)", validator.validate(this, "abadpassword").isValid());
        assertTrue("(A1.2)", validator.validate(this, "aGood8Pwd").isValid());
        assertTrue("(A2)", validator.validate(this, null).isValid());
        ValidationReport vReport = validator.validate(this, "abadpassword");
        assertNotNull("(A3.1)", vReport);
        assertFalse("(A3.2)", vReport.isValid());
        assertNotNull("A3.3", vReport.getMessage());
        PropertyAdapter pwdAdapter = new OgnlPropertyAdapter("password");
        pwdAdapter.addValidator(validator);
        assertFalse("(A4)", pwdAdapter.validate(this, "abadpassword").isValid());
        assertTrue("(A5)", pwdAdapter.validate(this, "aGood8pwd").isValid());
    }
}
