package org.openexi.fujitsu.schema;

/**
 * This class represents a typed atomic data value that results from
 * simple-type schema validation.
 */
public final class AtomicTypedValue extends TypedValue {

    private final int m_primTypeId;

    private Object m_variant;

    AtomicTypedValue(EXISchema corpus, int primTypeId) {
        super(corpus);
        m_primTypeId = primTypeId;
        m_variant = null;
    }

    @Override
    public boolean isList() {
        return false;
    }

    void setTypedValue(Object value) {
        m_variant = value;
    }

    public int getPrimTypeId() {
        return m_primTypeId;
    }

    /**
   * Returns the typed object.
   * The type of objects returned is one of
   * String, Float, Double, Boolean, ParsedDecimal,
   */
    public Object getValue() {
        return m_variant;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) && obj instanceof AtomicTypedValue) {
            AtomicTypedValue that = (AtomicTypedValue) obj;
            if (this.m_primTypeId == EXISchemaConst.BASE64BINARY_TYPE && that.m_primTypeId == EXISchemaConst.BASE64BINARY_TYPE || this.m_primTypeId == EXISchemaConst.HEXBINARY_TYPE && that.m_primTypeId == EXISchemaConst.HEXBINARY_TYPE) {
                byte[] thisBytes = (byte[]) this.m_variant;
                byte[] thatBytes = (byte[]) that.m_variant;
                if (thisBytes.length == thatBytes.length) {
                    int i, len;
                    for (i = 0, len = thisBytes.length; i < len; i++) {
                        if (thisBytes[i] != thatBytes[i]) return false;
                    }
                    return true;
                }
            } else return m_variant.equals(((AtomicTypedValue) obj).m_variant);
        }
        return false;
    }
}
