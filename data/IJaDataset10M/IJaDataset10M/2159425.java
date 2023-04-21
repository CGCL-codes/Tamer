package net.srcz.jsjvm.core;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public class JsClassVisitor implements ClassVisitor {

    PrintStream ps;

    Set<String> classesRef = new HashSet();

    String name;

    public JsClassVisitor(String name, PrintStream ps) {
        this.ps = ps;
        this.name = name;
    }

    public Set<String> getClassesRef() {
        return classesRef;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        ps.println("with(vm.declareClass(new JavaClass(\'" + name + "\'))) {");
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return new JsAnnotationVisitor(null, desc, classesRef, ps);
    }

    @Override
    public void visitAttribute(Attribute attr) {
        ps.println("// att " + attr.type);
    }

    @Override
    public void visitEnd() {
        ps.println("};");
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        ps.printf("fields[\'%s\'] = %s;\n", name, ConstantConverter.convert(value));
        return new JsFieldVisitor(classesRef, ps);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new JsMethodVisitor(access, name, desc, signature, exceptions, classesRef, ps);
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
    }

    @Override
    public void visitSource(String source, String debug) {
    }
}
