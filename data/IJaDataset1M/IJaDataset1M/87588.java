package org.icepdf.core.io;

import java.io.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * @author Mark Collette
 * @since 2.0
 */
public class RandomAccessFileInputStream extends InputStream implements SeekableInput {

    private static final Logger logger = Logger.getLogger(RandomAccessFileInputStream.class.toString());

    private long m_lMarkPosition;

    private RandomAccessFile m_RandomAccessFile;

    private final Object m_oLock;

    private Object m_oCurrentUser;

    public static RandomAccessFileInputStream build(File file) throws FileNotFoundException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        RandomAccessFileInputStream rafis = new RandomAccessFileInputStream(raf);
        return rafis;
    }

    protected RandomAccessFileInputStream(RandomAccessFile raf) {
        super();
        m_lMarkPosition = 0L;
        m_RandomAccessFile = raf;
        m_oLock = new Object();
    }

    public int read() throws IOException {
        return m_RandomAccessFile.read();
    }

    public int read(byte[] buffer) throws IOException {
        return m_RandomAccessFile.read(buffer);
    }

    public int read(byte[] buffer, int offset, int length) throws IOException {
        return m_RandomAccessFile.read(buffer, offset, length);
    }

    public void close() throws IOException {
        m_RandomAccessFile.close();
    }

    public int available() {
        return 0;
    }

    public void mark(int readLimit) {
        try {
            m_lMarkPosition = m_RandomAccessFile.getFilePointer();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public boolean markSupported() {
        return true;
    }

    public void reset() throws IOException {
        m_RandomAccessFile.seek(m_lMarkPosition);
    }

    public long skip(long n) throws IOException {
        int nn = (int) (n & 0xFFFFFFFF);
        return (long) m_RandomAccessFile.skipBytes(nn);
    }

    public void seekAbsolute(long absolutePosition) throws IOException {
        m_RandomAccessFile.seek(absolutePosition);
    }

    public void seekRelative(long relativeOffset) throws IOException {
        long pos = m_RandomAccessFile.getFilePointer();
        pos += relativeOffset;
        if (pos < 0L) pos = 0L;
        m_RandomAccessFile.seek(pos);
    }

    public void seekEnd() throws IOException {
        long end = m_RandomAccessFile.length();
        seekAbsolute(end);
    }

    public long getAbsolutePosition() throws IOException {
        return m_RandomAccessFile.getFilePointer();
    }

    public long getLength() throws IOException {
        return m_RandomAccessFile.length();
    }

    public InputStream getInputStream() {
        return this;
    }

    public void beginThreadAccess() {
        synchronized (m_oLock) {
            Object requestingUser = Thread.currentThread();
            while (true) {
                if (m_oCurrentUser == null) {
                    m_oCurrentUser = requestingUser;
                    break;
                } else if (m_oCurrentUser == requestingUser) {
                    break;
                } else {
                    try {
                        m_oLock.wait(100L);
                    } catch (InterruptedException ie) {
                    }
                }
            }
        }
    }

    public void endThreadAccess() {
        synchronized (m_oLock) {
            Object requestingUser = Thread.currentThread();
            if (m_oCurrentUser == null) {
                m_oLock.notifyAll();
            } else if (m_oCurrentUser == requestingUser) {
                m_oCurrentUser = null;
                m_oLock.notifyAll();
            } else {
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.severe("ERROR:  Thread finished using SeekableInput, but it wasn't locked by that Thread\n" + "        Thread: " + Thread.currentThread() + "\n" + "        Locking Thread: " + m_oCurrentUser + "\n" + "        SeekableInput: " + this);
                }
            }
        }
    }
}
