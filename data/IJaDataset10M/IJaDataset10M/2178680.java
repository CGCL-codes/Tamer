package org.datanucleus.enhancer.bcel.method;

import java.lang.reflect.Modifier;
import org.apache.bcel.Constants;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.IFNONNULL;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;
import org.datanucleus.enhancer.ClassEnhancer;
import org.datanucleus.enhancer.bcel.BCELClassEnhancer;
import org.datanucleus.enhancer.bcel.BCELClassMethod;
import org.datanucleus.enhancer.bcel.BCELUtils;
import org.datanucleus.enhancer.bcel.metadata.BCELFieldPropertyMetaData;
import org.datanucleus.enhancer.bcel.metadata.BCELMember;
import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaDataManager;
import org.datanucleus.metadata.PropertyMetaData;
import org.datanucleus.util.ClassUtils;

/**
 * create jdoCopyKeyFieldsToObjectId( ObjectIdFieldSupplier fs, Object oid ) method.
 * @version $Revision: 1.14 $
 */
public class JdoCopyKeyFieldsToObjectId2 extends BCELClassMethod {

    /**
     * @param methodName
     * @param type
     * @param resultType
     * @param argType
     * @param argName
     * @param synthetic
     * @param gen
     */
    public JdoCopyKeyFieldsToObjectId2(String methodName, int type, Type resultType, Type[] argType, String[] argName, boolean synthetic, BCELClassEnhancer gen) {
        super(methodName, type, resultType, argType, argName, synthetic, gen);
    }

    public static JdoCopyKeyFieldsToObjectId2 getInstance(BCELClassEnhancer gen) {
        return new JdoCopyKeyFieldsToObjectId2("jdoCopyKeyFieldsToObjectId", Constants.ACC_PUBLIC | Constants.ACC_FINAL, Type.VOID, new Type[] { BCELClassEnhancer.OT_ObjectIdFieldSupplier, Type.OBJECT }, new String[] { "fs", "oid" }, false, gen);
    }

    public void execute() {
        InstructionHandle lv_o[] = new InstructionHandle[2];
        String oidClassName = cmd.getObjectidClass();
        AbstractMemberMetaData fields[] = cmd.getManagedMembers();
        if (cmd.getMetaDataManager().getApiAdapter().isSingleFieldIdentityClass(oidClassName)) {
            createThrowException(ClassEnhancer.CN_JDOFatalInternalException, "It's illegal to call jdoCopyKeyFieldsToObjectId for a class with Single Field Identity.");
        } else if ((oidClassName != null) && (oidClassName.length() > 0)) {
            AbstractMemberMetaData targetFields[] = cmd.getManagedMembers();
            ObjectType oidType = new ObjectType(oidClassName);
            il.append(InstructionConstants.ALOAD_1);
            BranchInstruction checkFmNotNull = new IFNONNULL(null);
            il.append(checkFmNotNull);
            createThrowException(ClassEnhancer.CN_IllegalArgumentException, "ObjectIdFieldSupplier is null");
            checkFmNotNull.setTarget(il.append(InstructionConstants.ALOAD_2));
            il.append(factory.createInstanceOf(oidType));
            il.append(InstructionConstants.ICONST_1);
            BranchInstruction isInstanceof = new IF_ICMPEQ(null);
            il.append(isInstanceof);
            createThrowException(ClassEnhancer.CN_ClassCastException, "oid is not instanceof " + oidClassName);
            isInstanceof.setTarget(il.append(InstructionConstants.ALOAD_2));
            il.append(factory.createCast(Type.OBJECT, oidType));
            lv_o[0] = il.append(new ASTORE(3));
            if (fields != null) {
                for (int i = 0; i < fields.length; i++) {
                    AbstractMemberMetaData f = (AbstractMemberMetaData) fields[i];
                    BCELMember fieldMethod = ((BCELFieldPropertyMetaData) f).getEnhanceField();
                    if (f.isPrimaryKey()) {
                        createPathField(f.getName(), f instanceof PropertyMetaData, getModifiers(oidClassName, f.getName()), InstructionFactory.createLoad(oidType, 3));
                        BCELMember field = ((BCELFieldPropertyMetaData) targetFields[i]).getEnhanceField();
                        il.append(InstructionConstants.ALOAD_1);
                        il.append(BCELUtils.getBIPUSH(i));
                        String paramName = BCELUtils.getJDOMethodName(field.getType());
                        il.append(factory.createInvoke(ClassEnhancer.CN_ObjectIdFieldSupplier, "fetch" + paramName + "Field", BCELUtils.getJDOMethodType(field.getType()), new Type[] { Type.INT }, Constants.INVOKEINTERFACE));
                        if (BCELUtils.getJDOMethodType(field.getType()) == Type.OBJECT) {
                            String type = null;
                            if (field.getType() instanceof ArrayType) type = field.getType().getSignature(); else type = field.getType().toString();
                            il.append(new CHECKCAST(constantPoolGen.addClass(type)));
                        }
                        MetaDataManager mgr = cmd.getMetaDataManager();
                        AbstractClassMetaData cmd = mgr.getMetaDataForClass(f.getType(), enhancer.getClassLoaderResolver());
                        if (cmd != null) {
                            il.append(factory.createInvoke(ClassEnhancer.CN_JDOHelper, "getObjectId", Type.OBJECT, new Type[] { Type.OBJECT }, Constants.INVOKESTATIC));
                            ObjectType OT_objectidclass = new ObjectType(cmd.getObjectidClass());
                            il.append(factory.createCast(Type.OBJECT, OT_objectidclass));
                            createPutField(oidClassName, f.getName(), OT_objectidclass, f instanceof PropertyMetaData, getModifiers(oidClassName, f.getName()));
                        } else {
                            createPutField(oidClassName, f.getName(), fieldMethod.getType(), f instanceof PropertyMetaData, getModifiers(oidClassName, f.getName()));
                        }
                    }
                }
            }
            lv_o[1] = il.append(InstructionConstants.RETURN);
            methodGen.addLocalVariable("o", oidType, lv_o[0], lv_o[1]);
        } else {
            il.append(InstructionConstants.RETURN);
        }
    }

    /**
     * Access the field in the class
     * @param fieldName
     * @param isProperty
     * @param fieldModifiers
     * @param ih
     */
    private void createPathField(String fieldName, boolean isProperty, int fieldModifiers, Instruction ih) {
        il.append(ih);
        if (!isProperty && (Modifier.isPrivate(fieldModifiers) || Modifier.isProtected(fieldModifiers))) {
            il.append(factory.createInvoke("java.lang.Object", "getClass", Type.CLASS, Type.NO_ARGS, Constants.INVOKEVIRTUAL));
            il.append(new PUSH(constantPoolGen, fieldName));
            il.append(factory.createInvoke("java.lang.Class", "getDeclaredField", new ObjectType("java.lang.reflect.Field"), new Type[] { Type.STRING }, Constants.INVOKEVIRTUAL));
            il.append(new ASTORE(4));
            il.append(new ALOAD(4));
            il.append(InstructionConstants.ICONST_1);
            il.append(factory.createInvoke("java.lang.reflect.Field", "setAccessible", Type.VOID, new Type[] { Type.BOOLEAN }, Constants.INVOKEVIRTUAL));
            il.append(new ALOAD(4));
            il.append(ih);
        }
    }

    /**
     * Several forms of setting the fields : via persistent properties; persistent fields; private/protected fields
     * @param className
     * @param fieldName
     * @param fieldType
     * @param isProperty
     * @param isPrivate
     * @param isProtected
     */
    private void createPutField(String className, String fieldName, Type fieldType, boolean isProperty, int fieldModifiers) {
        if (isProperty) {
            il.append(factory.createInvoke(className, ClassUtils.getJavaBeanSetterName(fieldName), Type.VOID, new Type[] { fieldType }, Constants.INVOKEVIRTUAL));
        } else {
            if (!Modifier.isPrivate(fieldModifiers) && !Modifier.isProtected(fieldModifiers)) {
                il.append(factory.createPutField(className, fieldName, fieldType));
            } else {
                if (fieldType == Type.BOOLEAN) {
                    il.append(factory.createInvoke("java.lang.reflect.Field", "setBoolean", Type.VOID, new Type[] { Type.OBJECT, Type.BOOLEAN }, Constants.INVOKEVIRTUAL));
                } else if (fieldType == Type.BYTE) {
                    il.append(factory.createInvoke("java.lang.reflect.Field", "setByte", Type.VOID, new Type[] { Type.OBJECT, Type.BYTE }, Constants.INVOKEVIRTUAL));
                } else if (fieldType == Type.CHAR) {
                    il.append(factory.createInvoke("java.lang.reflect.Field", "setChar", Type.VOID, new Type[] { Type.OBJECT, Type.CHAR }, Constants.INVOKEVIRTUAL));
                } else if (fieldType == Type.DOUBLE) {
                    il.append(factory.createInvoke("java.lang.reflect.Field", "setDouble", Type.VOID, new Type[] { Type.OBJECT, Type.DOUBLE }, Constants.INVOKEVIRTUAL));
                } else if (fieldType == Type.FLOAT) {
                    il.append(factory.createInvoke("java.lang.reflect.Field", "setFloat", Type.VOID, new Type[] { Type.OBJECT, Type.FLOAT }, Constants.INVOKEVIRTUAL));
                } else if (fieldType == Type.INT) {
                    il.append(factory.createInvoke("java.lang.reflect.Field", "setInt", Type.VOID, new Type[] { Type.OBJECT, Type.INT }, Constants.INVOKEVIRTUAL));
                } else if (fieldType == Type.LONG) {
                    il.append(factory.createInvoke("java.lang.reflect.Field", "setLong", Type.VOID, new Type[] { Type.OBJECT, Type.LONG }, Constants.INVOKEVIRTUAL));
                } else if (fieldType == Type.SHORT) {
                    il.append(factory.createInvoke("java.lang.reflect.Field", "setShort", Type.VOID, new Type[] { Type.OBJECT, Type.SHORT }, Constants.INVOKEVIRTUAL));
                } else {
                    il.append(factory.createInvoke("java.lang.reflect.Field", "set", Type.VOID, new Type[] { Type.OBJECT, Type.OBJECT }, Constants.INVOKEVIRTUAL));
                }
            }
        }
    }

    /**
     * Retrieve the field modifiers for the given class
     * @param className
     * @param fieldName
     * @return
     */
    private int getModifiers(String className, String fieldName) {
        JavaClass javaClass;
        try {
            javaClass = Repository.getRepository().loadClass(className);
            for (int i = 0; i < javaClass.getFields().length; i++) {
                if (fieldName.equals(javaClass.getFields()[i].getName())) {
                    return javaClass.getFields()[i].getModifiers();
                }
            }
        } catch (ClassNotFoundException e) {
        }
        return -1;
    }
}
