package org.databene.commons.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * {@link PrintWriter} implementation which writes to a {@link File} 
 * and provides the file's identity.<br/>
 * <br/>
 * Created: 14.06.2011 09:10:05
 * @since 0.5.8
 * @author Volker Bergmann
 */
public class FilePrintWriter extends PrintWriter {

    protected File file;

    public FilePrintWriter(File file) throws FileNotFoundException {
        super(file);
        this.file = file;
    }

    public FilePrintWriter(File file, String encoding) throws FileNotFoundException, UnsupportedEncodingException {
        super(file, encoding);
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
