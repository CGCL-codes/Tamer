package org.dynalang.mop.beans;

import java.lang.reflect.Member;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Attila Szegedi
 * @version $Id: $
 * @param <T>
 */
final class ClassString<T extends Member> {

    private final Class<?>[] classes;

    ClassString(Class<?>[] classes) {
        this.classes = classes;
    }

    Class<?>[] getClasses() {
        return classes;
    }

    private static final int MORE_SPECIFIC = 0;

    private static final int LESS_SPECIFIC = 1;

    private static final int INDETERMINATE = 2;

    Object getMostSpecific(List<T> methods, boolean varArg) {
        LinkedList<T> applicables = getApplicables(methods, varArg);
        if (applicables.isEmpty()) {
            return OverloadedDynamicMethod.NO_SUCH_METHOD;
        }
        if (applicables.size() == 1) {
            return applicables.getFirst();
        }
        LinkedList<T> maximals = new LinkedList<T>();
        for (T applicable : applicables) {
            Class<?>[] appArgs = DynamicMethod.getParameterTypes(applicable);
            boolean lessSpecific = false;
            for (Iterator<T> maximal = maximals.iterator(); maximal.hasNext(); ) {
                Member max = maximal.next();
                Class<?>[] maxArgs = DynamicMethod.getParameterTypes(max);
                switch(moreSpecific(appArgs, maxArgs, varArg)) {
                    case MORE_SPECIFIC:
                        {
                            maximal.remove();
                            break;
                        }
                    case LESS_SPECIFIC:
                        {
                            lessSpecific = true;
                            break;
                        }
                }
            }
            if (!lessSpecific) {
                maximals.addLast(applicable);
            }
        }
        if (maximals.size() > 1) {
            return OverloadedDynamicMethod.AMBIGUOUS_METHOD;
        }
        return maximals.getFirst();
    }

    private static int moreSpecific(Class<?>[] c1, Class<?>[] c2, boolean varArg) {
        boolean c1MoreSpecific = false;
        boolean c2MoreSpecific = false;
        final int cl1 = c1.length;
        final int cl2 = c2.length;
        assert varArg || cl1 == cl2;
        for (int i = 0; i < cl1; ++i) {
            Class<?> class1 = getClass(c1, cl1, i, varArg);
            Class<?> class2 = getClass(c2, cl2, i, varArg);
            if (class1 != class2) {
                c1MoreSpecific = c1MoreSpecific || OverloadedMethodUtilities.isMoreSpecific(class1, class2);
                c2MoreSpecific = c2MoreSpecific || OverloadedMethodUtilities.isMoreSpecific(class2, class1);
            }
        }
        if (c1MoreSpecific) {
            if (c2MoreSpecific) {
                return INDETERMINATE;
            }
            return MORE_SPECIFIC;
        }
        if (c2MoreSpecific) {
            return LESS_SPECIFIC;
        }
        return INDETERMINATE;
    }

    private static Class<?> getClass(Class<?>[] classes, int l, int i, boolean varArg) {
        return varArg && i >= l - 1 ? classes[l - 1].getComponentType() : classes[i];
    }

    /**
     * Returns all methods that are applicable to actual
     * parameter classes represented by this ClassString object.
     */
    LinkedList<T> getApplicables(List<T> methods, boolean varArg) {
        LinkedList<T> list = new LinkedList<T>();
        for (T member : methods) {
            if (isApplicable(member, varArg)) {
                list.add(member);
            }
        }
        return list;
    }

    /**
     * Returns true if the supplied method is applicable to actual
     * parameter classes represented by this ClassString object.
     * 
     */
    private boolean isApplicable(T member, boolean varArg) {
        final Class<?>[] formalTypes = DynamicMethod.getParameterTypes(member);
        final int cl = classes.length;
        final int fl = formalTypes.length - (varArg ? 1 : 0);
        if (varArg) {
            if (cl < fl) {
                return false;
            }
        } else {
            if (cl != fl) {
                return false;
            }
        }
        for (int i = 0; i < fl; ++i) {
            if (!isMethodInvocationConvertible(formalTypes[i], classes[i])) {
                return false;
            }
        }
        if (varArg) {
            Class<?> varArgType = formalTypes[fl].getComponentType();
            for (int i = fl; i < cl; ++i) {
                if (!isMethodInvocationConvertible(varArgType, classes[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines whether a type represented by a class object is
     * convertible to another type represented by a class object using a 
     * method invocation conversion, treating object types of primitive 
     * types as if they were primitive types (that is, a Boolean actual 
     * parameter type matches boolean primitive formal type). This behavior
     * is because this method is used to determine applicable methods for 
     * an actual parameter list, and primitive types are represented by 
     * their object duals in reflective method calls.
     * @param formal the formal parameter type to which the actual 
     * parameter type should be convertible
     * @param actual the actual parameter type.
     * @return true if either formal type is assignable from actual type, 
     * or formal is a primitive type and actual is its corresponding object
     * type or an object type of a primitive type that can be converted to
     * the formal type.
     */
    static boolean isMethodInvocationConvertible(Class<?> formal, Class<?> actual) {
        if (formal.isAssignableFrom(actual)) {
            return true;
        }
        if (formal.isPrimitive()) {
            if (formal == Boolean.TYPE) return actual == Boolean.class;
            if (formal == Character.TYPE) return actual == Character.class;
            if (formal == Byte.TYPE && actual == Byte.class) return true;
            if (formal == Short.TYPE && (actual == Short.class || actual == Byte.class)) return true;
            if (formal == Integer.TYPE && (actual == Integer.class || actual == Short.class || actual == Byte.class)) return true;
            if (formal == Long.TYPE && (actual == Long.class || actual == Integer.class || actual == Short.class || actual == Byte.class)) return true;
            if (formal == Float.TYPE && (actual == Float.class || actual == Long.class || actual == Integer.class || actual == Short.class || actual == Byte.class)) return true;
            if (formal == Double.TYPE && (actual == Double.class || actual == Float.class || actual == Long.class || actual == Integer.class || actual == Short.class || actual == Byte.class)) return true;
        }
        return false;
    }
}
