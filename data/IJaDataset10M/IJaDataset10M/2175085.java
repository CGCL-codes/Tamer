package org.genxdm.bridgekit.atoms;

import org.genxdm.exceptions.PreCondition;
import org.genxdm.xs.types.NativeType;

/**
 * Corresponds to the W3C XML Schema <a href="http://www.w3.org/TR/xmlschema-2/#untypedAtomic">untypedAtomic</a>.
 */
public final class XmlUntypedAtomic extends XmlAbstractAtom {

    private final String value;

    public XmlUntypedAtomic(final String strval) {
        this.value = PreCondition.assertArgumentNotNull(strval, "strval");
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof XmlUntypedAtomic) {
            return value.equals(((XmlUntypedAtomic) obj).value);
        } else {
            return false;
        }
    }

    public String getC14NForm() {
        return value;
    }

    public NativeType getNativeType() {
        return NativeType.UNTYPED_ATOMIC;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public boolean isWhiteSpace() {
        return value.trim().length() == 0;
    }
}
