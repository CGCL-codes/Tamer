package org.freeworld.jmultiplug.validation;

import org.freeworld.jmultiplug.validation.core.ValidationLevel;
import org.freeworld.jmultiplug.validation.core.ValidationToken;

public interface ValidationResult {

    public boolean isPass();

    public boolean isFail();

    public ValidationLevel getValidationLevel();

    public String getValidationMessage();

    public ValidationToken getValidationToken();
}
