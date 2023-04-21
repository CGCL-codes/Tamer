package ch.bbv.dog.validation;

import com.jgoodies.validation.ValidationResult;

/**
 * The validation rule defines the interface for all validation formulas. An
 * immediate rule can always also be used in the deferred mode.
 * @author MarcelBaumann
 * @version $Revision: 1.8 $
 */
public interface ValidationRule {

    /**
   * Flag indicating if the validation rule should be applied upon change of the
   * property or can be applied later.
   * @return true if the rule is immediate otherwise false
   */
    boolean isImmediate();

    /**
   * Executes the validation rule on a Java bean.
   * @param bean bean on which to process the rule
   * @param results list where rule results can be added. The result is a
   *          validation rule result
   * @return true if the rule did not found any errors otherwise false
   * @pre (bean != null) && (results != null)
   */
    boolean execute(Object bean, ValidationResult results);

    /**
   * Executes the validation rule on a Java bean.
   * @param bean bean on which to process the rule
   * @param property property to validate
   * @param results list where rule results can be added. The result is a
   *          validation rule result
   * @return true if the rule did not found any errors otherwise false
   * @pre (bean != null) && (property != null) && (results != null)
   */
    boolean execute(Object bean, String property, ValidationResult results);

    /**
   * Executes the validation rule on a Java bean in an immediate mode.
   * @param bean bean on which to process the rule.
   * @param property name of the modified property.
   * @param oldValue old value of the property if defined.
   * @param newValue new value of the property.
   * @param results list where rule results can be added. The result is a
   *          validation rule result.
   * @return true if the rule did not found any errors otherwise false.
   * @pre (bean != null) && (property != null) && (results != null)
   */
    boolean execute(Object bean, String property, Object oldValue, Object newValue, ValidationResult results);

    /**
   * Indicates to a validation rule that validation of the model or part of the
   * model will start now. This can be used for preprocessing validation.
   * @param results list where rule results can be added. The result is a
   *          validation rule result
   */
    void validationStart(ValidationResult results);

    /**
   * Indicates to a validation rule that validation of the model or part of the
   * model has ended now. This can be used for postprocessing validation
   * results.
   * @param results list where rule results can be added. The result is a
   *          validation rule result
   */
    void validationEnd(ValidationResult results);
}
