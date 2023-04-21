package com.sadalbari.validator.core.validators;

import java.lang.reflect.Member;
import com.sadalbari.validator.core.PropertyValidationError;
import com.sadalbari.validator.core.ValidationContext;
import com.sadalbari.validator.core.annotation.AfterEqual;

/**
 * Validates that the annotated property is greater than another property
 * value.
 *  
 * @author Marvin van Schalkwyk
 */
public class AfterEqualValidator extends BaseDateComparisonValidator<AfterEqual> {

    public AfterEqualValidator() {
        super();
    }

    public PropertyValidationError[] validate(ValidationContext context, Member annotatedMember, AfterEqual annotation, Object target, Object value) {
        final String otherPropertyName = annotation.value();
        final boolean ignoreTime = annotation.ignoreTime();
        final boolean orNull = annotation.orNull();
        final String messageKey = annotation.message();
        return validate(context, annotatedMember, target, value, otherPropertyName, ignoreTime, orNull, messageKey);
    }

    @Override
    protected boolean doCompare(Object value, Object otherValue, boolean ignoreTime, boolean orNull) {
        return compareDates(value, otherValue, ignoreTime, orNull, -1, (orNull ? 1 : -1)) >= 0;
    }
}
