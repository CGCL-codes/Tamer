package com.mycila.plugin.spi.model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Static methods for working with types that we aren't publishing in the
 * public {@code Types} API.
 *
 * @author jessewilson@google.com (Jesse Wilson)
 */
class MoreTypes {

    public static final Type[] EMPTY_TYPE_ARRAY = new Type[] {};

    private MoreTypes() {
    }

    private static final Map<TypeLiteral<?>, TypeLiteral<?>> PRIMITIVE_TO_WRAPPER = new HashMap<TypeLiteral<?>, TypeLiteral<?>>() {

        {
            put(TypeLiteral.get(boolean.class), TypeLiteral.get(Boolean.class));
            put(TypeLiteral.get(byte.class), TypeLiteral.get(Byte.class));
            put(TypeLiteral.get(short.class), TypeLiteral.get(Short.class));
            put(TypeLiteral.get(int.class), TypeLiteral.get(Integer.class));
            put(TypeLiteral.get(long.class), TypeLiteral.get(Long.class));
            put(TypeLiteral.get(float.class), TypeLiteral.get(Float.class));
            put(TypeLiteral.get(double.class), TypeLiteral.get(Double.class));
            put(TypeLiteral.get(char.class), TypeLiteral.get(Character.class));
            put(TypeLiteral.get(void.class), TypeLiteral.get(Void.class));
        }
    };

    /**
     * Returns an type that's appropriate for use in a key.
     * <p>If the type is a primitive, the corresponding wrapper type will be returned.
     *
     * @throws IllegalArgumentException if {@code type} contains a type variable
     */
    public static <T> TypeLiteral<T> canonicalizeForKey(TypeLiteral<T> typeLiteral) {
        Type type = typeLiteral.getType();
        if (!isFullySpecified(type)) {
            throw new IllegalArgumentException(String.format("%s cannot be used as a key; It is not fully specified.", typeLiteral));
        }
        @SuppressWarnings("unchecked") TypeLiteral<T> wrappedPrimitives = (TypeLiteral<T>) PRIMITIVE_TO_WRAPPER.get(typeLiteral);
        return wrappedPrimitives != null ? wrappedPrimitives : typeLiteral;
    }

    /**
     * Returns true if {@code type} is free from type variables.
     */
    private static boolean isFullySpecified(Type type) {
        if (type instanceof Class) {
            return true;
        } else if (type instanceof CompositeType) {
            return ((CompositeType) type).isFullySpecified();
        } else return !(type instanceof TypeVariable) && ((CompositeType) canonicalize(type)).isFullySpecified();
    }

    /**
     * Returns a type that is functionally equal but not necessarily equal
     * according to {@link Object#equals(Object) Object.equals()}. The returned
     * type is {@link Serializable}.
     */
    public static Type canonicalize(Type type) {
        if (type instanceof Class) {
            Class<?> c = (Class<?>) type;
            return c.isArray() ? new GenericArrayTypeImpl(canonicalize(c.getComponentType())) : c;
        } else if (type instanceof CompositeType) {
            return type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) type;
            return new ParameterizedTypeImpl(p.getOwnerType(), p.getRawType(), p.getActualTypeArguments());
        } else if (type instanceof GenericArrayType) {
            GenericArrayType g = (GenericArrayType) type;
            return new GenericArrayTypeImpl(g.getGenericComponentType());
        } else if (type instanceof WildcardType) {
            WildcardType w = (WildcardType) type;
            return new WildcardTypeImpl(w.getUpperBounds(), w.getLowerBounds());
        } else {
            return type;
        }
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            Assert.checkArgument(rawType instanceof Class, "Expected a Class, but <%s> is of type %s", type, type.getClass().getName());
            return (Class<?>) rawType;
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        } else if (type instanceof TypeVariable) {
            return Object.class;
        } else {
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or " + "GenericArrayType, but <" + type + "> is of type " + type.getClass().getName());
        }
    }

    /**
     * Returns true if {@code a} and {@code b} are equal.
     */
    public static boolean equals(Type a, Type b) {
        if (a == b) {
            return true;
        } else if (a instanceof Class) {
            return a.equals(b);
        } else if (a instanceof ParameterizedType) {
            if (!(b instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType pa = (ParameterizedType) a;
            ParameterizedType pb = (ParameterizedType) b;
            return (pa.getOwnerType() == pb.getOwnerType() || (pa.getOwnerType() != null && pa.getOwnerType().equals(pb.getOwnerType()))) && pa.getRawType().equals(pb.getRawType()) && Arrays.equals(pa.getActualTypeArguments(), pb.getActualTypeArguments());
        } else if (a instanceof GenericArrayType) {
            if (!(b instanceof GenericArrayType)) {
                return false;
            }
            GenericArrayType ga = (GenericArrayType) a;
            GenericArrayType gb = (GenericArrayType) b;
            return equals(ga.getGenericComponentType(), gb.getGenericComponentType());
        } else if (a instanceof WildcardType) {
            if (!(b instanceof WildcardType)) {
                return false;
            }
            WildcardType wa = (WildcardType) a;
            WildcardType wb = (WildcardType) b;
            return Arrays.equals(wa.getUpperBounds(), wb.getUpperBounds()) && Arrays.equals(wa.getLowerBounds(), wb.getLowerBounds());
        } else if (a instanceof TypeVariable) {
            if (!(b instanceof TypeVariable)) {
                return false;
            }
            TypeVariable<?> va = (TypeVariable) a;
            TypeVariable<?> vb = (TypeVariable) b;
            return va.getGenericDeclaration() == vb.getGenericDeclaration() && va.getName().equals(vb.getName());
        } else {
            return false;
        }
    }

    private static int hashCodeOrZero(Object o) {
        return o != null ? o.hashCode() : 0;
    }

    public static String typeToString(Type type) {
        return type instanceof Class ? ((Class) type).getName() : type.toString();
    }

    /**
     * Returns {@code Field.class}, {@code Method.class} or {@code Constructor.class}.
     */
    public static Class<? extends Member> memberType(Member member) {
        Assert.checkNotNull(member, "member");
        if (member instanceof Field) {
            return Field.class;
        } else if (member instanceof Method) {
            return Method.class;
        } else if (member instanceof Constructor) {
            return Constructor.class;
        } else {
            throw new IllegalArgumentException("Unsupported implementation class for Member, " + member.getClass());
        }
    }

    /**
     * Formats a member as concise string, such as {@code java.util.ArrayList.size},
     * {@code java.util.ArrayList<init>()} or {@code java.util.List.remove()}.
     */
    public static String toString(Member member) {
        Class<? extends Member> memberType = memberType(member);
        if (memberType == Method.class) {
            return member.getDeclaringClass().getName() + "." + member.getName() + "()";
        } else if (memberType == Field.class) {
            return member.getDeclaringClass().getName() + "." + member.getName();
        } else if (memberType == Constructor.class) {
            return member.getDeclaringClass().getName() + ".<init>()";
        } else {
            throw new AssertionError();
        }
    }

    /**
     * Returns the generic supertype for {@code supertype}. For example, given a class {@code
     * IntegerSet}, the result for when supertype is {@code Set.class} is {@code Set<Integer>} and the
     * result when the supertype is {@code Collection.class} is {@code Collection<Integer>}.
     */
    public static Type getGenericSupertype(Type type, Class<?> rawType, Class<?> toResolve) {
        if (toResolve == rawType) {
            return type;
        }
        if (toResolve.isInterface()) {
            Class[] interfaces = rawType.getInterfaces();
            for (int i = 0, length = interfaces.length; i < length; i++) {
                if (interfaces[i] == toResolve) {
                    return rawType.getGenericInterfaces()[i];
                } else if (toResolve.isAssignableFrom(interfaces[i])) {
                    return getGenericSupertype(rawType.getGenericInterfaces()[i], interfaces[i], toResolve);
                }
            }
        }
        if (!rawType.isInterface()) {
            while (rawType != Object.class) {
                Class<?> rawSupertype = rawType.getSuperclass();
                if (rawSupertype == toResolve) {
                    return rawType.getGenericSuperclass();
                } else if (toResolve.isAssignableFrom(rawSupertype)) {
                    return getGenericSupertype(rawType.getGenericSuperclass(), rawSupertype, toResolve);
                }
                rawType = rawSupertype;
            }
        }
        return toResolve;
    }

    public static Type resolveTypeVariable(Type type, Class<?> rawType, TypeVariable unknown) {
        Class<?> declaredByRaw = declaringClassOf(unknown);
        if (declaredByRaw == null) {
            return unknown;
        }
        Type declaredBy = getGenericSupertype(type, rawType, declaredByRaw);
        if (declaredBy instanceof ParameterizedType) {
            int index = indexOf(declaredByRaw.getTypeParameters(), unknown);
            return ((ParameterizedType) declaredBy).getActualTypeArguments()[index];
        }
        return unknown;
    }

    private static int indexOf(Object[] array, Object toFind) {
        for (int i = 0; i < array.length; i++) {
            if (toFind.equals(array[i])) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * Returns the declaring class of {@code typeVariable}, or {@code null} if it was not declared by
     * a class.
     */
    private static Class<?> declaringClassOf(TypeVariable typeVariable) {
        GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
        return genericDeclaration instanceof Class ? (Class<?>) genericDeclaration : null;
    }

    public static class ParameterizedTypeImpl implements ParameterizedType, Serializable, CompositeType {

        private final Type ownerType;

        private final Type rawType;

        private final Type[] typeArguments;

        public ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
            if (rawType instanceof Class<?>) {
                Class rawTypeAsClass = (Class) rawType;
                Assert.checkArgument(ownerType != null || rawTypeAsClass.getEnclosingClass() == null, "No owner type for enclosed %s", rawType);
                Assert.checkArgument(ownerType == null || rawTypeAsClass.getEnclosingClass() != null, "Owner type for unenclosed %s", rawType);
            }
            this.ownerType = ownerType == null ? null : canonicalize(ownerType);
            this.rawType = canonicalize(rawType);
            this.typeArguments = typeArguments.clone();
            for (int t = 0; t < this.typeArguments.length; t++) {
                Assert.checkNotNull(this.typeArguments[t], "type parameter");
                checkNotPrimitive(this.typeArguments[t], "type parameters");
                this.typeArguments[t] = canonicalize(this.typeArguments[t]);
            }
        }

        public Type[] getActualTypeArguments() {
            return typeArguments.clone();
        }

        public Type getRawType() {
            return rawType;
        }

        public Type getOwnerType() {
            return ownerType;
        }

        public boolean isFullySpecified() {
            if (ownerType != null && !MoreTypes.isFullySpecified(ownerType)) {
                return false;
            }
            if (!MoreTypes.isFullySpecified(rawType)) {
                return false;
            }
            for (Type type : typeArguments) {
                if (!MoreTypes.isFullySpecified(type)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof ParameterizedType && MoreTypes.equals(this, (ParameterizedType) other);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(typeArguments) ^ rawType.hashCode() ^ hashCodeOrZero(ownerType);
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(30 * (typeArguments.length + 1));
            stringBuilder.append(typeToString(rawType));
            if (typeArguments.length == 0) {
                return stringBuilder.toString();
            }
            stringBuilder.append("<").append(typeToString(typeArguments[0]));
            for (int i = 1; i < typeArguments.length; i++) {
                stringBuilder.append(", ").append(typeToString(typeArguments[i]));
            }
            return stringBuilder.append(">").toString();
        }

        private static final long serialVersionUID = 0;
    }

    public static class GenericArrayTypeImpl implements GenericArrayType, Serializable, CompositeType {

        private final Type componentType;

        public GenericArrayTypeImpl(Type componentType) {
            this.componentType = canonicalize(componentType);
        }

        public Type getGenericComponentType() {
            return componentType;
        }

        public boolean isFullySpecified() {
            return MoreTypes.isFullySpecified(componentType);
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof GenericArrayType && MoreTypes.equals(this, (GenericArrayType) o);
        }

        @Override
        public int hashCode() {
            return componentType.hashCode();
        }

        @Override
        public String toString() {
            return typeToString(componentType) + "[]";
        }

        private static final long serialVersionUID = 0;
    }

    /**
     * The WildcardType interface supports multiple upper bounds and multiple
     * lower bounds. We only support what the Java 6 language needs - at most one
     * bound. If a lower bound is set, the upper bound must be Object.class.
     */
    public static class WildcardTypeImpl implements WildcardType, Serializable, CompositeType {

        private final Type upperBound;

        private final Type lowerBound;

        public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
            Assert.checkArgument(lowerBounds.length <= 1, "Must have at most one lower bound.");
            Assert.checkArgument(upperBounds.length == 1, "Must have exactly one upper bound.");
            if (lowerBounds.length == 1) {
                Assert.checkNotNull(lowerBounds[0], "lowerBound");
                checkNotPrimitive(lowerBounds[0], "wildcard bounds");
                Assert.checkArgument(upperBounds[0] == Object.class, "bounded both ways");
                this.lowerBound = canonicalize(lowerBounds[0]);
                this.upperBound = Object.class;
            } else {
                Assert.checkNotNull(upperBounds[0], "upperBound");
                checkNotPrimitive(upperBounds[0], "wildcard bounds");
                this.lowerBound = null;
                this.upperBound = canonicalize(upperBounds[0]);
            }
        }

        public Type[] getUpperBounds() {
            return new Type[] { upperBound };
        }

        public Type[] getLowerBounds() {
            return lowerBound != null ? new Type[] { lowerBound } : EMPTY_TYPE_ARRAY;
        }

        public boolean isFullySpecified() {
            return MoreTypes.isFullySpecified(upperBound) && (lowerBound == null || MoreTypes.isFullySpecified(lowerBound));
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof WildcardType && MoreTypes.equals(this, (WildcardType) other);
        }

        @Override
        public int hashCode() {
            return (lowerBound != null ? 31 + lowerBound.hashCode() : 1) ^ (31 + upperBound.hashCode());
        }

        @Override
        public String toString() {
            if (lowerBound != null) {
                return "? super " + typeToString(lowerBound);
            } else if (upperBound == Object.class) {
                return "?";
            } else {
                return "? extends " + typeToString(upperBound);
            }
        }

        private static final long serialVersionUID = 0;
    }

    private static void checkNotPrimitive(Type type, String use) {
        Assert.checkArgument(!(type instanceof Class<?>) || !((Class) type).isPrimitive(), "Primitive types are not allowed in %s: %s", use, type);
    }

    /**
     * A type formed from other types, such as arrays, parameterized types or wildcard types
     */
    private interface CompositeType {

        /**
         * Returns true if there are no type variables in this type.
         */
        boolean isFullySpecified();
    }
}
