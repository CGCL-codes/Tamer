package net.sf.xmlprocessor.xml.xsd.primitives;

import net.sf.xmlprocessor.xml.xsd.XSAbstractSchemaPart;
import net.sf.xmlprocessor.xml.xsd.XSSchema;
import net.sf.xmlprocessor.xml.xsd.XSVisitor;

/**
 * <code>XSIntType</code> represents the <code>xs:int</code> primitive type.
 * It is backed by a <code>BigDecimal</code> instance and extends
 * <code>XSLongType</code> by resetting the <code>maxInclusive</code> and
 * <code>minInclusive</code> constraints to <code>Integer.MAX_VALUE</code>
 * and <code>Integer.MIN_VALUE</code> respectivelly.
 * 
 * @author Emond Papegaaij
 */
public class XSIntType extends XSLongType {

    private static final long serialVersionUID = 2397314514900005045L;

    /**
	 * Creates a new <code>XSIntType</code>. This constructor is provided for
	 * serialization.
	 */
    protected XSIntType() {
    }

    /**
	 * Creates a new <code>XSIntType</code> for the given schema with a given
	 * name.
	 * 
	 * @param schema The schema the type is used in.
	 * @param name The name of the type.
	 * @param parent The parent construct of the type.
	 */
    public XSIntType(XSSchema schema, String name, XSAbstractSchemaPart parent) {
        super(schema, name, parent);
        Integer max = new Integer(Integer.MAX_VALUE);
        Integer min = new Integer(Integer.MIN_VALUE);
        addConstraintString("maxInclusive", max.toString());
        addConstraintString("minInclusive", min.toString());
    }

    public <R, A> R accept(XSVisitor<R, A> visitor, A arg) {
        return visitor.visit(this, arg);
    }
}
