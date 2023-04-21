package jimo.spi.im.api.net;

import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * A <code>SocksConnection</code> connects to a specified host and
 * port through a socks (version 4 or 5) proxy server. This type of
 * connection is used if the user is connecting through a socks
 * proxy server.
 *
 * @see     jimo.spi.im.api.net.DirectConnection DirectConnection
 * @see     jimo.spi.im.api.net.HttpConnection   HttpConnection
 */
public class SocksConnection implements Connection {

    /**
	 * The socket representing this connection.
	 */
    private Socket sock;

    /**
	 * The input stream of the connection.
	 */
    private InputStream in;

    /**
	 * The output stream of the connection.
	 */
    private OutputStream out;

    /**
	 * Construct a connection to the specified host and port using socks version 4
	 * protocol.
	 *
	 * @param socksHost             The socks proxy server host name.
	 * @param socksPort             The socks proxy server port number.
	 * @param host                  The host name to be connected.
	 * @param port                  The port number on which to be connected.
	 * @throws UnknownHostException If the IP address of the host could not be determined.
	 * @throws IOException          If an I/O error occurs when creating the connection.
	 * @throws SecurityException    If a security manager exists and its checkConnect
	 *                              method doesn't allow the operation.
	 */
    public SocksConnection(String socksHost, int socksPort, String host, int port) throws UnknownHostException, IOException {
        sock = new Socket(socksHost, socksPort);
        in = sock.getInputStream();
        out = sock.getOutputStream();
        out.write(0x04);
        out.write(0x01);
        out.write(port >>> 16);
        out.write(port & 0x00ff);
        byte[] addr = InetAddress.getByName(host).getAddress();
        out.write(addr);
        out.write(0x00);
        out.flush();
        int versionNumber = in.read();
        int resultCode = in.read();
        for (int i = 0; i < 6; i++) in.read();
        if (versionNumber != 0 || resultCode != 90) {
            close();
            throw new IOException("Socks connection failed. Return version number = " + versionNumber + " resultCode = " + resultCode);
        }
    }

    /**
	 * Construct a connection to the specified host and port using socks version 5
	 * protocol. A version 5 proxy may optionally require a user name and password
	 * for establishing connection. Those can be passed to this constructor. If the
	 * proxy does not require a user name and password, pass <code>null</code> for
	 * the parameters <code>username</code> and <code>password</code>.
	 *
	 * @param socksHost             The socks proxy server host name.
	 * @param socksPort             The socks proxy server port number.
	 * @param username              The username to connect to the proxy.
	 * @param password              The password to connect to the proxy.
	 * @param host                  The host name to be connected.
	 * @param port                  The port number on which to be connected.
	 * @throws UnknownHostException If the IP address of the host could not be determined.
	 * @throws IOException          If an I/O error occurs when creating the connection.
	 * @throws SecurityException    If a security manager exists and its checkConnect
	 *                              method doesn't allow the operation.
	 */
    public SocksConnection(String socksHost, int socksPort, String username, String password, String host, int port) throws UnknownHostException, IOException {
        byte method;
        if (username == null || password == null) {
            method = 0x00;
        } else {
            method = 0x02;
        }
        sock = new Socket(socksHost, socksPort);
        in = sock.getInputStream();
        out = sock.getOutputStream();
        out.write(0x05);
        out.write(0x01);
        out.write(method);
        out.flush();
        int version = in.read();
        int allowedMethod = in.read();
        if (version != 0x05 || method != allowedMethod) {
            close();
            throw new IOException("Socks proxy doesn't support this method: " + method);
        }
        if (method == 0x02) {
            out.write(0x05);
            out.write(username.length());
            out.write(username.getBytes());
            out.write(password.length());
            out.write(password.getBytes());
            out.flush();
            version = in.read();
            int success = in.read();
            if (version != 0x05 || success != 0x00) {
                close();
                throw new IOException("Authentication failed in socks proxy");
            }
        }
        out.write(0x05);
        out.write(0x01);
        out.write(0x00);
        out.write(0x01);
        byte[] addr = InetAddress.getByName(host).getAddress();
        out.write(addr);
        out.write(port >>> 16);
        out.write(port & 0x00ff);
        out.flush();
        int versionNumber = in.read();
        int resultCode = in.read();
        if (versionNumber != 0x05 || resultCode != 0x00) {
            close();
            throw new IOException("Socks connection failed. Return version number = " + versionNumber + " resultCode = " + resultCode);
        }
        in.read();
        int count = 0;
        switch(in.read()) {
            case 0x01:
                count = 6;
                break;
            case 0x03:
                count = in.read() + 2;
                break;
            case 0x04:
                count = 8;
                break;
        }
        for (int i = 0; i < count; i++) in.read();
    }

    /**
	 * Returns the number of bytes that can be read from this connection
	 * without blocking by the next caller of a method for this connection.
	 * The next caller might be the same thread or or another thread.
	 *
	 * <p>
	 * This method has exactly the same semantics as that of
	 * <code>java.io.InputStream.available()</code>.
	 *
	 * @return the number of bytes that can be read from this connection without blocking.
	 * @throws IOException if an I/O error occurs.
	 */
    public int available() throws IOException {
        return in.available();
    }

    /**
	 * Reads the next byte of data from the input stream. The value byte is returned as an
	 * int in the range 0 to 255. If no byte is available because the end of the stream has
	 * been reached, the value -1 is returned. This method blocks until input data is
	 * available, the end of the stream is detected, or an exception is thrown.
	 *
	 * @return the next byte of data, or -1 if the end of the stream is reached.
	 * @throws IOException if an I/O error occurs.
	 */
    public int read() throws IOException {
        return in.read();
    }

    /**
	 * Reads some number of bytes from the connection and stores them into
	 * the buffer array b. The number of bytes actually read is returned as
	 * an integer. If no bytes are available because of EOF, returns -1.
	 * This call has exactly the same semantics as that of
	 * <code>java.io.InputStream.read(byte[] b)</code>.
	 *
	 * @param b             The buffer to which data is read.
	 * @return              The total number of bytes read into the buffer, or -1 if there
	 *                      is no more data because the end of the stream has been reached.
	 * @throws IOException  If an I/O error occurs.
	 */
    public int read(byte[] b) throws IOException {
        return in.read(b);
    }

    /**
	 * Reads up to len bytes of data from the connection into an array of bytes.
	 * An attempt is made to read as many as len bytes, but a smaller number may
	 * be read, possibly zero. The number of bytes actually read is returned as
	 * an integer. If no bytes are available because of EOF, returns -1.
	 * This call has exactly the same semantics as that of
	 * <code>java.io.InputStream.read(byte[] b, int off, int len)</code>.
	 *
	 * @param b             The buffer to which data is read.
	 * @param off           The start offset in array <code>b</code> at which data is written.
	 * @param len           The maximum number of bytes to be read.
	 * @return              The total number of bytes read into the buffer, or -1 if there
	 *                      is no more data because the end of the stream has been reached.
	 * @throws IOException  If an I/O error occurs.
	 */
    public int read(byte[] b, int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    /**
	 * Writes b.length bytes from the specified byte array to this connection.
	 *
	 * @param b The data to be sent through this connection.
	 * @throws IOException If an I/O error occurs.
	 */
    public void write(byte[] b) throws IOException {
        out.write(b);
    }

    /**
	 * Flushes this connection and forces any buffered output bytes to be
	 * written out. The general contract of flush is that calling it is an
	 * indication that, if any bytes previously written have been buffered by
	 * the implementation of the connection, such bytes should immediately be
	 * written to their intended destination.
	 *
	 * @throws IOException If an I/O error occurs.
	 */
    public void flush() throws IOException {
        out.flush();
    }

    /**
	 * Closes this connection and releases any resources associated with it.
	 *
	 * @throws IOException If an I/O error occurs.
	 */
    public void close() throws IOException {
        in.close();
        out.close();
        sock.close();
    }

    /**
	 * Returns an InputStream that can be used to indirectly call the
	 * read methods.
	 *
	 * @return An InputStream for reading from this connection.
	 * @throws IOException If an I/O error occurs.
	 */
    public InputStream getInputStream() throws IOException {
        return in;
    }

    /**
	 * Returns the socket used by this connection.
	 * 
	 * @return the socket used by this connection
	 * @throws IOException If an I/O error occurs.
	 */
    public Socket getSocket() throws IOException {
        return this.sock;
    }
}
