package org.londonwicket.osiv.phonebook;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

public class PhoneNumberValidator extends AbstractValidator {

    @Override
    protected void onValidate(IValidatable validatable) {
        String number = (String) validatable.getValue();
        if (number.indexOf("+") == 0 || number.indexOf("0") == 0) {
            if (number.indexOf("+") == 0 && number.indexOf("0") == 1) {
                this.error(validatable);
                return;
            }
            number = number.substring(1);
        } else {
            this.error(validatable);
            return;
        }
        if (!(number.indexOf("(") == number.lastIndexOf("(") && number.indexOf(")") == number.lastIndexOf(")"))) {
            this.error(validatable);
            return;
        } else if ((number.indexOf("(") > 0 && number.indexOf(")") < 0) || (number.indexOf(")") > 0 && number.indexOf("(") < 0)) {
            this.error(validatable);
            return;
        }
        if (number.indexOf("(") > 0 && number.indexOf(")") > 0) {
            number = number.substring(0, number.indexOf("(")) + number.substring(number.indexOf("(") + 1, number.indexOf(")")) + number.substring(number.indexOf(")") + 1);
        }
        try {
            Long.parseLong(number);
        } catch (NumberFormatException e) {
            this.error(validatable);
        }
    }
}
