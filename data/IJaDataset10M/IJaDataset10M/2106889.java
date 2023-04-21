package net.sourceforge.annovalidator.validation;

/**
 * Interface for classes that deal with validation errors for specific
 * frameworks, allowing for multiple ways of handling validation results.
 */
public interface ValidationResult {

    /**
     * Records a violation of a validation rule for <code>field</code>.
     * 
     * @param field
     *          The field that has violated the validation rule.
     * @param errorCode
     *          The message source key associated with the violated rule.
     * @param value
     *          The value for <code>fieldName</code> that caused the violation.
     */
    void recordValidationViolation(String field, String errorCode, Object value);
}
