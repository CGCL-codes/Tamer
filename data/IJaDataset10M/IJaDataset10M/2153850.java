package net.community.chest.reflect.common;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import net.community.chest.reflect.Visibility;
import net.community.chest.util.compare.AbstractComparator;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 * 
 * <P>Compares two methods returning:</P></BR>
 * <UL>
 * 		<LI>0 - if methods have EXACTLY the same signature</LI>
 * 		<LI>positive - if 2nd method is "stronger"</LI>
 * 		<LI>negative - if 1st method is "stronger"</LI>
 * </UL>
 * <P>The "strength" of methods is defined as follows:</P></BR>
 * <UL>
 * 		<LI>null object(s) come first</LI>
 * 		<LI>if not same name, then <U>lexicographical</U> order</LI>
 * 		<LI>the one with <U>less</U> parameters comes first</LI>
 * 		<LI>the one with the "stronger" class in N-th parameter comes first</LI>
 * 		<LI>the one with more visibility comes first</LI>
 * 		<LI>the one implemented by a stronger class comes first</LI>
 * 		<LI>the static one comes last</LI>
 * </UL>
 * <P><B>Note(s):</B> the return type is not taken into account</P>
 * @author Lyor G.
 * @since Sep 14, 2008 9:17:37 AM
 */
public class MethodsComparator extends AbstractComparator<Method> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2679441462185636811L;

    public MethodsComparator(boolean ascending) {
        super(Method.class, !ascending);
    }

    /**
	 * Compares the "isStatic" state of the modifier
	 * @param m1 1st modifier
	 * @param m2 2nd modifier
	 * @return 0 if same, (+1) if 1st static, (-1) if 2nd static
	 */
    public static final int compareStaticNess(final int m1, final int m2) {
        if (Modifier.isStatic(m1)) return Modifier.isStatic(m2) ? 0 : (+1); else return Modifier.isStatic(m2) ? (-1) : 0;
    }

    public static final int compareStaticNess(final Method m1, final Method m2) {
        return compareStaticNess((null == m1) ? 0 : m1.getModifiers(), (null == m2) ? 0 : m2.getModifiers());
    }

    public static final int compareParameters(final Class<?>[] p1, final Class<?>[] p2) {
        final int p1Num = (null == p1) ? 0 : p1.length, p2Num = (null == p2) ? 0 : p2.length;
        if (p1Num != p2Num) return (p1Num < p2Num) ? (-1) : (+1);
        for (int pIndex = 0; pIndex < p1Num; pIndex++) {
            final Class<?> p1c = p1[pIndex], p2c = p2[pIndex];
            final int nComp = ClassesComparator.compareClasses(p1c, p2c);
            if (nComp != 0) return nComp;
        }
        return 0;
    }

    public static final int compareParameters(final Method m1, final Method m2) {
        return compareParameters((null == m1) ? null : m1.getParameterTypes(), (null == m2) ? null : m2.getParameterTypes());
    }

    /**
	 * Compares the 2 methods according to specified logic
	 * @param m1 first method
	 * @param m2 second method
	 * @return according to specified logic
	 * @throws ClassCastException if methods not from compatible classes
	 */
    public static final int compareMethods(final Method m1, final Method m2) throws ClassCastException {
        if (null == m1) return (null == m2) ? 0 : (-1); else if (null == m2) return (+1);
        final Class<?> c1 = m1.getDeclaringClass(), c2 = m2.getDeclaringClass();
        if ((!c1.isAssignableFrom(c2)) && (!c2.isAssignableFrom(c1))) throw new ClassCastException("compareMethods(" + m1.getName() + "," + m2.getName() + ") not of compatible classes: " + c1.getName() + " <> " + c2.getName());
        {
            final String n1 = m1.getName(), n2 = m2.getName();
            final int nComp = n1.compareTo(n2);
            if (nComp != 0) return nComp;
        }
        {
            final int nComp = compareParameters(m1, m2);
            if (nComp != 0) return nComp;
        }
        {
            final int vComp = Visibility.compareVisibility(m1, m2);
            if (vComp != 0) return vComp;
        }
        {
            final int cComp = ClassesComparator.compareClasses(c1, c2);
            if (cComp != 0) return cComp;
        }
        return compareStaticNess(m1, m2);
    }

    @Override
    public int compareValues(final Method o1, final Method o2) throws ClassCastException {
        return compareMethods(o1, o2);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof MethodsComparator);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    /**
	 * A globally allocated comparator(s)
	 */
    public static final MethodsComparator ASCENDING = new MethodsComparator(true), DESCENDING = new MethodsComparator(false);
}
