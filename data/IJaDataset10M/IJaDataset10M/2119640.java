package com.jmex.model.collada.schema;

import com.jmex.xml.types.SchemaString;

public class gl_shade_model_type extends SchemaString {

    public static final int EFLAT = 0;

    public static final int ESMOOTH = 1;

    public static String[] sEnumValues = { "FLAT", "SMOOTH" };

    public gl_shade_model_type() {
        super();
    }

    public gl_shade_model_type(String newValue) {
        super(newValue);
        validate();
    }

    public gl_shade_model_type(SchemaString newValue) {
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
        if (!isValidEnumerationValue(toString())) throw new com.jmex.xml.xml.XmlException("Value of gl_shade_model_type is invalid.");
    }
}
