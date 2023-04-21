package com.netx.basic.R1.logging;

import com.netx.generics.R1.util.Tools;
import com.netx.basic.R1.io.File;
import com.netx.basic.R1.io.FileSystemException;
import com.netx.basic.R1.io.FileNotFoundException;
import com.netx.basic.R1.io.ReadWriteException;

abstract class FileWriter {

    private final File _file;

    protected FileWriter(File f) {
        _file = f;
    }

    protected File getFile() {
        return _file;
    }

    protected void deleteIfEmpty() {
        if (_file == null) {
            return;
        }
        try {
            if (isBlank()) {
                _file.delete();
            }
        } catch (FileSystemException fse) {
            Tools.handleCriticalError("failed to delete empty log file", fse);
        }
    }

    public abstract void close() throws ReadWriteException;

    public abstract boolean isBlank() throws FileNotFoundException;
}
