package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaToken;

public class gles_texenv_mode_enums extends SchemaToken {

    public static final int EREPLACE = 0;

    public static final int EMODULATE = 1;

    public static final int EDECAL = 2;

    public static final int EBLEND = 3;

    public static final int EADD = 4;

    public static String[] sEnumValues = { "REPLACE", "MODULATE", "DECAL", "BLEND", "ADD" };

    public gles_texenv_mode_enums() {
        super();
    }

    public gles_texenv_mode_enums(String newValue) {
        super(newValue);
        validate();
    }

    public gles_texenv_mode_enums(SchemaToken newValue) {
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
        if (!isValidEnumerationValue(toString())) throw new homura.hde.util.xml.xml.XmlException("Value of gles_texenv_mode_enums is invalid.");
    }
}
