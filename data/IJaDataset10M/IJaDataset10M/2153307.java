package org.comparator4j.comparator;

import java.lang.reflect.Method;
import java.util.List;
import org.comparator4j.Comparator;
import org.comparator4j.CompareContext;
import org.comparator4j.CompareException;
import org.comparator4j.util.ReflectUtil;
import org.comparator4j.util.SuperClassFinder;

public class BasicComparator implements Comparator {

    public Result compare(Object o1, Object o2, CompareContext context) throws CompareException {
        Class<? extends Object> superClaz = SuperClassFinder.findSuperClass(o1, o2);
        if (superClaz != null && Comparable.class.isAssignableFrom(superClaz)) {
            return compare(o1, o2, superClaz);
        }
        return context.compare(o1, o2);
    }

    private Result compare(Object o1, Object o2, Class<? extends Object> superClaz) throws CompareException {
        try {
            Method compare = superClaz.getDeclaredMethod("compareTo", superClaz);
            int r = Integer.class.cast(ReflectUtil.invoke(compare, o1, o2)).intValue();
            if (r == 0) {
                return Result.EQ;
            } else if (r < 0) {
                return Result.LT;
            } else {
                return Result.GT;
            }
        } catch (SecurityException e) {
            throw new CompareException(e);
        } catch (NoSuchMethodException e) {
            throw new CompareException(e);
        }
    }

    public static void main(String[] args) throws SecurityException, NoSuchMethodException {
        System.out.println(List.class.getDeclaredMethod("contains", Object.class));
        System.out.println(List.class.getDeclaredMethod("contains", String.class));
    }
}
