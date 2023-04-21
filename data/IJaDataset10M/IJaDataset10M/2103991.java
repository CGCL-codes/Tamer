package com.jmex.model.collada.types;

public class SchemaBase64Binary extends SchemaBinaryBase {

    private static final long serialVersionUID = 1L;

    public SchemaBase64Binary() {
        super();
    }

    public SchemaBase64Binary(SchemaBase64Binary newvalue) {
        value = newvalue.value;
        isempty = newvalue.isempty;
        isnull = newvalue.isnull;
    }

    public SchemaBase64Binary(byte[] newvalue) {
        setValue(newvalue);
    }

    public SchemaBase64Binary(String newvalue) {
        parse(newvalue);
    }

    public SchemaBase64Binary(SchemaType newvalue) {
        assign(newvalue);
    }

    public SchemaBase64Binary(SchemaTypeBinary newvalue) {
        assign(newvalue);
    }

    public void parse(String newvalue) {
        if (newvalue == null) setNull(); else if (newvalue.length() == 0) setEmpty(); else {
            setNull();
            try {
                value = new sun.misc.BASE64Decoder().decodeBuffer(newvalue);
                isnull = false;
                isempty = (value.length == 0) ? true : false;
            } catch (java.io.IOException e) {
                value = null;
            }
        }
    }

    public int hashCode() {
        return value.hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SchemaBase64Binary)) return false;
        return value.equals(((SchemaBase64Binary) obj).value);
    }

    public Object clone() {
        return new SchemaBase64Binary(this);
    }

    public String toString() {
        if (isempty || isnull || value == null) return "";
        String sResult = new sun.misc.BASE64Encoder().encode(value);
        return sResult.replaceAll("\r", "");
    }

    public int binaryType() {
        return BINARY_VALUE_BASE64;
    }
}
