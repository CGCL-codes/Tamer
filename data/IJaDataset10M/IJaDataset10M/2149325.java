package net.sourceforge.code2uml.inspectors.java;

import java.io.DataInput;
import java.io.IOException;
import net.sourceforge.code2uml.unitdata.FieldInfo;
import net.sourceforge.code2uml.unitdata.MethodInfo;
import net.sourceforge.code2uml.unitdata.UnitInfo;
import net.sourceforge.code2uml.unitdata.UnitInfoImpl;

/**
 * This class is responsible for reading .class file's contents.
 *
 * @author Mateusz Wenus
 */
class ClassFileReader {

    private static final int ACC_PUBLIC = 0x0001;

    private static final int ACC_FINAL = 0x0010;

    private static final int ACC_SUPER = 0x0020;

    private static final int ACC_INTERFACE = 0x0200;

    private static final int ACC_ABSTRACT = 0x0400;

    private static final int ACC_SYNTHETIC = 0x1000;

    private static final int ACC_ANNOTATION = 0x2000;

    private static final int ACC_ENUM = 0x4000;

    private FieldInfoReader fieldReader = new FieldInfoReader();

    private MethodInfoReader methodReader = new MethodInfoReader();

    /**
     * Creates a new instance of ClassFileReader.
     */
    public ClassFileReader() {
    }

    /**
     * Returns class/interface/enum defined in given .class file.
     *
     * @param in DataInput to read contents of .class file from
     * @return class/interface/enum defined in that .class file
     * @throws IOException if an I/O error occurs
     */
    public UnitInfo read(DataInput in) throws IOException {
        UnitInfoImpl unit = new UnitInfoImpl();
        if (in.readInt() != 0xCAFEBABE) return null;
        in.readInt();
        int data = in.readUnsignedShort();
        Object[] pool = new ConstantPoolReader().read(in, data);
        data = in.readUnsignedShort();
        if ((data & ACC_INTERFACE) != 0) unit.setIsInterface(true); else if ((data & ACC_ENUM) != 0) unit.setIsEnum(true); else {
            unit.setIsClass(true);
            if ((data & ACC_ABSTRACT) != 0) unit.setIsAbstract(true);
        }
        if ((data & ACC_PUBLIC) != 0) unit.setIsPublic(true);
        data = in.readUnsignedShort();
        String name = (String) pool[(Integer) pool[data]];
        unit.setName(name.replace('/', '.'));
        unit.setSimpleName(unit.getName().substring(unit.getName().lastIndexOf('.') + 1));
        data = in.readUnsignedShort();
        if (data > 0) {
            name = (String) pool[(Integer) pool[data]];
            unit.addSupertype(name.replace('/', '.'));
        }
        data = in.readUnsignedShort();
        for (int i = 0; i < data; i++) {
            int idx = in.readUnsignedShort();
            name = (String) pool[(Integer) pool[idx]];
            unit.addSupertype(name.replace('/', '.'));
        }
        data = in.readUnsignedShort();
        for (int i = 0; i < data; i++) {
            FieldInfo field = fieldReader.read(in, pool);
            if (field != null) {
                if (field.getTypeName().equals("enum")) {
                    unit.addEnumValue(field.getName());
                } else {
                    unit.addField(field);
                }
            }
        }
        data = in.readUnsignedShort();
        for (int i = 0; i < data; i++) {
            MethodInfo method = methodReader.readMethod(in, pool);
            if (method != null) {
                unit.addMethod(method);
            }
        }
        return unit;
    }

    /**
     * Returns qualified name of a class/interface/enum defined in a .class 
     * file.
     *
     * @param in DataInput to read contents of .class file from
     * @return qualified name of a class/interafce/enum defined in given file
     * @throws IOException if I/O error occurs
     */
    public String readUnitName(DataInput in) throws IOException {
        ConstantPoolReader reader = new ConstantPoolReader();
        if (in.readInt() != 0xCAFEBABE) return null;
        in.readInt();
        int data = in.readUnsignedShort();
        Object[] pool = new ConstantPoolReader().read(in, data);
        in.readUnsignedShort();
        data = in.readUnsignedShort();
        return ((String) pool[(Integer) pool[data]]).replace('/', '.');
    }
}
