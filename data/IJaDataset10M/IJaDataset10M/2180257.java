package com.google.devtools.build.wireless.testing.java.injector;

import org.objectweb.asm.commons.EmptyVisitor;

/**
 * Monitor class used to expose parameters which are passing through the 
 * adaptation chain.
 * 
 * @author Michele Sama
 *
 */
public class SpyClassVisitor extends EmptyVisitor {

    public int version;

    public int opcode;

    public int access;

    public String superClassName;

    public String signature;

    public String className;

    public String methodName;

    public String ownerClassName;

    public String[] interfaces;

    public String description;

    /**
   * Keeps a reference to the name of the given parameters.
   * 
   * @see EmptyVisitor#visit(int, int, String, String, String, String[])
   */
    @Override
    public void visit(int version, int access, String className, String signature, String superClassName, String[] interfaces) {
        this.version = version;
        this.access = access;
        this.className = className;
        this.signature = signature;
        this.superClassName = superClassName;
        this.interfaces = interfaces;
        super.visit(version, access, className, signature, superClassName, interfaces);
    }

    /**
   * Keeps a reference to the name of the given parameters.
   * 
   * @see EmptyVisitor#visitMethodInsn(int, String, String, String)
   */
    @Override
    public void visitMethodInsn(int opcode, String ownerClassName, String methodName, String description) {
        this.opcode = opcode;
        this.ownerClassName = ownerClassName;
        this.methodName = methodName;
        this.description = description;
        super.visitMethodInsn(opcode, ownerClassName, methodName, description);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        this.opcode = opcode;
        this.ownerClassName = type;
        super.visitTypeInsn(opcode, type);
    }
}
