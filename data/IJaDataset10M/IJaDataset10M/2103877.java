package xbird.storage.index;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.annotation.concurrent.GuardedBy;
import xbird.util.codec.VariableByteCodec;
import xbird.util.collections.longs.Bytes2LongOpenHash;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public final class DiskHashTable implements Closeable {

    private final Bytes2LongOpenHash index;

    @GuardedBy("raf")
    private final RandomAccessFile raf;

    @GuardedBy("raf")
    private long pointer = 0L;

    public DiskHashTable(File file) {
        this(file, 8192);
    }

    public DiskHashTable(File file, int initMapSize) {
        if (file == null) {
            throw new IllegalArgumentException();
        }
        try {
            this.raf = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("File not found: " + file.getAbsolutePath(), e);
        }
        this.index = new Bytes2LongOpenHash(initMapSize);
    }

    public byte[] getValue(final byte[] key) {
        final long ptr = index.get(key);
        if (ptr == -1L) {
            return null;
        }
        try {
            return readRecord(ptr);
        } catch (IOException e) {
            throw new IllegalStateException("I/O error happened in reading a record: " + ptr, e);
        }
    }

    public void getValues(final byte[] key, final CallbackHandler handler) {
        byte[] b = getValue(key);
        while (b != null) {
            long nextptr = VariableByteCodec.decodeLong(b);
            int headerlen = VariableByteCodec.requiredBytes(nextptr);
            int reclen = b.length - headerlen;
            byte[] rec = new byte[reclen];
            System.arraycopy(b, 0, rec, 0, reclen);
            handler.indexInfo(null, rec);
            if (nextptr == 0L) {
                break;
            }
            try {
                b = readRecord(nextptr);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public void addValue(final byte[] key, final byte[] value) {
        final long ptr = index.get(key);
        final long pos;
        try {
            pos = appendRecord(value, (ptr == -1L) ? 0L : ptr);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        if (ptr == -1L) {
            index.put(key, pos);
        }
    }

    private long appendRecord(final byte[] value, final long nextPtr) throws IOException {
        int headerlen = VariableByteCodec.requiredBytes(nextPtr);
        int valuelen = value.length;
        final int len = valuelen + headerlen;
        final byte[] b = new byte[len];
        VariableByteCodec.encodeLong(nextPtr, b, 0);
        System.arraycopy(value, 0, b, headerlen, valuelen);
        long pos;
        synchronized (raf) {
            pos = pointer;
            if (pos == -1L) {
                pos = raf.length();
                this.pointer = pos;
            }
            raf.write(b, 0, len);
            this.pointer += len;
        }
        return pos;
    }

    public byte[] readRecord(final long ptr) throws IOException {
        final byte[] b;
        synchronized (raf) {
            raf.seek(ptr);
            this.pointer = -1L;
            int len = raf.readInt();
            b = new byte[len];
            raf.read(b);
        }
        return b;
    }

    public void flush() throws IOException {
        raf.getChannel().force(true);
    }

    public void close() throws IOException {
        raf.close();
    }
}
