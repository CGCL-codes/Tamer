package jode.type;

import jode.AssertError;

/**
 * This class represents the NullType.  The null type is special as it
 * may only occur as top type in a range type.  It represents the type
 * of the null constant, which may be casted to any object. <br>
 *
 * Question: Should we replace tUObject = tRange(tObject, tNull) by tNull?
 * Question2: if not, should null have type tNull?
 *
 * @author Jochen Hoenicke 
 */
public class NullType extends ReferenceType {

    public NullType() {
        super(TC_NULL);
    }

    public Type getSubType() {
        return this;
    }

    public Type createRangeType(ReferenceType bottomType) {
        return tRange(bottomType, this);
    }

    /**
     * Returns the generalized type of this and type.  We have two
     * classes and multiple interfaces.  The result should be the
     * object that is the the super class of both objects and all
     * interfaces, that one class or interface of each type 
     * implements.  
     */
    public Type getGeneralizedType(Type type) {
        if (type.typecode == TC_RANGE) type = ((RangeType) type).getTop();
        return type;
    }

    /**
     * Returns the specialized type of this and type.
     * We have two classes and multiple interfaces.  The result 
     * should be the object that extends both objects
     * and the union of all interfaces.
     */
    public Type getSpecializedType(Type type) {
        if (type.typecode == TC_RANGE) type = ((RangeType) type).getBottom();
        return type;
    }

    public String toString() {
        return "tNull";
    }
}
