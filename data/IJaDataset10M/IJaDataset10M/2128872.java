package org.kobjects.serialization;

/** This class encapsulates type information. */
public class ElementType {

    public static final Class OBJECT_CLASS = new Object().getClass();

    public static final Class STRING_CLASS = "".getClass();

    public static final Class INTEGER_CLASS = new Integer(0).getClass();

    public static final Class LONG_CLASS = new Long(0).getClass();

    public static final Class BOOLEAN_CLASS = new Boolean(true).getClass();

    public static final Class VECTOR_CLASS = new java.util.Vector().getClass();

    public static final ElementType OBJECT_TYPE = new ElementType(OBJECT_CLASS, false, null);

    /** Type of the property/elements. Should usually be
        an instance of class, */
    public Object type;

    /** if a property is multi-referenced, set this flag to true. */
    public boolean multiRef;

    /** Element type for array properties, null if not array prop. */
    public ElementType elementType;

    public ElementType() {
    }

    public ElementType(Object type) {
        this.type = type;
    }

    public ElementType(Object type, boolean multiRef, ElementType elementType) {
        this.type = type;
        this.multiRef = multiRef;
        this.elementType = elementType;
    }

    public void clear() {
        type = null;
        multiRef = false;
        elementType = null;
    }

    public void copy(ElementType t2) {
        type = t2.type;
        multiRef = t2.multiRef;
        elementType = t2.elementType;
    }
}
