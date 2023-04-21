package gnu.classpath.jdwp.transport;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.IllegalArgumentException;
import java.util.HashMap;

/**
 * A class representing a transport layer. This class serves as a generic
 * interface for all transport types used in the JDWP back-end.
 *
 * @author Keith Seitz (keiths@redhat.com)
 */
public interface ITransport {

    /**
   * Configure the transport with the given properties
   *
   * @param   properties  properties of the transport configuration
   * @throws  TransportException on configury error
   */
    public void configure(HashMap properties) throws TransportException;

    /**
   * Initialize the transport
   *
   * @throws  TransportException on initialization error
   */
    public void initialize() throws TransportException;

    /**
   * Shutdown the transport
   */
    public void shutdown();

    /**
   * Get the input stream for the transport
   */
    public InputStream getInputStream() throws IOException;

    /**
   * Get the output stream for the transport
   */
    public OutputStream getOutputStream() throws IOException;
}
