package net.sf.xmlprocessor.xml.xsd.primitives;

import java.math.BigDecimal;
import net.sf.xmlprocessor.xml.xsd.XSAbstractSchemaPart;
import net.sf.xmlprocessor.xml.xsd.XSException;
import net.sf.xmlprocessor.xml.xsd.XSSchema;
import net.sf.xmlprocessor.xml.xsd.XSVisitor;
import net.sf.xmlprocessor.xml.xsd.primitives.constraints.ComparableConstraintsChecker;
import net.sf.xmlprocessor.xml.xsd.primitives.constraints.Constraints;

/**
 * <code>XSDecimalType</code> represents the <code>xs:decimal</code>
 * primitive type. It is backed by a <code>BigDecimal</code> instance.
 * 
 * @author Emond Papegaaij
 */
public class XSDecimalType extends XSAbstractPrimitiveType<BigDecimal> {

    private static final long serialVersionUID = -803170784087996269L;

    /**
	 * Creates a new <code>XSDecimalType</code>. This constructor is provided
	 * for serialization.
	 */
    protected XSDecimalType() {
    }

    /**
	 * Creates a new <code>XSDecimalType</code> for the given schema with a
	 * given name.
	 * 
	 * @param schema The schema the type is used in.
	 * @param name The name of the type.
	 * @param parent The parent construct of the type.
	 */
    public XSDecimalType(XSSchema schema, String name, XSAbstractSchemaPart parent) {
        super(schema, name, parent);
    }

    protected Constraints createConstraints() {
        return new ComparableConstraintsChecker<BigDecimal, XSAbstractPrimitiveType<BigDecimal>>(this);
    }

    /**
	 * {@inheritDoc} <code>XSDecimalType</code> convertes the value to a
	 * <code>BigDecimal</code>.
	 * 
	 * @param value {@inheritDoc}
	 * @return {@inheritDoc}
	 */
    public BigDecimal getObjectValue(String value) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new XSException("'" + value + "' is not a valid decimal");
        }
    }

    public String getStringValue(BigDecimal value) {
        return value.toString();
    }

    /**
	 * {@inheritDoc} <code>XSDecimalType</code> is an atomic type, and
	 * therefore always returns 1.
	 * 
	 * @param value {@inheritDoc}
	 * @return {@inheritDoc}
	 */
    public int getObjectLength(String value) {
        return 1;
    }

    /**
	 * {@inheritDoc} <code>XSDecimalType</code> allows the
	 * <code>totalDigits</code>, <code>fractionDigits</code>,
	 * <code>pattern</code>, <code>whiteSpace</code>,
	 * <code>enumeration</code>, <code>maxInclusive</code>,
	 * <code>maxExclusive</code>, <code>minInclusive</code> and
	 * <code>minExclusive</code> constraints.
	 * 
	 * @param name {@inheritDoc}
	 * @return {@inheritDoc}
	 */
    public boolean constraintIsAllowed(String name) {
        if ("totalDigits".equals(name) || "fractionDigits".equals(name) || "pattern".equals(name) || "whiteSpace".equals(name) || "enumeration".equals(name) || "maxInclusive".equals(name) || "maxExclusive".equals(name) || "minInclusive".equals(name) || "minExclusive".equals(name)) {
            return true;
        }
        return false;
    }

    public <R, A> R accept(XSVisitor<R, A> visitor, A arg) {
        return visitor.visit(this, arg);
    }
}
