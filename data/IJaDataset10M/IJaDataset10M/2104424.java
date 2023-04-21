package com.idedeluxe.bc.core.model.struc;

import com.idedeluxe.bc.core.model.Element;
import com.idedeluxe.bc.core.model.Leaf;
import com.idedeluxe.bc.core.model.ModelInput;

/**
 * A descriptor that describes a leaf to be inserted if padding is required.
 */
public class PaddingStructure extends AbstractStructure {

    private final int alignment;

    public PaddingStructure(String description, int alignment) {
        super(description);
        this.alignment = alignment;
    }

    @Override
    public Element readElementFromInput(Element parent, ModelInput input) {
        int readedBytes = input.getReadedByteCount();
        int readedBytesInScope = input.getReadedByteCountInScope();
        final int offset = readedBytesInScope;
        final int paddingByteCount = (alignment - (offset % alignment)) % alignment;
        int byteResult = 0;
        for (int n = 0; n < paddingByteCount; n++) {
            byteResult = byteResult << 4;
            byteResult += input.readBytes(1);
        }
        Leaf leafModel = new Leaf();
        leafModel.setParent(parent);
        leafModel.setLocation(this);
        leafModel.setByteOffset(readedBytes);
        leafModel.setByteOffsetInScope(readedBytesInScope);
        leafModel.setByteCount(paddingByteCount);
        leafModel.setByteValue(byteResult);
        return leafModel;
    }
}
