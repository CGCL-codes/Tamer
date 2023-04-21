package org.progeeks.meta.echo2.editor;

import org.progeeks.meta.PropertyFormat;
import org.progeeks.meta.echo2.DefaultPropertyEditor;
import org.progeeks.meta.format.DefaultPropertyFormat;

/**
 *  Basic Echo2 Double editor implementation.
 *
 *  @version   $Revision: 1.1 $
 *  @author    Dave Sheremata
 */
public class DoubleEditor extends DefaultPropertyEditor {

    private static final Double ZERO = new Double(0);

    public DoubleEditor() {
        this(new Double(0d), null, null, new Double(1d));
    }

    public DoubleEditor(Double value, Double min, Double max, Double stepSize) {
        this(new DefaultPropertyFormat(), value, min, max, stepSize);
    }

    public DoubleEditor(PropertyFormat format, Double value, Double min, Double max, Double stepSize) {
        super(format);
        if (value == null) {
            if (min != null) value = min; else value = ZERO;
        }
    }

    protected Object getEmptyValue() {
        return (ZERO);
    }
}
