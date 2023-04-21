package ru.spbau.bytecode.info;

public class ConstantInvokeDynamicInfo extends ConstantPoolInfo {

    public final int bootstrapMethodAttrIndex;

    public final int nameAndTypeIndex;

    public ConstantInvokeDynamicInfo(int tag, int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
        super(tag);
        this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }
}
