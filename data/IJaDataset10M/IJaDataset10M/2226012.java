package org.dyno.visual.swing.types.editor;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class FloatCellEditorValidator implements ICellEditorValidator {

    public String isValid(Object value) {
        String string = (String) value;
        try {
            Float.parseFloat(string);
        } catch (NumberFormatException nfe) {
            return Messages.FloatCellEditorValidator_Incorrect_Format;
        }
        return null;
    }
}
