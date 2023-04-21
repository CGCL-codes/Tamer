package org.omegat.util;

import java.io.IOException;
import java.io.Writer;

/**
 * /dev/null writer. Used for filters where writer required by filter, but not
 * need for application.
 * 
 * @author Alex Buloichik <alex73mail@gmail.com>
 */
public class NullWriter extends Writer {

    public void write(char[] cbuf, int off, int len) throws IOException {
    }

    public void flush() throws IOException {
    }

    public void close() throws IOException {
    }
}
