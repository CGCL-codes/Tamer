package com.googlecode.cannedbeans.generator.core.exceptions;

import com.googlecode.cannedbeans.generator.model.Constraint;

/**
 *
 * @author Kim
 */
public class MissingPropertyException extends Throwable {

    private Constraint constraint;

    private String missingProperty;

    private String message = "";

    public MissingPropertyException(final Constraint constraint, final String missingProperty) {
        this.constraint = constraint;
        this.missingProperty = missingProperty;
    }

    public MissingPropertyException(Constraint constraint, String missingProperty, String message) {
        this(constraint, missingProperty);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message + " Missing property '" + missingProperty + "' for Constraint of type: " + constraint.getConstraintType().name();
    }
}
