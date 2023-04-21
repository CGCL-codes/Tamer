package it.cnr.stlab.xd.plugin.editor.model.validators;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class NumberEditorValidator implements ICellEditorValidator {

    public String isValid(Object value) {
        int intValue = -1;
        try {
            intValue = Integer.parseInt((String) value);
        } catch (NumberFormatException exc) {
            return "Not a number";
        }
        return (intValue >= 0) ? null : "Value must be >=  0";
    }
}
