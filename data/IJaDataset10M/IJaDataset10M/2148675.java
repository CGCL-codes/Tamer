package mipt.aaf.edit.form.convert;

import mipt.aaf.edit.form.DoubleFieldListener;
import mipt.aaf.edit.form.Field;
import mipt.aaf.edit.form.FieldListener;

/**
 * Extracts Double value from String (should we do implement two-side conversion of Integer?).
 * @author Evdokimov
 */
public class DoubleFieldConverter extends DoubleFieldListener implements FieldConverter {

    /**
	 * 
	 */
    public DoubleFieldConverter() {
    }

    /**
	 * @param nextListener
	 */
    public DoubleFieldConverter(FieldListener nextListener) {
        super(nextListener);
    }

    /**
	 * @see mipt.crec.vaadin.convert.FieldConverter#getViewValue(mipt.aaf.edit.form.Field, java.lang.Object)
	 */
    public Object getViewValue(Field field, Object modelValue) {
        if (modelValue instanceof String) return new Double(modelValue.toString());
        return modelValue;
    }
}
