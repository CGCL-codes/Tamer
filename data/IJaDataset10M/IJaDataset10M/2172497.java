package org.apache.harmony.pack200;

/**
 * Abstract superclass for constant pool entries
 */
public abstract class ConstantPoolEntry {

    private int index = -1;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
