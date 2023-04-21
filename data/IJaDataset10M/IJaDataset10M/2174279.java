package org.datanucleus.enhancer.asm.method;

import org.datanucleus.enhancer.ClassEnhancer;
import org.datanucleus.enhancer.asm.ASMClassMethod;
import org.datanucleus.enhancer.asm.ASMUtils;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.ClassMetaData;
import org.datanucleus.metadata.IdentityType;
import org.datanucleus.util.ClassUtils;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * Method to generate the method "jdoNewObjectIdInstance" using ASM.
 * For datastore/nondurable identity this is
 * <pre>
 * public Object jdoNewObjectIdInstance(Object key)
 * {
 *     return null;
 * }
 * </pre>
 * and for SingleFieldIdentity
 * <pre>
 * public Object jdoNewObjectIdInstance(Object key)
 * {
 *     if (key == null)
 *         throw new IllegalArgumentException("key is null");
 *     if (key instanceof String != true)
 *         return new YYYIdentity(this.getClass(), (YYY) key);
 *     return new YYYIdentity(this.getClass(), (String) key);
 * }
 * </pre>
 * and for user-supplied object ids
 * <pre>
 * public Object jdoNewObjectIdInstance(Object key)
 * {
 *     return new UserPrimaryKey((String) key);
 * }
 * </pre>
 */
public class JdoNewObjectIdInstance2 extends ASMClassMethod {

    public static JdoNewObjectIdInstance2 getInstance(ClassEnhancer enhancer) {
        return new JdoNewObjectIdInstance2(enhancer, enhancer.getNewObjectIdInstanceMethodName(), Opcodes.ACC_PUBLIC, Object.class, new Class[] { Object.class }, new String[] { "key" });
    }

    /**
     * Constructor.
     * @param enhancer ClassEnhancer
     * @param name Name of method
     * @param access Access type
     * @param returnType Return type
     * @param argTypes Argument types
     * @param argNames Argument names
     */
    public JdoNewObjectIdInstance2(ClassEnhancer enhancer, String name, int access, Object returnType, Object[] argTypes, String[] argNames) {
        super(enhancer, name, access, returnType, argTypes, argNames);
    }

    /**
     * Method to add the contents of the class method.
     */
    public void execute() {
        visitor.visitCode();
        Label startLabel = new Label();
        visitor.visitLabel(startLabel);
        ClassMetaData cmd = enhancer.getClassMetaData();
        if (cmd.getIdentityType() == IdentityType.APPLICATION) {
            if (!cmd.isInstantiable()) {
                visitor.visitTypeInsn(Opcodes.NEW, "javax/jdo/JDOFatalInternalException");
                visitor.visitInsn(Opcodes.DUP);
                visitor.visitLdcInsn("This class has no identity");
                visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "javax/jdo/JDOFatalInternalException", "<init>", "(Ljava/lang/String;)V");
                visitor.visitInsn(Opcodes.ATHROW);
                Label endLabel = new Label();
                visitor.visitLabel(endLabel);
                visitor.visitLocalVariable("this", getClassEnhancer().getClassDescriptor(), null, startLabel, endLabel, 0);
                visitor.visitLocalVariable("key", "Ljava/lang/Object;", null, startLabel, endLabel, 1);
                visitor.visitMaxs(3, 2);
            } else {
                String objectIdClass = cmd.getObjectidClass();
                int[] pkFieldNums = cmd.getPKMemberPositions();
                if (enhancer.getMetaDataManager().getApiAdapter().isSingleFieldIdentityClass(objectIdClass)) {
                    String ACN_objectIdClass = objectIdClass.replace('.', '/');
                    AbstractMemberMetaData fmd = enhancer.getClassMetaData().getMetaDataForManagedMemberAtAbsolutePosition(pkFieldNums[0]);
                    {
                        visitor.visitVarInsn(Opcodes.ALOAD, 1);
                        Label l1 = new Label();
                        visitor.visitJumpInsn(Opcodes.IFNONNULL, l1);
                        visitor.visitTypeInsn(Opcodes.NEW, "java/lang/IllegalArgumentException");
                        visitor.visitInsn(Opcodes.DUP);
                        visitor.visitLdcInsn("key is null");
                        visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
                        visitor.visitInsn(Opcodes.ATHROW);
                        visitor.visitLabel(l1);
                        visitor.visitVarInsn(Opcodes.ALOAD, 1);
                        visitor.visitTypeInsn(Opcodes.INSTANCEOF, "java/lang/String");
                        Label l3 = new Label();
                        visitor.visitJumpInsn(Opcodes.IFNE, l3);
                        visitor.visitTypeInsn(Opcodes.NEW, ACN_objectIdClass);
                        visitor.visitInsn(Opcodes.DUP);
                        visitor.visitVarInsn(Opcodes.ALOAD, 0);
                        visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
                        visitor.visitVarInsn(Opcodes.ALOAD, 1);
                        String objectTypeInConstructor = ASMUtils.getASMClassNameForSingleFieldIdentityConstructor(fmd.getType());
                        Class primitiveType = ClassUtils.getPrimitiveTypeForType(fmd.getType());
                        if (primitiveType != null) {
                            objectTypeInConstructor = fmd.getTypeName().replace('.', '/');
                        }
                        if (!objectIdClass.equals(enhancer.getObjectIdentityClass().getName()) || primitiveType != null) {
                            visitor.visitTypeInsn(Opcodes.CHECKCAST, objectTypeInConstructor);
                        }
                        visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, ACN_objectIdClass, "<init>", "(Ljava/lang/Class;" + "L" + objectTypeInConstructor + ";)V");
                        visitor.visitInsn(Opcodes.ARETURN);
                        visitor.visitLabel(l3);
                        visitor.visitTypeInsn(Opcodes.NEW, ACN_objectIdClass);
                        visitor.visitInsn(Opcodes.DUP);
                        visitor.visitVarInsn(Opcodes.ALOAD, 0);
                        visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
                        visitor.visitVarInsn(Opcodes.ALOAD, 1);
                        visitor.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/String");
                        visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, ACN_objectIdClass, "<init>", "(Ljava/lang/Class;Ljava/lang/String;" + ")V");
                        visitor.visitInsn(Opcodes.ARETURN);
                        Label endLabel = new Label();
                        visitor.visitLabel(endLabel);
                        visitor.visitLocalVariable("this", getClassEnhancer().getClassDescriptor(), null, startLabel, endLabel, 0);
                        visitor.visitLocalVariable("key", ASMUtils.CD_Object, null, startLabel, endLabel, 1);
                        visitor.visitMaxs(4, 2);
                    }
                } else {
                    String ACN_objectIdClass = objectIdClass.replace('.', '/');
                    visitor.visitTypeInsn(Opcodes.NEW, ACN_objectIdClass);
                    visitor.visitInsn(Opcodes.DUP);
                    visitor.visitVarInsn(Opcodes.ALOAD, 1);
                    visitor.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/String");
                    visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, ACN_objectIdClass, "<init>", "(Ljava/lang/String;)V");
                    visitor.visitInsn(Opcodes.ARETURN);
                    Label endLabel = new Label();
                    visitor.visitLabel(endLabel);
                    visitor.visitLocalVariable("this", getClassEnhancer().getClassDescriptor(), null, startLabel, endLabel, 0);
                    visitor.visitLocalVariable(argNames[0], ASMUtils.CD_Object, null, startLabel, endLabel, 1);
                    visitor.visitMaxs(3, 2);
                    visitor.visitEnd();
                }
            }
        } else {
            visitor.visitInsn(Opcodes.ACONST_NULL);
            visitor.visitInsn(Opcodes.ARETURN);
            Label endLabel = new Label();
            visitor.visitLabel(endLabel);
            visitor.visitLocalVariable("this", getClassEnhancer().getClassDescriptor(), null, startLabel, endLabel, 0);
            visitor.visitLocalVariable(argNames[0], "Ljava/lang/Object;", null, startLabel, endLabel, 1);
            visitor.visitMaxs(1, 2);
        }
        visitor.visitEnd();
    }
}
