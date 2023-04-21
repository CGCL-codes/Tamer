package de.unkrig.commons.asm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class StringConcatenator {

    public static final Map<Type, String> STRING_BUILDER_APPEND_TYPES;

    static {
        Map<Type, String> m = new HashMap<Type, String>();
        m.put(Type.BOOLEAN_TYPE, "(Z)Ljava/lang/StringBuilder;");
        m.put(Type.CHAR_TYPE, "(C)Ljava/lang/StringBuilder;");
        m.put(Type.BYTE_TYPE, "(I)Ljava/lang/StringBuilder;");
        m.put(Type.SHORT_TYPE, "(I)Ljava/lang/StringBuilder;");
        m.put(Type.INT_TYPE, "(I)Ljava/lang/StringBuilder;");
        m.put(Type.FLOAT_TYPE, "(F)Ljava/lang/StringBuilder;");
        m.put(Type.LONG_TYPE, "(J)Ljava/lang/StringBuilder;");
        m.put(Type.DOUBLE_TYPE, "(D)Ljava/lang/StringBuilder;");
        m.put(Types.OBJECT_TYPE, "(Ljava/lang/Object;)Ljava/lang/StringBuilder;");
        m.put(Types.CHAR_SEQUENCE_TYPE, "(Ljava/lang/CharSequence;)Ljava/lang/StringBuilder;");
        m.put(Types.STRING_TYPE, "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
        m.put(Types.STRING_BUFFER_TYPE, "(Ljava/lang/StringBuffer;)Ljava/lang/StringBuilder;");
        STRING_BUILDER_APPEND_TYPES = Collections.unmodifiableMap(m);
    }

    public static final Map<Type, String> STRING_VALUE_OF_TYPES;

    static {
        Map<Type, String> m = new HashMap<Type, String>();
        m.put(Type.BOOLEAN_TYPE, "(Z)Ljava/lang/String;");
        m.put(Type.CHAR_TYPE, "(C)Ljava/lang/String;");
        m.put(Type.BYTE_TYPE, "(I)Ljava/lang/String;");
        m.put(Type.SHORT_TYPE, "(I)Ljava/lang/String;");
        m.put(Type.INT_TYPE, "(I)Ljava/lang/String;");
        m.put(Type.FLOAT_TYPE, "(F)Ljava/lang/String;");
        m.put(Type.LONG_TYPE, "(J)Ljava/lang/String;");
        m.put(Type.DOUBLE_TYPE, "(D)Ljava/lang/String;");
        m.put(Types.OBJECT_TYPE, "(Ljava/lang/Object;)Ljava/lang/String;");
        STRING_VALUE_OF_TYPES = Collections.unmodifiableMap(m);
    }

    private final List<Component> components = new ArrayList<Component>();

    public StringConcatenator appendConstant(String value) {
        if (value.length() == 0) return this;
        if (!this.components.isEmpty()) {
            Component c = this.components.get(this.components.size() - 1);
            if (c instanceof ConstantComponent) {
                ConstantComponent cc = (ConstantComponent) c;
                cc.value += value;
                return this;
            }
        }
        this.components.add(new ConstantComponent(value));
        return this;
    }

    public StringConcatenator appendVariable(InsnList insns, Type type) {
        this.components.add(new VariableComponent(insns, type));
        return this;
    }

    public StringConcatenator appendVariablePrettily(InsnList code, Type type) {
        if (type.getSort() == Type.ARRAY) {
            Type et = type.getElementType();
            if (et.getSort() == Type.ARRAY || et.getSort() == Type.OBJECT) {
                code.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/util/Arrays", "deepToString", "([Ljava/lang/Object;)Ljava/lang/String;"));
                appendVariable(code, Types.STRING_TYPE);
            } else {
                code.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/util/Arrays", "toString", "([" + et.getDescriptor() + ")Ljava/lang/String;"));
                appendVariable(code, Types.STRING_TYPE);
            }
        } else if (type.equals(Types.STRING_TYPE) || type.equals(Types.STRING_BUFFER_TYPE) || type.equals(Types.STRING_BUILDER_TYPE) || type.equals(Types.CHAR_SEQUENCE_TYPE)) {
            appendConstant("\"");
            appendVariable(code, type);
            appendConstant("\"");
        } else if (type.equals(Type.CHAR_TYPE)) {
            appendConstant("'");
            appendVariable(code, Type.CHAR_TYPE);
            appendConstant("'");
        } else {
            appendVariable(code, type);
        }
        return this;
    }

    public InsnList finish() {
        InsnList insns = new InsnList();
        switch(this.components.size()) {
            case 0:
                insns.add(new LdcInsnNode(""));
                break;
            case 1:
                this.components.get(0).pushAsString(insns);
                break;
            case 2:
                this.components.get(0).pushAsString(insns);
                this.components.get(1).pushAsString(insns);
                insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;"));
                break;
            default:
                insns.add(new TypeInsnNode(Opcodes.NEW, "java/lang/StringBuilder"));
                insns.add(new InsnNode(Opcodes.DUP));
                (this.components.get(0)).pushAsString(insns);
                insns.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V"));
                for (int i = 1; i < this.components.size(); ++i) {
                    Component c = this.components.get(i);
                    Type t = c.push(insns);
                    String md = STRING_BUILDER_APPEND_TYPES.get(t);
                    if (md == null) md = "(Ljava/lang/Object;)Ljava/lang/StringBuilder;";
                    insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", md));
                }
                insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;"));
        }
        return insns;
    }

    abstract static interface Component {

        public abstract Type push(InsnList paramInsnList);

        public abstract void pushAsString(InsnList paramInsnList);
    }

    class ConstantComponent implements StringConcatenator.Component {

        String value;

        public ConstantComponent(String value) {
            this.value = value;
        }

        public Type push(InsnList result) {
            result.add(new LdcInsnNode(this.value));
            return Types.STRING_TYPE;
        }

        public void pushAsString(InsnList result) {
            result.add(new LdcInsnNode(this.value));
        }
    }

    class VariableComponent implements StringConcatenator.Component {

        InsnList insns;

        Type type;

        public VariableComponent(InsnList insns, Type type) {
            this.insns = insns;
            this.type = type;
        }

        public Type push(InsnList result) {
            result.add(this.insns);
            return this.type;
        }

        public void pushAsString(InsnList result) {
            result.add(this.insns);
            String md = StringConcatenator.STRING_VALUE_OF_TYPES.get(this.type);
            if (md == null) md = "(Ljava/lang/Object;)Ljava/lang/String;";
            result.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", md));
        }
    }
}
