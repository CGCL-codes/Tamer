package com.google.gwt.core.ext.typeinfo;

/**
 * Abstract superclass for types.
 */
public abstract class JType {

    JType() {
    }

    /**
   * All types use identity for comparison.
   */
    @Override
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    public abstract JType getErasedType();

    public abstract String getJNISignature();

    public JType getLeafType() {
        return this;
    }

    public String getParameterizedQualifiedSourceName() {
        return getQualifiedSourceName();
    }

    /**
   * TODO(scottb): remove if we can resolve param names differently.
   */
    public abstract String getQualifiedBinaryName();

    public abstract String getQualifiedSourceName();

    public abstract String getSimpleSourceName();

    /**
   * All types use identity for comparison.
   */
    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    /**
   * Returns this instance if it is a annotation or <code>null</code> if it is
   * not.
   * 
   * @return this instance if it is a annotation or <code>null</code> if it is
   *         not
   */
    public JAnnotationType isAnnotation() {
        return null;
    }

    public abstract JArrayType isArray();

    public abstract JClassType isClass();

    public JClassType isClassOrInterface() {
        JClassType type = isClass();
        if (type != null) {
            return type;
        }
        return isInterface();
    }

    /**
   * Returns this instance if it is an enumeration or <code>null</code> if it
   * is not.
   * 
   * @return this instance if it is an enumeration or <code>null</code> if it
   *         is not
   */
    public abstract JEnumType isEnum();

    public abstract JGenericType isGenericType();

    public abstract JClassType isInterface();

    public abstract JParameterizedType isParameterized();

    public abstract JPrimitiveType isPrimitive();

    public abstract JRawType isRawType();

    public JTypeParameter isTypeParameter() {
        return null;
    }

    public abstract JWildcardType isWildcard();

    /**
   * Returns either the substitution of this type based on the parameterized
   * type or this instance.
   * 
   * @param parameterizedType
   * @return either the substitution of this type based on the parameterized
   *         type or this instance
   */
    abstract JType getSubstitutedType(JParameterizedType parameterizedType);
}
