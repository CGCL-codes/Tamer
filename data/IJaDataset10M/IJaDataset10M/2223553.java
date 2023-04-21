package it.sauronsoftware.ftp4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Instances of this one represent connections with remote hosts.
 * 
 * @author Carlo Pelliccia
 */
public interface FTPConnection {

    /**
	 * This method returns the InputStream for this connection.
	 * 
	 * @return The InputStream for this connection.
	 * @throws IOException
	 *             If an I/O error occurs when creating the stream.
	 */
    public InputStream getInputStream() throws IOException;

    /**
	 * This method returns the OutputStream for this connection.
	 * 
	 * @return The OutputStream for this connection.
	 * @throws IOException
	 *             If an I/O error occurs when creating the stream.
	 */
    public OutputStream getOutputStream() throws IOException;

    /**
	 * This method closes the connection.
	 * 
	 * @throws IOException
	 *             If an I/O error occurs when closing the connection.
	 */
    public void close() throws IOException;
}
