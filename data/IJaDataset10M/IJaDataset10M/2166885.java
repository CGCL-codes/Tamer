package com4j;

import java.lang.reflect.Method;

/**
 * {@link ComMethod} that calls default methods.
 *
 * <p>
 * This is used so that the user cna do {@code foo()}
 * whereas the real COM method invocation goes like
 * {@code foo().bar().zot()}. 
 *
 * @author Kohsuke Kawaguchi
 */
public class DefaultedComMethod extends ComMethod {

    private final int[] vtids;

    private final StandardComMethod last;

    public DefaultedComMethod(Method m, ReturnValue retVal) {
        Class<? extends Com4jObject>[] intermediates = retVal.defaultPropertyThrough();
        vtids = new int[intermediates.length];
        for (int idx = 0; idx < vtids.length; idx++) {
            VTID id = m.getAnnotation(VTID.class);
            assert id != null;
            vtids[idx] = id.value();
            m = findDefaultMethod(intermediates[idx]);
            if (m == null) throw new IllegalAnnotationException(intermediates[idx].getName() + " needs to have a method with @DefaultMethod");
        }
        last = new StandardComMethod(m);
    }

    private Method findDefaultMethod(Class intf) {
        for (Method m : intf.getMethods()) {
            if (m.getAnnotation(DefaultMethod.class) != null) return m;
        }
        return null;
    }

    Object invoke(int ptr, Object[] args) {
        Native.addRef(ptr);
        for (int vtid : vtids) {
            int newPtr = (Integer) Native.invoke(ptr, vtid, EMPTY_ARRAY, EMPTY_INTARRAY, 0, false, NativeType.ComObject.code);
            Native.release(ptr);
            ptr = newPtr;
        }
        Object r = last.invoke(ptr, args);
        Native.release(ptr);
        return r;
    }

    private static final Object[] EMPTY_ARRAY = new Object[0];

    private static final int[] EMPTY_INTARRAY = new int[0];
}
