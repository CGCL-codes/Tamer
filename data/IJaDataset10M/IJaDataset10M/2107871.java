package com.incendiaryblue.forms.fields;

import com.incendiaryblue.validation.Validator;

/**
 * A form field with telephone number validation
 * @author  wlee
 * @version
 */
public class TelephoneField extends StringField {

    public TelephoneField() {
        super(20);
    }

    /**
	 * Checks that there is a value present if required, and if so check that it is a valid
	 * telephone number.
	 */
    public boolean validate() {
        return super.validate() && ((getValueAsString().length() == 0) || Validator.validTelephone(getValueAsString()));
    }

    /**
	 * Returns an error message if the field is not valid.
	 */
    public String getValidationError() {
        if (validate()) {
            return "";
        } else {
            return "Telephone number must contain numerical charaters only";
        }
    }
}
