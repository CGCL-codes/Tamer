package proguard.classfile;

import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.classfile.attribute.*;
import java.io.*;

/**
 * This is a complete representation of the data in a Java class file.
 * A ClassFile instance can be generated using the static create(DataInput)
 * method, manipulated using various operators, and persisted back using the
 * write(DataOutput) method.
 *
 * @author Mark Welsh
 * @author Eric Lafortune
 */
public class ProgramClassFile implements ClassFile {

    public int u4magic;

    public int u2minorVersion;

    public int u2majorVersion;

    public int u2constantPoolCount;

    public CpInfo[] constantPool;

    public int u2accessFlags;

    public int u2thisClass;

    public int u2superClass;

    public int u2interfacesCount;

    public int[] u2interfaces;

    public int u2fieldsCount;

    public ProgramFieldInfo[] fields;

    public int u2methodsCount;

    public ProgramMethodInfo[] methods;

    public int u2attributesCount;

    public AttrInfo[] attributes;

    /**
     * An extra field pointing to the subclasses of this class.
     * This field is filled out by the <code>{@link ClassFileReferenceInitializer}</code>.
     */
    public ClassFile[] subClasses = null;

    /**
     * An extra field in which visitors can store information.
     */
    public Object visitorInfo;

    /**
     * Creates a new ClassFile from the class file format data in the DataInput
     * stream.
     *
     * @throws IOException if class file is corrupt or incomplete
     */
    public static ClassFile create(DataInput din) throws IOException {
        ProgramClassFile cf = new ProgramClassFile();
        cf.read(din);
        return cf;
    }

    /**
     * Creates an empty ProgramClassFile.
     */
    private ProgramClassFile() {
    }

    /**
     * Imports the class data into this ProgramClassFile.
     */
    private void read(DataInput din) throws IOException {
        u4magic = din.readInt();
        ClassUtil.checkMagicNumber(u4magic);
        u2minorVersion = din.readUnsignedShort();
        u2majorVersion = din.readUnsignedShort();
        ClassUtil.checkVersionNumbers(u2majorVersion, u2minorVersion);
        u2constantPoolCount = din.readUnsignedShort();
        constantPool = new CpInfo[u2constantPoolCount];
        for (int i = 1; i < u2constantPoolCount; i++) {
            constantPool[i] = CpInfo.create(din);
            int tag = constantPool[i].getTag();
            if (tag == ClassConstants.CONSTANT_Long || tag == ClassConstants.CONSTANT_Double) {
                i++;
            }
        }
        u2accessFlags = din.readUnsignedShort();
        u2thisClass = din.readUnsignedShort();
        u2superClass = din.readUnsignedShort();
        u2interfacesCount = din.readUnsignedShort();
        u2interfaces = new int[u2interfacesCount];
        for (int i = 0; i < u2interfacesCount; i++) {
            u2interfaces[i] = din.readUnsignedShort();
        }
        u2fieldsCount = din.readUnsignedShort();
        fields = new ProgramFieldInfo[u2fieldsCount];
        for (int i = 0; i < u2fieldsCount; i++) {
            fields[i] = ProgramFieldInfo.create(din, this);
        }
        u2methodsCount = din.readUnsignedShort();
        methods = new ProgramMethodInfo[u2methodsCount];
        for (int i = 0; i < u2methodsCount; i++) {
            methods[i] = ProgramMethodInfo.create(din, this);
        }
        u2attributesCount = din.readUnsignedShort();
        attributes = new AttrInfo[u2attributesCount];
        for (int i = 0; i < u2attributesCount; i++) {
            attributes[i] = AttrInfo.create(din, this);
        }
    }

    /**
     * Exports the representation to a DataOutput stream.
     */
    public void write(DataOutput dout) throws IOException {
        dout.writeInt(u4magic);
        dout.writeShort(u2minorVersion);
        dout.writeShort(u2majorVersion);
        dout.writeShort(u2constantPoolCount);
        for (int i = 1; i < u2constantPoolCount; i++) {
            CpInfo cpInfo = constantPool[i];
            if (cpInfo != null) {
                cpInfo.write(dout);
            }
        }
        dout.writeShort(u2accessFlags);
        dout.writeShort(u2thisClass);
        dout.writeShort(u2superClass);
        dout.writeShort(u2interfacesCount);
        for (int i = 0; i < u2interfacesCount; i++) {
            dout.writeShort(u2interfaces[i]);
        }
        dout.writeShort(u2fieldsCount);
        for (int i = 0; i < u2fieldsCount; i++) {
            fields[i].write(dout);
        }
        dout.writeShort(u2methodsCount);
        for (int i = 0; i < u2methodsCount; i++) {
            methods[i].write(dout);
        }
        dout.writeShort(u2attributesCount);
        for (int i = 0; i < u2attributesCount; i++) {
            attributes[i].write(dout);
        }
    }

    /**
     * Returns the CpInfo at the given index in the constant pool.
     */
    public CpInfo getCpEntry(int cpIndex) {
        return constantPool[cpIndex];
    }

    /**
     * Returns the field with the given name and descriptor.
     */
    ProgramFieldInfo findProgramField(String name, String descriptor) {
        for (int i = 0; i < u2fieldsCount; i++) {
            ProgramFieldInfo field = fields[i];
            if ((name == null || field.getName(this).equals(name)) && (descriptor == null || field.getDescriptor(this).equals(descriptor))) {
                return field;
            }
        }
        return null;
    }

    /**
     * Returns the method with the given name and descriptor.
     */
    ProgramMethodInfo findProgramMethod(String name, String descriptor) {
        for (int i = 0; i < u2methodsCount; i++) {
            ProgramMethodInfo method = methods[i];
            if ((name == null || method.getName(this).equals(name)) && (descriptor == null || method.getDescriptor(this).equals(descriptor))) {
                return method;
            }
        }
        return null;
    }

    /**
     * Returns the attribute specified by the given name.
     */
    AttrInfo getAttribute(String name) {
        for (int i = 0; i < u2attributesCount; i++) {
            AttrInfo attribute = attributes[i];
            if (attribute.getAttributeName(this).equals(name)) {
                return attribute;
            }
        }
        return null;
    }

    public int getAccessFlags() {
        return u2accessFlags;
    }

    public String getName() {
        return getCpClassNameString(u2thisClass);
    }

    public String getSuperName() {
        return u2superClass == 0 ? null : getCpClassNameString(u2superClass);
    }

    public String getInterfaceName(int index) {
        return getCpClassNameString(u2interfaces[index]);
    }

    public int getCpTag(int cpIndex) {
        return constantPool[cpIndex].getTag();
    }

    public String getCpString(int cpIndex) {
        return ((Utf8CpInfo) constantPool[cpIndex]).getString();
    }

    public String getCpClassNameString(int cpIndex) {
        ClassCpInfo classEntry = (ClassCpInfo) constantPool[cpIndex];
        Utf8CpInfo nameEntry = (Utf8CpInfo) constantPool[classEntry.getNameIndex()];
        return nameEntry.getString();
    }

    public String getCpNameString(int cpIndex) {
        return ((NameAndTypeCpInfo) constantPool[cpIndex]).getName(this);
    }

    public String getCpTypeString(int cpIndex) {
        return ((NameAndTypeCpInfo) constantPool[cpIndex]).getType(this);
    }

    public void addSubClass(ClassFile classFile) {
        if (subClasses == null) {
            subClasses = new ClassFile[1];
        } else {
            ClassFile[] temp = new ClassFile[subClasses.length + 1];
            System.arraycopy(subClasses, 0, temp, 0, subClasses.length);
            subClasses = temp;
        }
        subClasses[subClasses.length - 1] = classFile;
    }

    public ClassFile getSuperClass() {
        return u2superClass != 0 ? ((ClassCpInfo) constantPool[u2superClass]).referencedClassFile : null;
    }

    public ClassFile getInterface(int index) {
        return ((ClassCpInfo) constantPool[u2interfaces[index]]).referencedClassFile;
    }

    public boolean extends_(ClassFile classFile) {
        if (this.equals(classFile)) {
            return true;
        }
        ClassFile superClass = getSuperClass();
        return superClass != null && superClass.extends_(classFile);
    }

    public boolean implements_(ClassFile classFile) {
        if (this.equals(classFile)) {
            return true;
        }
        for (int i = 0; i < u2interfacesCount; i++) {
            ClassFile interfaceClass = getInterface(i);
            if (interfaceClass != null && interfaceClass.implements_(classFile)) {
                return true;
            }
        }
        return false;
    }

    public FieldInfo findField(String name, String descriptor) {
        return findProgramField(name, descriptor);
    }

    public MethodInfo findMethod(String name, String descriptor) {
        return findProgramMethod(name, descriptor);
    }

    public void accept(ClassFileVisitor classFileVisitor) {
        classFileVisitor.visitProgramClassFile(this);
    }

    public void hierarchyAccept(boolean visitThisClass, boolean visitSuperClass, boolean visitInterfaces, boolean visitSubclasses, ClassFileVisitor classFileVisitor) {
        if (visitThisClass) {
            accept(classFileVisitor);
        }
        if (visitSuperClass) {
            ClassFile superClass = getSuperClass();
            if (superClass != null) {
                superClass.hierarchyAccept(true, true, visitInterfaces, false, classFileVisitor);
            }
        }
        if (visitInterfaces) {
            for (int i = 0; i < u2interfacesCount; i++) {
                ClassFile interfaceClass = getInterface(i);
                if (interfaceClass != null) {
                    interfaceClass.hierarchyAccept(true, true, true, false, classFileVisitor);
                }
            }
        }
        if (visitSubclasses) {
            if (subClasses != null) {
                for (int i = 0; i < subClasses.length; i++) {
                    ClassFile subClass = subClasses[i];
                    subClass.hierarchyAccept(true, false, false, true, classFileVisitor);
                }
            }
        }
    }

    public void constantPoolEntriesAccept(CpInfoVisitor cpInfoVisitor) {
        for (int i = 1; i < u2constantPoolCount; i++) {
            if (constantPool[i] != null) {
                constantPool[i].accept(this, cpInfoVisitor);
            }
        }
    }

    public void constantPoolEntryAccept(int index, CpInfoVisitor cpInfoVisitor) {
        constantPool[index].accept(this, cpInfoVisitor);
    }

    public void fieldsAccept(MemberInfoVisitor memberInfoVisitor) {
        for (int i = 0; i < u2fieldsCount; i++) {
            fields[i].accept(this, memberInfoVisitor);
        }
    }

    public void fieldAccept(String name, String descriptor, MemberInfoVisitor memberInfoVisitor) {
        ProgramFieldInfo field = findProgramField(name, descriptor);
        if (field != null) {
            field.accept(this, memberInfoVisitor);
        }
    }

    public void methodsAccept(MemberInfoVisitor memberInfoVisitor) {
        for (int i = 0; i < u2methodsCount; i++) {
            methods[i].accept(this, memberInfoVisitor);
        }
    }

    public void methodAccept(String name, String descriptor, MemberInfoVisitor memberInfoVisitor) {
        ProgramMethodInfo method = findProgramMethod(name, descriptor);
        if (method != null) {
            method.accept(this, memberInfoVisitor);
        }
    }

    public void attributesAccept(AttrInfoVisitor attrInfoVisitor) {
        for (int i = 0; i < u2attributesCount; i++) {
            attributes[i].accept(this, attrInfoVisitor);
        }
    }

    public Object getVisitorInfo() {
        return visitorInfo;
    }

    public void setVisitorInfo(Object visitorInfo) {
        this.visitorInfo = visitorInfo;
    }
}
