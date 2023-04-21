package org.reassembler.jarfish;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * If you call parse() this is the method order.
 * 
 * <ul>
 *   <li>testSig(din);
     <li>parseHeader(cm, din);</li>
     <li>parsePool(cm, din);</li>
     <li>parseAccessFlags(cm, din);</li>
     <li>readTaxonomy(cm, din);</li>
     <li>readFields(cm, din);</li>
     <li>parseMethods(cm, din);</li>
     <li>parseClassAttributes(cm, din);</li>
    </ul>    
 *
 */
public class ClassParser {

    public void parseHeader(ClassMeta cm, DataInputStream din) throws IOException {
        cm.setMinorVersion(din.readUnsignedShort());
        cm.setMajorVersion(din.readUnsignedShort());
        String versionString = Integer.toString(cm.getMajorVersion()) + "." + Integer.toString(cm.getMinorVersion());
        String javaVersion = null;
        if (versionString.equals("45.3")) {
            javaVersion = "1.0/1.1";
        } else if (versionString.equals("46.0")) {
            javaVersion = "1.2";
        } else if (versionString.equals("47.0")) {
            javaVersion = "1.3";
        } else if (versionString.equals("48.0")) {
            javaVersion = "1.4";
        } else if (versionString.equals("49.0")) {
            javaVersion = "1.5";
        } else if (versionString.equals("50.0")) {
            javaVersion = "1.6";
        } else {
            javaVersion = "> 1.6";
        }
        cm.setClassVersion(versionString);
        cm.setJavaVersion(javaVersion);
    }

    void skipJunk(ClassMeta cm, DataInputStream din) throws IOException {
        readTaxonomy(cm, din);
        readFields(cm, din);
    }

    public void parseMethods(ClassMeta cm, DataInputStream din) throws IOException {
        int methodsCount = readU2(din);
        cm.setMethods(readMethods(cm, methodsCount, din));
    }

    private Method[] readMethods(ClassMeta cm, int count, DataInputStream din) throws IOException {
        Method[] ms = new Method[count];
        for (int i = 0; i < count; i++) {
            Method m = ms[i] = new Method();
            int accessFlags = readU2(din);
            m.setAccessFlags(accessFlags);
            int nameIndex = readU2(din);
            String name = cm.getConstantEntry(nameIndex - 1).getValue().toString();
            m.setName(name);
            int descriptorIndex = readU2(din);
            name = cm.getConstantEntry(descriptorIndex - 1).getValue().toString();
            m.setDescriptor(name);
            int ac = readU2(din);
            Attribute[] as = readAttributes(cm, ac, din);
            m.setAttributes(as);
        }
        return ms;
    }

    private void skipBytes(int count, DataInputStream din) throws IOException {
        for (int i = 0; i < count; i++) {
            din.readByte();
        }
    }

    private Attribute[] readAttributes(ClassMeta cm, int attributeCount, DataInputStream din) throws IOException {
        Attribute[] as = new Attribute[attributeCount];
        for (int j = 0; j < attributeCount; j++) {
            int nameIndex = readU2(din);
            String name = cm.getPoolString(nameIndex - 1);
            int length = readU4(din);
            Attribute a = null;
            if (name.equals(Attribute.CODE)) {
                CodeAttribute ca = new CodeAttribute();
                a = as[j] = ca;
                a.setName(name);
                a.setLength(length);
                ca.setMaxStack(readU2(din));
                ca.setMaxLocals(readU2(din));
                int codeLength = readU4(din);
                ca.setCodeLength(codeLength);
                byte[] code = new byte[codeLength];
                din.readFully(code);
                ca.setCode(code);
                int exceptionTableLength = readU2(din);
                skipBytes(exceptionTableLength, din);
                int ac = readU2(din);
                Attribute[] attributes = readAttributes(cm, ac, din);
                ca.setAttributes(attributes);
            } else if (name.equals(Attribute.LINE_NUMBER_TABLE)) {
                LineNumberTableAttribute lnt = new LineNumberTableAttribute();
                a = as[j] = lnt;
                a.setName(name);
                int tableLength = readU2(din);
                LineNumberEntry[] entries = new LineNumberEntry[tableLength];
                for (int i = 0; i < entries.length; i++) {
                    LineNumberEntry lne = entries[i] = new LineNumberEntry();
                    lne.setStartPc(readU2(din));
                    lne.setLineNumber(readU2(din));
                }
                lnt.setLineNumberTable(entries);
            } else if (name.equals(Attribute.SOURCE_FILE)) {
                SourceFileAttribute sfa = new SourceFileAttribute();
                a = as[j] = sfa;
                a.setName(name);
                int sfni = readU2(din);
                String sourceName = cm.getPoolString(sfni - 1);
                sfa.setSourceFileName(sourceName);
                sfa.setSourceNameIndex(sfni);
                cm.setSourceFileName(sourceName);
            } else {
                System.out.println("no class for: " + name);
                a = as[j] = new Attribute();
                a.setName(name);
                skipBytes(length, din);
            }
        }
        return as;
    }

    private int readU2(DataInputStream din) throws IOException {
        return din.readUnsignedShort();
    }

    private int readU4(DataInputStream din) throws IOException {
        return din.readInt();
    }

    public void parseAccessFlags(ClassMeta cm, DataInputStream din) throws IOException {
        int val = readU2(din);
        int ACC_PUBLIC = 0x01;
        int ACC_FINAL = 0x10;
        int ACC_SUPER = 0x20;
        int ACC_INTERFACE = 0x0200;
        int ACC_ABSTRACT = 0x0400;
        cm.setPublic((ACC_PUBLIC & val) == ACC_PUBLIC);
        cm.setFinal((ACC_FINAL & val) == ACC_FINAL);
        cm.setSuper((ACC_SUPER & val) == ACC_SUPER);
        cm.setInterface((ACC_INTERFACE & val) == ACC_INTERFACE);
        cm.setAbstract((ACC_ABSTRACT & val) == ACC_ABSTRACT);
    }

    public void parsePool(ClassMeta cm, DataInputStream din) throws IOException {
        int cpSize = readU2(din) - 1;
        cm.setConstantPoolSize(cpSize);
        List entries = new ArrayList();
        for (int i = 0; i < cpSize; i++) {
            int type = din.readUnsignedByte();
            Object value = readValue(type, din);
            entries.add(new ConstantEntry(type, value));
            String typeName = ConstantEntry.NAMES[type];
            if (typeName.equals("CONSTANT_Long") || typeName.equals("CONSTANT_Double")) {
                entries.add(new ConstantEntry(0, null));
                i++;
            }
        }
        cm.setEntries(entries);
    }

    private Object readValue(int type, DataInputStream din) throws IOException {
        switch(type) {
            case 7:
                return new Integer(readU2(din));
            case 9:
            case 10:
            case 11:
            case 12:
                readU2(din);
                readU2(din);
                break;
            case 8:
                return new Integer(readU2(din));
            case 3:
            case 4:
                readU4(din);
                break;
            case 5:
            case 6:
                readU4(din);
                readU4(din);
                break;
            case 1:
                String val = din.readUTF();
                return val;
            default:
                return null;
        }
        return null;
    }

    public void parseClassAttributes(ClassMeta cm, DataInputStream din) throws IOException {
        int attributeCount = readU2(din);
        Attribute[] tribs = readAttributes(cm, attributeCount, din);
        cm.setAttributes(tribs);
    }

    public void readTaxonomy(ClassMeta cm, DataInputStream din) throws IOException {
        int thisClass = readU2(din);
        int index = ((Integer) cm.getConstantEntry(thisClass - 1).getValue()).intValue();
        cm.setTypeName(cm.getPoolString(index - 1));
        int superClass = readU2(din);
        index = ((Integer) cm.getConstantEntry(superClass - 1).getValue()).intValue();
        cm.setSuperTypeName(cm.getPoolString(index - 1));
        int interfacesCount = readU2(din);
        String[] interfaces = new String[interfacesCount];
        for (int i = 0; i < interfacesCount; i++) {
            int classIndex = readU2(din);
            index = ((Integer) cm.getConstantEntry(classIndex - 1).getValue()).intValue();
            interfaces[i] = cm.getPoolString(index - 1);
        }
        cm.setInterfaces(interfaces);
    }

    public void readFields(ClassMeta cm, DataInputStream din) throws IOException {
        int fieldsCount = readU2(din);
        Field[] fields = new Field[fieldsCount];
        cm.setFields(fields);
        for (int i = 0; i < fieldsCount; i++) {
            Field fi = fields[i] = new Field();
            fi.setAccessFlags(readU2(din));
            fi.setNameIndex(readU2(din));
            fi.setName(cm.getPoolString(fi.getNameIndex() - 1));
            fi.setDescriptorIndex(readU2(din));
            fi.setDescriptor(cm.getPoolString(fi.getDescriptorIndex() - 1));
            int attributeCount = readU2(din);
            fi.setAttributes(readAttributes(cm, attributeCount, din));
        }
    }

    public boolean testSig(DataInputStream din) throws IOException {
        return readU4(din) == 0xCAFEBABE;
    }

    public void parse(ClassMeta cm, DataInputStream din) throws IOException {
        testSig(din);
        parseHeader(cm, din);
        parsePool(cm, din);
        parseAccessFlags(cm, din);
        readTaxonomy(cm, din);
        readFields(cm, din);
        parseMethods(cm, din);
        parseClassAttributes(cm, din);
    }
}
