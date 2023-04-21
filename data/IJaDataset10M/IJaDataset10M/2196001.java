package netxrv.jnlp.services;

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.ref.*;
import javax.jnlp.*;
import netxrv.jnlp.*;

/**
 * File contents.
 *
 * @author <a href="mailto:jmaxwell@users.sourceforge.net">Jon A. Maxwell (JAM)</a> - initial author
 * @version $Revision: 1.1 $ 
 */
class XFileContents implements FileContents {

    /** the file */
    private File file;

    /**
     * Create a file contents implementation for the file.
     */
    protected XFileContents(File file) {
        this.file = file;
    }

    /**
     *
     * @throws IOException if an I/O exception occurs.
     */
    public boolean canRead() throws IOException {
        return file.canRead();
    }

    /**
     *
     * @throws IOException if an I/O exception occurs.
     */
    public boolean canWrite() throws IOException {
        return file.canWrite();
    }

    /**
     *
     * @throws IOException if an I/O exception occurs.
     */
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    /**
     *
     * @throws IOException if an I/O exception occurs.
     */
    public long getLength() throws IOException {
        return file.length();
    }

    /**
     *
     * @throws IOException if an I/O exception occurs.
     */
    public long getMaxLength() throws IOException {
        return Long.MAX_VALUE;
    }

    /**
     *
     * @throws IOException if an I/O exception occurs.
     */
    public String getName() throws IOException {
        return file.getName();
    }

    /**
     *
     * @throws IOException if an I/O exception occurs.
     */
    public OutputStream getOutputStream(boolean overwrite) throws IOException {
        return new FileOutputStream(file.getPath(), !overwrite);
    }

    /**
     *
     * @throws IOException if an I/O exception occurs.
     */
    public JNLPRandomAccessFile getRandomAccessFile(String mode) throws IOException {
        return null;
    }

    /**
     *
     * @throws IOException if an I/O exception occurs.
     */
    public long setMaxLength(long maxlength) throws IOException {
        return maxlength;
    }
}
