package org.formproc.validation;

import java.util.Locale;
import org.formproc.FormData;
import org.formproc.FormElement;

/**
 * A form validator which allows any incoming data to be passed as valid. In other words, it does no validation.
 *
 * @author Anthony Eden
 */
public class PassValidator extends Validator {

    /**
     * Validate the given FormData.  This implementation will allow all incoming values to validate.
     *
     * @param formElements An array of FormElement objects
     * @param formData An array of FormData objects
     * @param locale The Locale
     * @throws Exception
     */
    public ValidationResult validate(FormElement[] formElements, FormData[] formData, Locale locale) throws Exception {
        return new ValidationResult(formElements, formData, locale);
    }
}
