package com.google.gwt.dev.shell.rewrite;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Performs any rewriting necessary to ensure that class files are 1.5
 * compatible.
 */
public class ForceClassVersion15 extends ClassAdapter {

    public ForceClassVersion15(ClassVisitor v) {
        super(v);
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        assert (version >= Opcodes.V1_5 && version <= Opcodes.V1_6);
        super.visit(Opcodes.V1_5, access, name, signature, superName, interfaces);
    }
}
