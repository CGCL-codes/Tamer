package alt.jiapi.file;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import alt.jiapi.file.attribute.BootstrapMethodsAttribute;
import alt.jiapi.util.Configuration;

/**
 * ConstantPool.
 * 
 * @author Mika Riekkinen
 */
public class ConstantPool {

    @SuppressWarnings("unused")
    private static Configuration config = new Configuration();

    /**
	 * Constant, that represents a Utf8 tag in constant pool
	 */
    public static final byte CONSTANT_Utf8 = 1;

    /**
	 * Constant, that represents a integer tag in constant pool
	 */
    public static final byte CONSTANT_Integer = 3;

    /**
	 * Constant, that represents a float tag in constant pool
	 */
    public static final byte CONSTANT_Float = 4;

    /**
	 * Constant, that represents a long tag in constant pool
	 */
    public static final byte CONSTANT_Long = 5;

    /**
	 * Constant, that represents a double tag in constant pool
	 */
    public static final byte CONSTANT_Double = 6;

    /**
	 * Constant, that represents a class_ref tag in constant pool
	 */
    public static final byte CONSTANT_Class = 7;

    /**
	 * Constant, that represents a string tag in constant pool
	 */
    public static final byte CONSTANT_String = 8;

    /**
	 * Constant, that represents a field_ref tag in constant pool
	 */
    public static final byte CONSTANT_Fieldref = 9;

    /**
	 * Constant, that represents a method_ref tag in constant pool
	 */
    public static final byte CONSTANT_Methodref = 10;

    /**
	 * Constant, that represents a interfacemethod_ref tag in constant pool
	 */
    public static final byte CONSTANT_InterfaceMethodref = 11;

    /**
	 * Constant, that represents a nameAndType tag in constant pool
	 */
    public static final byte CONSTANT_NameAndType = 12;

    /**
	 * Constant, that represents a methodHandle tag in constant pool
	 */
    public static final byte CONSTANT_MethodHandle = 15;

    /**
	 * Constant, that represents a methodType tag in constant pool
	 */
    public static final byte CONSTANT_MethodType = 16;

    /**
	 * Constant, that represents a invokeDynamic tag in constant pool
	 */
    public static final byte CONSTANT_InvokeDynamic = 18;

    private ArrayList<Entry> cp;

    ConstantPool(int size) {
        cp = new ArrayList<Entry>(size);
    }

    /**
	 * Creates an empty constant pool.
	 */
    public ConstantPool() {
        cp = new ArrayList<Entry>();
    }

    /**
	 * Adds an entry to this constant pool. If entry has sub-entries, those are
	 * added also.
	 */
    public short add(ConstantPool.Entry entry) {
        if (entry instanceof ClassInfo) {
            return addClassInfo(((ClassInfo) entry).getName()).getEntryIndex();
        } else if (entry instanceof FieldRefInfo) {
            FieldRefInfo fri = (FieldRefInfo) entry;
            short ciIdx = add(fri.getClassInfo());
            return addFieldRefInfo((ClassInfo) get(ciIdx), fri.getNameAndTypeInfo().getName(), fri.getNameAndTypeInfo().getDescriptor()).getEntryIndex();
        } else if (entry instanceof MethodRefInfo) {
            MethodRefInfo mri = (MethodRefInfo) entry;
            short ciIdx = add(mri.getClassInfo());
            return addMethodRefInfo((ClassInfo) get(ciIdx), mri.getNameAndTypeInfo().getName(), mri.getNameAndTypeInfo().getDescriptor()).getEntryIndex();
        } else if (entry instanceof InterfaceMethodRefInfo) {
            InterfaceMethodRefInfo imri = (InterfaceMethodRefInfo) entry;
            short ciIdx = add(imri.getClassInfo());
            return addInterfaceMethodRefInfo((ClassInfo) get(ciIdx), imri.getNameAndTypeInfo().getName(), imri.getNameAndTypeInfo().getDescriptor()).getEntryIndex();
        } else if (entry instanceof StringInfo) {
            StringInfo si = (StringInfo) entry;
            return addStringInfo(si.stringValue()).getEntryIndex();
        } else if (entry instanceof IntegerInfo || entry instanceof FloatInfo) {
            cp.add(entry);
            return (short) cp.size();
        } else if (entry instanceof LongInfo || entry instanceof DoubleInfo) {
            cp.add(entry);
            cp.add(new NullEntry());
            return (short) (cp.size() - 1);
        } else if (entry instanceof NameAndTypeInfo) {
            NameAndTypeInfo nti = (NameAndTypeInfo) entry;
            return addNameAndTypeInfo(nti.getName(), nti.getDescriptor()).getEntryIndex();
        } else if (entry instanceof Utf8Info) {
            Utf8Info utf8 = (Utf8Info) entry;
            return addUtf8Info(utf8.stringValue()).getEntryIndex();
        } else {
            System.out.println("ERROR: " + entry + " not converted");
        }
        return 0;
    }

    short addClassInfo(short nameIndex) {
        if (nameIndex <= 0) {
            throw new ParseException("Adding class_info, but name_index does not points to 0", this);
        }
        if (nameIndex <= cp.size()) {
            Entry e = get(nameIndex);
            if (!(e instanceof Utf8Info)) {
                throw new ParseException("Adding class_info, but name_index does not point to Utf8Info; index=" + nameIndex + ", " + e, this);
            }
        }
        ClassInfo ci = new ClassInfo(nameIndex);
        cp.add(ci);
        return (short) (cp.size());
    }

    /**
	 * Adds a new ClassInfo.
	 * 
	 * @param className
	 *            a fully qualified class name
	 * @return ClassInfo
	 */
    public ClassInfo addClassInfo(String className) {
        String internalName = className.replace('.', '/');
        short idx = findClassInfo(0, internalName);
        if (idx != -1) {
            return (ClassInfo) cp.get(idx);
        }
        Utf8Info u8 = addUtf8Info(internalName);
        ClassInfo ci = new ClassInfo(u8.getEntryIndex());
        cp.add(ci);
        return ci;
    }

    /**
	 * Adds a new FieldRefInfo.
	 * 
	 * @param ci
	 *            ClassInfo, that is supposed to contain created field reference
	 * @param name
	 *            Name of the field
	 * @param desc
	 *            descriptor of the field
	 * @return FieldRefInfo
	 */
    public FieldRefInfo addFieldRefInfo(ClassInfo ci, String name, String desc) {
        FieldRefInfo __fi = findFieldRefInfo(0, ci, name, desc);
        if (__fi != null) {
            return __fi;
        }
        short classIndex = ci.getEntryIndex();
        short nameAndTypeIndex = addNameAndTypeInfo(name, desc).getEntryIndex();
        FieldRefInfo fi = new FieldRefInfo(classIndex, nameAndTypeIndex);
        cp.add(fi);
        return fi;
    }

    short addFieldRefInfo(short classIndex, short nameAndTypeIndex) {
        if (classIndex <= 0 || nameAndTypeIndex <= 0) {
            throw new ParseException("Adding fieldRef_info, but class_index or nameAndType_index is 0", this);
        }
        if (classIndex <= cp.size()) {
            Entry e = get(classIndex);
            if (!(e instanceof ClassInfo)) {
                throw new ParseException("Adding fieldRef_info, but class_index does not point to classInfo; index=" + classIndex + ", " + e, this);
            }
        }
        FieldRefInfo fi = new FieldRefInfo(classIndex, nameAndTypeIndex);
        cp.add(fi);
        return (short) (cp.size());
    }

    /**
	 * Adds a new MethodRefInfo.
	 * 
	 * @param ci
	 *            ClassInfo, that is supposed to contain created method
	 *            reference
	 * @param name
	 *            Name of the method
	 * @param desc
	 *            descriptor of the method
	 * @return MethodRefInfo
	 */
    public MethodRefInfo addMethodRefInfo(ClassInfo ci, String name, String desc) {
        MethodRefInfo __mi = findMethodRefInfo(0, ci, name, desc);
        if (__mi != null) {
            return __mi;
        }
        short classIndex = ci.getEntryIndex();
        short nameAndTypeIndex = addNameAndTypeInfo(name, desc).getEntryIndex();
        MethodRefInfo mi = new MethodRefInfo(classIndex, nameAndTypeIndex);
        cp.add(mi);
        return mi;
    }

    short addMethodRefInfo(short classIndex, short nameAndTypeIndex) {
        if (classIndex <= 0 || nameAndTypeIndex <= 0) {
            throw new ParseException("Adding methodRef_info, but class_index or nameAndType_index is 0", this);
        }
        if (classIndex <= cp.size()) {
            Entry e = get(classIndex);
            if (!(e instanceof ClassInfo)) {
                throw new ParseException("Adding methodRef_info, but class_index does not point to classInfo; index=" + classIndex + ", " + e, this);
            }
        }
        MethodRefInfo mi = new MethodRefInfo(classIndex, nameAndTypeIndex);
        cp.add(mi);
        return (short) (cp.size());
    }

    /**
	 * Adds a new InterfaceMethodRefInfo.
	 * 
	 * @param ci
	 *            ClassInfo, that is supposed to contain created interface
	 *            method reference
	 * @param name
	 *            Name of the method
	 * @param desc
	 *            descriptor of the method
	 * @return InterfaceMethodRefInfo
	 */
    public InterfaceMethodRefInfo addInterfaceMethodRefInfo(ClassInfo ci, String name, String desc) {
        short classIndex = ci.getEntryIndex();
        short nameAndTypeIndex = addNameAndTypeInfo(name, desc).getEntryIndex();
        InterfaceMethodRefInfo imi = new InterfaceMethodRefInfo(classIndex, nameAndTypeIndex);
        cp.add(imi);
        return imi;
    }

    short addInterfaceMethodRefInfo(short classIndex, short nameAndTypeIndex) {
        if (classIndex <= 0 || nameAndTypeIndex <= 0) {
            throw new ParseException("Adding interfaceMethodRef_info, but class_index or nameAndType_index is 0", this);
        }
        if (classIndex <= cp.size()) {
            Entry e = get(classIndex);
            if (!(e instanceof ClassInfo)) {
                throw new ParseException("Adding interfaceMethodRef_info, but class_index does not point to classInfo; index=" + classIndex + ", " + e, this);
            }
        }
        InterfaceMethodRefInfo imi = new InterfaceMethodRefInfo(classIndex, nameAndTypeIndex);
        cp.add(imi);
        return (short) (cp.size());
    }

    /**
	 * Adds a new StringInfo.
	 * 
	 * @param s
	 *            String constant to add
	 * @return StringInfo
	 */
    public StringInfo addStringInfo(String s) {
        StringInfo __si = findStringInfo(0, s);
        if (__si != null) {
            return __si;
        }
        Utf8Info u8 = addUtf8Info(s);
        StringInfo si = new StringInfo(u8.getEntryIndex());
        cp.add(si);
        return si;
    }

    short addString_info(short stringIndex) {
        if (stringIndex <= 0) {
            throw new ParseException("Adding string_info, but string_index is 0", this);
        }
        if (stringIndex <= cp.size()) {
            Entry e = get(stringIndex);
            if (!(e instanceof Utf8Info)) {
                throw new ParseException("Adding string_info, but string_index does not point to Utf8Info; index=" + stringIndex + ", " + e, this);
            }
        }
        StringInfo si = new StringInfo(stringIndex);
        cp.add(si);
        return (short) (cp.size());
    }

    /**
	 * Adds a new IntegerInfo.
	 * 
	 * @param bytes
	 *            integer constant
	 * @return IntegerInfo
	 */
    public IntegerInfo addIntegerInfo(int bytes) {
        IntegerInfo ii = new IntegerInfo(bytes);
        cp.add(ii);
        return ii;
    }

    short addInteger_info(int bytes) {
        IntegerInfo ii = new IntegerInfo(bytes);
        cp.add(ii);
        return (short) (cp.size());
    }

    /**
	 * Adds a new FloatInfo.
	 * 
	 * @param bytes
	 *            float constant
	 * @return FloatInfo
	 */
    public FloatInfo addFloatInfo(int bytes) {
        FloatInfo fi = new FloatInfo(bytes);
        cp.add(fi);
        return fi;
    }

    short addFloat_info(int bytes) {
        FloatInfo fi = new FloatInfo(bytes);
        cp.add(fi);
        return (short) (cp.size());
    }

    /**
	 * Adds a new LongInfo.
	 * 
	 * @param highBytes
	 *            long constant
	 * @param lowBytes
	 *            long constant
	 * @return LongInfo
	 */
    public LongInfo addLongInfo(int highBytes, int lowBytes) {
        LongInfo li = new LongInfo(highBytes, lowBytes);
        cp.add(li);
        return li;
    }

    short addLong_info(int highBytes, int lowBytes) {
        LongInfo fi = new LongInfo(highBytes, lowBytes);
        cp.add(fi);
        cp.add(new NullEntry());
        return (short) (cp.size() - 1);
    }

    /**
	 * Adds a new DoubleInfo.
	 * 
	 * @param highBytes
	 *            double constant
	 * @param lowBytes
	 *            double constant
	 * @return DoubleInfo
	 */
    public DoubleInfo addDoubleInfo(int highBytes, int lowBytes) {
        DoubleInfo di = new DoubleInfo(highBytes, lowBytes);
        cp.add(di);
        return di;
    }

    short addDouble_info(int highBytes, int lowBytes) {
        DoubleInfo fi = new DoubleInfo(highBytes, lowBytes);
        cp.add(fi);
        cp.add(new NullEntry());
        return (short) (cp.size() - 1);
    }

    /**
	 * Adds a new NameAndTypeInfo.
	 * 
	 * @param name
	 *            Name to add
	 * @param descriptor
	 *            descriptor to add
	 * @return NameAndTypeInfo
	 */
    public NameAndTypeInfo addNameAndTypeInfo(String name, String descriptor) {
        NameAndTypeInfo ni = findNameAndTypeInfo(0, name, descriptor);
        if (ni != null) {
            return ni;
        }
        short nameIndex = addUtf8Info(name).getEntryIndex();
        short descriptorIndex = addUtf8Info(descriptor).getEntryIndex();
        NameAndTypeInfo fi = new NameAndTypeInfo(nameIndex, descriptorIndex);
        cp.add(fi);
        return fi;
    }

    short addNameAndTypeInfo(short nameIndex, short descriptorIndex) {
        if (nameIndex <= 0 || descriptorIndex <= 0) {
            throw new ParseException("Adding nameAndType_info, but name_index or descriptor_index is 0", this);
        }
        if (nameIndex <= cp.size()) {
            Entry e = get(nameIndex);
            if (!(e instanceof Utf8Info)) {
                throw new ParseException("Adding nameAndType_info, but name_index does not point to Utf8Info; index=" + nameIndex + ", " + e, this);
            }
        }
        if (descriptorIndex <= cp.size()) {
            Entry e = get(descriptorIndex);
            if (!(e instanceof Utf8Info)) {
                throw new ParseException("Adding nameAndType_info, but descriptor_index does not point to Utf8Info; name_index=" + nameIndex + ", descriptor_index=" + descriptorIndex + ", " + e, this);
            }
        }
        NameAndTypeInfo fi = new NameAndTypeInfo(nameIndex, descriptorIndex);
        cp.add(fi);
        return (short) (cp.size());
    }

    /**
	 * Adds a new Utf8Info.
	 * 
	 * @param s
	 *            String to add
	 * @return Utf8Info
	 */
    public Utf8Info addUtf8Info(String s) {
        Utf8Info u8 = findUtf8Info(0, s);
        if (u8 != null) {
            return u8;
        }
        return (Utf8Info) cp.get(addUtf8_info(s.getBytes()) - 1);
    }

    short addUtf8_info(byte[] bytes) {
        Utf8Info ui = new Utf8Info(bytes);
        cp.add(ui);
        return (short) (cp.size());
    }

    /**
	 * Gets an entry from constant pool. Indexing starts from 1.
	 * 
	 * @param index
	 *            index into constant pool
	 * @return An instance of Entry
	 */
    public Entry get(int index) {
        return (Entry) cp.get(index - 1);
    }

    /**
	 * Gets a String at given constant pool index. index must point to a
	 * Utf8Info.
	 * 
	 * @param index
	 *            index into constant pool
	 * @return An instance of Entry
	 * @exception IllegalArgumentException
	 *                is throw, if entry at given index does not point into
	 *                Utf8Info
	 */
    public String getUtf8(short index) {
        ConstantPool.Entry utf8 = get(index);
        if (utf8 instanceof ConstantPool.Utf8Info) {
            byte[] bytes = ((ConstantPool.Utf8Info) utf8).getBytes();
            return new String(bytes);
        } else {
            throw new IllegalArgumentException("index does not point to utf8_info structure: index " + index + " -> " + utf8);
        }
    }

    /**
	 * Gets a String at given constant pool index. index must point to a
	 * StringInfo or Utf8Info.
	 * 
	 * @param index
	 *            index into constant pool
	 * @return a String
	 * @exception IllegalArgumentException
	 *                is throw, if entry at given index does not point into
	 *                StringInfo or utf8Info
	 */
    public String getStringValue(short index) {
        ConstantPool.Entry str = get(index);
        if (str instanceof ConstantPool.StringInfo) {
            String s = ((ConstantPool.StringInfo) str).stringValue();
            return s;
        } else if (str instanceof ConstantPool.Utf8Info) {
            byte[] bytes = ((ConstantPool.Utf8Info) str).getBytes();
            return new String(bytes);
        } else {
            throw new IllegalArgumentException("index does not point to string_info structure: index " + index + " -> " + str);
        }
    }

    /**
	 * Index must point to a Class_info structure in Constant pool
	 * 
	 * @param index
	 *            index to ClassInfo in constant pool.
	 * @return an internal representation of classname, like
	 *         <b>java/lang/Object</b>
	 * @exception IllegalArgumentException
	 *                is throw, if entry at given index does not point into
	 *                ClassInfo, or ClassInfo.getNameIndex() does not point to
	 *                Utf8Info
	 */
    public String getClassName(short index) {
        ConstantPool.Entry ci = get(index);
        if (ci instanceof ConstantPool.ClassInfo) {
            short nameIndex = ((ConstantPool.ClassInfo) ci).getNameIndex();
            return getUtf8(nameIndex).replace('/', '.');
        } else {
            throw new IllegalArgumentException("index does not point to Class_info structure: index " + index + " -> " + ci);
        }
    }

    /**
	 * Base class for entries in ConstantPool.
	 */
    public abstract class Entry {

        private byte tag;

        private short entryIndex;

        private Entry() {
        }

        /**
		 * Constructor.
		 * 
		 * @param tag
		 *            One of CONSTANT_XXX defined in ConstantPool
		 */
        public Entry(byte tag) {
            this.entryIndex = (short) (cp.size() + 1);
            this.tag = tag;
        }

        /**
		 * Gets the tag related to this entry.
		 * 
		 * @return tag
		 */
        public byte getTag() {
            return tag;
        }

        /**
		 * Get the index in the constant pool.
		 * 
		 * @return constant pool index of this entry
		 */
        public short getEntryIndex() {
            return entryIndex;
        }

        /**
		 * Writes this Entry to DataOutputStream given.
		 * 
		 * @param dos
		 *            DataOutputStream used
		 */
        public abstract void writeData(DataOutputStream dos) throws IOException;
    }

    /**
     */
    public class NullEntry extends Entry {

        public NullEntry() {
        }

        public void writeData(DataOutputStream dos) {
        }
    }

    /**
	 * Represents a CONSTANT_Class_info in constant pool
	 * 
	 * @see Java Virtual Machine Specification, 2nd edition, ch. 4.4.1
	 */
    public class ClassInfo extends Entry {

        private short nameIndex;

        ClassInfo(short nameIndex) {
            super(CONSTANT_Class);
            this.nameIndex = nameIndex;
        }

        /**
		 * Gets an index in constant pool, that points to Utf8_info,
		 * representing name of this ClassInfo
		 */
        short getNameIndex() {
            return nameIndex;
        }

        /**
		 * Gets a name, that is represented by this ClassInfo
		 */
        public String getName() {
            return getUtf8(nameIndex);
        }

        public String toString() {
            return "Class_info:\n  " + get(nameIndex);
        }

        public void writeData(DataOutputStream dos) throws IOException {
            dos.writeShort(nameIndex);
        }
    }

    /**
	 * Represents a CONSTANT_FieldRef in constant pool
	 * 
	 * @see Java Virtual Machine Specification, 2nd edition, ch. 4.4.2
	 */
    public class FieldRefInfo extends Entry {

        private short classIndex;

        private short nameAndTypeIndex;

        FieldRefInfo(short classIndex, short nameAndTypeIndex) {
            super(CONSTANT_Fieldref);
            this.classIndex = classIndex;
            this.nameAndTypeIndex = nameAndTypeIndex;
        }

        short getClassIndex() {
            return classIndex;
        }

        public ClassInfo getClassInfo() {
            return (ClassInfo) get(classIndex);
        }

        short getNameAndTypeIndex() {
            return nameAndTypeIndex;
        }

        public NameAndTypeInfo getNameAndTypeInfo() {
            return (NameAndTypeInfo) get(nameAndTypeIndex);
        }

        /**
		 * Gets the name of the field referenced by this FieldRefInfo
		 */
        public String getFieldName() {
            NameAndTypeInfo nti = (NameAndTypeInfo) get(nameAndTypeIndex);
            return getUtf8(nti.getNameIndex());
        }

        /**
		 * Get Fields descriptor
		 */
        public String getDescriptor() {
            NameAndTypeInfo nti = (NameAndTypeInfo) get(nameAndTypeIndex);
            return getUtf8(nti.getDescriptorIndex());
        }

        public String toString() {
            return "fieldref_info:\n  " + get(classIndex) + "\n  " + get(nameAndTypeIndex);
        }

        public void writeData(DataOutputStream dos) throws IOException {
            dos.writeShort(classIndex);
            dos.writeShort(nameAndTypeIndex);
        }
    }

    /**
	 * Represents a CONSTANT_MethodRef in constant pool
	 * 
	 * @see Java Virtual Machine Specification, 2nd edition, ch. 4.4.2
	 */
    public class MethodRefInfo extends Entry {

        private short classIndex;

        private short nameAndTypeIndex;

        MethodRefInfo(short classIndex, short nameAndTypeIndex) {
            super(CONSTANT_Methodref);
            this.classIndex = classIndex;
            this.nameAndTypeIndex = nameAndTypeIndex;
        }

        short getClassIndex() {
            return classIndex;
        }

        public ClassInfo getClassInfo() {
            return (ClassInfo) get(classIndex);
        }

        short getNameAndTypeIndex() {
            return nameAndTypeIndex;
        }

        public NameAndTypeInfo getNameAndTypeInfo() {
            return (NameAndTypeInfo) get(nameAndTypeIndex);
        }

        /**
		 * Gets the name of the method referenced by this MethodRefInfo
		 */
        public String getMethodName() {
            NameAndTypeInfo nti = (NameAndTypeInfo) get(nameAndTypeIndex);
            return getUtf8(nti.getNameIndex());
        }

        /**
		 * Get methods descriptor
		 */
        public String getDescriptor() {
            NameAndTypeInfo nti = (NameAndTypeInfo) get(nameAndTypeIndex);
            return getUtf8(nti.getDescriptorIndex());
        }

        public String toString() {
            return "methodref_info:\n  " + get(classIndex) + "(" + classIndex + ")\n  " + get(nameAndTypeIndex);
        }

        public void writeData(DataOutputStream dos) throws IOException {
            dos.writeShort(classIndex);
            dos.writeShort(nameAndTypeIndex);
        }
    }

    /**
	 * Represents a CONSTANT_InterfaceMethodRef in constant pool
	 * 
	 * @see Java Virtual Machine Specification, 2nd edition, ch. 4.4.2
	 */
    public class InterfaceMethodRefInfo extends Entry {

        private short classIndex;

        private short nameAndTypeIndex;

        InterfaceMethodRefInfo(short classIndex, short nameAndTypeIndex) {
            super(CONSTANT_InterfaceMethodref);
            this.classIndex = classIndex;
            this.nameAndTypeIndex = nameAndTypeIndex;
        }

        short getClassIndex() {
            return classIndex;
        }

        public ClassInfo getClassInfo() {
            return (ClassInfo) get(classIndex);
        }

        short getNameAndTypeIndex() {
            return nameAndTypeIndex;
        }

        public NameAndTypeInfo getNameAndTypeInfo() {
            return (NameAndTypeInfo) get(nameAndTypeIndex);
        }

        /**
		 * Gets the name of the method referenced by this MethodRefInfo
		 */
        public String getMethodName() {
            NameAndTypeInfo nti = (NameAndTypeInfo) get(nameAndTypeIndex);
            return getUtf8(nti.getNameIndex());
        }

        /**
		 * Get methods descriptor
		 */
        public String getDescriptor() {
            NameAndTypeInfo nti = (NameAndTypeInfo) get(nameAndTypeIndex);
            return getUtf8(nti.getDescriptorIndex());
        }

        public String toString() {
            return "interfacemethodref_info:\n  " + get(classIndex) + "\n  " + get(nameAndTypeIndex);
        }

        public void writeData(DataOutputStream dos) throws IOException {
            dos.writeShort(classIndex);
            dos.writeShort(nameAndTypeIndex);
        }
    }

    public class StringInfo extends Entry {

        private short stringIndex;

        StringInfo(short stringIndex) {
            super(CONSTANT_String);
            this.stringIndex = stringIndex;
        }

        short getStringIndex() {
            return stringIndex;
        }

        public String stringValue() {
            Utf8Info u8 = (Utf8Info) get(stringIndex);
            return u8.stringValue();
        }

        public String toString() {
            return "string_info:\n  " + get(stringIndex);
        }

        public void writeData(DataOutputStream dos) throws IOException {
            dos.writeShort(stringIndex);
        }
    }

    public class IntegerInfo extends Entry {

        private int bytes;

        IntegerInfo(int bytes) {
            super(CONSTANT_Integer);
            this.bytes = bytes;
        }

        public int getBytes() {
            return bytes;
        }

        public String toString() {
            return "integer_info: " + bytes;
        }

        public void writeData(DataOutputStream dos) throws IOException {
            dos.writeInt(bytes);
        }
    }

    public class FloatInfo extends Entry {

        private int bytes;

        FloatInfo(int bytes) {
            super(CONSTANT_Float);
            this.bytes = bytes;
        }

        public int getBytes() {
            return bytes;
        }

        public String toString() {
            return "float_info: " + bytes;
        }

        public void writeData(DataOutputStream dos) throws IOException {
            dos.writeInt(bytes);
        }
    }

    public class LongInfo extends Entry {

        private int highBytes;

        private int lowBytes;

        LongInfo(int highBytes, int lowBytes) {
            super(CONSTANT_Long);
            this.highBytes = highBytes;
            this.lowBytes = lowBytes;
        }

        public int getHighBytes() {
            return highBytes;
        }

        public int getLowBytes() {
            return lowBytes;
        }

        public String toString() {
            return "long_info: " + highBytes + " " + lowBytes;
        }

        public void writeData(DataOutputStream dos) throws IOException {
            dos.writeInt(highBytes);
            dos.writeInt(lowBytes);
        }
    }

    public class DoubleInfo extends Entry {

        private int highBytes;

        private int lowBytes;

        DoubleInfo(int highBytes, int lowBytes) {
            super(CONSTANT_Double);
            this.highBytes = highBytes;
            this.lowBytes = lowBytes;
        }

        public int getHighBytes() {
            return highBytes;
        }

        public int getLowBytes() {
            return lowBytes;
        }

        public String toString() {
            return "double_info: " + highBytes + " " + lowBytes;
        }

        public void writeData(DataOutputStream dos) throws IOException {
            dos.writeInt(highBytes);
            dos.writeInt(lowBytes);
        }
    }

    /**
	 * Represents a CONSTANT_NameAndType in constant pool
	 * 
	 * @see Java Virtual Machine Specification, 2nd edition, ch. 4.4.6
	 */
    public class NameAndTypeInfo extends Entry {

        private short nameIndex;

        private short descriptorIndex;

        NameAndTypeInfo(short nameIndex, short descriptorIndex) {
            super(CONSTANT_NameAndType);
            this.nameIndex = nameIndex;
            this.descriptorIndex = descriptorIndex;
        }

        short getNameIndex() {
            return nameIndex;
        }

        public String getName() {
            return getUtf8(nameIndex);
        }

        short getDescriptorIndex() {
            return descriptorIndex;
        }

        public String getDescriptor() {
            return getUtf8(descriptorIndex);
        }

        public String toString() {
            return "nameandtype_info:\n  " + get(nameIndex) + "\n  " + get(descriptorIndex);
        }

        public void writeData(DataOutputStream dos) throws IOException {
            dos.writeShort(nameIndex);
            dos.writeShort(descriptorIndex);
        }
    }

    public class Utf8Info extends Entry {

        private byte[] bytes;

        Utf8Info(byte[] bytes) {
            super(CONSTANT_Utf8);
            this.bytes = bytes;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public String stringValue() {
            return new String(bytes);
        }

        public String toString() {
            return "utf8_info: " + new String(bytes);
        }

        public void writeData(DataOutputStream dos) throws IOException {
            dos.writeShort(bytes.length);
            for (int i = 0; i < bytes.length; i++) {
                dos.writeByte(bytes[i]);
            }
        }
    }

    public class MethodHandleInfo extends Entry {

        public static final byte REF_getField = 1;

        public static final byte REF_getStatic = 2;

        public static final byte REF_putField = 3;

        public static final byte REF_putStatic = 4;

        public static final byte REF_invokeVirtual = 5;

        public static final byte REF_invokeStatic = 6;

        public static final byte REF_invokeSpecial = 7;

        public static final byte REF_newInvokeSpecial = 8;

        public static final byte REF_invokeInterface = 9;

        private final byte reference_kind;

        private final short reference_index;

        public MethodHandleInfo(byte reference_kind, short reference_index) {
            this.reference_kind = reference_kind;
            this.reference_index = reference_index;
        }

        public byte getKind() {
            return reference_kind;
        }

        public ConstantPool.Entry getReference() {
            return cp.get(reference_index);
        }

        @Override
        public void writeData(DataOutputStream dos) throws IOException {
            dos.writeByte(reference_kind);
            dos.writeShort(reference_index);
        }
    }

    public class MethodTypeInfo extends Entry {

        private final short descriptor_index;

        public MethodTypeInfo(short descriptor_index) {
            this.descriptor_index = descriptor_index;
        }

        public Utf8Info getDescriptor() {
            return (Utf8Info) cp.get(descriptor_index);
        }

        @Override
        public void writeData(DataOutputStream dos) throws IOException {
            dos.writeShort(descriptor_index);
        }
    }

    public class InvokeDynamicInfo extends Entry {

        private final short bootstrap_method_attr_index;

        private final short name_and_type_index;

        public InvokeDynamicInfo(short bootstrap_method_attr_index, short name_and_type_index) {
            super(CONSTANT_InvokeDynamic);
            this.bootstrap_method_attr_index = bootstrap_method_attr_index;
            this.name_and_type_index = name_and_type_index;
        }

        public NameAndTypeInfo getNameAndTypeInfo() {
            return (NameAndTypeInfo) cp.get(name_and_type_index);
        }

        /**
		 * 
		 * @return an index to bootstrap_method in BootstrapMethodsAttribute
		 */
        public short getBootstrapMethodsIndex() {
            return bootstrap_method_attr_index;
        }

        @Override
        public void writeData(DataOutputStream dos) throws IOException {
            dos.writeShort(bootstrap_method_attr_index);
            dos.writeShort(name_and_type_index);
        }
    }

    /**
	 * Get the size of this ConstantPool.
	 * 
	 * @return number of entries in constant-pool
	 */
    public int size() {
        return cp.size();
    }

    List<Entry> getList() {
        cp.trimToSize();
        return cp;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        int idx = 1;
        Iterator<Entry> i = cp.iterator();
        while (i.hasNext()) {
            sb.append(idx);
            sb.append(": ");
            sb.append(i.next());
            if (i.hasNext()) {
                sb.append('\n');
            }
            idx++;
        }
        return sb.toString();
    }

    @SuppressWarnings("unused")
    public byte[] toBytes() {
        if (true) {
            throw new RuntimeException("NOT IMPLEMENTED");
        }
        return null;
    }

    short findClassInfo(int start, String s) {
        for (int i = start; i < cp.size(); i++) {
            Entry e = (Entry) cp.get(i);
            if (e instanceof ClassInfo) {
                ClassInfo ci = (ClassInfo) e;
                if (ci.getName().equals(s)) {
                    return (short) (i);
                }
            }
        }
        return -1;
    }

    MethodRefInfo findMethodRefInfo(int start, ClassInfo ci, String name, String desc) {
        for (int i = start; i < cp.size(); i++) {
            Entry e = (Entry) cp.get(i);
            if (e instanceof MethodRefInfo) {
                MethodRefInfo mi = (MethodRefInfo) e;
                if (mi.getMethodName().equals(name) && mi.getDescriptor().equals(desc)) {
                    ClassInfo __ci = mi.getClassInfo();
                    if (__ci.getEntryIndex() == ci.getEntryIndex()) {
                        return mi;
                    }
                }
            }
        }
        return null;
    }

    FieldRefInfo findFieldRefInfo(int start, ClassInfo ci, String name, String desc) {
        for (int i = start; i < cp.size(); i++) {
            Entry e = (Entry) cp.get(i);
            if (e instanceof FieldRefInfo) {
                FieldRefInfo fi = (FieldRefInfo) e;
                if (fi.getFieldName().equals(name) && fi.getDescriptor().equals(desc)) {
                    ClassInfo __ci = fi.getClassInfo();
                    if (__ci.getEntryIndex() == ci.getEntryIndex()) {
                        return fi;
                    }
                }
            }
        }
        return null;
    }

    NameAndTypeInfo findNameAndTypeInfo(int start, String name, String desc) {
        for (int i = start; i < cp.size(); i++) {
            Entry e = (Entry) cp.get(i);
            if (e instanceof NameAndTypeInfo) {
                NameAndTypeInfo ni = (NameAndTypeInfo) e;
                if (ni.getName().equals(name) && ni.getDescriptor().equals(desc)) {
                    return ni;
                }
            }
        }
        return null;
    }

    StringInfo findStringInfo(int start, String s) {
        for (int i = start; i < cp.size(); i++) {
            Entry e = (Entry) cp.get(i);
            if (e instanceof StringInfo) {
                StringInfo si = (StringInfo) e;
                if (si.stringValue().equals(s)) {
                    return si;
                }
            }
        }
        return null;
    }

    Utf8Info findUtf8Info(int start, String s) {
        for (int i = start; i < cp.size(); i++) {
            Entry e = (Entry) cp.get(i);
            if (e instanceof Utf8Info) {
                Utf8Info u8 = (Utf8Info) e;
                if (u8.stringValue().equals(s)) {
                    return u8;
                }
            }
        }
        return null;
    }

    /**
	 * Verifies this ConstantPool
	 */
    void verify() {
        Iterator<Entry> i = cp.iterator();
        while (i.hasNext()) {
            Entry e = i.next();
            if (e instanceof ClassInfo) {
                ClassInfo ci = (ClassInfo) e;
                if (ci.getNameIndex() <= cp.size()) {
                    Entry e2 = get(ci.getNameIndex());
                    if (!(e2 instanceof Utf8Info)) {
                        throw new ParseException("Invalid class_info; name_index does not point to Utf8Info; index=" + ci.getNameIndex() + ", " + e2, this);
                    }
                }
            }
        }
    }
}
