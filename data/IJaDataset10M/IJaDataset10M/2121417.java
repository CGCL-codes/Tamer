package org.j2uml.intermediate.records.def.core;

import org.j2uml.intermediate.records.interfaces.TypeCategory;
import org.j2uml.intermediate.records.interfaces.uml.UMLClass;

/**
 * @author Vijayaraghavan Kalyanapasupathy
 *         vijayaraghavan.k@isis.vanderbilt.edu
 *
 */
public abstract class AbstractClass extends AbstractType implements UMLClass {

    protected AbstractClass() {
        super();
        super.setObjectType(TypeCategory.ClassType);
    }

    public final void setObjectType(TypeCategory p_otype) {
        throw new IllegalArgumentException("Cannot set object-type for classes. Pre-assigned. ");
    }
}
