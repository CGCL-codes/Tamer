package com.incendiaryblue.cmslite.forms.fields;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import com.incendiaryblue.forms.fields.StringField;
import com.incendiaryblue.lang.StringHelper;
import com.incendiaryblue.util.ErrorMsgMap;

/**
 *  Validates string-representations of dates in the dd/MM/yyyy format.
 */
public class BeingUsedField extends StringField {

    public static final String ATTR_BEING_USED = "beingused";

    public static final String ERR_MSG = ErrorMsgMap.getMessage("Forms", "buf.already.used", "Value is already being used.");

    public BeingUsedField() {
        super();
    }

    public boolean validate() {
        return "".equals(this.getValidationError());
    }

    public String getValidationError() {
        String errorMsg = "";
        if (isActive()) {
            errorMsg = super.getValidationError();
            if ("".equals(errorMsg)) {
                String stringInvalidValues = (String) this.getAttribute(ATTR_BEING_USED);
                Set setInvalidValues = new HashSet();
                if (stringInvalidValues != null) {
                    setInvalidValues.addAll(StringHelper.toList(stringInvalidValues, ","));
                    if (setInvalidValues.contains(getValueAsString())) {
                        errorMsg = ERR_MSG;
                    }
                }
            }
        }
        return errorMsg;
    }
}
