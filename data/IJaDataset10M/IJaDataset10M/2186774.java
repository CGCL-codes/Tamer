package com.sadalbari.validator.core.validators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import com.sadalbari.validator.core.PropertyValidationError;
import com.sadalbari.validator.core.Resources;
import com.sadalbari.validator.core.ValidationContext;
import com.sadalbari.validator.core.ValidatorUtils;

/**
 * Base class for numeric comparison validators.
 * 
 * <p>Emits the annotated and other property names, respectively, as
 * message arguments.</p>
 * 
 * @author Marvin van Schalkwyk
 *
 * @param <A> the validator annotation type
 */
public abstract class BaseNumericComparisonValidator<A extends Annotation> extends BaseComparativeValidator<A> {

    public BaseNumericComparisonValidator() {
        super();
    }

    @SuppressWarnings("unchecked")
    protected int compareNumbers(Number value, final Number otherValue, int valueIfNull, int valueIfOtherNull) {
        if (value == null || otherValue == null) {
            return (otherValue == null ? valueIfOtherNull : valueIfNull);
        }
        return ((Comparable) value).compareTo(otherValue);
    }

    /**
     * Returns true if the comparison is valid.
     */
    protected abstract boolean doCompare(Number value, final Number otherValue, final boolean orNull);

    /**
     * Perform the validation of the two numeric types. 
     */
    protected PropertyValidationError[] validate(ValidationContext context, Member annotatedMember, Object target, Number value, final String otherPropertyName, final boolean orNull, final String messageKey) {
        final Number otherValue = (Number) ValidatorUtils.getPropertyValue(target, otherPropertyName);
        if (!doCompare(value, otherValue, orNull)) {
            final String message = Resources.getInstance().formatMessage(messageKey, messageArguments(context, target, annotatedMember, otherPropertyName));
            return new PropertyValidationError[] { new PropertyValidationError(message, target, context.getPropertyName(target, annotatedMember)) };
        }
        return null;
    }
}
