package org.drftpd.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @author mog
 * @version $Id: SafeFileOutputStream.java 1621 2007-02-13 20:41:31Z djb61 $
 */
public class SafeFileOutputStream extends OutputStream {

    private File _actualFile;

    private OutputStreamWriter _out;

    private File _tempFile;

    private boolean failed = true;

    public SafeFileOutputStream(File file) throws IOException {
        _actualFile = file;
        if (!_actualFile.getAbsoluteFile().getParentFile().canWrite()) {
            throw new IOException("Can't write to target dir");
        }
        File dir = _actualFile.getParentFile();
        if (dir == null) {
            dir = new File(".");
        }
        String prefix = _actualFile.getName();
        while (prefix.length() < 3) {
            prefix = "x" + prefix;
        }
        _tempFile = File.createTempFile(prefix, null, dir);
        _out = new OutputStreamWriter(new FileOutputStream(_tempFile), "UTF-8");
    }

    public SafeFileOutputStream(String fileName) throws IOException {
        this(new File(fileName));
    }

    public void close() throws IOException {
        if (_out == null) {
            return;
        }
        _out.flush();
        _out.close();
        _out = null;
        if (!failed) {
            if (_actualFile.exists() && !_actualFile.delete()) {
                throw new IOException("delete() failed");
            }
            if (!_tempFile.exists()) {
                throw new IOException("source doesn't exist");
            }
            if (!_tempFile.renameTo(_actualFile)) {
                throw new IOException("renameTo(" + _tempFile + ", " + _actualFile + ") failed");
            }
        }
    }

    public void flush() throws IOException {
        _out.flush();
    }

    public void write(int b) throws IOException {
        _out.write(b);
        failed = false;
    }
}
