package org.xito.launcher.jnlp.service;

import java.io.*;
import java.security.*;
import java.util.logging.*;
import javax.jnlp.*;

/**
 *
 * @author Deane Richan
 */
public class FileContentsImpl implements FileContents {

    private static final Logger logger = Logger.getLogger(FileContentsImpl.class.getName());

    private static final long DEFAUL_MAX = 20 * 1000 * 1000;

    private String name;

    private boolean canRead_flag;

    private boolean canWrite_flag;

    private File file;

    private long length;

    private long maxLength;

    /** Creates a new instance of FileContentsImpl */
    public FileContentsImpl(File f) {
        file = f;
        name = f.getAbsolutePath();
        canRead_flag = f.canRead();
        canWrite_flag = f.canWrite();
        length = f.length();
        maxLength = DEFAUL_MAX;
    }

    /**
    * Get RandomAccessFile
    */
    public JNLPRandomAccessFile getRandomAccessFile(String str) {
        return null;
    }

    /**
    * Get OutputStream
    * 
    */
    public OutputStream getOutputStream(final boolean overwrite) throws IOException {
        OutputStream out = (OutputStream) AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                try {
                    return new SecureOutputStream(!overwrite);
                } catch (IOException ioExp) {
                    logger.log(Level.WARNING, ioExp.getMessage(), ioExp);
                    return null;
                }
            }
        });
        if (out == null) {
            throw new IOException("Error Creating OutputStream");
        }
        return out;
    }

    /**
    * Set Max Length
    */
    public long setMaxLength(long max) {
        if (max < length) {
            max = length;
        }
        maxLength = max;
        return maxLength;
    }

    /**
    * GetName
    */
    public String getName() {
        return name;
    }

    /** 
    * Get Max Length
    */
    public long getMaxLength() {
        return maxLength;
    }

    /**
    * Get Length
    */
    public long getLength() {
        return length;
    }

    /**
    * Get InputStream 
    */
    public InputStream getInputStream() throws IOException {
        InputStream in = (InputStream) AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                try {
                    return new SecureInputStream();
                } catch (IOException ioExp) {
                    logger.log(Level.WARNING, ioExp.getMessage(), ioExp);
                    return null;
                }
            }
        });
        if (in == null) {
            throw new IOException("Error Creating OutputStream");
        }
        return in;
    }

    /**
    * Can Write
    */
    public boolean canWrite() {
        return canWrite_flag;
    }

    /**
    * Can Read
    */
    public boolean canRead() {
        return canRead_flag;
    }

    /*************************************************
    * SecureOutputStream Wraps a Files Output Stream
    **************************************************/
    class SecureOutputStream extends OutputStream {

        long bytesWritten = 0;

        FileOutputStream out;

        /**
       * Create the Stream
       */
        public SecureOutputStream(boolean append) throws IOException {
            out = new FileOutputStream(file, append);
        }

        public void write(byte[] b, int off, int len) throws IOException {
            bytesWritten = bytesWritten + b.length;
            if (bytesWritten > maxLength) {
                throw new IOException("Max File Size has been Exceeded");
            }
            out.write(b, off, len);
        }

        public void write(byte[] b) throws IOException {
            bytesWritten = bytesWritten + b.length;
            if (bytesWritten > maxLength) {
                throw new IOException("Max File Size has been Exceeded");
            }
            out.write(b);
        }

        public void write(int b) throws IOException {
            bytesWritten++;
            if (bytesWritten > maxLength) {
                throw new IOException("Max File Size has been Exceeded");
            }
            out.write(b);
        }

        public void flush() throws IOException {
            out.flush();
        }

        public void close() throws IOException {
            out.close();
            bytesWritten = 0;
        }
    }

    /*************************************************
    * SecureInputStream Wraps a Files Input Stream
    **************************************************/
    class SecureInputStream extends InputStream {

        FileInputStream in;

        /**
       * Create the Stream
       */
        public SecureInputStream() throws IOException {
            in = new FileInputStream(file);
        }

        public int read(byte[] b, int off, int len) throws IOException {
            return in.read(b, off, len);
        }

        public int read(byte[] b) throws IOException {
            return in.read(b);
        }

        public void mark(int readlimit) {
            in.mark(readlimit);
        }

        public long skip(long n) throws IOException {
            return in.skip(n);
        }

        public void reset() throws IOException {
            in.reset();
        }

        public int read() throws IOException {
            return in.read();
        }

        public boolean markSupported() {
            return in.markSupported();
        }

        public int available() throws IOException {
            return in.available();
        }

        public void close() throws IOException {
            in.close();
        }
    }
}
