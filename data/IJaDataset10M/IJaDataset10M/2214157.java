package org.hibernate.jsr303.tck.tests.validation;

/**
 * Test wrapper for {@link ValidateValueTest}.
 */
public class ValidateValueGwtTest extends AbstractValidationTest {

    private final ValidateValueTest delegate = new ValidateValueTest();

    public void testExistingPropertyWoConstraintsNorCascaded() {
        delegate.testExistingPropertyWoConstraintsNorCascaded();
    }

    public void testValidateValue() {
        delegate.testValidateValue();
    }

    public void testValidateValueFailure() {
        delegate.testValidateValueFailure();
    }

    public void testValidateValuePassingNullAsGroup() {
        delegate.testValidateValuePassingNullAsGroup();
    }

    public void testValidateValueSuccess() {
        delegate.testValidateValueSuccess();
    }

    public void testValidateValueWithEmptyPropertyPath() {
        delegate.testValidateValueWithEmptyPropertyPath();
    }

    public void testValidateValueWithInvalidPropertyPath() {
        delegate.testValidateValueWithInvalidPropertyPath();
    }

    public void testValidateValueWithNullObject() {
        delegate.testValidateValueWithNullObject();
    }

    public void testValidateValueWithNullPropertyName() {
        delegate.testValidateValueWithNullPropertyName();
    }

    public void testValidIsNotHonoredValidateValue() {
        delegate.testValidIsNotHonoredValidateValue();
    }
}
