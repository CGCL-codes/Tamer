package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaString;

public class gles_texcombiner_operandAlpha_enums extends gl_blend_type {

    public static final int ESRC_ALPHA = 0;

    public static final int EONE_MINUS_SRC_ALPHA = 1;

    public static String[] sEnumValues = { "SRC_ALPHA", "ONE_MINUS_SRC_ALPHA" };

    public gles_texcombiner_operandAlpha_enums() {
        super();
    }

    public gles_texcombiner_operandAlpha_enums(String newValue) {
        super(newValue);
        validate();
    }

    public gles_texcombiner_operandAlpha_enums(SchemaString newValue) {
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
        if (!isValidEnumerationValue(toString())) throw new homura.hde.util.xml.xml.XmlException("Value of gles_texcombiner_operandAlpha_enums is invalid.");
    }
}
