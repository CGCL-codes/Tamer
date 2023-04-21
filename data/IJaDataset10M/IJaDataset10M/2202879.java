package org.nakedobjects.noa.reflect;

import org.nakedobjects.noa.spec.NakedObjectSpecification;

/**
 * Enumerates the features that a particular annotation can be applied to.
 * 
 * <p>
 * Modelled after Java 5 <tt>ElementType</tt>.
 * 
 * 
 * <p>
 * TODO: should rationalize this and {@link NakedObjectSpecification#getType()}.  
 * Note though that we don't distinguish value properties and reference properties
 * (and we probably shouldn't in {@link NakedObjectSpecification}, either).
 */
public final class NakedObjectFeatureType {

    public static final NakedObjectFeatureType OBJECT = new NakedObjectFeatureType(0, "Object");

    public static final NakedObjectFeatureType PROPERTY = new NakedObjectFeatureType(1, "Property");

    public static final NakedObjectFeatureType COLLECTION = new NakedObjectFeatureType(2, "Collection");

    public static final NakedObjectFeatureType ACTION = new NakedObjectFeatureType(3, "Action");

    public static final NakedObjectFeatureType ACTION_PARAMETER = new NakedObjectFeatureType(4, "Parameter");

    private NakedObjectFeatureType(int num, String nameInCode) {
        this.num = num;
        this.name = nameInCode;
    }

    private final int num;

    public int getNum() {
        return num;
    }

    private final String name;

    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }
}
