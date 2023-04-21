package net.sf.doolin.gui.field.validator;

import net.sf.doolin.gui.core.validation.ValidationError;
import net.sf.doolin.gui.core.validation.ValidationReport;
import net.sf.doolin.gui.field.Field;

public abstract class AbstractValueValidator extends AbstractValidator {

    public ValidationError validate(Field field, ValidationReport validationReport) {
        Object fieldData = field.getFieldData(field.getForm().getFormData());
        return validateValue(field, fieldData, validationReport);
    }

    protected abstract ValidationError validateValue(Field field, Object fieldData, ValidationReport validationReport);
}
