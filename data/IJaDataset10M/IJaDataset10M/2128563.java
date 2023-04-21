package org.oslo.ocl20.semantics.model.types;

import org.oslo.ocl20.semantics.SemanticsElement;

public interface BagType extends SemanticsElement, CollectionType, org.oslo.ocl20.semantics.bridge.DataType, org.oslo.ocl20.semantics.bridge.Classifier, org.oslo.ocl20.semantics.bridge.Namespace, org.oslo.ocl20.semantics.bridge.ModelElement {

    /** Override the toString */
    public String toString();

    /** Clone the object */
    public Object clone();
}
