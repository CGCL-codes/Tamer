package com.netx.basic.R1.logging;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import com.netx.basic.R1.shared.Constants;
import com.netx.basic.R1.shared.Disposable;
import com.netx.basic.R1.io.File;
import com.netx.basic.R1.io.Streams;
import com.netx.basic.R1.io.Translator;
import com.netx.basic.R1.io.ExtendedInputStream;
import com.netx.basic.R1.io.ExtendedOutputStream;
import com.netx.basic.R1.io.FileNotFoundException;
import com.netx.basic.R1.io.AccessDeniedException;
import com.netx.basic.R1.io.ReadWriteException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

class XlsWriter extends ColumnWriter implements Disposable {

    public static final int MAX_NUM_LINES = 10000;

    private final HSSFWorkbook _wb;

    private final HSSFSheet _sheet;

    private final List<String[]> _buffer;

    private int _rowNum;

    private boolean _changed;

    private ExtendedOutputStream _out;

    public XlsWriter(File file, ExtendedInputStream template) throws FileNotFoundException, AccessDeniedException, ReadWriteException {
        super(file);
        _buffer = new ArrayList<String[]>();
        boolean emptyFile = false;
        if (file.isBlank() && template != null) {
            _out = file.getOutputStream();
            Streams.copy(template, _out);
            template.close();
            _out.flush();
            _out.close();
        }
        if (template == null) {
            _wb = new HSSFWorkbook();
            _sheet = _wb.createSheet();
            emptyFile = true;
        } else {
            ExtendedInputStream in = file.getInputStream();
            try {
                POIFSFileSystem fs = new POIFSFileSystem(in);
                _wb = new HSSFWorkbook(fs);
                _sheet = _wb.getSheetAt(0);
            } catch (IOException io) {
                throw Translator.translateIOE(io, in.getPath());
            } finally {
                in.close();
            }
        }
        if (emptyFile) {
            _rowNum = 0;
        } else {
            for (_rowNum = _sheet.getLastRowNum() + 1; true; _rowNum--) {
                HSSFRow r = _sheet.getRow(_rowNum);
                if (r != null) {
                    HSSFCell c = r.getCell((short) 0);
                    if (c != null) {
                        HSSFRichTextString value = c.getRichStringCellValue();
                        if (value != null) {
                            if (!value.toString().equals(Constants.EMPTY)) {
                                break;
                            }
                        }
                    }
                }
            }
            _rowNum++;
        }
        _changed = false;
        _out = file.getOutputStreamAndLock();
    }

    public XlsWriter(File file) throws AccessDeniedException, IOException {
        this(file, null);
    }

    public int getMaxNumLines() {
        return MAX_NUM_LINES;
    }

    public int getTotalNumLines() {
        return _rowNum;
    }

    public void write(String[] row) throws ReadWriteException {
        HSSFRow r = _sheet.getRow(_rowNum);
        if (r == null) {
            r = _sheet.createRow(_rowNum);
        }
        for (int i = 0; i < row.length; i++) {
            HSSFCell cell = r.getCell((short) i);
            if (cell == null) {
                cell = r.createCell((short) i);
            }
            cell.setCellValue(new HSSFRichTextString(row[i] == null ? Constants.EMPTY : row[i]));
        }
        _rowNum++;
        _changed = true;
        _buffer.add(row);
        if (_buffer.size() > LogFile.getXlsBufferSize()) {
            _flush();
        }
    }

    public synchronized void close() throws ReadWriteException {
        _writeToFile();
        _out.close();
        deleteIfEmpty();
    }

    public void onDispose() throws ReadWriteException {
        close();
    }

    List<String[]> getBuffer() {
        return _buffer;
    }

    private void _flush() throws ReadWriteException {
        if (_changed) {
            _writeToFile();
            _buffer.clear();
            _changed = false;
        }
    }

    private void _writeToFile() throws ReadWriteException {
        try {
            _wb.write(_out);
        } catch (IOException io) {
            throw Translator.translateIOE(io, _out.getPath());
        }
        _out.flush();
    }
}
