package org.aspectme.cldc.reflect.util;

import java.util.ArrayList;
import java.util.List;
import org.aspectme.cldc.reflect.model.ReflectClass;
import org.aspectme.cldc.reflect.model.ReflectClassLoader;
import org.aspectme.cldc.reflect.model.ReflectConstructor;
import org.aspectme.cldc.reflect.model.ReflectMethod;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.signature.SignatureWriter;

public class Signature {

    public static int countParameterTypes(String desc) {
        ParameterCounter sv = new ParameterCounter();
        SignatureReader reader = new SignatureReader(desc);
        reader.accept(sv);
        return sv.getParameterCount();
    }

    public static ReflectClass[] getParameterTypes(String desc, ReflectClassLoader classLoader) {
        MethodSignatureVisitor sv = new MethodSignatureVisitor(classLoader);
        SignatureReader reader = new SignatureReader(desc);
        reader.accept(sv);
        return sv.getParameterTypes();
    }

    public static String getSignature(ReflectMethod method) {
        SignatureWriter sv = new SignatureWriter();
        ReflectClass[] paramTypes = method.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            sv.visitParameterType();
            ReflectClass paramType = paramTypes[i];
            writeType(sv, paramType);
        }
        sv.visitReturnType();
        writeType(sv, method.getReturnType());
        return sv.toString();
    }

    public static String getSignature(ReflectConstructor constructor) {
        SignatureWriter sv = new SignatureWriter();
        ReflectClass[] paramTypes = constructor.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            sv.visitParameterType();
            ReflectClass paramType = paramTypes[i];
            writeType(sv, paramType);
        }
        sv.visitReturnType();
        sv.visitBaseType('V');
        return sv.toString();
    }

    private static void writeType(SignatureWriter sv, ReflectClass type) {
        if (type.isArray()) {
            sv.visitArrayType();
            writeType(sv, type.getComponentType());
            return;
        }
        if (type.isPrimitive()) {
            sv.visitBaseType(getBaseType(type));
        } else {
            sv.visitClassType(type.getName().replace('.', '/'));
            sv.visitEnd();
        }
    }

    private static char getBaseType(ReflectClass clazz) {
        Class<?> type = clazz.toPrimitiveType();
        if (type == Void.TYPE) {
            return 'V';
        }
        if (type == Boolean.TYPE) {
            return 'Z';
        }
        if (type == Byte.TYPE) {
            return 'B';
        }
        if (type == Character.TYPE) {
            return 'C';
        } else if (type == Short.TYPE) {
            return 'S';
        } else if (type == Integer.TYPE) {
            return 'I';
        } else if (type == Long.TYPE) {
            return 'J';
        }
        if (type == Float.TYPE) {
            return 'F';
        }
        if (type == Double.TYPE) {
            return 'D';
        }
        throw new IllegalArgumentException("Class " + clazz.getName() + " is not a primitive type");
    }

    static class MethodSignatureVisitor implements SignatureVisitor {

        private List<ReflectClass> classes = new ArrayList<ReflectClass>();

        private ReflectClass currentType;

        private ReflectClassLoader classLoader;

        private boolean isArray;

        public MethodSignatureVisitor(ReflectClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        public ReflectClass[] getParameterTypes() {
            return (ReflectClass[]) classes.toArray(new ReflectClass[classes.size()]);
        }

        public SignatureVisitor visitArrayType() {
            isArray = true;
            return this;
        }

        public void visitBaseType(char descriptor) {
            switch(descriptor) {
                case 'V':
                    currentType = classLoader.loadClass("void");
                    break;
                case 'B':
                    currentType = classLoader.loadClass("byte");
                    break;
                case 'J':
                    currentType = classLoader.loadClass("long");
                    break;
                case 'Z':
                    currentType = classLoader.loadClass("boolean");
                    break;
                case 'I':
                    currentType = classLoader.loadClass("int");
                    break;
                case 'S':
                    currentType = classLoader.loadClass("short");
                    break;
                case 'C':
                    currentType = classLoader.loadClass("char");
                    break;
                case 'F':
                    currentType = classLoader.loadClass("float");
                    break;
                default:
                    currentType = classLoader.loadClass("double");
                    break;
            }
            if (currentType == null) {
                System.out.println("COULD NOT LOAD PRIM TYPE " + descriptor);
            }
            endType();
        }

        private void endType() {
            if (isArray) {
                currentType = currentType.asArray();
                if (currentType == null) {
                    System.out.println("COULD NOT LOAD ARRAY TYPE");
                }
            }
            classes.add(currentType);
            currentType = null;
            isArray = false;
        }

        public SignatureVisitor visitClassBound() {
            return this;
        }

        public void visitClassType(String name) {
            currentType = classLoader.loadClass(name.replace('/', '.'));
            if (currentType == null) {
                System.out.println("COULD NOT LOAD " + name);
            }
            endType();
        }

        public void visitEnd() {
        }

        public SignatureVisitor visitExceptionType() {
            return new EmptySignatureVisitor();
        }

        public void visitFormalTypeParameter(String name) {
        }

        public void visitInnerClassType(String name) {
        }

        public SignatureVisitor visitInterface() {
            return new EmptySignatureVisitor();
        }

        public SignatureVisitor visitInterfaceBound() {
            return new EmptySignatureVisitor();
        }

        public SignatureVisitor visitParameterType() {
            return this;
        }

        public SignatureVisitor visitReturnType() {
            return new EmptySignatureVisitor();
        }

        public SignatureVisitor visitSuperclass() {
            return new EmptySignatureVisitor();
        }

        public void visitTypeArgument() {
        }

        public SignatureVisitor visitTypeArgument(char wildcard) {
            return new EmptySignatureVisitor();
        }

        public void visitTypeVariable(String name) {
        }
    }

    static class ParameterCounter implements SignatureVisitor {

        private int paramCount;

        public ParameterCounter() {
        }

        public int getParameterCount() {
            return paramCount;
        }

        public SignatureVisitor visitArrayType() {
            return this;
        }

        public void visitBaseType(char descriptor) {
        }

        public SignatureVisitor visitClassBound() {
            return this;
        }

        public void visitClassType(String name) {
        }

        public void visitEnd() {
        }

        public SignatureVisitor visitExceptionType() {
            return new EmptySignatureVisitor();
        }

        public void visitFormalTypeParameter(String name) {
        }

        public void visitInnerClassType(String name) {
        }

        public SignatureVisitor visitInterface() {
            return new EmptySignatureVisitor();
        }

        public SignatureVisitor visitInterfaceBound() {
            return new EmptySignatureVisitor();
        }

        public SignatureVisitor visitParameterType() {
            paramCount++;
            return this;
        }

        public SignatureVisitor visitReturnType() {
            return new EmptySignatureVisitor();
        }

        public SignatureVisitor visitSuperclass() {
            return new EmptySignatureVisitor();
        }

        public void visitTypeArgument() {
        }

        public SignatureVisitor visitTypeArgument(char wildcard) {
            return new EmptySignatureVisitor();
        }

        public void visitTypeVariable(String name) {
        }
    }

    static class EmptySignatureVisitor implements SignatureVisitor {

        public SignatureVisitor visitArrayType() {
            return this;
        }

        public void visitBaseType(char descriptor) {
        }

        public SignatureVisitor visitClassBound() {
            return this;
        }

        public void visitClassType(String name) {
        }

        public void visitEnd() {
        }

        public SignatureVisitor visitExceptionType() {
            return this;
        }

        public void visitFormalTypeParameter(String name) {
        }

        public void visitInnerClassType(String name) {
        }

        public SignatureVisitor visitInterface() {
            return this;
        }

        public SignatureVisitor visitInterfaceBound() {
            return this;
        }

        public SignatureVisitor visitParameterType() {
            return this;
        }

        public SignatureVisitor visitReturnType() {
            return this;
        }

        public SignatureVisitor visitSuperclass() {
            return this;
        }

        public void visitTypeArgument() {
        }

        public SignatureVisitor visitTypeArgument(char wildcard) {
            return this;
        }

        public void visitTypeVariable(String name) {
        }
    }
}
