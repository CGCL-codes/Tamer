package org.aspectme.cldc.reflect.model.asm;

import java.util.ArrayList;
import java.util.List;
import org.aspectme.cldc.reflect.model.ReflectClass;
import org.aspectme.cldc.reflect.model.ReflectClassLoader;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;

/**
 * Helper class for Java signatures.
 * 
 * @author Magnus Robertsson
 */
public class AsmSignature {

    public static ReflectClass[] getParameterTypes(String desc, ReflectClassLoader classLoader) {
        MethodSignatureVisitor sv = new MethodSignatureVisitor(classLoader);
        SignatureReader reader = new SignatureReader(desc);
        reader.accept(sv);
        return sv.getParameterTypes();
    }

    public static ReflectClass getReturnType(String desc, ReflectClassLoader classLoader) {
        MethodSignatureVisitor sv = new MethodSignatureVisitor(classLoader);
        SignatureReader reader = new SignatureReader(desc);
        reader.accept(sv);
        return sv.getReturnType();
    }

    public static ReflectClass getFieldType(String desc, ReflectClassLoader classLoader) {
        MethodSignatureVisitor sv = new MethodSignatureVisitor(classLoader);
        SignatureReader reader = new SignatureReader(desc);
        reader.acceptType(sv);
        return sv.getCurrentType();
    }

    static class MethodSignatureVisitor implements SignatureVisitor {

        private static final int READ_PARAM_TYPE = 1;

        private static final int READ_RETURN_TYPE = 2;

        private List<ReflectClass> classes = new ArrayList<ReflectClass>();

        private ReflectClass currentType;

        private ReflectClass returnType;

        private ReflectClassLoader classLoader;

        private int arrayLevel;

        private int state;

        public MethodSignatureVisitor(ReflectClassLoader classLoader) {
            this.classLoader = classLoader;
            this.state = READ_PARAM_TYPE;
        }

        public ReflectClass[] getParameterTypes() {
            return (ReflectClass[]) classes.toArray(new ReflectClass[classes.size()]);
        }

        public ReflectClass getCurrentType() {
            System.out.println("Returning current type: " + classes.get(0));
            return classes.get(0);
        }

        public ReflectClass getReturnType() {
            return returnType;
        }

        public SignatureVisitor visitArrayType() {
            arrayLevel++;
            return this;
        }

        public void visitBaseType(char descriptor) {
            switch(descriptor) {
                case 'V':
                    currentType = AsmPrimitiveClass.VOID_TYPE;
                    break;
                case 'B':
                    currentType = AsmPrimitiveClass.BYTE_TYPE;
                    break;
                case 'J':
                    currentType = AsmPrimitiveClass.LONG_TYPE;
                    break;
                case 'Z':
                    currentType = AsmPrimitiveClass.BOOLEAN_TYPE;
                    break;
                case 'I':
                    currentType = AsmPrimitiveClass.INTEGER_TYPE;
                    break;
                case 'S':
                    currentType = AsmPrimitiveClass.SHORT_TYPE;
                    break;
                case 'C':
                    currentType = AsmPrimitiveClass.CHARACTER_TYPE;
                    break;
                case 'F':
                    currentType = AsmPrimitiveClass.FLOAT_TYPE;
                    break;
                default:
                    currentType = AsmPrimitiveClass.DOUBLE_TYPE;
                    break;
            }
            endType();
        }

        private void endType() {
            while (arrayLevel > 0) {
                currentType = new AsmArrayClass(currentType);
                arrayLevel--;
            }
            switch(state) {
                case READ_PARAM_TYPE:
                    classes.add(currentType);
                    break;
                case READ_RETURN_TYPE:
                    returnType = currentType;
                    break;
            }
            currentType = null;
            state = READ_PARAM_TYPE;
        }

        public SignatureVisitor visitClassBound() {
            return this;
        }

        public void visitClassType(String name) {
            String className = name.replace('/', '.');
            currentType = classLoader.loadClass(className);
            if (currentType == null) {
                currentType = new AsmUnknownClass(className);
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
            state = READ_RETURN_TYPE;
            return this;
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
