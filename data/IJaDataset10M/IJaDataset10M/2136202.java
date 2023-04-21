package com.dustedpixels.asm;

import org.objectweb.asm.ClassVisitor;

/**
 * @author micapolos@gmail.com (Michal Pociecha-Los)
 */
public final class ClassVisitors {

    private static final ClassVisitor NULL_CLASS_VISITOR = new NullClassVisitor();

    public static ClassVisitor nullify() {
        return NULL_CLASS_VISITOR;
    }
}
