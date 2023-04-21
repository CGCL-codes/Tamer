package org.antban.antser.node;

import org.antban.antser.VBRValue;

/**
 * @author Dmitry Sorokin
 */
public abstract class AbstractNode implements Node {

    private final long index;

    private int dataSize;

    public AbstractNode(long index, int dataSize) {
        this.index = index;
        this.dataSize = dataSize;
    }

    public final long getIndex() {
        return index;
    }

    public final int getDataSize() {
        return dataSize;
    }

    protected final void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public final int getTotalSize() {
        final int nodeIndexLength = VBRValue.getByteCount(getType().modifyIndex(getIndex()));
        return getDataSize() + nodeIndexLength + VBRValue.getByteCount(getDataSize());
    }
}
