package net.sf.joafip.meminspector.entity;

import net.sf.joafip.NotStorableClass;
import net.sf.joafip.meminspector.ExcludedClass;

/**
 * referencing object and its referenced object couple
 * 
 * @author luc peuvrier
 * 
 */
@ExcludedClass
@NotStorableClass
public class ReferencingReferencedCouple {

    private final Object referencingObject;

    private final Object referencedObject;

    private final String referencingDeclaringClassName;

    private final String fieldName;

    private final boolean staticField;

    public ReferencingReferencedCouple(final Object referencingObject, final Object referencedObject, final String referencingDeclaringClassName, final String fieldName, final boolean staticField) {
        super();
        this.referencingObject = referencingObject;
        this.referencedObject = referencedObject;
        this.referencingDeclaringClassName = referencingDeclaringClassName;
        this.fieldName = fieldName;
        this.staticField = staticField;
    }

    public Object getReferencedObject() {
        return referencedObject;
    }

    public Object getReferencingObject() {
        return referencingObject;
    }

    public String getReferencingDeclaringClassName() {
        return referencingDeclaringClassName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public boolean isStaticField() {
        return staticField;
    }
}
