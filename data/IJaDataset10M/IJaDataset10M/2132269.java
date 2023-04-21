package net.sf.xmlprocessor.xml.xsd.primitives.constraints;

import net.sf.xmlprocessor.xml.xsd.primitives.XSAbstractPrimitiveType;

/**
 * <code>MaxInclusiveConstraint</code> represents a
 * <code>xs:maxInclusive</code> constraint.
 * 
 * @param <OT> The object type.
 * @param <TT> The <code>XSAbstractPrimitiveType</code> implementing the type
 *           for which the constraint is used.
 * @author Emond Papegaaij
 */
public class MaxInclusiveConstraint<OT extends Comparable<OT>, TT extends XSAbstractPrimitiveType<OT>> extends AbstractCompareConstraint<OT, TT> {

    private static final long serialVersionUID = -5440817026328977230L;

    /**
	 * {@inheritDoc} <code>MaxInclusiveConstraint</code> checks if the given
	 * object value is smaller than, or equal to <code>value</code>. The
	 * object value is constructed with the <code>
	 * {@link XSAbstractPrimitiveType#getObjectValue}</code>
	 * method.
	 * 
	 * @param type {@inheritDoc}
	 * @param checkValue {@inheritDoc}
	 * @return {@inheritDoc}
	 */
    public boolean check(TT type, String checkValue) {
        return type.getObjectValue(checkValue).compareTo(value) <= 0;
    }
}
