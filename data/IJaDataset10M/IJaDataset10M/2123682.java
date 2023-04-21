package net.sf.eBus.net;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * Accepts new socket connections and creates echo clients to
 * handle those sockets.
 *
 * @author <a href="mailto:rapp@acm.org">Charles Rapp</a>
 */
public final class EchoServer implements ServerSocketListener {

    public EchoServer(final int port, final int response) throws IOException {
        _serverSocket = new AsyncServerSocket(this);
        _serverSocket.open(port);
        _response = response;
        _clients = new LinkedList<EchoClient>();
        _badClients = new LinkedList<SocketChannel>();
        _caughtex = null;
    }

    public Throwable getException() {
        return (_caughtex);
    }

    public int getResponse() {
        return (_response);
    }

    public void setResponse(int response) {
        _response = response;
        return;
    }

    public synchronized void closeClients() {
        for (EchoClient client : _clients) {
            client.close();
        }
        _clients.clear();
        for (SocketChannel badClient : _badClients) {
            if (badClient != null) {
                try {
                    badClient.close();
                } catch (IOException ioex) {
                }
            }
        }
        _badClients.clear();
        return;
    }

    public synchronized void closeAll() {
        if (_serverSocket != null) {
            if (_serverSocket.isOpen() == true) {
                _serverSocket.close();
            }
            _serverSocket = null;
        }
        closeClients();
        return;
    }

    @Override
    public synchronized void handleAccept(final SocketChannel s, final AsyncServerSocket server) {
        try {
            if (_response == AsyncSocketTest.CLOSE_ON_OPEN) {
                try {
                    s.close();
                } catch (IOException ioex) {
                }
            } else if (_response == AsyncSocketTest.BAD_CLIENT) {
                _badClients.add(s);
            } else {
                final boolean echoFlag = (_response == AsyncSocketTest.ECHO_CLIENT);
                _clients.add(new EchoClient(s, echoFlag));
            }
        } catch (IOException ioex) {
            _caughtex = ioex;
        }
        return;
    }

    @Override
    public void handleClose(final Throwable t, final AsyncServerSocket s) {
        _caughtex = t;
        return;
    }

    private AsyncServerSocket _serverSocket;

    private int _response;

    private final List<EchoClient> _clients;

    private final List<SocketChannel> _badClients;

    private Throwable _caughtex;
}
