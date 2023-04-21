package ru.spbau.bytecode.info;

public class ConstantNameAndTypeInfo extends ConstantPoolInfo {

    public final int nameIndex;

    public final int descriptorIndex;

    public ConstantNameAndTypeInfo(int tag, int nameIndex, int descriptorIndex) {
        super(tag);
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }
}
