package net.rootnode.loomchild.util.io;

import java.io.IOException;
import java.io.Writer;

/** 
 * Writer that does not write anywhere. Idea is similar to /dev/null.
 * @author loomchild
 */
public class NullWriter extends Writer {

    public void close() throws IOException {
    }

    public void flush() throws IOException {
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
    }
}
