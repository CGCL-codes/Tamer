package com.incendiaryblue.cmslite.forms.fields;

import com.incendiaryblue.cmslite.StructureItem;
import com.incendiaryblue.forms.Field;
import java.text.*;
import java.util.*;
import com.incendiaryblue.util.ErrorMsgMap;

/**
 *  Performs validation by calling validation functions in StructureItem
 */
public class StructureItemField extends Field {

    private static final SimpleDateFormat DF = new SimpleDateFormat("dd/MM/yyyy");

    private StructureItem si;

    public StructureItemField(StructureItem si) {
        super();
        if (si == null) {
            throw new NullPointerException();
        }
        this.si = si;
    }

    public String getValueAsString() {
        return (value == null) ? "" : value.toString();
    }

    public boolean validate() {
        return "".equals(getValidationError());
    }

    public String getValidationError() {
        String validationError = "";
        if (isActive()) {
            if (!"".equals(this.getValueAsString())) {
                if (!this.si.isValidForDataType(this.getValueAsString(), this.si.getDataType())) {
                    String dataType = si.getDataType();
                    if (StructureItem.DATE.equals(dataType)) {
                        validationError = ErrorMsgMap.getMessage("Forms", "sif.invalid.date", "Value is an invalid date.  Date format is dd/mm/yyyy.");
                    } else {
                        validationError = "Value is an invalid  " + si.getDataTypeDescription();
                    }
                } else if (!si.isValidValue(si.parseFieldValue(this.getValueAsString()))) {
                    validationError = ErrorMsgMap.getMessage("Forms", "sif.out.of.range", "Value is not in the allowed range of values.");
                }
            } else if (isRequired()) {
                validationError = ErrorMsgMap.getMessage("Forms", "sif.value.required", "Value is required.");
            }
        }
        return validationError;
    }

    public void setValue(Object newValue) {
        if (newValue == null) {
            this.value = "";
        } else if (newValue instanceof Date) {
            this.value = DF.format((Date) newValue);
        } else {
            this.value = newValue.toString();
        }
    }

    public StructureItem getSi() {
        return si;
    }

    public void setSi(StructureItem si) {
        this.si = si;
    }
}
