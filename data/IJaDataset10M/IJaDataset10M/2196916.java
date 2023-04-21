package org.wicketrad.propertyeditor.validator;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * IValidator that validates that a String input conforms to a DateFormat given.
 *
 */
public class DateFormatValidator extends AbstractValidator {

    private String dateFormat;

    public DateFormatValidator(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    protected void onValidate(IValidatable validatable) {
        try {
            String value = validatable.getValue().toString();
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            sdf.setLenient(false);
            Date date = sdf.parse(value);
        } catch (Exception e) {
            this.error(validatable);
        }
    }
}
