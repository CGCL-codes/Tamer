package org.ws4d.java.communication.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a connection. Used to decouple the stack from CLDC and Java SE.
 * Socket implementations.
 */
public interface IConnection {

    /**
	 * Opens an <code>InputStream</code>.
	 * 
	 * @return the InputStream.
	 * @throws IOException an I/O exception.
	 */
    InputStream openInputStream() throws IOException;

    /**
	 * Opens an <code>OutputStream</code>.
	 * 
	 * @return the OutputStream.
	 * @throws IOException an I/O exception.
	 */
    OutputStream openOutputStream() throws IOException;

    /**
	 * Closes the connection.
	 * 
	 * @throws IOException an I/O exception.
	 */
    void close() throws IOException;
}
