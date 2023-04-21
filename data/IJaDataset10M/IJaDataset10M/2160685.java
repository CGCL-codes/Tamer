package com.netx.basic.R1.logging;

import com.netx.basic.R1.io.File;
import com.netx.basic.R1.io.ReadWriteException;

abstract class ColumnWriter extends FileWriter {

    protected ColumnWriter(File file) {
        super(file);
    }

    public abstract int getMaxNumLines();

    public abstract int getTotalNumLines();

    public abstract void write(String[] line) throws ReadWriteException;

    public boolean isBlank() {
        return getTotalNumLines() <= 1;
    }
}
