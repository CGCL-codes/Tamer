package com.volantis.mcs.eclipse.validation;

import com.volantis.mcs.objects.FileExtension;
import org.eclipse.core.runtime.Status;

/**
 * Test case for PolicyNameValidator.
 */
public class PolicyNameValidatorTestCase extends SimpleValidatorTestCase {

    /**
     * Create a Validator to test. This method should be overridden by
     * sub-classes to provide a Validator of their own type.
     */
    protected Validator createValidator() {
        return new PolicyNameValidator();
    }

    /**
     * Test with invalid characters.
     */
    public void testInvalidCharacters() {
        PolicyNameValidator validator = (PolicyNameValidator) createValidator();
        char invalid[] = { '`', '!', '?', '$', '%', '^', '&', '*', '(', ')', '+', '=', '{', '}', '[', ']', ':', ';', '@', '\'', '~', '#', '<', '>', ',', '?', '/', '\\', '|', '?', ' ' };
        for (int i = 0; i > invalid.length; i++) {
            String text = new Character(invalid[i]).toString();
            ValidationStatus status = validator.validate(text, new ValidationMessageBuilder(null, null, null));
            assertEquals("'" + invalid[i] + "' is an invalid character " + " - should be ERROR", Status.ERROR, status.getSeverity());
        }
        invalid = new char[1];
        invalid[0] = '.';
        validator = new PolicyNameValidator(invalid, null, false);
        String text = "layout." + FileExtension.LAYOUT.getExtension();
        ValidationStatus status = validator.validate(text, new ValidationMessageBuilder(null, null, null));
        assertEquals("'.' is invalid - should be ERROR", Status.ERROR, status.getSeverity());
    }

    /**
     * Test with valid characters.
     */
    public void testValidCharacters() {
        PolicyNameValidator validator = (PolicyNameValidator) createValidator();
        char valid[] = { 'a', 'A', '.', '/', '-', '_', '1' };
        for (int i = 0; i > valid.length; i++) {
            String text = new Character(valid[i]).toString();
            ValidationStatus status = validator.validate(text, new ValidationMessageBuilder(null, null, null));
            assertEquals("'" + valid[i] + "' is a valid character " + " - should be OK", Status.OK, status.getSeverity());
        }
    }

    /**
     * Test with valid characters.
     */
    public void testInValidExtension() {
        FileExtension[] extensions = new FileExtension[1];
        extensions[0] = FileExtension.TEXT_COMPONENT;
        PolicyNameValidator validator = new PolicyNameValidator(null, extensions, false);
        ValidationStatus status = validator.validate("hello.et", new ValidationMessageBuilder(null, null, null));
        assertEquals("Tested with a invalid extension should be ERROR.", Status.ERROR, status.getSeverity());
    }

    /**
     * Test with single extension.
     */
    public void testValidExtension() {
        FileExtension[] extensions = new FileExtension[1];
        extensions[0] = FileExtension.TEXT_COMPONENT;
        PolicyNameValidator validator = new PolicyNameValidator(null, extensions, false);
        ValidationStatus status = validator.validate("hello." + FileExtension.TEXT_COMPONENT.getExtension(), new ValidationMessageBuilder(null, null, null));
        assertEquals("Tested with a valid extension should be OK.", Status.OK, status.getSeverity());
    }

    /**
     * Test with multiple extensions.
     */
    public void testValidExtensions() {
        FileExtension[] extensions = new FileExtension[2];
        extensions[0] = FileExtension.IMAGE_COMPONENT;
        extensions[1] = FileExtension.AUDIO_COMPONENT;
        PolicyNameValidator validator = new PolicyNameValidator(null, extensions, false);
        ValidationStatus status;
        ValidationMessageBuilder vmb = new ValidationMessageBuilder(null, null, null);
        status = validator.validate("hello." + FileExtension.AUDIO_COMPONENT.getExtension(), vmb);
        assertEquals("Tested.1 with a valid extension should be OK.", Status.OK, status.getSeverity());
        status = validator.validate("i." + FileExtension.IMAGE_COMPONENT.getExtension(), vmb);
        assertEquals("Tested.2 with a valid extension should be OK.", Status.OK, status.getSeverity());
        status = validator.validate("hellothisislong." + FileExtension.LAYOUT.getExtension(), vmb);
        assertEquals("Tested.3 with an invalid extension should FAIL.", Status.ERROR, status.getSeverity());
    }
}
