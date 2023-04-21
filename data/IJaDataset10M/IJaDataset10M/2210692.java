package org.robotframework.maven.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Lasse Koskela
 */
public class NullOutputStream extends OutputStream {

    public void write(int data) throws IOException {
    }
}
