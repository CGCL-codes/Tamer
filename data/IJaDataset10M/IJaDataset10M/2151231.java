package org.genxdm.bridgekit.atoms;

import org.genxdm.exceptions.PreCondition;
import org.genxdm.xs.types.NativeType;

/**
 * Corresponds to the W3C XML Schema <a href="http://www.w3.org/TR/xmlschema-2/#normalizedString">normalizedString</a>.
 */
public final class XmlNormalizedString extends XmlAbstractAtom {

    private final String value;

    public XmlNormalizedString(final String value) {
        this.value = PreCondition.assertArgumentNotNull(value, "value");
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof XmlNormalizedString) {
            return value.equals(((XmlNormalizedString) obj).value);
        } else {
            return false;
        }
    }

    public String getC14NForm() {
        return value;
    }

    public NativeType getNativeType() {
        return NativeType.NORMALIZED_STRING;
    }

    public String getString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public boolean isWhiteSpace() {
        return value.trim().length() == 0;
    }
}
