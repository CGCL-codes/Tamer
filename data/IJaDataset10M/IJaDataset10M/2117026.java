package org.avaje.ebean.enhance.agent;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import org.avaje.ebean.enhance.asm.ClassVisitor;
import org.avaje.ebean.enhance.asm.Label;
import org.avaje.ebean.enhance.asm.MethodVisitor;
import org.avaje.ebean.enhance.asm.Opcodes;
import org.avaje.ebean.enhance.asm.Type;

/**
 * Holds meta data for a field.
 * <p>
 * This can then generate the appropriate byte code for this field.
 * </p>
 */
public class FieldMeta implements Opcodes, EnhanceConstants {

    final String fieldName;

    final String fieldDesc;

    final HashSet<String> annotations = new HashSet<String>();

    final Type asmType;

    final boolean primativeType;

    final boolean arrayType;

    final boolean objectType;

    final String getMethodName;

    final String getMethodDesc;

    final String setMethodName;

    final String setMethodDesc;

    final String publicSetterName;

    final String publicGetterName;

    /**
	 * Construct based on field.
	 * <p>
	 * Used when getting inherited fields from super classes.
	 * </p>
	 */
    public FieldMeta(Field field) {
        this(field.getName(), Type.getDescriptor(field.getType()));
        Annotation[] anno = field.getAnnotations();
        for (Annotation a : anno) {
            addAnnotation(a);
        }
    }

    /**
	 * Construct based on field name and desc from reading byte code.
	 * <p>
	 * Used for reading local fields (not inherited) via visiting the class bytes.
	 * </p>
	 */
    public FieldMeta(String name, String desc) {
        this.fieldName = name;
        this.fieldDesc = desc;
        asmType = Type.getType(desc);
        int sort = asmType.getSort();
        primativeType = sort > Type.VOID && sort <= Type.DOUBLE;
        arrayType = sort == Type.ARRAY;
        objectType = sort == Type.OBJECT;
        getMethodName = "_ebean_get_" + name;
        getMethodDesc = "()" + desc;
        setMethodName = "_ebean_set_" + name;
        setMethodDesc = "(" + desc + ")V";
        String initCap = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        publicSetterName = "set" + initCap;
        if (fieldDesc.equals("Z")) {
            publicGetterName = "is" + initCap;
        } else {
            publicGetterName = "get" + initCap;
        }
    }

    public String toString() {
        return fieldName;
    }

    /**
	 * The expected public getter name following bean naming convention.
	 * <p>
	 * This is generally used for subclassing rather than javaagent enhancement.
	 * </p>
	 */
    public String getPublicGetterName() {
        return publicGetterName;
    }

    /**
	 * The expected public setter name following bean naming convention.
	 * <p>
	 * This is generally used for subclassing rather than javaagent enhancement.
	 * </p>
	 */
    public String getPublicSetterName() {
        return publicSetterName;
    }

    /**
	 * Return true if this is the public setter for this field according to
	 * bean naming convention.
	 */
    public boolean isPersistentSetter(String methodDesc) {
        return setMethodDesc.equals(methodDesc) && isInterceptSet();
    }

    /**
	 * Return true if this is the public getter for this field according to
	 * bean naming convention.
	 */
    public boolean isPersistentGetter(String methodDesc) {
        return getMethodDesc.equals(methodDesc) && isInterceptGet();
    }

    /**
	 * Add a field annotation.
	 */
    protected void addAnnotationDesc(String desc) {
        annotations.add(desc);
    }

    /**
	 * Add a field annotation.
	 */
    private void addAnnotation(Annotation a) {
        String at = a.annotationType().getName();
        at = "L" + at.replace('.', '/') + ";";
        annotations.add(at);
    }

    /**
	 * Return the field name.
	 */
    public String getName() {
        return fieldName;
    }

    /**
	 * Return the field bytecode type description.
	 */
    public String getDesc() {
        return fieldDesc;
    }

    private boolean isInterceptGet() {
        if (isId()) {
            return false;
        }
        if (isTransient()) {
            return false;
        }
        if (isMany()) {
            return true;
        }
        return true;
    }

    private boolean isInterceptSet() {
        if (isId()) {
            return false;
        }
        if (isTransient()) {
            return false;
        }
        if (isMany()) {
            return false;
        }
        return true;
    }

    /**
	 * Return true if this field type is an Array of Objects.
	 * <p>
	 * We can not support Object Arrays for field types.
	 * </p>
	 */
    public boolean isObjectArray() {
        if (fieldDesc.charAt(0) == '[') {
            if (fieldDesc.length() > 2) {
                if (!isTransient()) {
                    System.err.println("ERROR: We can not support Object Arrays... for field: " + fieldName);
                }
                return true;
            }
        }
        return false;
    }

    /**
	 * Return true is this is a persistent field.
	 */
    public boolean isPersistent() {
        return !isTransient();
    }

    /**
	 * Return true if this is a transient field.
	 */
    public boolean isTransient() {
        return annotations.contains("Ljavax/persistence/Transient;");
    }

    /**
	 * Return true if this is an ID field.
	 * <p>
	 * ID fields are used in generating equals() logic based on identity.
	 * </p>
	 */
    public boolean isId() {
        boolean idField = (annotations.contains("Ljavax/persistence/Id;") || annotations.contains("Ljavax/persistence/EmbeddedId;"));
        return idField;
    }

    /**
	 * Return true if this is a OneToMany or ManyToMany field.
	 */
    public boolean isMany() {
        return annotations.contains("Ljavax/persistence/OneToMany;") || annotations.contains("Ljavax/persistence/ManyToMany;");
    }

    /**
	 * Return true if this is an Embedded field.
	 */
    public boolean isEmbedded() {
        return annotations.contains("Ljavax/persistence/Embedded;") || annotations.contains("Lcom/avaje/ebean/annotation/EmbeddedColumns;");
    }

    /**
	 * As part of the switch statement to read the fields generate the get code.
	 */
    public void appendSwitchGet(MethodVisitor mv, ClassMeta classMeta, boolean intercept) {
        if (classMeta.isSubclassing()) {
            if (intercept) {
                mv.visitMethodInsn(INVOKEVIRTUAL, classMeta.getClassName(), publicGetterName, getMethodDesc);
            } else {
                mv.visitMethodInsn(INVOKESPECIAL, classMeta.getSuperClassName(), publicGetterName, getMethodDesc);
            }
        } else {
            if (intercept) {
                mv.visitMethodInsn(INVOKEVIRTUAL, classMeta.getClassName(), getMethodName, getMethodDesc);
            } else {
                mv.visitFieldInsn(GETFIELD, classMeta.getClassName(), fieldName, fieldDesc);
            }
        }
        if (primativeType) {
            Type objectWrapperType = PrimitiveHelper.getObjectWrapper(asmType);
            String objDesc = objectWrapperType.getInternalName();
            String primDesc = asmType.getDescriptor();
            if (intercept && classMeta.isLog(9)) {
                classMeta.log(" ... get primitive field " + fieldName + " " + objDesc + " valueOf " + primDesc);
            }
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, objDesc, "valueOf", "(" + primDesc + ")L" + objDesc + ";");
        }
    }

    public void appendSwitchSet(MethodVisitor mv, ClassMeta classMeta, boolean intercept) {
        if (primativeType) {
            Type objectWrapperType = PrimitiveHelper.getObjectWrapper(asmType);
            String primDesc = asmType.getDescriptor();
            String primType = asmType.getClassName();
            String objInt = objectWrapperType.getInternalName();
            mv.visitTypeInsn(CHECKCAST, objInt);
            mv.visitMethodInsn(INVOKEVIRTUAL, objInt, primType + "Value", "()" + primDesc);
        } else {
            mv.visitTypeInsn(CHECKCAST, asmType.getInternalName());
        }
        if (classMeta.isSubclassing()) {
            if (intercept) {
                mv.visitMethodInsn(INVOKEVIRTUAL, classMeta.getClassName(), publicSetterName, setMethodDesc);
            } else {
                mv.visitMethodInsn(INVOKESPECIAL, classMeta.getSuperClassName(), publicSetterName, setMethodDesc);
            }
        } else {
            if (intercept) {
                mv.visitMethodInsn(INVOKEVIRTUAL, classMeta.getClassName(), setMethodName, setMethodDesc);
            } else {
                mv.visitFieldInsn(PUTFIELD, classMeta.getClassName(), fieldName, fieldDesc);
            }
        }
    }

    /**
	 * Only for subclass generation - add public getter and setter methods for interception.
	 */
    public void addPublicGetSetMethods(ClassVisitor cv, ClassMeta classMeta, boolean checkExisting) {
        if (isPersistent()) {
            if (isId()) {
            } else if (isMany()) {
                addPublicGetMethod(cv, classMeta, checkExisting);
            } else {
                addPublicGetMethod(cv, classMeta, checkExisting);
                addPublicSetMethod(cv, classMeta, checkExisting);
            }
        }
    }

    private void addPublicGetMethod(ClassVisitor cv, ClassMeta classMeta, boolean checkExisting) {
        if (checkExisting && !classMeta.isExistingSuperMethod(publicGetterName, getMethodDesc)) {
            if (classMeta.isLog(1)) {
                classMeta.log("excluding " + publicGetterName + " as not on super object");
            }
            return;
        }
        addGetMethod(new VisitMethodParams(cv, ACC_PUBLIC, publicGetterName, getMethodDesc, null, null), classMeta);
    }

    public void addFieldCopy(MethodVisitor mv, ClassMeta classMeta) {
        if (classMeta.isSubclassing()) {
            String copyClassName = classMeta.getSuperClassName();
            mv.visitMethodInsn(INVOKESPECIAL, copyClassName, publicGetterName, getMethodDesc);
            mv.visitMethodInsn(INVOKEVIRTUAL, copyClassName, publicSetterName, setMethodDesc);
        } else {
            String className = classMeta.getClassName();
            mv.visitFieldInsn(GETFIELD, className, fieldName, fieldDesc);
            mv.visitFieldInsn(PUTFIELD, className, fieldName, fieldDesc);
        }
    }

    private void addGetMethod(VisitMethodParams params, ClassMeta classMeta) {
        MethodVisitor mv = params.visitMethod();
        int iReturnOpcode = asmType.getOpcode(Opcodes.IRETURN);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(1, l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, classMeta.getClassName(), INTERCEPT_FIELD, L_INTERCEPT);
        mv.visitLdcInsn(fieldName);
        mv.visitMethodInsn(INVOKEVIRTUAL, C_INTERCEPT, "preGetter", "(Ljava/lang/String;)V");
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLineNumber(1, l1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, classMeta.getSuperClassName(), params.name, params.desc);
        mv.visitInsn(iReturnOpcode);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLocalVariable("this", "L" + classMeta.getClassName() + ";", null, l0, l2, 0);
        mv.visitMaxs(2, 1);
        mv.visitEnd();
    }

    /**
	 * Add ebean get and set methods.
	 */
    public void addGetSetMethods(ClassVisitor cv, ClassMeta classMeta) {
        addGet(cv, classMeta);
        addSet(cv, classMeta);
    }

    public void addGet(ClassVisitor cw, ClassMeta classMeta) {
        int iReturnOpcode = asmType.getOpcode(Opcodes.IRETURN);
        if (classMeta.isLog(3)) {
            classMeta.log(getMethodName + " " + getMethodDesc + " intercept:" + isInterceptGet() + " " + annotations);
        }
        String className = classMeta.getClassName();
        MethodVisitor mv = cw.visitMethod(ACC_PROTECTED, getMethodName, getMethodDesc, null, null);
        mv.visitCode();
        Label l0 = null;
        if (isInterceptGet()) {
            l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(1, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, className, INTERCEPT_FIELD, L_INTERCEPT);
            mv.visitLdcInsn(fieldName);
            mv.visitMethodInsn(INVOKEVIRTUAL, C_INTERCEPT, "preGetter", "(Ljava/lang/String;)V");
        }
        Label l1 = new Label();
        if (l0 == null) {
            l0 = l1;
        }
        mv.visitLabel(l1);
        mv.visitLineNumber(1, l1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, className, fieldName, fieldDesc);
        mv.visitInsn(iReturnOpcode);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLocalVariable("this", "L" + className + ";", null, l0, l2, 0);
        mv.visitMaxs(2, 1);
        mv.visitEnd();
    }

    private void addPublicSetMethod(ClassVisitor cv, ClassMeta classMeta, boolean checkExisting) {
        if (checkExisting && !classMeta.isExistingSuperMethod(publicSetterName, setMethodDesc)) {
            if (classMeta.isLog(1)) {
                classMeta.log("excluding " + publicSetterName + " as not on super object");
            }
            return;
        }
        addSetMethod(new VisitMethodParams(cv, ACC_PUBLIC, publicSetterName, setMethodDesc, null, null), classMeta);
    }

    private void addSetMethod(VisitMethodParams params, ClassMeta classMeta) {
        MethodVisitor mv = params.visitMethod();
        String publicGetterName = getPublicGetterName();
        String preSetterArgTypes = "Ljava/lang/Object;Ljava/lang/Object;";
        if (!objectType) {
            preSetterArgTypes = fieldDesc + fieldDesc;
        }
        int iLoadOpcode = asmType.getOpcode(Opcodes.ILOAD);
        String className = classMeta.getClassName();
        String superClassName = classMeta.getSuperClassName();
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(1, l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, className, INTERCEPT_FIELD, L_INTERCEPT);
        mv.visitLdcInsn(fieldName);
        mv.visitVarInsn(iLoadOpcode, 1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, className, publicGetterName, getMethodDesc);
        mv.visitMethodInsn(INVOKEVIRTUAL, C_INTERCEPT, "preSetter", "(Ljava/lang/String;" + preSetterArgTypes + ")V");
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLineNumber(1, l1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(iLoadOpcode, 1);
        mv.visitMethodInsn(INVOKESPECIAL, superClassName, params.name, params.desc);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLineNumber(1, l2);
        mv.visitInsn(RETURN);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitLocalVariable("this", "L" + className + ";", null, l0, l3, 0);
        mv.visitLocalVariable("newValue", fieldDesc, null, l0, l3, 1);
        mv.visitMaxs(4, 2);
        mv.visitEnd();
    }

    public void addSet(ClassVisitor cw, ClassMeta classMeta) {
        String preSetterArgTypes = "Ljava/lang/Object;Ljava/lang/Object;";
        if (!objectType) {
            preSetterArgTypes = fieldDesc + fieldDesc;
        }
        int iLoadOpcode = asmType.getOpcode(Opcodes.ILOAD);
        int iPosition = asmType.getSize();
        if (classMeta.isLog(3)) {
            classMeta.log(setMethodName + " " + setMethodDesc + " intercept:" + isInterceptSet() + " opCode:" + iLoadOpcode + "," + iPosition + " preSetterArgTypes" + preSetterArgTypes);
        }
        String className = classMeta.getClassName();
        MethodVisitor mv = cw.visitMethod(ACC_PROTECTED, setMethodName, setMethodDesc, null, null);
        mv.visitCode();
        Label l0 = null;
        if (isInterceptSet()) {
            l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(1, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, className, INTERCEPT_FIELD, L_INTERCEPT);
            mv.visitLdcInsn(fieldName);
            mv.visitVarInsn(iLoadOpcode, 1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, className, getMethodName, getMethodDesc);
            mv.visitMethodInsn(INVOKEVIRTUAL, C_INTERCEPT, "preSetter", "(Ljava/lang/String;" + preSetterArgTypes + ")V");
        }
        Label l1 = new Label();
        if (l0 == null) {
            l0 = l1;
        }
        mv.visitLabel(l1);
        mv.visitLineNumber(1, l1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(iLoadOpcode, 1);
        mv.visitFieldInsn(PUTFIELD, className, fieldName, fieldDesc);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLineNumber(1, l2);
        mv.visitInsn(RETURN);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitLocalVariable("this", "L" + className + ";", null, l0, l3, 0);
        mv.visitLocalVariable("_newValue", fieldDesc, null, l0, l3, 1);
        mv.visitMaxs(4, 2);
        mv.visitEnd();
    }
}
