package org.imagava.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An output stream which just throws everything away
 * @author Brad
 */
public class NullOutputStream extends OutputStream {

    @Override
    public void write(int b) throws IOException {
    }
}
