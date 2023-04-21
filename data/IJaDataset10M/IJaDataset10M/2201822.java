package org.apache.bookkeeper.bookie;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * This is the file handle for a ledger's index file that maps entry ids to location.
 * It is used by LedgerCache.
 */
class FileInfo {

    private FileChannel fc;

    /**
     * The fingerprint of a ledger index file
     */
    private byte header[] = "BKLE\0\0\0\0".getBytes();

    static final long START_OF_DATA = 1024;

    private long size;

    private int useCount;

    private boolean isClosed;

    public FileInfo(File lf) throws IOException {
        fc = new RandomAccessFile(lf, "rws").getChannel();
        size = fc.size();
        if (size == 0) {
            fc.write(ByteBuffer.wrap(header));
        }
    }

    public synchronized long size() {
        long rc = size - START_OF_DATA;
        if (rc < 0) {
            rc = 0;
        }
        return rc;
    }

    public synchronized int read(ByteBuffer bb, long position) throws IOException {
        int total = 0;
        while (bb.remaining() > 0) {
            int rc = fc.read(bb, position + START_OF_DATA);
            if (rc <= 0) {
                throw new IOException("Short read");
            }
            total += rc;
        }
        return total;
    }

    public synchronized void close() throws IOException {
        isClosed = true;
        if (useCount == 0) {
            fc.close();
        }
    }

    public synchronized long write(ByteBuffer[] buffs, long position) throws IOException {
        long total = 0;
        try {
            fc.position(position + START_OF_DATA);
            while (buffs[buffs.length - 1].remaining() > 0) {
                long rc = fc.write(buffs);
                if (rc <= 0) {
                    throw new IOException("Short write");
                }
                total += rc;
            }
        } finally {
            long newsize = position + START_OF_DATA + total;
            if (newsize > size) {
                size = newsize;
            }
        }
        return total;
    }

    public synchronized void use() {
        useCount++;
    }

    public synchronized void release() {
        useCount--;
        if (isClosed && useCount == 0) {
            try {
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
