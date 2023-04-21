package com.netx.basic.R1.logging;

import com.netx.basic.R1.shared.Constants;
import com.netx.basic.R1.io.File;
import com.netx.basic.R1.io.ExtendedReader;
import com.netx.basic.R1.io.ExtendedWriter;
import com.netx.basic.R1.io.FileNotFoundException;
import com.netx.basic.R1.io.AccessDeniedException;
import com.netx.basic.R1.io.ReadWriteException;

class CsvWriter extends ColumnWriter {

    public static final int MAX_NUM_LINES = 65536;

    private final ExtendedWriter _out;

    private int _numLines;

    public CsvWriter(File file) throws FileNotFoundException, AccessDeniedException, ReadWriteException {
        super(file);
        _numLines = 0;
        if (!file.isBlank()) {
            ExtendedReader reader = new ExtendedReader(file.getInputStream());
            while (reader.readLine() != null) {
                _numLines++;
            }
            reader.close();
            reader = null;
        }
        _out = new ExtendedWriter(file.getOutputStreamAndLock(true));
    }

    public int getMaxNumLines() {
        return MAX_NUM_LINES;
    }

    public int getTotalNumLines() {
        return _numLines;
    }

    public void write(String[] line) throws ReadWriteException {
        for (int i = 0; i < line.length; i++) {
            _out.write(line[i] == null ? Constants.EMPTY : line[i].toString());
            if (i < line.length - 1) {
                _out.write(",");
            }
        }
        _out.newLine();
        _out.flush();
        _numLines++;
    }

    public void close() throws ReadWriteException {
        _out.close();
        deleteIfEmpty();
    }
}
