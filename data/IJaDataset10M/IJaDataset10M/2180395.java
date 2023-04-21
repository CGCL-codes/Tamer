package org.hibernate.jsr303.tck.tests.validation.validatorcontext;

import com.google.gwt.junit.client.GWTTestCase;
import org.hibernate.jsr303.tck.util.client.Failing;

/**
 * Test wrapper for {@link ConstraintValidatorContextTest}.
 */
public class ConstraintValidatorContextGwtTest extends GWTTestCase {

    private final ConstraintValidatorContextTest delegate = new ConstraintValidatorContextTest();

    @Override
    public String getModuleName() {
        return "org.hibernate.jsr303.tck.tests.validation.validatorcontext.TckTest";
    }

    public void testDefaultError() {
        delegate.testDefaultError();
    }

    public void testDisableDefaultErrorWithCustomErrorNoSubNode() {
        delegate.testDisableDefaultErrorWithCustomErrorNoSubNode();
    }

    @Failing(issue = 6907)
    public void testDisableDefaultErrorWithCustomErrorWithSubNode() {
        delegate.testDisableDefaultErrorWithCustomErrorWithSubNode();
    }

    public void testDisableDefaultErrorWithoutAddingCustomError() {
        delegate.testDisableDefaultErrorWithoutAddingCustomError();
    }
}
