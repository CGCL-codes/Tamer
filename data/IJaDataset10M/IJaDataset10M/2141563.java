package org.electro;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 *
 * <p>
 * </p>
 *
 * @author XNeb
 */
public class ProxyFactory {

    /**
	 * This method returns a class instance from an interface implementation.
	 *
	 * This is the main method of this class.
	 *
	 * The returned class has for constructor a method that use a
	 * InvocationHandler as parameter.
	 *
	 * Code sample :
	 *
	 * <pre>
	 * <code>
	 * 	interface i { int add (int a,int b) }
	 * 	Class<?> c = new PorxyFactory().createClassInterface(i.class);
	 * 	i instance = c.getConstructor(InvocationHandler.class).invoque(myInvocationHandler);
	 * </code>
	 * </pre>
	 *
	 * @param interfaceImpl
	 *            the class Interface
	 * @param handler
	 * @return a class instance
	 * @throws Exception
	 */
    public Class<?> createClassInterface(Class<?> interfaceImpl) throws Exception {
        String interfaceBiteCodeName = interfaceImpl.getName().replaceAll("\\.", "/");
        String classBiteCodeName = interfaceBiteCodeName + "$Implementation";
        String className = interfaceImpl.getName() + "$Implementation";
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC, classBiteCodeName, null, "java/lang/Object", new String[] { interfaceBiteCodeName });
        createSimpleConstructor(cw);
        String handlerFieldName = "handler";
        String handlerByteClassName = "Ljava/lang/reflect/InvocationHandler;";
        FieldVisitor fvHandler = cw.visitField(Opcodes.ACC_PRIVATE, handlerFieldName, handlerByteClassName, null, null);
        fvHandler.visitEnd();
        createHandlerConstructor(classBiteCodeName, cw, handlerFieldName, handlerByteClassName);
        ClassReader cr = new ClassReader(interfaceImpl.getName());
        ClassPrinter cp = new ClassPrinter();
        cr.accept(cp, 0);
        for (MethodSignature ms : cp.getMethodSignatures()) {
            if (ms.isPublic() && ms.isAbstract() && !ms.isStatic()) {
                MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, ms.getName(), ms.getDesc(), ms.getSignature(), ms.getExceptions());
                mv.visitCode();
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitFieldInsn(Opcodes.GETFIELD, classBiteCodeName, handlerFieldName, handlerByteClassName);
                int argNumber = ms.getArgumentNumber();
                mv.visitIntInsn(Opcodes.BIPUSH, argNumber);
                mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
                int stackPosition = ms.getFirstLoadStackPosition();
                mv.visitVarInsn(Opcodes.ASTORE, stackPosition);
                int objectArrayPosition = stackPosition;
                stackPosition++;
                mv.visitIntInsn(Opcodes.BIPUSH, argNumber);
                mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Class");
                mv.visitVarInsn(Opcodes.ASTORE, stackPosition);
                int classArrayPosition = stackPosition;
                stackPosition++;
                for (int i = 0; i < ms.getParameters().size(); i++) {
                    Parameter param = ms.getParameters().get(i);
                    mv.visitVarInsn(Opcodes.ALOAD, objectArrayPosition);
                    mv.visitIntInsn(Opcodes.BIPUSH, i);
                    int optLoad = param.getActionOptCode("LOAD");
                    mv.visitVarInsn(optLoad, param.getStackPosition());
                    param.castObjectToTypeValue(mv);
                    mv.visitInsn(Opcodes.AASTORE);
                    mv.visitVarInsn(Opcodes.ALOAD, classArrayPosition);
                    mv.visitIntInsn(Opcodes.BIPUSH, i);
                    if (param.isTypeClass()) {
                        String object = param.getObjectType();
                        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/" + object, "TYPE", "Ljava/lang/Class;");
                    } else {
                        mv.visitLdcInsn(Type.getType(param.getDesc()));
                    }
                    mv.visitInsn(Opcodes.AASTORE);
                }
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
                mv.visitLdcInsn(ms.getName());
                mv.visitVarInsn(Opcodes.ALOAD, classArrayPosition);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;");
                int methodStackPosition = stackPosition;
                mv.visitVarInsn(Opcodes.ASTORE, methodStackPosition);
                stackPosition++;
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitFieldInsn(Opcodes.GETFIELD, classBiteCodeName, handlerFieldName, handlerByteClassName);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitVarInsn(Opcodes.ALOAD, methodStackPosition);
                mv.visitVarInsn(Opcodes.ALOAD, objectArrayPosition);
                mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/lang/reflect/InvocationHandler", "invoke", "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;");
                int resultStackPosition = stackPosition;
                mv.visitVarInsn(Opcodes.ASTORE, resultStackPosition);
                stackPosition++;
                mv.visitVarInsn(Opcodes.ALOAD, resultStackPosition);
                if (ms.getReturn().equals("V")) {
                    mv.visitInsn(Opcodes.RETURN);
                } else if (!ms.getReturn().endsWith(";") && !ms.getReturn().startsWith("[")) {
                    String classType = Parameter.getTypeObjectFromDescription(ms.getReturn());
                    String methodConversionType = (classType.equals("Integer") ? "int" : classType.toLowerCase()) + "Value";
                    mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/" + classType);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/" + classType, methodConversionType, "()" + ms.getReturn());
                    String returnPrefix = ms.getReturn();
                    String list = "ZCBS";
                    if (list.contains(returnPrefix)) returnPrefix = "I"; else if (returnPrefix.equals("J")) returnPrefix = "L";
                    int ctsReturn = Opcodes.class.getDeclaredField(returnPrefix + "RETURN").getInt(null);
                    mv.visitInsn(ctsReturn);
                } else {
                    String rtCast = ms.getCastFromReturn();
                    mv.visitTypeInsn(Opcodes.CHECKCAST, rtCast);
                    System.out.println("Object cast return : " + rtCast);
                    mv.visitInsn(Opcodes.ARETURN);
                }
                mv.visitMaxs(0, 0);
                mv.visitEnd();
            }
        }
        cw.visitEnd();
        byte[] b = cw.toByteArray();
        FileOutputStream fos = new FileOutputStream("./" + className + ".class");
        fos.write(b);
        fos.close();
        DynClassLoader cl = new DynClassLoader();
        Class<?> ClassImpl = cl.defineClass(className, b);
        return ClassImpl;
    }

    /**
	 * @param classBiteCodeName
	 * @param cw
	 * @param handlerFieldName
	 * @param handlerByteClassName
	 */
    protected void createHandlerConstructor(String classBiteCodeName, ClassWriter cw, String handlerFieldName, String handlerByteClassName) {
        MethodVisitor mvConstrHandler = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "(" + handlerByteClassName + ")V", null, null);
        mvConstrHandler.visitCode();
        mvConstrHandler.visitVarInsn(Opcodes.ALOAD, 0);
        mvConstrHandler.visitMethodInsn(Opcodes.INVOKESPECIAL, classBiteCodeName, "<init>", "()V");
        mvConstrHandler.visitVarInsn(Opcodes.ALOAD, 0);
        mvConstrHandler.visitVarInsn(Opcodes.ALOAD, 1);
        mvConstrHandler.visitFieldInsn(Opcodes.PUTFIELD, classBiteCodeName, handlerFieldName, handlerByteClassName);
        mvConstrHandler.visitInsn(Opcodes.RETURN);
        mvConstrHandler.visitMaxs(2, 2);
        mvConstrHandler.visitEnd();
    }

    /**
	 * @param cw
	 */
    protected void createSimpleConstructor(ClassWriter cw) {
        MethodVisitor mvConstr = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mvConstr.visitCode();
        mvConstr.visitVarInsn(Opcodes.ALOAD, 0);
        mvConstr.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mvConstr.visitInsn(Opcodes.RETURN);
        mvConstr.visitMaxs(1, 1);
        mvConstr.visitEnd();
    }

    public class DynClassLoader extends ClassLoader {

        public Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }

    protected class MethodSignature {

        private int access;

        private String name;

        private String desc;

        private String signature;

        private String[] exceptions;

        private ArrayList<Parameter> parameters;

        private int firstLoadStackPosition;

        /**
		 * @param access
		 * @param name
		 * @param desc
		 * @param signature
		 * @param exceptions
		 */
        public MethodSignature(int access, String name, String desc, String signature, String[] exceptions) {
            super();
            this.access = access;
            this.name = name;
            this.desc = desc;
            this.signature = signature;
            this.exceptions = exceptions;
            initParameters();
        }

        private void initParameters() {
            this.parameters = new ArrayList<Parameter>();
            Pattern p = Pattern.compile("\\(([^)]*)\\)");
            Matcher m = p.matcher(desc);
            int argNbr = 0;
            int stackPosition = 1;
            if (m.find()) {
                String argDesc = m.group(1);
                Pattern pArg = Pattern.compile("\\[*(Z|C|B|S|I|F|J|D|(L[^;]*;))");
                Matcher mArg = pArg.matcher(argDesc);
                while (mArg.find()) {
                    argNbr++;
                    System.out.println(argNbr + "� " + mArg.group() + " " + stackPosition);
                    Parameter param = new Parameter();
                    param.setStackPosition(stackPosition);
                    param.setDesc(mArg.group());
                    parameters.add(param);
                    stackPosition++;
                    if (param.getDesc().equals("J") || param.getDesc().equals("D")) {
                        stackPosition++;
                    }
                }
                firstLoadStackPosition = stackPosition;
            }
        }

        public boolean isPublic() {
            return (access & Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC;
        }

        public boolean isStatic() {
            return (access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC;
        }

        public boolean isAbstract() {
            return (access & Opcodes.ACC_ABSTRACT) == Opcodes.ACC_ABSTRACT;
        }

        public String getReturn() {
            return desc.replaceAll("\\([^)]*\\)", "");
        }

        /**
		 * The purpose of this method is to transform the definition of the class
		 * like Ljava/lang/Object; to java/lang/Object
		 *
		 * @return a string representing the name of this class in the java form...
		 */
        public String getCastFromReturn() {
            String cast = getReturn();
            if (!cast.startsWith("[")) {
                cast = cast.replaceFirst("^L", "").replace(";", "");
            }
            return cast;
        }

        public int getArgumentNumber() {
            return parameters.size();
        }

        public int getAccess() {
            return access;
        }

        public String getName() {
            return name;
        }

        public String getDesc() {
            return desc;
        }

        public String getSignature() {
            return signature;
        }

        public String[] getExceptions() {
            return exceptions;
        }

        public ArrayList<Parameter> getParameters() {
            return parameters;
        }

        public int getFirstLoadStackPosition() {
            return firstLoadStackPosition;
        }
    }

    protected static class Parameter {

        private static HashMap<String, String> typeObject;

        static {
            typeObject = new HashMap<String, String>();
            typeObject.put("Z", "Boolean");
            typeObject.put("C", "Char");
            typeObject.put("B", "Byte");
            typeObject.put("S", "Short");
            typeObject.put("I", "Integer");
            typeObject.put("F", "Float");
            typeObject.put("J", "Long");
            typeObject.put("D", "Double");
        }

        public static String getTypeObjectFromDescription(String desc) {
            return typeObject.get(desc);
        }

        private int stackPosition;

        private String desc;

        private Class<?> javaClass;

        public Parameter() {
        }

        public String getObjectType() {
            return typeObject.get(desc);
        }

        public int getStackPosition() {
            return stackPosition;
        }

        /**
		 * This method invokes the type value of an object !!! This is done
		 * directly in the assembly code.
		 *
		 * @param mv
		 *            the method visitor !
		 */
        public void castObjectToTypeValue(MethodVisitor mv) {
            String classType = typeObject.get(desc);
            if (classType != null) {
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/" + classType, "valueOf", "(" + desc + ")Ljava/lang/" + classType + ";");
            }
        }

        public boolean isTypeClass() {
            return typeObject.containsKey(desc);
        }

        public boolean isObjectClass() {
            return !isTypeClass();
        }

        public String getDesc() {
            return desc;
        }

        public Class<?> getJavaClass() {
            return javaClass;
        }

        public void setStackPosition(int stackPosition) {
            this.stackPosition = stackPosition;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public void setJavaClass(Class<?> javaClass) {
            this.javaClass = javaClass;
        }

        public String getActionPrefix() {
            String returnPrefix;
            if (!desc.endsWith(";") && !desc.startsWith("[")) {
                returnPrefix = desc;
                String list = "ZCBS";
                if (list.contains(returnPrefix)) returnPrefix = "I"; else if (returnPrefix.equals("J")) returnPrefix = "L";
            } else {
                returnPrefix = "A";
            }
            return returnPrefix;
        }

        public int getActionOptCode(String actionSuffix) throws Exception {
            int optAction = Opcodes.class.getDeclaredField(getActionPrefix() + actionSuffix).getInt(null);
            return optAction;
        }
    }

    protected class ClassPrinter implements ClassVisitor {

        private ArrayList<MethodSignature> methodSignatures;

        public ClassPrinter() {
            methodSignatures = new ArrayList<MethodSignature>();
        }

        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            System.out.println(name + " extends " + superName + " {");
        }

        public void visitSource(String source, String debug) {
        }

        public void visitOuterClass(String owner, String name, String desc) {
        }

        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            return null;
        }

        public void visitAttribute(Attribute attr) {
        }

        public void visitInnerClass(String name, String outerName, String innerName, int access) {
        }

        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            System.out.println(" " + desc + " " + name);
            return null;
        }

        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            System.out.print((access & Opcodes.ACC_ABSTRACT) == Opcodes.ACC_ABSTRACT ? "Abstract " : "");
            System.out.print((access & Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC ? "public " : "");
            System.out.print((access & Opcodes.ACC_PROTECTED) == Opcodes.ACC_PROTECTED ? "protected " : "");
            System.out.print((access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE ? "private " : "");
            System.out.print((access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC ? "static " : "");
            System.out.println(access + " " + name + " " + desc);
            MethodSignature m = new MethodSignature(access, name, desc, signature, exceptions);
            methodSignatures.add(m);
            return null;
        }

        public void visitEnd() {
            System.out.println("}");
        }

        public ArrayList<MethodSignature> getMethodSignatures() {
            return methodSignatures;
        }
    }
}
