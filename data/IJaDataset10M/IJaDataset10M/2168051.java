package com.google.gwt.core.ext.typeinfo;

import com.google.gwt.dev.util.collect.HashSet;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Type used to represent any non-primitive type.
 */
@SuppressWarnings("deprecation")
public abstract class JClassType extends JType implements HasAnnotations, HasMetaData {

    /**
   * Returns all of the superclasses and superinterfaces for a given type
   * including the type itself. The returned set maintains an internal
   * breadth-first ordering of the type, followed by its interfaces (and their
   * super-interfaces), then the supertype and its interfaces, and so on.
   */
    protected static Set<JClassType> getFlattenedSuperTypeHierarchy(JClassType type) {
        Set<JClassType> flattened = type.flattenedSupertypes;
        if (flattened == null) {
            flattened = new LinkedHashSet<JClassType>();
            getFlattenedSuperTypeHierarchyRecursive(type, flattened);
            type.flattenedSupertypes = Collections.unmodifiableSet(flattened);
        }
        return flattened;
    }

    /**
   * Returns <code>true</code> if the rhs array type can be assigned to the lhs
   * array type.
   */
    private static boolean areArraysAssignable(JArrayType lhsType, JArrayType rhsType) {
        assert (lhsType != rhsType);
        JType lhsComponentType = lhsType.getComponentType();
        JType rhsComponentType = rhsType.getComponentType();
        if (lhsComponentType.isPrimitive() != null || rhsComponentType.isPrimitive() != null) {
            return false;
        }
        assert (lhsComponentType instanceof JClassType);
        assert (rhsComponentType instanceof JClassType);
        JClassType thisComponentClass = (JClassType) lhsComponentType;
        JClassType subtypeComponentClass = (JClassType) rhsComponentType;
        return areClassTypesAssignable(thisComponentClass, subtypeComponentClass);
    }

    /**
   * Returns <code>true</code> if the rhsType can be assigned to the lhsType.
   */
    private static boolean areClassTypesAssignable(JClassType lhsType, JClassType rhsType) {
        Set<JClassType> rhsSupertypes = getFlattenedSuperTypeHierarchy(rhsType);
        for (JClassType rhsSupertype : rhsSupertypes) {
            if (areClassTypesAssignableNoSupers(lhsType, rhsSupertype)) {
                return true;
            }
        }
        return false;
    }

    /**
   * Returns <code>true</code> if the lhs and rhs are assignable without
   * consideration of the supertypes of the rhs.
   * 
   * @param lhsType
   * @param rhsType
   * @return true if rhsType can be assigned to lhsType
   */
    private static boolean areClassTypesAssignableNoSupers(JClassType lhsType, JClassType rhsType) {
        if (lhsType == rhsType) {
            return true;
        }
        if (lhsType == lhsType.getOracle().getJavaLangObject()) {
            return true;
        }
        if (lhsType.isGenericType() != null) {
            lhsType = lhsType.isGenericType().getRawType();
        }
        if (rhsType.isGenericType() != null) {
            rhsType = rhsType.isGenericType().getRawType();
        }
        JTypeParameter lhsTypeParam = lhsType.isTypeParameter();
        JTypeParameter rhsTypeParam = rhsType.isTypeParameter();
        if (lhsTypeParam != null) {
            JClassType[] lhsTypeBounds = lhsTypeParam.getBounds();
            for (JClassType lhsTypeBound : lhsTypeBounds) {
                if (!areClassTypesAssignable(lhsTypeBound, rhsType)) {
                    return false;
                }
            }
            return true;
        } else if (rhsTypeParam != null) {
            JClassType[] possibleSubtypeBounds = rhsTypeParam.getBounds();
            for (JClassType possibleSubtypeBound : possibleSubtypeBounds) {
                if (areClassTypesAssignable(lhsType, possibleSubtypeBound)) {
                    return true;
                }
            }
            return false;
        }
        JWildcardType lhsWildcard = lhsType.isWildcard();
        JWildcardType rhsWildcard = rhsType.isWildcard();
        if (lhsWildcard != null && rhsWildcard != null) {
            return areWildcardsAssignable(lhsWildcard, rhsWildcard);
        } else if (lhsWildcard != null) {
            JClassType[] lowerBounds = lhsWildcard.getLowerBounds();
            if (lowerBounds.length > 0) {
                return true;
            } else {
                return areClassTypesAssignable(lhsWildcard.getFirstBound(), rhsType);
            }
        }
        JArrayType lhsArray = lhsType.isArray();
        JArrayType rhsArray = rhsType.isArray();
        if (lhsArray != null) {
            if (rhsArray == null) {
                return false;
            } else {
                return areArraysAssignable(lhsArray, rhsArray);
            }
        } else if (rhsArray != null) {
            return false;
        }
        JMaybeParameterizedType lhsMaybeParameterized = lhsType.isMaybeParameterizedType();
        JMaybeParameterizedType rhsMaybeParameterized = rhsType.isMaybeParameterizedType();
        if (lhsMaybeParameterized != null && rhsMaybeParameterized != null) {
            if (lhsMaybeParameterized.getBaseType() == rhsMaybeParameterized.getBaseType()) {
                if (lhsMaybeParameterized.isRawType() != null || rhsMaybeParameterized.isRawType() != null) {
                    return true;
                }
                assert (lhsMaybeParameterized.isRawType() == null && rhsMaybeParameterized.isRawType() == null);
                JParameterizedType lhsParameterized = lhsMaybeParameterized.isParameterized();
                JParameterizedType rhsParameterized = rhsMaybeParameterized.isParameterized();
                assert (lhsParameterized != null && rhsParameterized != null);
                return areTypeArgumentsAssignable(lhsParameterized, rhsParameterized);
            }
        }
        return false;
    }

    /**
   * Returns <code>true</code> if the type arguments of the rhs parameterized
   * type are assignable to the type arguments of the lhs parameterized type.
   */
    private static boolean areTypeArgumentsAssignable(JParameterizedType lhsType, JParameterizedType rhsType) {
        assert (lhsType != rhsType);
        assert (lhsType.getBaseType() == rhsType.getBaseType());
        JClassType[] lhsTypeArgs = lhsType.getTypeArgs();
        JClassType[] rhsTypeArgs = rhsType.getTypeArgs();
        JGenericType lhsBaseType = lhsType.getBaseType();
        JTypeParameter[] lhsTypeParams = lhsBaseType.getTypeParameters();
        for (int i = 0; i < lhsTypeParams.length; ++i) {
            if (!doesTypeArgumentContain(lhsTypeArgs[i], rhsTypeArgs[i])) {
                return false;
            }
        }
        return true;
    }

    /**
   * Returns <code>true</code> if the rhsWildcard can be assigned to the
   * lhsWildcard. This method does not consider supertypes of either lhs or rhs.
   */
    private static boolean areWildcardsAssignable(JWildcardType lhsWildcard, JWildcardType rhsWildcard) {
        assert (lhsWildcard != rhsWildcard);
        assert (lhsWildcard != null && rhsWildcard != null);
        if (lhsWildcard.getLowerBounds().length > 0 && rhsWildcard.getLowerBounds().length > 0) {
            return areClassTypesAssignable(rhsWildcard.getFirstBound(), lhsWildcard.getFirstBound());
        } else if (lhsWildcard.getUpperBounds().length > 0 && lhsWildcard.getLowerBounds().length == 0 && rhsWildcard.getUpperBounds().length > 0 && rhsWildcard.getLowerBounds().length == 0) {
            return areClassTypesAssignable(lhsWildcard.getFirstBound(), rhsWildcard.getFirstBound());
        }
        return false;
    }

    /**
   * A restricted version of areClassTypesAssignable that is used for comparing
   * the type arguments of parameterized types, where the lhsTypeArg is the
   * container.
   */
    private static boolean doesTypeArgumentContain(JClassType lhsTypeArg, JClassType rhsTypeArg) {
        if (lhsTypeArg == rhsTypeArg) {
            return true;
        }
        JWildcardType lhsWildcard = lhsTypeArg.isWildcard();
        JWildcardType rhsWildcard = rhsTypeArg.isWildcard();
        if (lhsWildcard != null) {
            if (rhsWildcard != null) {
                return areWildcardsAssignable(lhsWildcard, rhsWildcard);
            } else {
                if (lhsWildcard.getLowerBounds().length > 0) {
                    return areClassTypesAssignable(rhsTypeArg, lhsWildcard.getFirstBound());
                } else {
                    return areClassTypesAssignable(lhsWildcard.getFirstBound(), rhsTypeArg);
                }
            }
        }
        return false;
    }

    private static void getFlattenedSuperTypeHierarchyRecursive(JClassType type, Set<JClassType> typesSeen) {
        if (typesSeen.contains(type)) {
            return;
        }
        typesSeen.add(type);
        JClassType[] intfs = type.getImplementedInterfaces();
        for (JClassType intf : intfs) {
            typesSeen.addAll(getFlattenedSuperTypeHierarchy(intf));
        }
        JClassType superclass = type.getSuperclass();
        if (superclass != null) {
            typesSeen.addAll(getFlattenedSuperTypeHierarchy(superclass));
        }
    }

    /**
   * Cached set of supertypes for this type (including itself). If null, the set
   * has not been calculated yet.
   */
    private Set<JClassType> flattenedSupertypes;

    /**
   * True if this type may be enhanced with server-only fields. This property is
   * 'sticky' and may be set but not unset, since we need to generate the
   * relevant RPC code for handling the server fields if there is any chance the
   * class will be enhanced.
   */
    private boolean isEnhanced = false;

    public JParameterizedType asParameterizationOf(JGenericType type) {
        Set<JClassType> supertypes = getFlattenedSuperTypeHierarchy(this);
        for (JClassType supertype : supertypes) {
            JParameterizedType isParameterized = supertype.isParameterized();
            if (isParameterized != null && isParameterized.getBaseType() == type) {
                return isParameterized;
            }
            JRawType isRaw = supertype.isRawType();
            if (isRaw != null && isRaw.getBaseType() == type) {
                return isRaw.asParameterizedByWildcards();
            }
        }
        return null;
    }

    /**
   * Find an annotation on a type or on one of its superclasses or
   * superinterfaces.
   * <p>
   * This provides semantics similar to that of
   * {@link java.lang.annotation.Inherited} except that it checks all types to
   * which this type is assignable. {@code @Inherited} only works on
   * superclasses, not superinterfaces.
   * <p>
   * Annotations present on the superclass chain will be returned preferentially
   * over those found in the superinterface hierarchy. Note that the annotation
   * does not need to be tagged with {@code @Inherited} in order to be returned
   * from the superclass chain.
   * 
   * @param annotationType the type of the annotation to look for
   * @return the desired annotation or <code>null</code> if the annotation is
   *         not present in the type's type hierarchy
   */
    public <T extends Annotation> T findAnnotationInTypeHierarchy(Class<T> annotationType) {
        Set<JClassType> seen = new HashSet<JClassType>();
        List<JClassType> searchTypes = new LinkedList<JClassType>();
        searchTypes.add(this);
        T toReturn = null;
        while (!searchTypes.isEmpty()) {
            JClassType current = searchTypes.remove(0);
            if (!seen.add(current)) {
                continue;
            }
            toReturn = current.getAnnotation(annotationType);
            if (toReturn != null) {
                break;
            }
            if (current.getSuperclass() != null) {
                searchTypes.add(0, current.getSuperclass());
            }
            Collections.addAll(searchTypes, current.getImplementedInterfaces());
        }
        return toReturn;
    }

    public abstract JConstructor findConstructor(JType[] paramTypes);

    public abstract JField findField(String name);

    public abstract JMethod findMethod(String name, JType[] paramTypes);

    public abstract JClassType findNestedType(String typeName);

    public abstract <T extends Annotation> T getAnnotation(Class<T> annotationClass);

    public abstract JConstructor getConstructor(JType[] paramTypes) throws NotFoundException;

    public abstract JConstructor[] getConstructors();

    public abstract JClassType getEnclosingType();

    @Override
    public abstract JClassType getErasedType();

    public abstract JField getField(String name);

    public abstract JField[] getFields();

    /**
   * Returns all of the superclasses and superinterfaces for a given type
   * including the type itself. The returned set maintains an internal
   * breadth-first ordering of the type, followed by its interfaces (and their
   * super-interfaces), then the supertype and its interfaces, and so on.
   */
    public Set<JClassType> getFlattenedSupertypeHierarchy() {
        return getFlattenedSuperTypeHierarchy(this);
    }

    public abstract JClassType[] getImplementedInterfaces();

    /**
   * Iterates over the most-derived declaration of each unique inheritable
   * method available in the type hierarchy of the specified type, including
   * those found in superclasses and superinterfaces. A method is inheritable if
   * its accessibility is <code>public</code>, <code>protected</code>, or
   * package protected.
   * 
   * This method offers a convenient way for Generators to find candidate
   * methods to call from a subclass.
   * 
   * @return an array of {@link JMethod} objects representing inheritable
   *         methods
   */
    public abstract JMethod[] getInheritableMethods();

    @Deprecated
    public final String[][] getMetaData(String tagName) {
        return TypeOracle.NO_STRING_ARR_ARR;
    }

    @Deprecated
    public final String[] getMetaDataTags() {
        return TypeOracle.NO_STRINGS;
    }

    public abstract JMethod getMethod(String name, JType[] paramTypes) throws NotFoundException;

    public abstract JMethod[] getMethods();

    public abstract String getName();

    public abstract JClassType getNestedType(String typeName) throws NotFoundException;

    public abstract JClassType[] getNestedTypes();

    public abstract TypeOracle getOracle();

    public abstract JMethod[] getOverloads(String name);

    /**
   * Iterates over the most-derived declaration of each unique overridable
   * method available in the type hierarchy of the specified type, including
   * those found in superclasses and superinterfaces. A method is overridable if
   * it is not <code>final</code> and its accessibility is <code>public</code>,
   * <code>protected</code>, or package protected.
   * 
   * Deferred binding generators often need to generate method implementations;
   * this method offers a convenient way to find candidate methods to implement.
   * 
   * Note that the behavior does not match
   * {@link Class#getMethod(String, Class[])}, which does not return the most
   * derived method in some cases.
   * 
   * @return an array of {@link JMethod} objects representing overridable
   *         methods
   */
    public abstract JMethod[] getOverridableMethods();

    public abstract JPackage getPackage();

    public abstract JClassType[] getSubtypes();

    public abstract JClassType getSuperclass();

    public abstract boolean isAbstract();

    public abstract boolean isAnnotationPresent(Class<? extends Annotation> annotationClass);

    /**
   * Returns <code>true</code> if this {@link JClassType} is assignable from the
   * specified {@link JClassType} parameter.
   * 
   * @param possibleSubtype possible subtype of this {@link JClassType}
   * @return <code>true</code> if this {@link JClassType} is assignable from the
   *         specified {@link JClassType} parameter
   * 
   * @throws NullPointerException if <code>possibleSubtype</code> is
   *           <code>null</code>
   */
    public boolean isAssignableFrom(JClassType possibleSubtype) {
        if (possibleSubtype == null) {
            throw new NullPointerException("possibleSubtype");
        }
        return areClassTypesAssignable(this, possibleSubtype);
    }

    /**
   * Returns <code>true</code> if this {@link JClassType} is assignable to the
   * specified {@link JClassType} parameter.
   * 
   * @param possibleSupertype possible supertype of this {@link JClassType}
   * @return <code>true</code> if this {@link JClassType} is assignable to the
   *         specified {@link JClassType} parameter
   * 
   * @throws NullPointerException if <code>possibleSupertype</code> is
   *           <code>null</code>
   */
    public boolean isAssignableTo(JClassType possibleSupertype) {
        if (possibleSupertype == null) {
            throw new NullPointerException("possibleSupertype");
        }
        return areClassTypesAssignable(possibleSupertype, this);
    }

    /**
   * Determines if the class can be constructed using a simple <code>new</code>
   * operation. Specifically, the class must
   * <ul>
   * <li>be a class rather than an interface,</li>
   * <li>have either no constructors or a parameterless constructor, and</li>
   * <li>be a top-level class or a static nested class.</li>
   * </ul>
   * 
   * @return <code>true</code> if the type is default instantiable, or
   *         <code>false</code> otherwise
   */
    public abstract boolean isDefaultInstantiable();

    /**
   * Returns true if the type may be enhanced on the server to contain extra
   * fields that are unknown to client code.
   * 
   * @return <code>true</code> if the type might be enhanced on the server
   */
    public final boolean isEnhanced() {
        return isEnhanced;
    }

    public abstract boolean isFinal();

    @Override
    public abstract JGenericType isGenericType();

    @Override
    public abstract JClassType isInterface();

    /**
   * @deprecated local types are not modeled
   */
    @Deprecated
    public final boolean isLocalType() {
        return false;
    }

    /**
   * Tests if this type is contained within another type.
   * 
   * @return true if this type has an enclosing type, false if this type is a
   *         top-level type
   */
    public abstract boolean isMemberType();

    public abstract boolean isPrivate();

    public abstract boolean isProtected();

    public abstract boolean isPublic();

    public abstract boolean isStatic();

    /**
   * Indicates that the type may be enhanced on the server to contain extra
   * fields that are unknown to client code.
   * 
   * TODO(rice): find a better way to do this.
   */
    public void setEnhanced() {
        this.isEnhanced = true;
    }

    @Override
    public String toString() {
        return this.getQualifiedSourceName();
    }

    protected abstract void acceptSubtype(JClassType me);

    protected abstract void getInheritableMethodsOnSuperclassesAndThisClass(Map<String, JMethod> methodsBySignature);

    /**
   * Gets the methods declared in interfaces that this type extends. If this
   * type is a class, its own methods are not added. If this type is an
   * interface, its own methods are added. Used internally by
   * {@link #getOverridableMethods()}.
   * 
   * @param methodsBySignature
   */
    protected abstract void getInheritableMethodsOnSuperinterfacesAndMaybeThisInterface(Map<String, JMethod> methodsBySignature);

    protected abstract int getModifierBits();

    protected JMaybeParameterizedType isMaybeParameterizedType() {
        return null;
    }

    /**
   * Tells this type's superclasses and superinterfaces about it.
   */
    protected abstract void notifySuperTypesOf(JClassType me);

    protected abstract void removeSubtype(JClassType me);

    abstract void addConstructor(JConstructor ctor);

    abstract void addField(JField field);

    abstract void addImplementedInterface(JClassType intf);

    abstract void addMethod(JMethod method);

    abstract void addModifierBits(int bits);

    abstract void addNestedType(JClassType type);

    abstract JClassType findNestedTypeImpl(String[] typeName, int index);

    /**
   * Returns all of the annotations declared or inherited by this instance.
   * 
   * NOTE: This method is for testing purposes only.
   */
    abstract Annotation[] getAnnotations();

    /**
   * Returns all of the annotations declared on this instance.
   * 
   * NOTE: This method is for testing purposes only.
   */
    abstract Annotation[] getDeclaredAnnotations();

    @Override
    abstract JClassType getSubstitutedType(JParameterizedType parameterizedType);

    abstract void notifySuperTypes();

    /**
   * Removes references to this instance from all of its super types.
   */
    abstract void removeFromSupertypes();

    abstract void setSuperclass(JClassType type);
}
