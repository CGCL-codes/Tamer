package com.ibm.wala.util.ref;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import com.ibm.wala.util.debug.Assertions;

/**
 * A factory for References ... useful for debugging.
 */
public final class CacheReference {

    private static final byte SOFT = 0;

    private static final byte WEAK = 1;

    private static final byte HARD = 2;

    private static final byte choice = SOFT;

    public static final Object make(final Object referent) {
        switch(choice) {
            case SOFT:
                return new SoftReference<Object>(referent);
            case WEAK:
                return new WeakReference<Object>(referent);
            case HARD:
                return referent;
            default:
                Assertions.UNREACHABLE();
                return null;
        }
    }

    public static final Object get(final Object reference) throws IllegalArgumentException {
        if (reference == null) {
            return null;
        }
        switch(choice) {
            case SOFT:
                if (!(reference instanceof java.lang.ref.SoftReference)) {
                    throw new IllegalArgumentException("not ( reference instanceof java.lang.ref.SoftReference ) ");
                }
                return ((SoftReference) reference).get();
            case WEAK:
                return ((WeakReference) reference).get();
            case HARD:
                return reference;
            default:
                Assertions.UNREACHABLE();
                return null;
        }
    }
}
