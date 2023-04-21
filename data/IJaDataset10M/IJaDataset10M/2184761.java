package fr.esrf.tangoatk.core;

import fr.esrf.TangoDs.*;

public class TypeProperty extends Property implements TangoConst {

    public TypeProperty(ICommand parent, String name, Number value, boolean editable) {
        super(parent, name, value, editable);
    }

    public String getPresentation() {
        return Tango_CmdArgTypeName[((Number) value).intValue()];
    }

    public String getVersion() {
        return "$Id: TypeProperty.java 12968 2009-01-26 17:54:56Z poncet $";
    }

    public void setValueFromString(String stringValue) {
        try {
            if (value instanceof Double) {
                Double tempVal = Double.valueOf(stringValue);
                setValue(tempVal);
            } else if (value instanceof Float) {
                Float tempVal = Float.valueOf(stringValue);
                setValue(tempVal);
            } else if (value instanceof Integer) {
                Integer tempVal = Integer.valueOf(stringValue);
                setValue(tempVal);
            } else if (value instanceof Long) {
                Long tempVal = Long.valueOf(stringValue);
                setValue(tempVal);
            } else if (value instanceof Short) {
                Short tempVal = Short.valueOf(stringValue);
                setValue(tempVal);
            } else {
                Byte tempVal = Byte.valueOf(stringValue);
                setValue(tempVal);
            }
        } catch (NumberFormatException nfe) {
        }
    }
}
