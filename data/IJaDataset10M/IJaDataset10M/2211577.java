package koala.dynamicjava.classfile;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class represents JVM bytecode constant pools
 *
 * @author Stephane Hillion
 * @version 1.0 - 1999/05/05
 */
public class ConstantPool {

    private static final byte CONSTANT_UTF8 = 1;

    private static final byte CONSTANT_INTEGER = 3;

    private static final byte CONSTANT_FLOAT = 4;

    private static final byte CONSTANT_LONG = 5;

    private static final byte CONSTANT_DOUBLE = 6;

    private static final byte CONSTANT_CLASS = 7;

    private static final byte CONSTANT_STRING = 8;

    private static final byte CONSTANT_FIELDREF = 9;

    private static final byte CONSTANT_METHODREF = 10;

    private static final byte CONSTANT_INTERFACEMETHODREF = 11;

    private static final byte CONSTANT_NAMEANDTYPE = 12;

    /**
     * The constants
     */
    private Map constants;

    /**
     * The constant count
     */
    private short count;

    /**
     * The constants sorted in a list
     */
    List constantList;

    /**
     * Creates a new constant pool
     */
    public ConstantPool() {
        constants = new HashMap();
        count = 1;
        constantList = new LinkedList();
    }

    /**
     * Returns the constant pool count according to the JVM Spec.
     */
    public short getCount() {
        return count;
    }

    /**
     * Adds a constant to the pool. If the constant is already present
     * in the pool, do nothing.
     * @param  cst the constant to add
     * @return the index of the constant in the pool
     */
    public short put(Integer cst) {
        Info info = (Info) constants.get(cst);
        if (info == null) {
            info = new IntegerInfo(cst);
        }
        return info.index;
    }

    /**
     * Adds a constant to the pool. If the constant is already present
     * in the pool, do nothing.
     * @param  cst the constant to add
     * @return the index of the constant in the pool
     */
    public short put(Long cst) {
        Info info = (Info) constants.get(cst);
        if (info == null) {
            info = new LongInfo(cst);
        }
        return info.index;
    }

    /**
     * Adds a constant to the pool. If the constant is already present
     * in the pool, do nothing.
     * @param  cst the constant to add
     * @return the index of the constant in the pool
     */
    public short put(Float cst) {
        Info info = (Info) constants.get(cst);
        if (info == null) {
            info = new FloatInfo(cst);
        }
        return info.index;
    }

    /**
     * Adds a constant to the pool. If the constant is already present
     * in the pool, do nothing.
     * @param  cst the constant to add
     * @return the index of the constant in the pool
     */
    public short put(Double cst) {
        Info info = (Info) constants.get(cst);
        if (info == null) {
            info = new DoubleInfo(cst);
        }
        return info.index;
    }

    /**
     * Adds a constant to the pool. If the constant is already present
     * in the pool, do nothing.
     * @param  cst the constant to add
     * @return the index of the constant in the pool
     */
    public short put(ConstantString cst) {
        Info info = (Info) constants.get(cst);
        if (info == null) {
            info = new StringInfo(cst);
        }
        return info.index;
    }

    /**
     * Adds a constant to the pool. If the constant is already present
     * in the pool, do nothing.
     * @param  cst the constant to add
     * @return the index of the constant in the pool
     */
    public short put(ClassIdentifier cst) {
        Info info = (Info) constants.get(cst);
        if (info == null) {
            info = new ClassInfo(cst);
        }
        return info.index;
    }

    /**
     * Adds a constant to the pool. If the constant is already present
     * in the pool, do nothing.
     * @param  cst the constant to add
     * @return the index of the constant in the pool
     */
    public short put(FieldIdentifier cst) {
        Info info = (Info) constants.get(cst);
        if (info == null) {
            info = new FieldInfo(cst);
        }
        return info.index;
    }

    /**
     * Adds a constant to the pool. If the constant is already present
     * in the pool, do nothing.
     * @param  cst the constant to add
     * @return the index of the constant in the pool
     */
    public short put(MethodIdentifier cst) {
        Info info = (Info) constants.get(cst);
        if (info == null) {
            info = new MethodInfo(cst);
        }
        return info.index;
    }

    /**
     * Adds a constant to the pool. If the constant is already present
     * in the pool, do nothing.
     * @param  cst the constant to add
     * @return the index of the constant in the pool
     */
    public short put(InterfaceMethodIdentifier cst) {
        Info info = (Info) constants.get(cst);
        if (info == null) {
            info = new InterfaceMethodInfo(cst);
        }
        return info.index;
    }

    /**
     * Writes the content of this pool to the given output stream
     */
    public void write(OutputStream out) throws IOException {
        write(new DataOutputStream(out));
    }

    /**
     * Writes the content of this pool to the given output stream
     */
    public void write(DataOutputStream out) throws IOException {
        out.writeShort(count);
        Iterator it = constantList.iterator();
        while (it.hasNext()) {
            ((Info) it.next()).write(out);
        }
    }

    /**
     * Adds a constant to the pool. If the constant is already present
     * in the pool, do nothing.
     * @param  name the name
     * @param  type the type
     * @param  type the type of the parameters
     * @return the index of the constant in the pool
     */
    short putNameAndType(String name, String type, String[] params) {
        NameAndTypeKey ntk = new NameAndTypeKey(name, type, params);
        Info info = (Info) constants.get(ntk);
        if (info == null) {
            info = new NameAndTypeInfo(ntk);
        }
        return info.index;
    }

    /**
     * Adds a constant to the pool. If the constant is already present
     * in the pool, do nothing.
     * @param  cst the constant to add
     * @return the index of the constant in the pool
     */
    short putUTF8(String cst) {
        Info info = (Info) constants.get(cst);
        if (info == null) {
            info = new UTF8Info(cst);
        }
        return info.index;
    }

    /**
     * Returns the entry at the given position in the pool
     */
    Info get(short i) {
        return (Info) constantList.get(i);
    }

    /**
     * This class is used to store info in the pool
     */
    abstract class Info {

        /**
	 * The index of the constant in the pool
	 */
        short index;

        /**
	 * Initializes the object
	 */
        Info(Object cst) {
            index = count;
            count += getIndexIncrement();
            constants.put(cst, this);
            constantList.add(this);
        }

        /**
	 * Returns the index increment for this type of info
	 */
        short getIndexIncrement() {
            return 1;
        }

        /**
	 * Writes the constant information to the specified stream
	 */
        abstract void write(DataOutputStream out) throws IOException;
    }

    /**
     * This class is used to store integer constants in the pool
     */
    class IntegerInfo extends Info {

        /**
	 * The value of the constant
	 */
        Integer value;

        /**
	 * Creates a new integer constant information object
	 */
        IntegerInfo(Integer v) {
            super(v);
            value = v;
        }

        /**
	 * Writes the constant information to the specified stream
	 */
        void write(DataOutputStream out) throws IOException {
            out.writeByte(CONSTANT_INTEGER);
            out.writeInt(value.intValue());
        }
    }

    /**
     * This class is used to store long constants in the pool
     */
    class LongInfo extends Info {

        /**
	 * The value of the constant
	 */
        Long value;

        /**
	 * Creates a new long constant information object
	 */
        LongInfo(Long v) {
            super(v);
            value = v;
        }

        /**
	 * Returns the index increment for this type of info
	 */
        short getIndexIncrement() {
            return 2;
        }

        /**
	 * Writes the constant information to the specified stream
	 */
        void write(DataOutputStream out) throws IOException {
            out.writeByte(CONSTANT_LONG);
            out.writeLong(value.longValue());
        }
    }

    /**
     * This class is used to store float constants in the pool
     */
    class FloatInfo extends Info {

        /**
	 * The value of the constant
	 */
        Float value;

        /**
	 * Creates a new float constant information object
	 */
        FloatInfo(Float v) {
            super(v);
            value = v;
        }

        /**
	 * Writes the constant information to the specified stream
	 */
        void write(DataOutputStream out) throws IOException {
            out.writeByte(CONSTANT_FLOAT);
            out.writeFloat(value.floatValue());
        }
    }

    /**
     * This class is used to store double constants in the pool
     */
    class DoubleInfo extends Info {

        /**
	 * The value of the constant
	 */
        Double value;

        /**
	 * Creates a new double constant information object
	 */
        DoubleInfo(Double v) {
            super(v);
            value = v;
        }

        /**
	 * Returns the index increment for this type of info
	 */
        short getIndexIncrement() {
            return 2;
        }

        /**
	 * Writes the constant information to the specified stream
	 */
        void write(DataOutputStream out) throws IOException {
            out.writeByte(CONSTANT_DOUBLE);
            out.writeDouble(value.doubleValue());
        }
    }

    /**
     * This class is used to store string constants in the pool
     */
    class StringInfo extends Info {

        /**
	 * The UTF8 index
	 */
        int UTF8Index;

        /**
	 * Creates a new string constant information object
	 */
        StringInfo(ConstantString v) {
            super(v);
            UTF8Index = putUTF8(v.getValue());
        }

        /**
	 * Writes the constant information to the specified stream
	 */
        void write(DataOutputStream out) throws IOException {
            out.writeByte(CONSTANT_STRING);
            out.writeShort(UTF8Index);
        }
    }

    /**
     * This class is used to store class constants in the pool
     */
    class ClassInfo extends Info {

        /**
	 * The UTF8 index
	 */
        int UTF8Index;

        /**
	 * Creates a new class constant information object
	 */
        ClassInfo(ClassIdentifier v) {
            super(v);
            UTF8Index = putUTF8(v.getValue());
        }

        /**
	 * Writes the constant information to the specified stream
	 */
        void write(DataOutputStream out) throws IOException {
            out.writeByte(CONSTANT_CLASS);
            out.writeShort(UTF8Index);
        }
    }

    /**
     * This class is used to store field constants in the pool
     */
    class FieldInfo extends Info {

        /**
	 * The declaring class index
	 */
        int classIndex;

        /**
	 * The name and type info index
	 */
        int nameAndTypeIndex;

        /**
	 * Creates a new field constant information object
	 */
        FieldInfo(FieldIdentifier v) {
            super(v);
            classIndex = put(new ClassIdentifier(v.getDeclaringClass()));
            nameAndTypeIndex = putNameAndType(v.getName(), v.getType(), null);
        }

        /**
	 * Writes the constant information to the specified stream
	 */
        void write(DataOutputStream out) throws IOException {
            out.writeByte(CONSTANT_FIELDREF);
            out.writeShort(classIndex);
            out.writeShort(nameAndTypeIndex);
        }
    }

    /**
     * This class is used to store method constants in the pool
     */
    class MethodInfo extends Info {

        /**
	 * The method declaring class
	 */
        int classIndex;

        /**
	 * The name and type info index
	 */
        int nameAndTypeIndex;

        /**
	 * Creates a new method constant information object
	 */
        MethodInfo(MethodIdentifier v) {
            super(v);
            classIndex = put(new ClassIdentifier(v.getDeclaringClass()));
            nameAndTypeIndex = putNameAndType(v.getName(), v.getType(), v.getParameters());
        }

        /**
	 * Writes the constant information to the specified stream
	 */
        void write(DataOutputStream out) throws IOException {
            out.writeByte(CONSTANT_METHODREF);
            out.writeShort(classIndex);
            out.writeShort(nameAndTypeIndex);
        }
    }

    /**
     * This class is used to store interface method constants in the pool
     */
    class InterfaceMethodInfo extends Info {

        /**
	 * The method declaring class
	 */
        int classIndex;

        /**
	 * The name and type info index
	 */
        int nameAndTypeIndex;

        /**
	 * Creates a new method constant information object
	 */
        InterfaceMethodInfo(InterfaceMethodIdentifier v) {
            super(v);
            classIndex = put(new ClassIdentifier(v.getDeclaringClass()));
            nameAndTypeIndex = putNameAndType(v.getName(), v.getType(), v.getParameters());
        }

        /**
	 * Writes the constant information to the specified stream
	 */
        void write(DataOutputStream out) throws IOException {
            out.writeByte(CONSTANT_INTERFACEMETHODREF);
            out.writeShort(classIndex);
            out.writeShort(nameAndTypeIndex);
        }
    }

    /**
     * This class is used to store name and type constants in the pool
     */
    class NameAndTypeInfo extends Info {

        /**
	 * The name
	 */
        int nameIndex;

        /**
	 * The type of the field
	 */
        int typeIndex;

        /**
	 * Creates a new class constant information object
	 */
        NameAndTypeInfo(NameAndTypeKey v) {
            super(v);
            nameIndex = putUTF8(v.name);
            typeIndex = putUTF8(v.type);
        }

        /**
	 * Writes the constant information to the specified stream
	 */
        void write(DataOutputStream out) throws IOException {
            out.writeByte(CONSTANT_NAMEANDTYPE);
            out.writeShort(nameIndex);
            out.writeShort(typeIndex);
        }
    }

    /**
     * This class is used to store UTF8 constants in the pool
     */
    class UTF8Info extends Info {

        /**
	 * The value of the constant
	 */
        String value;

        /**
	 * Creates a new string constant information object
	 */
        UTF8Info(String v) {
            super(v);
            value = v;
        }

        /**
	 * Writes the constant information to the specified stream
	 */
        void write(DataOutputStream out) throws IOException {
            out.writeByte(CONSTANT_UTF8);
            out.writeUTF(value);
        }
    }

    /**
     * This class is used as a key to store a name and type info in the map
     */
    static class NameAndTypeKey {

        String name;

        String type;

        NameAndTypeKey(String n, String t, String[] p) {
            name = n;
            type = JVMUtilities.createMethodDescriptor(t, p);
        }

        public boolean equals(Object other) {
            if (other == null || !(other instanceof NameAndTypeKey)) {
                return false;
            }
            NameAndTypeKey fk = (NameAndTypeKey) other;
            return name.equals(fk.name) && type.equals(fk.type);
        }

        public int hashCode() {
            return name.hashCode() + type.hashCode();
        }
    }
}
