package org.josso.tooling.gshell.core.spring;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * Simple OutputStream, that do not close the underlying
 * stream when close() is called.
 */
public class NoCloseOutputStream extends FilterOutputStream {

    public NoCloseOutputStream(OutputStream out) {
        super(out);
    }

    public void close() throws IOException {
        try {
            flush();
        } catch (IOException ignored) {
        } finally {
            out = null;
        }
    }
}
