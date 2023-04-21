package com.google.devtools.depan.java.bytecode.impl;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;

/**
 * Implements a visitor of the ASM package, to find the dependencies in a Field
 * and build the dependency tree. A single {@link FieldDepLister} is used
 * for each field found in a class.
 * 
 * To build the dependencies tree, it calls the methods of a
 * DependenciesListener.
 * 
 * @author ycoppel@google.com (Yohann Coppel)
 * 
 */
public class FieldDepLister implements FieldVisitor {

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return null;
    }

    public void visitAttribute(Attribute attr) {
    }

    public void visitEnd() {
    }
}
