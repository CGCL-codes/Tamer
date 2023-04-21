package j3dworkbench.props.common;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class NumberCellValidator implements ICellEditorValidator {

    private final Object id;

    public NumberCellValidator(Object id) {
        this.id = id;
    }

    public String isValid(Object value) {
        Float number = null;
        boolean hasMinMax = id instanceof NumberDescriptor && ((NumberDescriptor) id).hasMinMax();
        try {
            number = Float.parseFloat((String) value);
        } catch (NumberFormatException nfex) {
            return value + " is not a number.";
        }
        if (hasMinMax) {
            NumberDescriptor des = (NumberDescriptor) id;
            if (number >= des.getMin().floatValue() && number <= des.getMax().floatValue()) {
                return null;
            }
            return " Value must be between " + des.getMin() + " & " + des.getMax();
        }
        return null;
    }
}
