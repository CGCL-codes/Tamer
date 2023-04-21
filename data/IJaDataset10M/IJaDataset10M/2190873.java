package com.incendiaryblue.forms.fields;

import com.incendiaryblue.forms.Field;
import java.text.ParseException;
import java.util.Date;
import com.incendiaryblue.util.SimpleDate;
import com.incendiaryblue.util.ErrorMsgMap;
import java.text.ParseException;

/**
 * Validates string-representations of dates in the dd/MM/yyyy format.
 *
 * The 'value' member of this Field holds a string representation of a date (in
 * the above format) that, once validated, can be used to construct a SimpleDate
 * object. Thus, any form calling setValue() should ensure a properly formatted
 * date string is set.
 */
public class SimpleDateField extends Field {

    /**
	 * The day, month, and year settings must be set to appropriate values
	 * before validation is called (for it to succeed).
	 *
	 * @return
	 */
    public boolean validate() {
        return "".equals(getValidationError());
    }

    public String getValidationError() {
        String validationError = "";
        if (!"".equals(getValueAsString())) {
            java.text.DateFormat DF = new java.text.SimpleDateFormat("dd/MM/yyyy");
            DF.setLenient(false);
            try {
                Date date = DF.parse((String) value);
                if (date == null) throw new ParseException("", 0);
            } catch (ParseException e) {
                validationError = ErrorMsgMap.getMessage("Forms", "sdf.invalid.date", "Please supply a valid date: dd/MM/yyyy");
            }
        } else if (isRequired()) {
            validationError = ErrorMsgMap.getMessage("Forms", "sdf.required.field", "Please supply a valid date.");
        }
        return validationError;
    }

    /**
	 * @return The value object of this Field (which is a String anyway).
	 */
    public String getValueAsString() {
        return (value == null) ? "" : (String) value;
    }
}
