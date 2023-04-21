package com.faunos.util.net.http.file;

import java.io.File;
import java.io.IOException;
import com.faunos.util.Validator;

/**
 * A simple, stateless, filename-based {@linkplain DirectoryFileDefaulter}
 * implementation.
 *
 * @author Babak Farhang
 */
public class SimpleFileDefaulter implements DirectoryFileDefaulter {

    /**
     * An instance that returns an "index.html" file.
     */
    public static final SimpleFileDefaulter INDEX_DOT_HTML = new SimpleFileDefaulter("index.html");

    final String filename;

    /**
     * Creates a new instance with the specified simple <tt>filename</tt>.
     */
    public SimpleFileDefaulter(String filename) {
        Validator.ARG.notEmpty(filename, "empty default file name");
        this.filename = filename;
    }

    /**
     * Returns the <tt>filename</tt> passed in at instantiation.
     */
    public String getFilename() {
        return filename;
    }

    public File getDefaultChild(File directory) throws IOException {
        return new File(directory, getFilename());
    }
}
