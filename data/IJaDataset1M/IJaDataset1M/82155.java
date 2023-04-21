package net.sf.securejdms.modeler;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.CodeVisitor;
import org.objectweb.asm.Constants;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

class DynamicBeanWriter implements Constants {

    private static class MyClassloader extends ClassLoader {

        public MyClassloader(ClassLoader parent) {
            super(parent);
        }

        public Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }

    private static final String MODELER_PACKAGE = DynamicBeanWriter.class.getPackage().getName().replace('.', '/');

    private static final String DYNAMIC_BEAN_NAME_PREFIX = "/$DBean$";

    private String beanClassName;

    private ClassWriter classWriter;

    @SuppressWarnings("unchecked")
    public Class<DynamicBean> generateDynamicBean(String beanName, String[] properties, Class<?>[] types) {
        classWriter = new ClassWriter(true);
        beanClassName = getBeanClassName(beanName);
        classWriter.visit(V1_5, ACC_PUBLIC + ACC_FINAL, beanClassName, Type.getInternalName(DynamicBean.class), new String[] {}, null);
        CodeVisitor constructorVisitor = classWriter.visitMethod(Constants.ACC_PUBLIC, "<init>", "(" + Type.getDescriptor(ModelElement.class) + ")V", new String[0], null);
        constructorVisitor.visitVarInsn(ALOAD, 0);
        constructorVisitor.visitVarInsn(Constants.ALOAD, 1);
        constructorVisitor.visitMethodInsn(Constants.INVOKESPECIAL, Type.getInternalName(DynamicBean.class), "<init>", "(" + Type.getDescriptor(ModelElement.class) + ")V");
        for (int i = 0; i < properties.length; i++) {
            initializeProperty(constructorVisitor, properties[i], types[i]);
        }
        constructorVisitor.visitInsn(Constants.RETURN);
        constructorVisitor.visitMaxs(0, 0);
        for (int i = 0; i < properties.length; i++) {
            implementProperty(properties[i], types[i]);
        }
        classWriter.visitEnd();
        byte[] bs = classWriter.toByteArray();
        saveByteArray(bs);
        Class<?> clazz = new MyClassloader(DynamicBeanWriter.class.getClassLoader()).defineClass(beanClassName.replace('/', '.'), bs);
        return (Class<DynamicBean>) clazz;
    }

    private void initializeProperty(CodeVisitor constructorVisitor, String propertyName, Class<?> propertyType) {
        if (!isListType(propertyType)) {
            return;
        }
        constructorVisitor.visitVarInsn(ALOAD, 0);
        constructorVisitor.visitTypeInsn(NEW, Type.getInternalName(ModelerList.class));
        constructorVisitor.visitInsn(DUP);
        constructorVisitor.visitVarInsn(ALOAD, 0);
        constructorVisitor.visitVarInsn(ALOAD, 0);
        constructorVisitor.visitLdcInsn(propertyName);
        constructorVisitor.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(DynamicBean.class), "retrieveModelProperty", "(" + Type.getDescriptor(String.class) + ")" + Type.getDescriptor(ModelProperty.class));
        constructorVisitor.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(ModelerList.class), "<init>", "(" + Type.getDescriptor(DynamicBean.class) + Type.getDescriptor(ModelProperty.class) + ")V");
        constructorVisitor.visitFieldInsn(PUTFIELD, beanClassName, propertyName, Type.getDescriptor(propertyType));
    }

    private void implementProperty(String propertyName, Class<?> propertyType) {
        char upperCasePropertyName = Character.toUpperCase(propertyName.charAt(0));
        String getterName = (propertyType.equals(boolean.class) ? "is" : "get") + upperCasePropertyName + propertyName.substring(1);
        String setterName = "set" + upperCasePropertyName + propertyName.substring(1);
        String loadedPropertName = propertyName + "$loaded";
        if (isListType(propertyType)) {
            classWriter.visitField(ACC_PRIVATE + ACC_FINAL, propertyName, Type.getDescriptor(propertyType), null, null);
        } else {
            classWriter.visitField(ACC_PRIVATE, propertyName, Type.getDescriptor(propertyType), null, null);
        }
        classWriter.visitField(ACC_PRIVATE, loadedPropertName, Type.getDescriptor(boolean.class), null, null);
        CodeVisitor visitMethod = classWriter.visitMethod(ACC_PUBLIC, getterName, "()" + Type.getDescriptor(propertyType), null, null);
        visitMethod.visitVarInsn(ALOAD, 0);
        visitMethod.visitFieldInsn(GETFIELD, beanClassName, loadedPropertName, Type.getDescriptor(boolean.class));
        Label l0 = new Label();
        visitMethod.visitJumpInsn(IFNE, l0);
        visitMethod.visitVarInsn(ALOAD, 0);
        visitMethod.visitLdcInsn(propertyName);
        visitMethod.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(DynamicBean.class), "$loadProperty", "(Ljava/lang/String;)V");
        visitMethod.visitLabel(l0);
        visitMethod.visitVarInsn(ALOAD, 0);
        visitMethod.visitFieldInsn(GETFIELD, beanClassName, propertyName, Type.getDescriptor(propertyType));
        visitMethod.visitInsn(getReturnConstant(propertyType));
        visitMethod.visitMaxs(0, 0);
        visitMethod = classWriter.visitMethod(ACC_PUBLIC, setterName, "(" + Type.getDescriptor(propertyType) + ")V", null, null);
        int oldValueReg = 1 + getSize(propertyType);
        visitMethod.visitVarInsn(ALOAD, 0);
        visitMethod.visitInsn(ICONST_1);
        visitMethod.visitFieldInsn(PUTFIELD, beanClassName, loadedPropertName, Type.getDescriptor(boolean.class));
        if (isListType(propertyType)) {
            visitMethod.visitVarInsn(ALOAD, 0);
            visitMethod.visitFieldInsn(GETFIELD, beanClassName, propertyName, Type.getDescriptor(propertyType));
            visitMethod.visitTypeInsn(CHECKCAST, Type.getInternalName(ModelerList.class));
            visitMethod.visitVarInsn(ALOAD, 1);
            visitMethod.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(ModelerList.class), "setAll", "(" + Type.getDescriptor(Collection.class) + ")V");
        } else {
            visitMethod.visitVarInsn(ALOAD, 0);
            visitMethod.visitFieldInsn(GETFIELD, beanClassName, propertyName, Type.getDescriptor(propertyType));
            visitMethod.visitVarInsn(getStoreConstant(propertyType), oldValueReg);
            visitMethod.visitVarInsn(ALOAD, 0);
            visitMethod.visitVarInsn(getLoadConstant(propertyType), 1);
            visitMethod.visitFieldInsn(PUTFIELD, beanClassName, propertyName, Type.getDescriptor(propertyType));
            visitMethod.visitVarInsn(ALOAD, 0);
            visitMethod.visitLdcInsn(propertyName);
            loadBoxedObjectFromLocalVar(visitMethod, oldValueReg, propertyType);
            loadBoxedObjectFromLocalVar(visitMethod, 1, propertyType);
            visitMethod.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(DynamicBean.class), "firePropertyChange", "(" + Type.getDescriptor(String.class) + Type.getDescriptor(Object.class) + Type.getDescriptor(Object.class) + ")V");
        }
        visitMethod.visitInsn(RETURN);
        visitMethod.visitMaxs(0, 0);
    }

    private boolean isListType(Class<?> propertyType) {
        return Collection.class.isAssignableFrom(propertyType);
    }

    private static void loadBoxedObjectFromLocalVar(CodeVisitor visitMethod, int localVarIndex, Class<?> type) {
        if (!type.isPrimitive()) {
            visitMethod.visitVarInsn(ALOAD, localVarIndex);
            return;
        }
        String objectType = getBoxingClassForPrimitiv(type);
        visitMethod.visitVarInsn(getLoadConstant(type), localVarIndex);
        visitMethod.visitMethodInsn(INVOKESTATIC, objectType, "valueOf", "(" + Type.getDescriptor(type) + ")L" + objectType + ";");
    }

    private static String getBoxingClassForPrimitiv(Class<?> type) {
        if (type.equals(int.class)) {
            return Type.getInternalName(Integer.class);
        }
        if (type.equals(boolean.class)) {
            return Type.getInternalName(Boolean.class);
        }
        if (type.equals(long.class)) {
            return Type.getInternalName(Long.class);
        }
        if (type.equals(byte.class)) {
            return Type.getInternalName(Byte.class);
        }
        if (type.equals(char.class)) {
            return Type.getInternalName(Character.class);
        }
        if (type.equals(short.class)) {
            return Type.getInternalName(Short.class);
        }
        if (type.equals(float.class)) {
            return Type.getInternalName(Float.class);
        }
        if (type.equals(double.class)) {
            return Type.getInternalName(Double.class);
        }
        throw new RuntimeException("Unknown primitiv type: " + type.getCanonicalName());
    }

    /**
	 * Return corresponding store constant for the type <code>type</code>
	 * 
	 * @param type type to get store constant for
	 * @return corresponding store constant for the type <code>type</code>
	 */
    private static int getStoreConstant(Class<?> type) {
        if (!type.isPrimitive()) {
            return ASTORE;
        }
        if (type.equals(long.class)) {
            return LSTORE;
        }
        if (type.equals(double.class)) {
            return DSTORE;
        }
        if (type.equals(float.class)) {
            return FSTORE;
        }
        return ISTORE;
    }

    /**
	 * Return corresponding load constant for the type <code>type</code>
	 * 
	 * @param type type to get load constant for
	 * @return corresponding load constant for the type <code>type</code>
	 */
    private static int getLoadConstant(Class<?> type) {
        if (!type.isPrimitive()) {
            return ALOAD;
        }
        if (type.equals(long.class)) {
            return LLOAD;
        }
        if (type.equals(double.class)) {
            return DLOAD;
        }
        if (type.equals(float.class)) {
            return FLOAD;
        }
        return ILOAD;
    }

    /**
	 * Return corresponding return constant for the type <code>type</code>
	 * 
	 * @param type type to get return constant for
	 * @return corresponding return constant for the type <code>type</code>
	 */
    private static int getReturnConstant(Class<?> type) {
        if (!type.isPrimitive()) {
            return ARETURN;
        }
        if (type.equals(long.class)) {
            return LRETURN;
        }
        if (type.equals(double.class)) {
            return DRETURN;
        }
        if (type.equals(float.class)) {
            return FRETURN;
        }
        return IRETURN;
    }

    /**
	 * Return count of register needed to store type <code>type</code>
	 * 
	 * @param type type to get size in registers of
	 * @return size in registers of type <code>type</code>
	 */
    private static int getSize(Class<?> type) {
        if (type.equals(long.class) || type.equals(double.class)) {
            return 2;
        }
        return 1;
    }

    /**
	 * Return name of the generated class for bean with name <code>beanName</code>
	 * 
	 * @param beanName bean name
	 * @return name of the generated class
	 */
    private static String getBeanClassName(String beanName) {
        return MODELER_PACKAGE + DYNAMIC_BEAN_NAME_PREFIX + beanName;
    }

    private void saveByteArray(byte[] bs) {
        try {
            String fullpath = "GeneratedBeans/" + beanClassName;
            String pathname = fullpath.substring(0, fullpath.lastIndexOf('/') + 1);
            String filename = fullpath.substring(fullpath.lastIndexOf('/') + 1) + ".class";
            File path = new File(pathname.replace('/', File.separatorChar));
            path.mkdirs();
            FileOutputStream fileOutputStream = new FileOutputStream(pathname + filename);
            fileOutputStream.write(bs);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
