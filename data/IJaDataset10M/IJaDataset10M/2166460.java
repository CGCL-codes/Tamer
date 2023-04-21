package com.seovic.validation;

import static com.seovic.validation.HelperClasses.*;
import com.seovic.util.Condition;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests for {@link AnyValidatorGroup}
 * 
 * @author ic  2009.06.08
 */
public class AnyValidatorGroupTests {

    @Test
    public void defaultsToFastValidate() {
        AnyValidatorGroup vg = new AnyValidatorGroup();
        assertTrue(vg.isFastValidate());
    }

    @Test
    public void whenAllValidatorsReturnFalse() {
        ValidatorGroup vg = new AnyValidatorGroup().add(new FalseValidator()).add(new FalseValidator()).add(new FalseValidator());
        ValidationErrors errors = new DefaultValidationErrors();
        errors.addError("existingErrors", new ErrorMessage("error", null));
        boolean valid = vg.validate(new Object(), errors);
        assertFalse("Validation should fail when all inner validators return false.", valid);
        assertEquals(3, errors.getErrors("errors").size());
        assertEquals(1, errors.getErrors("existingErrors").size());
    }

    @Test
    public void whenAllValidatorsReturnTrue() {
        ValidatorGroup vg = new AnyValidatorGroup().add(new TrueValidator()).add(new TrueValidator()).add(new TrueValidator());
        ValidationErrors errors = new DefaultValidationErrors();
        errors.addError("existingErrors", new ErrorMessage("error", null));
        boolean valid = vg.validate(new Object(), errors);
        assertTrue("Validation should succeed when all inner validators return true.", valid);
        assertEquals(0, errors.getErrors("errors").size());
        assertEquals(1, errors.getErrors("existingErrors").size());
    }

    @Test
    public void whenSingleValidatorReturnsTrueFastAndFastValidateIsTrue() {
        AnyValidatorGroup vg = new AnyValidatorGroup();
        vg.setFastValidate(true);
        whenSingleValidatorReturnsTrue(vg);
        assertTrue(((BaseTestValidator) vg.getValidators().get(0)).wasCalled());
        assertTrue(((BaseTestValidator) vg.getValidators().get(1)).wasCalled());
        assertFalse(((BaseTestValidator) vg.getValidators().get(2)).wasCalled());
    }

    @Test
    public void whenSingleValidatorReturnsTrueAndFastValidateIsFalse() {
        AnyValidatorGroup vg = new AnyValidatorGroup();
        vg.setFastValidate(false);
        whenSingleValidatorReturnsTrue(vg);
        assertTrue(((BaseTestValidator) vg.getValidators().get(0)).wasCalled());
        assertTrue(((BaseTestValidator) vg.getValidators().get(1)).wasCalled());
        assertTrue(((BaseTestValidator) vg.getValidators().get(2)).wasCalled());
    }

    @Test
    public void whenGroupIsNotValidatedBecauseWhenExpressionReturnsFalse() {
        ValidatorGroup vg = new AnyValidatorGroup().when(Condition.FALSE).add(new FalseValidator()).add(new FalseValidator());
        ValidationErrors errors = new DefaultValidationErrors();
        errors.addError("existingErrors", new ErrorMessage("error", null));
        boolean valid = vg.validate(new Object(), errors);
        assertTrue("Validation should succeed when group validator is not evaluated.", valid);
        assertEquals(0, errors.getErrors("errors").size());
        assertEquals(1, errors.getErrors("existingErrors").size());
    }

    private static void whenSingleValidatorReturnsTrue(AnyValidatorGroup vg) {
        vg.add(new FalseValidator()).add(new TrueValidator()).add(new FalseValidator());
        ValidationErrors errors = new DefaultValidationErrors();
        errors.addError("existingErrors", new ErrorMessage("error", null));
        boolean valid = vg.validate(new Object(), errors);
        assertTrue("Validation should succeed when single inner validator returns true.", valid);
        assertEquals(0, errors.getErrors("errors").size());
        assertEquals(1, errors.getErrors("existingErrors").size());
    }
}
