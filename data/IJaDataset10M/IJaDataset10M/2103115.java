package javax.validation;

import java.lang.annotation.Annotation;

/**
 * Define the logic to validate a given constraint
 * 
 */
public interface Constraint<A extends Annotation> {

    /**
	 * Initialize the constraint validator.
	 * 
	 * This method is guaranteed to be called once right after the constraint is
	 * retrieved from the <code>ConstraintFactory</code> and before the Bean
	 * Validation provider starts using it.
	 * 
	 * @param constraintAnnotation
	 *            The constraint declaration
	 */
    void initialize(A constraintAnnotation);

    /**
	 * Evaluates the constraint against a value. This method must be thread
	 * safe.
	 * 
	 * @param value
	 *            The object to validate
	 * @return false if the value is not valid, true otherwise
	 * @exception IllegalArgumentException
	 *                The value's type isn't understood by the constraint
	 *                validator
	 */
    boolean isValid(Object value);
}
