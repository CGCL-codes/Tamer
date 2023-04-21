package fireteam.orb.gzip;

import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.transport.ORBSocketFactory;
import com.sun.corba.se.pept.transport.Acceptor;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import fireteam.security.RSA;
import javax.net.ssl.SSLContext;

/**
 * Created by IntelliJ IDEA.
 * User: tolik1
 * Date: 19.02.2008
 * Time: 11:25:19
 * To change this template use File | Settings | File Templates.
 */
public class IIOPServerSocketFactory implements ORBSocketFactory, Serializable {

    public void setORB(ORB orb) {
    }

    public ServerSocket createServerSocket(String type, InetSocketAddress inetSocketAddress) throws IOException {
        GZIPServerSocket srvSock = new GZIPServerSocket(inetSocketAddress.getPort(), 5, inetSocketAddress.getAddress());
        return srvSock;
    }

    public Socket createSocket(String type, InetSocketAddress inetSocketAddress) throws IOException {
        GZIPSocket sock = new GZIPSocket(inetSocketAddress.getAddress(), inetSocketAddress.getPort());
        return sock;
    }

    public void setAcceptedSocketOptions(Acceptor acceptor, ServerSocket serverSocket, Socket socket) throws SocketException {
        System.out.println("[" + DateFormat.getDateTimeInstance().format(new Date()) + "]  " + "connection from: " + socket.getInetAddress().getHostAddress() + " (" + socket.getInetAddress().getHostName() + ")");
    }
}
