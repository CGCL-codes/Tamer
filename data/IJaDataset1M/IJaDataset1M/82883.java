package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaString;

public class fx_surface_format_hint_precision_enum extends SchemaString {

    public static final int ELOW = 0;

    public static final int EMID = 1;

    public static final int EHIGH = 2;

    public static String[] sEnumValues = { "LOW", "MID", "HIGH" };

    public fx_surface_format_hint_precision_enum() {
        super();
    }

    public fx_surface_format_hint_precision_enum(String newValue) {
        super(newValue);
        validate();
    }

    public fx_surface_format_hint_precision_enum(SchemaString newValue) {
        super(newValue);
        validate();
    }

    public static int getEnumerationCount() {
        return sEnumValues.length;
    }

    public static String getEnumerationValue(int index) {
        return sEnumValues[index];
    }

    public static boolean isValidEnumerationValue(String val) {
        for (int i = 0; i < sEnumValues.length; i++) {
            if (val.equals(sEnumValues[i])) return true;
        }
        return false;
    }

    public void validate() {
        if (!isValidEnumerationValue(toString())) throw new homura.hde.util.xml.xml.XmlException("Value of fx_surface_format_hint_precision_enum is invalid.");
    }
}
