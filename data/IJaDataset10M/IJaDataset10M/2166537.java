package org.formproc.validation;

import java.util.Locale;
import org.formproc.FormData;
import org.formproc.FormElement;

/**
 * Class which represents a form validation results.
 *
 * @author Anthony Eden
 */
public class ValidationResult {

    protected FormElement[] formElements;

    protected FormData[] formData;

    protected Locale locale;

    protected Validator validator;

    protected boolean valid;

    protected Throwable error;

    /**
     * Construct a new ValidationResult object.  This constructor will set the valid flag to true.
     *
     * @param formElements The FormElements which were validated
     * @param formData The FormData array
     * @param locale The Locale used at the time of validation
     */
    public ValidationResult(FormElement[] formElements, FormData[] formData, Locale locale) {
        this(formElements, formData, locale, null, true, null);
    }

    /**
     * Construct a new ValidationResult object.  This constructor will set the valid flag to false.
     *
     * @param formElements The FormElements which were validated
     * @param formData The FormData array
     * @param locale The Locale used at the time of validation
     * @param validator The validator which caused the error
     */
    public ValidationResult(FormElement[] formElements, FormData[] formData, Locale locale, Validator validator) {
        this(formElements, formData, locale, validator, false, null);
    }

    /**
     * Construct a new ValidationResult object.  This constructor will set the valid flag to false.
     *
     * @param formElements The FormElements which were validated
     * @param formData The FormData array
     * @param locale The Locale used at the time of validation
     * @param validator The validator which caused the error
     * @param error The Throwable
     */
    public ValidationResult(FormElement[] formElements, FormData[] formData, Locale locale, Validator validator, Throwable error) {
        this(formElements, formData, locale, validator, false, error);
    }

    /**
     * Construct a ValidationResult object.
     *
     * @param formElements The FormElements which were validated
     * @param formData The FormData array
     * @param locale The Locale used at the time of validation
     * @param validator The Validator which caused the error (or null)
     * @param valid True if the field was valid
     * @param error The error object (or null)
     */
    protected ValidationResult(FormElement[] formElements, FormData[] formData, Locale locale, Validator validator, boolean valid, Throwable error) {
        this.formElements = formElements;
        this.formData = formData;
        this.locale = locale;
        this.validator = validator;
        this.valid = valid;
        this.error = error;
    }

    /**
     * Return true all fields are valid.
     *
     * @return True if the field is valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Return the error message from the validator which was in error using the default Locale.
     *
     * @return The error message
     */
    public String getErrorMessage() {
        Locale messageLocale = getLocale();
        if (messageLocale == null) {
            messageLocale = Locale.getDefault();
        }
        return getErrorMessage(messageLocale);
    }

    /**
     * Return the error message from the validator which was in error using the given Locale.
     *
     * @param locale The Locale
     * @return The error message
     */
    public String getErrorMessage(Locale locale) {
        return validator.getErrorMessage(locale);
    }

    /**
     * Get the array of validated elements.
     *
     * @return The array of formElements
     */
    public FormElement[] getFormElements() {
        return formElements;
    }

    /**
     * Get the validated FormData array.
     *
     * @return The FormData array
     */
    public FormData[] getFormData() {
        return formData;
    }

    /**
     * Get the Locale which was specified at validation time.
     *
     * @return The Locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Return the Throwable error or null.
     *
     * @return The Throwable error
     */
    public Throwable getError() {
        return error;
    }

    /**
     * Return the validator which is in error.   This method must return null if the validation was successful.
     *
     * @return The validator in error or null
     */
    public Validator getValidator() {
        return validator;
    }
}
