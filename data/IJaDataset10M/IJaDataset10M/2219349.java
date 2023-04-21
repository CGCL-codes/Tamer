package org.genxdm.bridgekit.atoms;

import org.genxdm.exceptions.PreCondition;
import org.genxdm.xs.types.NativeType;

/**
 * Corresponds to the W3C XML Schema <a href="http://www.w3.org/TR/xmlschema-2/#ID">ID</a>.
 */
public final class XmlID extends XmlAbstractAtom {

    private final String value;

    public XmlID(final String value) {
        this.value = PreCondition.assertArgumentNotNull(value, "value");
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof XmlID) {
            return value.equals(((XmlID) obj).value);
        } else {
            return false;
        }
    }

    public String getC14NForm() {
        return value;
    }

    public NativeType getNativeType() {
        return NativeType.ID;
    }

    public String getString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public boolean isWhiteSpace() {
        return false;
    }
}
