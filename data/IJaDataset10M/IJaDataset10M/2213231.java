package org.octaedr.upnp.cp.datatype;

/** 
 * MIME-style Base64 encoded binary BLOB. Takes 3 Bytes, splits them into 4 parts,
 * and maps each 6 bit piece to an octet. (3 octets are encoded as 4.) No limit on
 * size.
 * 
 * The type used for values is TODO -define type-.
 */
class BinBase64DataTypeInfo extends SimpleDataTypeInfo {

    /** Data type name. */
    private static final String DATA_TYPE_NAME = "bin.base64";

    public BinBase64DataTypeInfo() {
        super(DATA_TYPE_NAME);
    }

    public boolean checkValue(Object value) {
        return false;
    }

    public Object parseValue(String valueString) {
        return null;
    }

    public String valueToString(Object value) {
        return null;
    }

    public Class<?> getValueClass() {
        return null;
    }
}
