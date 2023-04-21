package org.oslo.ocl20.semantics.model.expressions;

import java.util.List;
import org.oslo.ocl20.semantics.SemanticsElement;

public interface TupleLiteralExp extends SemanticsElement, LiteralExp, OclExpression {

    /** Get the 'partType' from 'TupleType' */
    public List getTuplePart();

    /** Set the 'partType' from 'TupleType' */
    public void setTuplePart(List partType);

    /** Override the toString */
    public String toString();

    /** Clone the object */
    public Object clone();
}
