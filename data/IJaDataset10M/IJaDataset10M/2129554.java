package annone.local.linker.java;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalVariableTableAttribute extends Attribute {

    private class LocalVariable {

        private final int startPc;

        private final int length;

        private final int nameIndex;

        private final int descriptorIndex;

        private final int localIndex;

        public LocalVariable(int startPc, int length, int nameIndex, int descriptorIndex, int localIndex) {
            this.startPc = startPc;
            this.length = length;
            this.nameIndex = nameIndex;
            this.descriptorIndex = descriptorIndex;
            this.localIndex = localIndex;
        }
    }

    private final ConstantPool constantPool;

    private final List<LocalVariable> localVariables;

    public LocalVariableTableAttribute(ConstantPool constantPool) {
        super(constantPool.getUtf8Index("LocalVariableTable"));
        this.constantPool = constantPool;
        this.localVariables = new ArrayList<LocalVariableTableAttribute.LocalVariable>(0);
    }

    @Override
    protected int getLength() {
        return 2 + localVariables.size() * 10;
    }

    public void addLocalVariable(int startPc, int length, String name, String descriptor, int localIndex) {
        localVariables.add(new LocalVariable(startPc, length, constantPool.getUtf8Index(name), constantPool.getUtf8Index(descriptor), localIndex));
    }

    @Override
    public void writeTo(DataOutput out) throws IOException {
        super.writeTo(out);
        out.writeShort(localVariables.size());
        for (LocalVariable lv : localVariables) {
            out.writeShort(lv.startPc);
            out.writeShort(lv.length);
            out.writeShort(lv.nameIndex);
            out.writeShort(lv.descriptorIndex);
            out.writeShort(lv.localIndex);
        }
    }
}
