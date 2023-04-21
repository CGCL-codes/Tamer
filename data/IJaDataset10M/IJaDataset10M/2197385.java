package com.antlersoft.ilanalyze;

/**
 * A type that is normally a built-in value type
 * @author Michael A. MacDonald
 *
 */
public class BuiltinType extends BasicType {

    static class NameCode {

        NameCode(String n, int c) {
            name = n;
            code = c;
        }

        String name;

        int code;
    }

    private static NameCode[] nameCodes = new NameCode[] { new NameCode("bool", CustomAttributeBytes.ELEMENT_TYPE_BOOLEAN), new NameCode("char", CustomAttributeBytes.ELEMENT_TYPE_CHAR), new NameCode("float32", CustomAttributeBytes.ELEMENT_TYPE_R4), new NameCode("float64", CustomAttributeBytes.ELEMENT_TYPE_R8), new NameCode("int8", CustomAttributeBytes.ELEMENT_TYPE_I1), new NameCode("int16", CustomAttributeBytes.ELEMENT_TYPE_I2), new NameCode("int32", CustomAttributeBytes.ELEMENT_TYPE_I4), new NameCode("int64", CustomAttributeBytes.ELEMENT_TYPE_I8), new NameCode("string", CustomAttributeBytes.ELEMENT_TYPE_STRING), new NameCode("uint8", CustomAttributeBytes.ELEMENT_TYPE_U1), new NameCode("uint16", CustomAttributeBytes.ELEMENT_TYPE_U2), new NameCode("uint32", CustomAttributeBytes.ELEMENT_TYPE_U4), new NameCode("uint64", CustomAttributeBytes.ELEMENT_TYPE_U8) };

    /**
	 * Create the type with the given name
	 * @param name
	 */
    public BuiltinType(String name) {
        super(name);
    }

    /**
	 * Get the built in type corresponding to an in type code
	 * @param c Int version of type, from list above
	 * @return Type corresponding to type code, or null if type is not identified
	 */
    public static BuiltinType getTypeFromCode(int c) {
        BuiltinType result = null;
        for (int i = 0; result == null && i < nameCodes.length; ++i) {
            if (nameCodes[i].code == c) {
                result = new BuiltinType(nameCodes[i].name);
            }
        }
        return result;
    }

    public boolean isClassType() {
        return false;
    }

    public int getTypeCode() {
        for (int i = 0; i < nameCodes.length; ++i) {
            if (nameCodes[i].name.equals(m_name)) return nameCodes[i].code;
        }
        return CustomAttributeBytes.ELEMENT_TYPE_VOID;
    }
}
