package org.openware.job.validation;

import java.util.LinkedList;

public class DateConfig extends FieldConfig {

    public void addFieldValidator(IDateFieldValidator validator) {
        validators.add(validator);
    }
}
