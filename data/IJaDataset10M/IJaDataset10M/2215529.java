package ru.adv.repository.dump;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Iterator;
import org.springframework.util.Assert;
import ru.adv.db.base.DBValue;

/**
 * Date: 03.10.2003 Time: 14:33:07
 * 
 * @see DumpWriter
 */
public class DumpReader {

    private DumpHeader _header = null;

    private ObjectInputStream _inputStream = null;

    private DumpObjectHeader _lastObjectHeader = null;

    private int _nextItem = 0;

    private File _tmpDir = null;

    /**
     * Открывает поток на чтение и читает из него {@link DumpHeader}
     * 
     * @param inputStream
     * @see #setTmpDir
     * @throws IOException
     */
    public void open(InputStream inputStream) throws IOException {
        _inputStream = new ObjectInputStream(inputStream);
        _header = DumpHeader.read(_inputStream);
        readNextItem();
        if (_header.isIncludeFiles()) {
            if (getTmpDir() == null) {
                throw new IOException("Temporary directory is not set for DumpReader");
            }
        }
    }

    /**
     * временная директория, в которую записвается прочитанные файлы, должна
     * существовать и быть установлена если {@link DumpHeader#isIncludeFiles}
     * установлен в true
     * 
     * @return
     */
    public File getTmpDir() {
        return _tmpDir;
    }

    /**
     * установить временную директорию, в которую записвается прочитанные файлы,
     * должна существовать и быть установлена если
     * {@link DumpHeader#isIncludeFiles} установлен в true
     */
    public void setTmpDir(File tmpDir) throws IOException {
        if (tmpDir != null) {
            if (!tmpDir.exists() || !tmpDir.isDirectory()) {
                throw new IOException("Temporary directory does not exist " + tmpDir.getAbsolutePath());
            }
            if (!tmpDir.canWrite()) {
                throw new IOException("Can't write to temporary directory " + tmpDir.getAbsolutePath());
            }
        }
        _tmpDir = tmpDir;
    }

    /**
     * Возвращает считаный из потока заголовок
     * 
     * @return заголовок
     */
    public DumpHeader getDumpHeader() throws IOException {
        checkStream();
        return _header;
    }

    /**
     * проверяет, что inputstream был открыт
     * 
     * @throws IOException
     */
    private void checkStream() throws IOException {
        if (_inputStream == null) {
            throw new IOException("InputStream not open for " + this);
        }
    }

    /**
     * Закрывает поток на чтение
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        checkStream();
        _inputStream.close();
        _inputStream = null;
    }

    private void readNextItem() throws IOException {
        checkStream();
        _nextItem = _inputStream.read();
    }

    /**
     * Признак того, что в потоке следующей записью является
     * {@link DumpObjectHeader}
     * 
     * @return
     */
    public boolean nextIsObjectHeader() {
        return _nextItem == DumpWriter.NEXT_IS_OBJECT_HEADER;
    }

    /**
     * Признак конца файла, потока
     * 
     * @return
     */
    public boolean nextIsEOF() {
        return _nextItem == DumpWriter.NEXT_IS_EOF;
    }

    /**
     * Читает из потока {@link DumpObjectHeader}
     * 
     * @return
     */
    public DumpObjectHeader readObjectHeader() throws IOException {
        DumpObjectHeader objectHeader = null;
        try {
            objectHeader = DumpObjectHeader.read(_inputStream);
            readNextItem();
        } catch (Throwable e) {
            throw new IOException("Cant read DumpObjectHeader form dump: " + e);
        }
        _lastObjectHeader = objectHeader;
        return objectHeader;
    }

    /**
     * Признак того, что в потоке следующей записью является
     * {@link DumpObjectHeader}
     * 
     * @return
     */
    public boolean nextIsRow() {
        return _nextItem == DumpWriter.NEXT_IS_ROW;
    }

    /**
     * Читает из потока {@link DumpRow}
     * 
     * @return
     */
    public DumpRow readRow() throws IOException {
        try {
            if (_lastObjectHeader == null) {
                throw new IOException("Current ObjectHeader is not set");
            }
            DumpRow row = DumpRow.readFromStream(_lastObjectHeader, _inputStream, getTmpDir());
            readNextItem();
            return row;
        } catch (Throwable e) {
            throw new IOException("Cant read DumpRow form dump: " + e);
        }
    }

    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    /**
     * Сравнивает два дампа
     * 
     * @param dumpFilePath1
     * @param dumpFilePath2
     * @param tmpDir
     * @throws Exception если дампы не равны
     */
    public static void comapreDumps(String dumpFilePath1, String dumpFilePath2, File tmpDir) throws Exception {
        DumpReader dumpReader1 = new DumpReader();
        DumpReader dumpReader2 = new DumpReader();
        dumpReader1.setTmpDir(tmpDir);
        dumpReader2.setTmpDir(tmpDir);
        dumpReader1.open(new FileInputStream(dumpFilePath1));
        dumpReader2.open(new FileInputStream(dumpFilePath2));
        if (!dumpReader1.getDumpHeader().equals(dumpReader2.getDumpHeader())) {
            throw new Exception("Not eqauls dump headers");
        }
        while (dumpReader1.nextIsObjectHeader()) {
            DumpObjectHeader objectHeader1 = dumpReader1.readObjectHeader();
            if (!dumpReader2.nextIsObjectHeader()) {
                throw new Exception("Not equals, expected DumpObjectHeader for " + objectHeader1.getObjectName());
            }
            DumpObjectHeader objectHeader2 = dumpReader2.readObjectHeader();
            if (!objectHeader1.equals(objectHeader2)) {
                throw new Exception("Not equals, DumpObjectHeader is different " + objectHeader1 + " <> " + objectHeader2);
            }
            while (dumpReader1.nextIsRow()) {
                DumpRow row1 = dumpReader1.readRow();
                if (!dumpReader2.nextIsRow()) {
                    throw new Exception("Not equals, expected DumpRow for " + objectHeader1.getObjectName());
                }
                DumpRow row2 = dumpReader2.readRow();
                String message = "Not equals, DumpRow is different  " + row1 + " <> " + row2;
                if (objectHeader1.isSequence()) {
                    message += " sequence " + objectHeader1.getObjectName() + " " + objectHeader1.getTypeAttrs();
                    long id1 = (Long) row1.getDbValue(0).get();
                    long id2 = (Long) row2.getDbValue(0).get();
                    Assert.isTrue(Math.abs(id1 - id2) <= 1, message);
                    Assert.isTrue(row1.getDbValue(1).equals(row2.getDbValue(1)), message);
                } else {
                    if (!row1.equals(row2)) {
                        throw new Exception(message);
                    }
                }
            }
        }
        dumpReader1.close();
        dumpReader2.close();
    }

    /**
     * Read dump file form STDIN and print its content in human readable form to
     * STDOUT
     * 
     * @param args
     */
    public static void main(String[] args) throws Exception {
        DumpReader dumpReader = new DumpReader();
        dumpReader.setTmpDir(new File("./"));
        dumpReader.open(System.in);
        DumpHeader header = dumpReader.getDumpHeader();
        if (header.isDiff() || header.isInjectDiff()) {
            System.out.println(header.toString());
            while (dumpReader.nextIsObjectHeader()) {
                DumpObjectHeader objectHeader = dumpReader.readObjectHeader();
                System.out.println(objectHeader.toString());
                while (dumpReader.nextIsRow()) {
                    DumpRow row = dumpReader.readRow();
                    System.out.println(row.toString());
                }
            }
        } else {
            while (dumpReader.nextIsObjectHeader()) {
                DumpObjectHeader objectHeader = dumpReader.readObjectHeader();
                String start = "INSERT INTO " + objectHeader.getObjectName() + " (";
                for (Iterator<TypeAttr> i = objectHeader.getTypeAttrs().iterator(); i.hasNext(); ) {
                    TypeAttr attr = i.next();
                    start += attr.getAttrName();
                    if (i.hasNext()) start += ",";
                }
                start += ") VALUES (";
                while (dumpReader.nextIsRow()) {
                    String out = start;
                    DumpRow row = dumpReader.readRow();
                    for (Iterator<DBValue> i = row.getValues().iterator(); i.hasNext(); ) {
                        DBValue value = i.next();
                        out += "'" + value.get() + "'";
                        if (i.hasNext()) out += ",";
                    }
                    out += ");";
                    System.out.println(out);
                }
            }
        }
        dumpReader.close();
    }
}
