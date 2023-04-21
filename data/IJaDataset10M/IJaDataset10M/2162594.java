package net.sf.csv2sql.storage;

import java.nio.ByteBuffer;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Properties;
import net.sf.csv2sql.storage.exceptions.MissingRequiredParameterException;
import net.sf.csv2sql.storage.exceptions.StorageException;

/**
 * Storage implementation that save temporary data in a tempfile.
 * <p>
 * supported lines around 275000.
 * @see Storage Storage
 * @author <a href="mailto:dconsonni@enter.it">Davide Consonni</a>
 */
public class Disk extends Storage {

    private File file;

    private int bufferLength;

    public boolean add(String o) throws StorageException {
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(raf.length());
            ByteBuffer buf = ByteBuffer.allocate(bufferLength);
            buf.put(o.getBytes());
            raf.write(buf.array());
            raf.close();
            raf = null;
            return true;
        } catch (IOException e) {
            throw new StorageException("can't add element to storage", e);
        }
    }

    public String get(int index) throws IndexOutOfBoundsException, StorageException {
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(index * bufferLength);
            byte[] b = new byte[bufferLength];
            raf.read(b);
            String str = new String(b);
            raf.close();
            raf = null;
            return str.trim();
        } catch (IOException e) {
            throw new StorageException("can't get element from storage", e);
        }
    }

    public long size() throws StorageException {
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            int length = (int) raf.length() / bufferLength;
            raf.close();
            raf = null;
            return length;
        } catch (IOException e) {
            throw new StorageException("can't stat size of storage", e);
        }
    }

    public void clear() throws StorageException {
        file.delete();
    }

    private void calculateBuffer() {
        bufferLength = Integer.parseInt(getStorageProperties().getProperty("buffer", "2000"));
    }

    private File calculateTempFile() throws StorageException {
        try {
            String filename = getStorageProperties().getProperty("tempfile");
            return (filename == null) ? File.createTempFile("csvtosql", ".tmp") : new File(filename);
        } catch (IOException e) {
            throw new StorageException("can't calculate temp file", e);
        }
    }

    public void configure(Properties properties) throws MissingRequiredParameterException, StorageException {
        super.configure(properties);
        calculateBuffer();
        file = calculateTempFile();
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("can't create temp file", e);
        }
    }

    protected void finalize() throws Throwable {
        clear();
    }

    public HashMap requiredParameterList() {
        return null;
    }

    public HashMap optionalParameterList() {
        HashMap hm = new HashMap();
        hm.put("buffer", "length in char of buffer. default 2000 characters.");
        hm.put("tempfile", "temporary filename (absolute). default random name in system tmp directory.");
        return hm;
    }
}
