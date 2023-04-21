package org.oslo.ocl20.semantics.model.expressions;

import org.oslo.ocl20.semantics.SemanticsElement;
import org.oslo.ocl20.semantics.bridge.Classifier;

public interface TypeLiteralExp extends SemanticsElement, LiteralExp, OclExpression {

    /** Override the toString */
    public String toString();

    /** Clone the object */
    public Object clone();

    public Classifier getLiteralType();
}
