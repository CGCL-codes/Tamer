package jode.type;

import jode.bytecode.ClassInfo;
import java.util.Vector;

/** 
 * This type represents an array type.
 *
 * @author Jochen Hoenicke 
 */
public class ArrayType extends ReferenceType {

    static final ClassInfo[] arrayIfaces = { ClassInfo.forName("java.lang.Cloneable"), ClassInfo.forName("java.io.Serializable") };

    Type elementType;

    ArrayType(Type elementType) {
        super(TC_ARRAY);
        this.elementType = elementType;
    }

    public Type getElementType() {
        return elementType;
    }

    public Type getSuperType() {
        if (elementType instanceof IntegerType) return tRange(tObject, this); else return tRange(tObject, (ReferenceType) tArray(elementType.getSuperType()));
    }

    public Type getSubType() {
        if (elementType instanceof IntegerType) return this; else return tArray(elementType.getSubType());
    }

    public Type getHint() {
        return tArray(elementType.getHint());
    }

    public Type getCanonic() {
        return tArray(elementType.getCanonic());
    }

    /**
     * Create the type corresponding to the range from bottomType to this.
     * @param bottomType the start point of the range
     * @return the range type, or tError if not possible.
     */
    public Type createRangeType(ReferenceType bottom) {
        if (bottom.getTypeCode() == TC_ARRAY) return tArray(elementType.intersection(((ArrayType) bottom).elementType));
        if (bottom.getTypeCode() == TC_CLASS) {
            ClassInterfacesType bottomCIT = (ClassInterfacesType) bottom;
            if (bottomCIT.clazz == null && implementsAllIfaces(null, arrayIfaces, bottomCIT.ifaces)) return tRange(bottomCIT, this);
        }
        return tError;
    }

    /**
     * Returns the common sub type of this and type.
     * @param type the other type.
     * @return the common sub type.
     */
    public Type getSpecializedType(Type type) {
        if (type.getTypeCode() == TC_RANGE) {
            type = ((RangeType) type).getBottom();
        }
        if (type == tNull) return this;
        if (type.getTypeCode() == TC_ARRAY) {
            Type elType = elementType.intersection(((ArrayType) type).elementType);
            return elType != tError ? tArray(elType) : tError;
        }
        if (type.getTypeCode() == TC_CLASS) {
            ClassInterfacesType other = (ClassInterfacesType) type;
            if (other.clazz == null && implementsAllIfaces(null, arrayIfaces, other.ifaces)) return this;
        }
        return tError;
    }

    /**
     * Returns the common super type of this and type.
     * @param type the other type.
     * @return the common super type.
     */
    public Type getGeneralizedType(Type type) {
        if (type.getTypeCode() == TC_RANGE) {
            type = ((RangeType) type).getTop();
        }
        if (type == tNull) return this;
        if (type.getTypeCode() == TC_ARRAY) {
            Type elType = elementType.intersection(((ArrayType) type).elementType);
            if (elType != tError) return tArray(elType);
            return ClassInterfacesType.create(null, arrayIfaces);
        }
        if (type.getTypeCode() == TC_CLASS) {
            ClassInterfacesType other = (ClassInterfacesType) type;
            if (implementsAllIfaces(other.clazz, other.ifaces, arrayIfaces)) return ClassInterfacesType.create(null, arrayIfaces);
            if (other.clazz == null && implementsAllIfaces(null, arrayIfaces, other.ifaces)) return other;
            Vector newIfaces = new Vector();
            iface_loop: for (int i = 0; i < arrayIfaces.length; i++) {
                if (other.clazz != null && arrayIfaces[i].implementedBy(other.clazz)) {
                    newIfaces.addElement(arrayIfaces[i]);
                    continue iface_loop;
                }
                for (int j = 0; j < other.ifaces.length; j++) {
                    if (arrayIfaces[i].implementedBy(other.ifaces[j])) {
                        newIfaces.addElement(arrayIfaces[i]);
                        continue iface_loop;
                    }
                }
            }
            ClassInfo[] ifaceArray = new ClassInfo[newIfaces.size()];
            newIfaces.copyInto(ifaceArray);
            return ClassInterfacesType.create(null, ifaceArray);
        }
        return tError;
    }

    /**
     * Checks if we need to cast to a middle type, before we can cast from
     * fromType to this type.
     * @return the middle type, or null if it is not necessary.
     */
    public Type getCastHelper(Type fromType) {
        Type hintType = fromType.getHint();
        switch(hintType.getTypeCode()) {
            case TC_ARRAY:
                if (!elementType.isClassType() || !((ArrayType) hintType).elementType.isClassType()) return tObject;
                Type middleType = elementType.getCastHelper(((ArrayType) hintType).elementType);
                if (middleType != null) return tArray(middleType);
                return null;
            case TC_CLASS:
                ClassInterfacesType hint = (ClassInterfacesType) hintType;
                if (hint.clazz == null && implementsAllIfaces(null, arrayIfaces, hint.ifaces)) return null;
                return tObject;
            case TC_UNKNOWN:
                return null;
        }
        return tObject;
    }

    /**
     * Checks if this type represents a valid type instead of a list
     * of minimum types.
     */
    public boolean isValidType() {
        return elementType.isValidType();
    }

    public boolean isClassType() {
        return true;
    }

    public String getTypeSignature() {
        return "[" + elementType.getTypeSignature();
    }

    public Class getTypeClass() throws ClassNotFoundException {
        return Class.forName("[" + elementType.getTypeSignature());
    }

    public String toString() {
        return elementType.toString() + "[]";
    }

    private static String pluralize(String singular) {
        return singular + ((singular.endsWith("s") || singular.endsWith("x") || singular.endsWith("sh") || singular.endsWith("ch")) ? "es" : "s");
    }

    public String getDefaultName() {
        if (elementType instanceof ArrayType) return elementType.getDefaultName();
        return pluralize(elementType.getDefaultName());
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (o instanceof ArrayType) {
            ArrayType type = (ArrayType) o;
            return type.elementType.equals(elementType);
        }
        return false;
    }
}
