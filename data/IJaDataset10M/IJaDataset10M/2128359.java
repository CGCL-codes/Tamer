package org.openware.job.validation;

import java.util.LinkedList;

public class NumberConfig extends FieldConfig {

    public void addFieldValidator(INumberFieldValidator validator) {
        validators.add(validator);
    }
}
