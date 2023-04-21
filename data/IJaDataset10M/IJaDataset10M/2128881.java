package net.sf.xmlprocessor.xml.xsd.primitives.constraints;

import net.sf.xmlprocessor.xml.xsd.primitives.XSAbstractPrimitiveType;

/**
 * <code>LengthConstraint</code> represents a <code>xs:length</code>
 * constraint.
 * 
 * @param <OT> The object type.
 * @param <TT> The <code>XSAbstractPrimitiveType</code> implementing the type
 *           for which the constraint is used.
 * @author Emond Papegaaij
 */
public class LengthConstraint<OT, TT extends XSAbstractPrimitiveType<OT>> extends AbstractIntegerConstraint<OT, TT> {

    private static final long serialVersionUID = -2774511327542401767L;

    /**
	 * {@inheritDoc} <code>LengthConstraint</code> checks if the given value
	 * has an object length of exactly <code>value</code>. The length is
	 * calculated with the
	 * <code>{@link XSAbstractPrimitiveType#getObjectLength}</code> method.
	 * 
	 * @param type {@inheritDoc}
	 * @param checkValue {@inheritDoc}
	 * @return {@inheritDoc}
	 */
    public boolean check(TT type, String checkValue) {
        return value == type.getObjectLength(checkValue);
    }
}
