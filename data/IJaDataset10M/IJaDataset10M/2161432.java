package be.vds.jtbdive.core.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import be.vds.jtbdive.core.exceptions.TransferException;

public interface DataComInterface {

    boolean open() throws TransferException;

    boolean close() throws TransferException;

    boolean isClosed();

    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;
}
