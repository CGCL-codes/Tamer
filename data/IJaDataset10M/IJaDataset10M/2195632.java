package annone.local.linker.java;

public class InterfaceMethodrefConstant extends RefConstant {

    public InterfaceMethodrefConstant(int classIndex, int nameAndTypeIndex) {
        super(classIndex, nameAndTypeIndex);
    }

    @Override
    public ConstantType getType() {
        return ConstantType.INTERFACE_METHODREF;
    }
}
