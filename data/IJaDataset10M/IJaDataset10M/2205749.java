package org.jmlspecs.jml4.rac;

import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;

/**
 * Adapted from org.eclipse.jdt.core.dom.Modifier.
 * 
 * @author Patrice Chalin, DSRG.org.
 * 
 */
public class Modifier {

    /**
	 * Modifier constant (bit mask, value 0) indicating no modifiers.
	 */
    public static final int NONE = 0x0000;

    public static boolean isAbstract(int flags) {
        return (flags & ClassFileConstants.AccAbstract) != 0;
    }

    public static boolean isFinal(int flags) {
        return (flags & ClassFileConstants.AccFinal) != 0;
    }

    public static boolean isNative(int flags) {
        return (flags & ClassFileConstants.AccNative) != 0;
    }

    public static boolean isPrivate(int flags) {
        return (flags & ClassFileConstants.AccPrivate) != 0;
    }

    public static boolean isProtected(int flags) {
        return (flags & ClassFileConstants.AccProtected) != 0;
    }

    public static boolean isPublic(int flags) {
        return (flags & ClassFileConstants.AccPublic) != 0;
    }

    public static boolean isStatic(int flags) {
        return (flags & ClassFileConstants.AccStatic) != 0;
    }

    public static boolean isStrictfp(int flags) {
        return (flags & ClassFileConstants.AccStrictfp) != 0;
    }

    public static boolean isSynchronized(int flags) {
        return (flags & ClassFileConstants.AccSynchronized) != 0;
    }

    public static boolean isTransient(int flags) {
        return (flags & ClassFileConstants.AccTransient) != 0;
    }

    public static boolean isVolatile(int flags) {
        return (flags & ClassFileConstants.AccVolatile) != 0;
    }
}
