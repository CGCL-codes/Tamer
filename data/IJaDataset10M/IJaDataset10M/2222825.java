package net.sf.cotta;

import java.io.InputStream;
import java.io.OutputStream;

public interface StreamFactory {

    InputStream inputStream() throws TIoException;

    OutputStream outputStream(OutputMode mode) throws TIoException;
}
