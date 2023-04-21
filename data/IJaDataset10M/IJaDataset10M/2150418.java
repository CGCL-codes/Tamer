package test.servidor.ejecutor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;

class SocketTest extends SocketImpl {

    private InputStream inputStream;

    private OutputStream outputStream;

    /**
	 * 
	 */
    public SocketTest(String text) {
        super();
        this.inputStream = new ByteArrayInputStream(text.getBytes());
        this.outputStream = System.out;
    }

    /**
	 * @see java.net.SocketImpl#accept(java.net.SocketImpl)
	 */
    @Override
    protected void accept(SocketImpl s) throws IOException {
    }

    /**
	 * @see java.net.SocketImpl#available()
	 */
    @Override
    protected int available() throws IOException {
        return 0;
    }

    /**
	 * @see java.net.SocketImpl#bind(java.net.InetAddress, int)
	 */
    @Override
    protected void bind(InetAddress host, int port) throws IOException {
    }

    /**
	 * @see java.net.SocketImpl#close()
	 */
    @Override
    protected void close() throws IOException {
        this.inputStream.close();
    }

    /**
	 * @see java.net.SocketImpl#connect(java.net.InetAddress, int)
	 */
    @Override
    protected void connect(InetAddress address, int port) throws IOException {
    }

    /**
	 * @see java.net.SocketImpl#connect(java.net.SocketAddress, int)
	 */
    @Override
    protected void connect(SocketAddress address, int timeout) throws IOException {
    }

    /**
	 * @see java.net.SocketImpl#connect(java.lang.String, int)
	 */
    @Override
    protected void connect(String host, int port) throws IOException {
    }

    /**
	 * @see java.net.SocketImpl#create(boolean)
	 */
    @Override
    protected void create(boolean stream) throws IOException {
    }

    /**
	 * @see java.net.SocketImpl#getInputStream()
	 */
    @Override
    protected InputStream getInputStream() throws IOException {
        return this.inputStream;
    }

    /**
	 * @see java.net.SocketImpl#getOutputStream()
	 */
    @Override
    protected OutputStream getOutputStream() throws IOException {
        return this.outputStream;
    }

    /**
	 * @see java.net.SocketImpl#listen(int)
	 */
    @Override
    protected void listen(int backlog) throws IOException {
    }

    /**
	 * @see java.net.SocketImpl#sendUrgentData(int)
	 */
    @Override
    protected void sendUrgentData(int data) throws IOException {
    }

    /**
	 * @see java.net.SocketOptions#getOption(int)
	 */
    public Object getOption(int optID) throws SocketException {
        return null;
    }

    /**
	 * @see java.net.SocketOptions#setOption(int, java.lang.Object)
	 */
    public void setOption(int optID, Object value) throws SocketException {
    }
}
