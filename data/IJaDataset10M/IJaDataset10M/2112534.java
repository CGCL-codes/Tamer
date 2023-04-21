package ch.trackedbean.tracking.injection;

import java.beans.*;
import java.util.*;
import ch.msoftch.asm.*;
import ch.msoftch.asm.tree.*;
import ch.trackedbean.loaders.internal.lazy.*;
import ch.trackedbean.loaders.lazy.*;
import ch.trackedbean.tracking.internal.*;

/**
 * Adapter which implements the {@link StatusInformationInternal} interface and adds {@link PropertyChangeSupport} for all classes defined as
 * {@link IClassInformation#isTracked()}.<br>
 * Child classes (so were {@link IClassInformation#isParentTracked()} is true) were also adapted so that they use the features of the
 * {@link StatusInformationInternal} interface.
 * 
 * @author M. Hautle
 */
@SuppressWarnings("unchecked")
public class TrackedBeanAdapter {

    /** The name of the internal property change method */
    private static final String PROP_CH_METHOD = "_propChanged";

    /** The name of the Beanstatus field */
    private static final String BEAN_STATUS_FIELD = "_BeanStatus";

    /** The name of the property change support field */
    private static final String PROP_CH_SUP_FIELD = "_PropChSup";

    /** The access flags for a protected transient member */
    private static final int PROTECTED_TRANSIENT = Opcodes.ACC_PROTECTED + Opcodes.ACC_TRANSIENT;

    /** The access flags for a public member */
    private static final int PUBLIC_FINAL_ACCESS_FLAG = Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL;

    /** The access flags for a protected member */
    private static final int PROTECTED_FINAL_ACCESS_FLAG = Opcodes.ACC_PROTECTED + Opcodes.ACC_FINAL;

    /** The internal name of the {@link StatusInformationInternal} interface */
    private static final String STATUS_INTERFACE = Type.getInternalName(StatusInformationInternal.class);

    /** The internal name of the {@link PropertyChangeSupport} class */
    private static final String PROPERTY_CH_SUP = Type.getInternalName(PropertyChangeSupport.class);

    /** The internal name of the {@link PropertyChangeListener} class */
    private static final String PROPERTY_CH_LIST = Type.getInternalName(PropertyChangeListener.class);

    /** The internal name of the {@link BeanStatusImpl} class */
    private static final String BEAN_STATUS = Type.getInternalName(BeanStatusImpl.class);

    /** The internal name of the {@link List} class */
    private static final String LIST = Type.getInternalName(List.class);

    /** The internal name of the {@link LazyLoaderManager} class */
    private static final String LAZY_MANAGER = Type.getInternalName(LazyLoaderManager.class);

    /** The description of the {@link StatusInformationInternal#_getBeanStatus()} method */
    private static final String GET_BEAN_STATUS_M_DESC = "()L" + BEAN_STATUS + ";";

    /** The description of the {@link LoadLazy} annotation class */
    private static final String LAZY_ANNOTATION_DESC = "L" + Type.getInternalName(LoadLazy.class) + ";";

    /** The description of the {@link BeanStatusImpl} field */
    private static final String BEAN_STATUS_FIELD_DESC = "L" + BEAN_STATUS + ";";

    /** The description of the {@link BeanStatusImpl} constructor */
    private static final String BEAN_STATUS_CONST_DESC = "(L" + Type.getInternalName(Object.class) + ";)V";

    /** The description of the propertyChangeListener methods */
    private static final String PROPERTY_CH_LIST_M_DESC = "(L" + PROPERTY_CH_LIST + ";)V";

    /** The description of the {@link PropertyChangeSupport} field */
    private static final String PROPERTY_CH_SUP_FIELD_DESC = "L" + PROPERTY_CH_SUP + ";";

    /** The OP Code groups of a "normal" setter */
    private static final int[] NORMAL_SETTER = new int[] { AbstractInsnNode.VAR_INSN, AbstractInsnNode.VAR_INSN, AbstractInsnNode.FIELD_INSN };

    /** The OP Code groups of a "normal" getter */
    private static final int[] NORMAL_GETTER = new int[] { AbstractInsnNode.VAR_INSN, AbstractInsnNode.FIELD_INSN };

    /** The class analyzer to use. */
    private final IClassAnalyzer classAnalyzer;

    /**
     * Default constructor.
     * 
     * @param analyzer The analyzer to use
     */
    public TrackedBeanAdapter(IClassAnalyzer analyzer) {
        classAnalyzer = analyzer;
    }

    /**
     * Adapts the class represented by the given byte array.
     * 
     * @param ci The corresponding class information
     * @param b The class file as byte array
     * @return The byte array to use for the class declaration
     */
    public byte[] adapt(IClassInformation ci, byte[] b) {
        final ClassNode cn = new ClassNode();
        new ClassReader(b).accept(cn, 0);
        if (ci.isTracked() && !ci.isParentTracked()) adaptCompleteClass(cn, ci.isSerializable()); else if (ci.isParentTracked()) adaptSettersOnly(cn, ci.isSerializable());
        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        return cw.toByteArray();
    }

    /**
     * Adapts all setter methods of the given class.<br>
     * Used for classes where a parent class implements "TrackedBean"
     * 
     * @param cn The class to adapt
     * @param serializable If the class is serializable
     */
    private void adaptSettersOnly(final ClassNode cn, final boolean serializable) {
        final List methods = cn.methods;
        InsnList deserialInst = null;
        MethodNode deserialMeth = null;
        if (serializable) {
            deserialMeth = new MethodNode(Opcodes.ACC_PRIVATE, "readObject", "(Ljava/io/ObjectInputStream;)V", null, new String[] { "java/io/IOException", "java/lang/ClassNotFoundException" });
            deserialInst = deserialMeth.instructions;
        }
        final StringBuilder propertyName = new StringBuilder();
        for (int i = 0, cnt = methods.size(); i < cnt; i++) {
            final MethodNode m = (MethodNode) methods.get(i);
            if (!adaptable(m.access)) continue;
            final String methodName = m.name;
            if (isProperty(methodName)) adaptProperty(deserialInst, propertyName, m); else if (methodName.charAt(0) == '<') preinitializeLists(m, cn.name);
        }
        if (deserialInst != null && deserialInst.size() > 0) {
            deserialInst.insert(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/ObjectInputStream", "defaultReadObject", "()V"));
            deserialInst.insert(new VarInsnNode(Opcodes.ALOAD, 1));
            deserialInst.add(new InsnNode(Opcodes.RETURN));
            cn.methods.add(deserialMeth);
        }
    }

    /**
     * Implements the {@link StatusInformationInternal} interface in the given class and adapt all setters.
     * 
     * @param cn The class where to implement
     * @param serializable True if the class is serializable
     */
    private void adaptCompleteClass(final ClassNode cn, final boolean serializable) {
        cn.fields.add(new FieldNode(PROTECTED_TRANSIENT, PROP_CH_SUP_FIELD, PROPERTY_CH_SUP_FIELD_DESC, null, null));
        cn.fields.add(new FieldNode(PROTECTED_TRANSIENT, BEAN_STATUS_FIELD, BEAN_STATUS_FIELD_DESC, null, null));
        cn.interfaces.add(STATUS_INTERFACE);
        MethodNode m = new MethodNode(PUBLIC_FINAL_ACCESS_FLAG, "_getBeanStatus", GET_BEAN_STATUS_M_DESC, null, null);
        InsnList instr = m.instructions;
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new FieldInsnNode(Opcodes.GETFIELD, cn.name, BEAN_STATUS_FIELD, BEAN_STATUS_FIELD_DESC));
        instr.add(new InsnNode(Opcodes.ARETURN));
        final List methods = cn.methods;
        methods.add(m);
        m = new MethodNode(PROTECTED_FINAL_ACCESS_FLAG, PROP_CH_METHOD, "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", null, null);
        instr = m.instructions;
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new FieldInsnNode(Opcodes.GETFIELD, cn.name, PROP_CH_SUP_FIELD, PROPERTY_CH_SUP_FIELD_DESC));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 1));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 2));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 3));
        instr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, PROPERTY_CH_SUP, "firePropertyChange", "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new FieldInsnNode(Opcodes.GETFIELD, cn.name, BEAN_STATUS_FIELD, BEAN_STATUS_FIELD_DESC));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 1));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 2));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 3));
        instr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, BEAN_STATUS, "propertyChanged", "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"));
        instr.add(new InsnNode(Opcodes.RETURN));
        methods.add(m);
        m = new MethodNode(Opcodes.ACC_PUBLIC, "addPropertyChangeListener", PROPERTY_CH_LIST_M_DESC, null, null);
        instr = m.instructions;
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new FieldInsnNode(Opcodes.GETFIELD, cn.name, PROP_CH_SUP_FIELD, PROPERTY_CH_SUP_FIELD_DESC));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 1));
        instr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, PROPERTY_CH_SUP, "addPropertyChangeListener", PROPERTY_CH_LIST_M_DESC));
        instr.add(new InsnNode(Opcodes.RETURN));
        methods.add(m);
        m = new MethodNode(Opcodes.ACC_PUBLIC, "removePropertyChangeListener", PROPERTY_CH_LIST_M_DESC, null, null);
        instr = m.instructions;
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new FieldInsnNode(Opcodes.GETFIELD, cn.name, PROP_CH_SUP_FIELD, PROPERTY_CH_SUP_FIELD_DESC));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 1));
        instr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, PROPERTY_CH_SUP, "removePropertyChangeListener", PROPERTY_CH_LIST_M_DESC));
        instr.add(new InsnNode(Opcodes.RETURN));
        methods.add(m);
        InsnList deserialMeth = null;
        if (serializable) {
            m = new MethodNode(Opcodes.ACC_PRIVATE, "readObject", "(Ljava/io/ObjectInputStream;)V", null, new String[] { "java/io/IOException", "java/lang/ClassNotFoundException" });
            deserialMeth = m.instructions;
            deserialMeth.add(new VarInsnNode(Opcodes.ALOAD, 1));
            deserialMeth.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/ObjectInputStream", "defaultReadObject", "()V"));
            deserialMeth.add(new VarInsnNode(Opcodes.ALOAD, 0));
            deserialMeth.add(new TypeInsnNode(Opcodes.NEW, PROPERTY_CH_SUP));
            deserialMeth.add(new InsnNode(Opcodes.DUP));
            deserialMeth.add(new VarInsnNode(Opcodes.ALOAD, 0));
            deserialMeth.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, PROPERTY_CH_SUP, "<init>", "(Ljava/lang/Object;)V"));
            deserialMeth.add(new FieldInsnNode(Opcodes.PUTFIELD, cn.name, PROP_CH_SUP_FIELD, PROPERTY_CH_SUP_FIELD_DESC));
            deserialMeth.add(new VarInsnNode(Opcodes.ALOAD, 0));
            deserialMeth.add(new TypeInsnNode(Opcodes.NEW, BEAN_STATUS));
            deserialMeth.add(new InsnNode(Opcodes.DUP));
            deserialMeth.add(new VarInsnNode(Opcodes.ALOAD, 0));
            deserialMeth.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, BEAN_STATUS, "<init>", BEAN_STATUS_CONST_DESC));
            deserialMeth.add(new FieldInsnNode(Opcodes.PUTFIELD, cn.name, BEAN_STATUS_FIELD, BEAN_STATUS_FIELD_DESC));
            methods.add(m);
        }
        final StringBuilder propertyName = new StringBuilder();
        for (int i = 0, cnt = methods.size(); i < cnt; i++) {
            m = (MethodNode) methods.get(i);
            if (!adaptable(m.access)) continue;
            final String methodName = m.name;
            if (isProperty(methodName)) {
                adaptProperty(deserialMeth, propertyName, m);
            } else if (methodName.charAt(0) == '<') {
                preinitializeLists(m, cn.name);
                adaptConstructor(m, cn.name);
            }
        }
        if (deserialMeth != null) deserialMeth.add(new InsnNode(Opcodes.RETURN));
    }

    /**
     * Check wherever the given method may be a property accessor.<br>
     * <code>boolean</code> getters were ignored - the were not suitable for weaving.
     * 
     * @param methodName A method name
     * @return True if it seams to be a property accessor
     */
    private boolean isProperty(final String methodName) {
        final int len = methodName.length();
        if (len > 3 && Character.isUpperCase(methodName.charAt(3))) return true;
        return len > 4 && Character.isUpperCase(methodName.charAt(4));
    }

    /**
     * Checks if a method with the given access flags is adaptable
     * 
     * @param access The access flags
     * @return True if the method is adaptable
     */
    private boolean adaptable(final int access) {
        return (access & Opcodes.ACC_NATIVE) != Opcodes.ACC_NATIVE && (access & Opcodes.ACC_ABSTRACT) != Opcodes.ACC_ABSTRACT && (access & Opcodes.ACC_STATIC) != Opcodes.ACC_STATIC;
    }

    /**
     * Checks if the passed method represents a property and adapts it if necessary.
     * 
     * @param serRead The node list of the readObject methode or null if there is none
     * @param worker The string builder to use (to avoid object trashing)
     * @param m The method node which may be a getter/setter
     */
    @SuppressWarnings("cast")
    private void adaptProperty(final InsnList serRead, final StringBuilder worker, final MethodNode m) {
        final String methodName = m.name;
        if (methodName.startsWith("set")) {
            adaptSetter(m, extractPropertyName(worker, methodName), serRead);
            return;
        }
        if (m.visibleAnnotations == null || !methodName.startsWith("get")) return;
        final List<AnnotationNode> ans = (List<AnnotationNode>) m.visibleAnnotations;
        for (int i = 0, cnt = ans.size(); i < cnt; i++) {
            if (!LAZY_ANNOTATION_DESC.equals(ans.get(i).desc)) continue;
            adaptGetter(m, extractPropertyName(worker, methodName), serRead);
            return;
        }
    }

    /**
     * Extracts the property name out of a method name.
     * 
     * @param worker The string builder to use (to avoid object trashing)
     * @param methodName The method name
     * @return The property name for the method
     */
    private String extractPropertyName(final StringBuilder worker, final String methodName) {
        worker.setLength(0);
        worker.append(methodName, 3, methodName.length());
        if (worker.length() == 1 || Character.isLowerCase(worker.charAt(1))) worker.setCharAt(0, Character.toLowerCase(worker.charAt(0)));
        return worker.toString();
    }

    /**
     * Adapts the given getter for lazy loading
     * 
     * @param m The method node representing the getter
     * @param property The name of the property
     * @param serRead The node list of the readObject method or null if there is none
     */
    private void adaptGetter(final MethodNode m, final String property, final InsnList serRead) {
        final Type[] args = Type.getArgumentTypes(m.desc);
        final Type ret = Type.getReturnType(m.desc);
        if (args.length != 0 || Type.VOID == ret.getSort()) return;
        final FieldInsnNode getNode = extractPropertyNode(m, NORMAL_GETTER, Opcodes.ARETURN);
        if (getNode == null) return;
        switch(ret.getSort()) {
            case Type.OBJECT:
            case Type.ARRAY:
                adaptObjectGetter(property, m, ret, getNode.name, getNode.desc, getNode.owner);
                break;
            default:
                throw new RuntimeException("Unknown Getter type: " + ret.getClassName());
        }
        if (serRead != null) deserializeLazyAdaption(serRead, property, getNode.name, getNode.desc, getNode.owner);
    }

    /**
     * Returns the field access node of a getter/setter method.
     * 
     * @param m The method
     * @param instructions The expected opcodes ({@link #NORMAL_GETTER} or {@value #NORMAL_SETTER})
     * @param returnCode The expected return code after the opcode sequence
     * @return The node accessing the field or null if the method had not the expected structure
     */
    private FieldInsnNode extractPropertyNode(MethodNode m, final int[] instructions, final int returnCode) {
        AbstractInsnNode inst = m.instructions.getFirst();
        for (int i = 0; inst != null && i < instructions.length; inst = inst.getNext()) {
            final int type = inst.getType();
            if (type == AbstractInsnNode.LABEL || type == AbstractInsnNode.LINE) continue;
            if (type != instructions[i++]) return null;
        }
        if (inst == null) return null;
        final FieldInsnNode n = ((FieldInsnNode) inst.getPrevious());
        for (; inst != null; inst = inst.getNext()) {
            final int type = inst.getType();
            if (type != AbstractInsnNode.LABEL && type != AbstractInsnNode.LINE && inst.getOpcode() != returnCode) return null;
        }
        return n;
    }

    /**
     * Adapts the readObject method so that the status of a {@link LoadLazy} annotated property will initialized.
     * 
     * @param serRead The node list of the readObject method
     * @param propertyName The name of the property represented by this getter
     * @param fieldName The name of the field to get (so where the setter stores the value)
     * @param fieldType The type of the field where the value is stored
     * @param owner The owning class
     */
    private void deserializeLazyAdaption(final InsnList serRead, final String propertyName, final String fieldName, final String fieldType, final String owner) {
        final LabelNode blockEnd = new LabelNode();
        serRead.add(new VarInsnNode(Opcodes.ALOAD, 0));
        serRead.add(new FieldInsnNode(Opcodes.GETFIELD, owner, fieldName, fieldType));
        serRead.add(new JumpInsnNode(Opcodes.IFNONNULL, blockEnd));
        serRead.add(new VarInsnNode(Opcodes.ALOAD, 0));
        serRead.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, owner, "_getBeanStatus", GET_BEAN_STATUS_M_DESC));
        serRead.add(new LdcInsnNode(propertyName));
        serRead.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, BEAN_STATUS, "setLazy", "(Ljava/lang/String;)V"));
        serRead.add(blockEnd);
    }

    /**
     * Adapts a getter for objects or arrays
     * 
     * @param propertyName The name of the property represented by this getter
     * @param m The getter method
     * @param returnType The returntype of the getter
     * @param fieldName The name of the field to get (so where the setter stores the value)
     * @param fieldType The type of the field where the value is stored
     * @param owner The owning class
     */
    private void adaptObjectGetter(final String propertyName, final MethodNode m, final Type returnType, final String fieldName, final String fieldType, final String owner) {
        final ListIterator instr = m.instructions.iterator();
        final LabelNode blockEnd = new LabelNode();
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new FieldInsnNode(Opcodes.GETFIELD, owner, fieldName, fieldType));
        instr.add(new JumpInsnNode(Opcodes.IFNONNULL, blockEnd));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, owner, "_getBeanStatus", GET_BEAN_STATUS_M_DESC));
        instr.add(new LdcInsnNode(propertyName));
        instr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, BEAN_STATUS, "loadField", "(Ljava/lang/String;)Z"));
        instr.add(new JumpInsnNode(Opcodes.IFEQ, blockEnd));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new LdcInsnNode(Type.getObjectType(owner)));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new LdcInsnNode(propertyName));
        instr.add(new MethodInsnNode(Opcodes.INVOKESTATIC, LAZY_MANAGER, "loadProperty", "(Ljava/lang/Class;Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;"));
        instr.add(new TypeInsnNode(Opcodes.CHECKCAST, returnType.getInternalName()));
        instr.add(new FieldInsnNode(Opcodes.PUTFIELD, owner, fieldName, fieldType));
        instr.add(blockEnd);
    }

    /**
     * Adapts the given setter so that it notifies listeners when it's called
     * 
     * @param m The method node representing the setter
     * @param property The name of the property
     * @param serRead The node list of the readObject method or null if there is none
     */
    private void adaptSetter(final MethodNode m, final String property, final InsnList serRead) {
        final Type[] args = Type.getArgumentTypes(m.desc);
        if (args.length != 1) return;
        final FieldInsnNode setNode = extractPropertyNode(m, NORMAL_SETTER, Opcodes.RETURN);
        if (setNode == null) return;
        switch(args[0].getSort()) {
            case Type.OBJECT:
                final String name = args[0].getInternalName();
                if (name.equals(LIST)) {
                    adaptListSetter(property, m, setNode.name, setNode.desc, setNode.owner, serRead);
                    break;
                } else if (implementsClassStructureFlag(name)) {
                    adaptBeanSetter(property, m, setNode.name, setNode.desc, setNode.owner, serRead);
                    break;
                }
            case Type.ARRAY:
                adaptObjectSetter(property, m, setNode.name, setNode.desc, setNode.owner);
                break;
            case Type.INT:
                adaptPrimitiveSetter(property, m, "java/lang/Integer", "(I)V", setNode.name, setNode.desc, setNode.owner);
                break;
            case Type.LONG:
                adaptBigPrimitiveSetter(property, m, "java/lang/Long", "(J)V", setNode.name, setNode.desc, setNode.owner);
                break;
            case Type.BOOLEAN:
                adaptPrimitiveSetter(property, m, "java/lang/Boolean", "(Z)V", setNode.name, setNode.desc, setNode.owner);
                break;
            case Type.DOUBLE:
                adaptBigPrimitiveSetter(property, m, "java/lang/Double", "(D)V", setNode.name, setNode.desc, setNode.owner);
                break;
            case Type.FLOAT:
                adaptPrimitiveSetter(property, m, "java/lang/Float", "(F)V", setNode.name, setNode.desc, setNode.owner);
                break;
            case Type.SHORT:
                adaptPrimitiveSetter(property, m, "java/lang/Short", "(S)V", setNode.name, setNode.desc, setNode.owner);
                break;
            case Type.CHAR:
                adaptPrimitiveSetter(property, m, "java/lang/Character", "(C)V", setNode.name, setNode.desc, setNode.owner);
                break;
            case Type.BYTE:
                adaptPrimitiveSetter(property, m, "java/lang/Byte", "(B)V", setNode.name, setNode.desc, setNode.owner);
                break;
            default:
                throw new RuntimeException("Unknown Setter type: " + args[0].getClassName());
        }
    }

    /**
     * Injects the code for the initializing of the fields _PropChSup and _BeanStatus
     * 
     * @param m The constructor to patch
     * @param className The internal name of the class where this constructor is defined
     */
    private void adaptConstructor(final MethodNode m, final String className) {
        final ListIterator instr = m.instructions.iterator();
        AbstractInsnNode inst = (AbstractInsnNode) instr.next();
        while (inst.getType() != AbstractInsnNode.METHOD_INSN) inst = (AbstractInsnNode) instr.next();
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new TypeInsnNode(Opcodes.NEW, PROPERTY_CH_SUP));
        instr.add(new InsnNode(Opcodes.DUP));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, PROPERTY_CH_SUP, "<init>", "(Ljava/lang/Object;)V"));
        instr.add(new FieldInsnNode(Opcodes.PUTFIELD, className, PROP_CH_SUP_FIELD, PROPERTY_CH_SUP_FIELD_DESC));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new TypeInsnNode(Opcodes.NEW, BEAN_STATUS));
        instr.add(new InsnNode(Opcodes.DUP));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, BEAN_STATUS, "<init>", BEAN_STATUS_CONST_DESC));
        instr.add(new FieldInsnNode(Opcodes.PUTFIELD, className, BEAN_STATUS_FIELD, BEAN_STATUS_FIELD_DESC));
    }

    /**
     * Wraps {@link List} created in the constructor into {@link StatusList}s before they get assigned to a instance field.
     * 
     * @param m The constructor to patch
     * @param className The internal name of the class where this constructor is defined
     */
    private void preinitializeLists(final MethodNode m, final String className) {
        final ListIterator instr = m.instructions.iterator();
        for (AbstractInsnNode inst = (AbstractInsnNode) instr.next(); instr.hasNext(); inst = (AbstractInsnNode) instr.next()) {
            if (inst.getType() != AbstractInsnNode.FIELD_INSN || inst.getOpcode() != Opcodes.PUTFIELD) continue;
            final FieldInsnNode n = (FieldInsnNode) inst;
            if (!"Ljava/util/List;".equals(n.desc) || !className.equals(n.owner)) continue;
            instr.previous();
            instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
            instr.add(new FieldInsnNode(Opcodes.GETFIELD, className, BEAN_STATUS_FIELD, BEAN_STATUS_FIELD_DESC));
            instr.add(new InsnNode(Opcodes.SWAP));
            instr.add(new InsnNode(Opcodes.ACONST_NULL));
            instr.add(new InsnNode(Opcodes.SWAP));
            instr.add(new LdcInsnNode(((FieldInsnNode) inst).name));
            instr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, BEAN_STATUS, "registerChildList", "(Ljava/util/List;Ljava/util/List;Ljava/lang/String;)Ljava/util/List;"));
            instr.next();
        }
    }

    /**
     * Adapts setters for primitive values (not for Long/Double, for those use
     * {@link #adaptBigPrimitiveSetter(String, MethodNode, String, String, String, String, String)})
     * 
     * @param propertyName The name of the property represented by this setter
     * @param m The setter method
     * @param wrapperType The wrapper type for this simple type
     * @param wraperDesc The description for the wraper type constructor
     * @param fieldName The name of the field to set (so where the setter stores the value)
     * @param fieldType The type of the field where the value is stored
     * @param owner The owning class
     */
    private void adaptPrimitiveSetter(final String propertyName, final MethodNode m, final String wrapperType, final String wraperDesc, final String fieldName, final String fieldType, final String owner) {
        final ListIterator instr = m.instructions.iterator();
        instr.add(new TypeInsnNode(Opcodes.NEW, wrapperType));
        instr.add(new InsnNode(Opcodes.DUP));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new FieldInsnNode(Opcodes.GETFIELD, owner, fieldName, fieldType));
        instr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, wrapperType, "<init>", wraperDesc));
        instr.add(new VarInsnNode(Opcodes.ASTORE, 2));
        AbstractInsnNode inst = (AbstractInsnNode) instr.next();
        while (inst != null && inst.getOpcode() != Opcodes.RETURN) inst = (AbstractInsnNode) instr.next();
        instr.previous();
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new LdcInsnNode(propertyName));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 2));
        instr.add(new TypeInsnNode(Opcodes.NEW, wrapperType));
        instr.add(new InsnNode(Opcodes.DUP));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new FieldInsnNode(Opcodes.GETFIELD, owner, fieldName, fieldType));
        instr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, wrapperType, "<init>", wraperDesc));
        instr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, owner, PROP_CH_METHOD, "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"));
    }

    /**
     * Adapts setters for long and double values (for other primitive values use
     * {@link #adaptPrimitiveSetter(String, MethodNode, String, String, String, String, String)})
     * 
     * @param propertyName The name of the property represented by this setter
     * @param m The setter method
     * @param wrapperType The wrapper type for this simple type
     * @param wraperDesc The description for the wraper type constructor
     * @param fieldName The name of the field to set (so where the setter stores the value)
     * @param fieldType The type of the field where the value is stored
     * @param owner The owning class
     */
    private void adaptBigPrimitiveSetter(final String propertyName, final MethodNode m, final String wrapperType, final String wraperDesc, final String fieldName, final String fieldType, final String owner) {
        final ListIterator instr = m.instructions.iterator();
        instr.add(new TypeInsnNode(Opcodes.NEW, wrapperType));
        instr.add(new InsnNode(Opcodes.DUP));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new FieldInsnNode(Opcodes.GETFIELD, owner, fieldName, fieldType));
        instr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, wrapperType, "<init>", wraperDesc));
        instr.add(new VarInsnNode(Opcodes.ASTORE, 3));
        AbstractInsnNode inst = (AbstractInsnNode) instr.next();
        while (inst != null && inst.getOpcode() != Opcodes.RETURN) inst = (AbstractInsnNode) instr.next();
        instr.previous();
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new LdcInsnNode(propertyName));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 3));
        instr.add(new TypeInsnNode(Opcodes.NEW, wrapperType));
        instr.add(new InsnNode(Opcodes.DUP));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new FieldInsnNode(Opcodes.GETFIELD, owner, fieldName, fieldType));
        instr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, wrapperType, "<init>", wraperDesc));
        instr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, owner, PROP_CH_METHOD, "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"));
    }

    /**
     * Adapts a setter for objects or arrays
     * 
     * @param propertyName The name of the property represented by this setter
     * @param m The setter method
     * @param fieldName The name of the field to set (so where the setter stores the value)
     * @param fieldType The type of the field where the value is stored
     * @param owner The owning class
     */
    private void adaptObjectSetter(final String propertyName, final MethodNode m, final String fieldName, final String fieldType, final String owner) {
        final ListIterator instr = m.instructions.iterator();
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new FieldInsnNode(Opcodes.GETFIELD, owner, fieldName, fieldType));
        instr.add(new VarInsnNode(Opcodes.ASTORE, 2));
        AbstractInsnNode inst = (AbstractInsnNode) instr.next();
        while (inst != null && inst.getOpcode() != Opcodes.RETURN) inst = (AbstractInsnNode) instr.next();
        instr.previous();
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new LdcInsnNode(propertyName));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 2));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new FieldInsnNode(Opcodes.GETFIELD, owner, fieldName, fieldType));
        instr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, owner, PROP_CH_METHOD, "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"));
    }

    /**
     * Adapts a setter for {@link List}s
     * 
     * @param propertyName The name of the property represented by this setter
     * @param m The setter method
     * @param fieldName The name of the field to set (so where the setter stores the value)
     * @param fieldType The type of the field where the value is stored
     * @param owner The owning class
     * @param serRead The node list of the readObject methode or null if there is none
     */
    private void adaptListSetter(final String propertyName, final MethodNode m, final String fieldName, final String fieldType, final String owner, final InsnList serRead) {
        final ListIterator instr = m.instructions.iterator();
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new FieldInsnNode(Opcodes.GETFIELD, owner, fieldName, fieldType));
        instr.add(new VarInsnNode(Opcodes.ASTORE, 2));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new FieldInsnNode(Opcodes.GETFIELD, owner, BEAN_STATUS_FIELD, BEAN_STATUS_FIELD_DESC));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 2));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 1));
        instr.add(new LdcInsnNode(propertyName));
        instr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, BEAN_STATUS, "registerChildList", "(Ljava/util/List;Ljava/util/List;Ljava/lang/String;)Ljava/util/List;"));
        instr.add(new VarInsnNode(Opcodes.ASTORE, 1));
        AbstractInsnNode inst = (AbstractInsnNode) instr.next();
        while (inst != null && inst.getOpcode() != Opcodes.RETURN) inst = (AbstractInsnNode) instr.next();
        instr.previous();
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new LdcInsnNode(propertyName));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 2));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new FieldInsnNode(Opcodes.GETFIELD, owner, fieldName, fieldType));
        instr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, owner, PROP_CH_METHOD, "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"));
        if (serRead != null) {
            serRead.add(new VarInsnNode(Opcodes.ALOAD, 0));
            serRead.add(new VarInsnNode(Opcodes.ALOAD, 0));
            serRead.add(new FieldInsnNode(Opcodes.GETFIELD, owner, BEAN_STATUS_FIELD, BEAN_STATUS_FIELD_DESC));
            serRead.add(new InsnNode(Opcodes.ACONST_NULL));
            serRead.add(new VarInsnNode(Opcodes.ALOAD, 0));
            serRead.add(new FieldInsnNode(Opcodes.GETFIELD, owner, fieldName, fieldType));
            serRead.add(new LdcInsnNode(propertyName));
            serRead.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, BEAN_STATUS, "registerChildList", "(Ljava/util/List;Ljava/util/List;Ljava/lang/String;)Ljava/util/List;"));
            serRead.add(new FieldInsnNode(Opcodes.PUTFIELD, owner, fieldName, fieldType));
        }
    }

    /**
     * Adapts a setter for "TrackedBeans"
     * 
     * @param propertyName The name of the property represented by this setter
     * @param m The setter method
     * @param fieldName The name of the field to set (so where the setter stores the value)
     * @param fieldType The type of the field where the value is stored
     * @param owner The owning class
     * @param serRead The node list of the readObject methode or null if there is none
     */
    private void adaptBeanSetter(final String propertyName, final MethodNode m, final String fieldName, final String fieldType, final String owner, final InsnList serRead) {
        final ListIterator instr = m.instructions.iterator();
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new FieldInsnNode(Opcodes.GETFIELD, owner, fieldName, fieldType));
        instr.add(new VarInsnNode(Opcodes.ASTORE, 2));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new FieldInsnNode(Opcodes.GETFIELD, owner, BEAN_STATUS_FIELD, BEAN_STATUS_FIELD_DESC));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 2));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 1));
        instr.add(new LdcInsnNode(propertyName));
        instr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, BEAN_STATUS, "registerChild", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/String;)V"));
        AbstractInsnNode inst = (AbstractInsnNode) instr.next();
        while (inst != null && inst.getOpcode() != Opcodes.RETURN) inst = (AbstractInsnNode) instr.next();
        instr.previous();
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new LdcInsnNode(propertyName));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 2));
        instr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instr.add(new FieldInsnNode(Opcodes.GETFIELD, owner, fieldName, fieldType));
        instr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, owner, PROP_CH_METHOD, "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"));
        if (serRead != null) {
            serRead.add(new VarInsnNode(Opcodes.ALOAD, 0));
            serRead.add(new FieldInsnNode(Opcodes.GETFIELD, owner, BEAN_STATUS_FIELD, BEAN_STATUS_FIELD_DESC));
            serRead.add(new InsnNode(Opcodes.ACONST_NULL));
            serRead.add(new VarInsnNode(Opcodes.ALOAD, 0));
            serRead.add(new FieldInsnNode(Opcodes.GETFIELD, owner, fieldName, fieldType));
            instr.add(new LdcInsnNode(propertyName));
            instr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, BEAN_STATUS, "registerChild", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/String;)V"));
        }
    }

    /**
     * Analyze the class structure of the given class
     * 
     * @param name The class your interested in
     * @return True if the class or one of it's parents has the marker interface implemented
     */
    private boolean implementsClassStructureFlag(final String name) {
        final IClassInformation known = classAnalyzer.getInformation(name);
        return known.isTracked() || known.isParentTracked();
    }
}
