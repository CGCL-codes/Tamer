package phex.connection;

import java.io.*;
import java.net.*;
import phex.common.*;
import phex.host.*;
import phex.utils.*;

public final class SocketProvider {

    private SocketProvider() {
    }

    public static Socket connect(HostAddress address) throws Exception {
        return connect(address.getHostName(), address.getPort(), 0);
    }

    public static Socket connect(HostAddress address, int timeout) throws IOException {
        return connect(address.getHostName(), address.getPort(), timeout);
    }

    /**
     * Opens a socket with a timeout in millis
     */
    public static Socket connect(String host, int port, int timeout) throws IOException {
        if (port < 0 || port > 0xFFFF) {
            throw new IOException("Wrong host address (port out of range: " + port + " )");
        }
        if (ServiceManager.sCfg.mProxyUse) {
            return connectSock5(host, port);
        }
        SocketFactory factory = new SocketFactory(host, port, timeout);
        return factory.createSocket();
    }

    private static Socket connectSock5(String host, int port) throws IOException {
        Socket sock = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            sock = new Socket(ServiceManager.sCfg.mProxyHost, ServiceManager.sCfg.mProxyPort);
            setSocketTimeout(sock, sock.getInetAddress().getAddress());
            is = sock.getInputStream();
            os = sock.getOutputStream();
            byte[] buf = new byte[600];
            int len;
            buf[0] = (byte) 0x05;
            buf[1] = (byte) 0x01;
            buf[2] = (byte) 0x00;
            len = 3;
            if (ServiceManager.sCfg.mProxyUserName.length() > 0) {
                buf[1] = (byte) 0x02;
                buf[3] = (byte) 0x02;
                len = 4;
            }
            os.write(buf, 0, len);
            int c = is.read();
            if (c != 5) {
                StringBuffer buffer = new StringBuffer();
                buffer.append((char) c);
                while (c != -1) {
                    c = is.read();
                    buffer.append((char) c);
                }
                throw new IOException("Invalid response from Socks5 proxy server: " + buffer.toString());
            }
            byte method = (byte) is.read();
            if (method == (byte) 0xFF) {
                throw new IOException("No acceptable authentication method selected by Socks5 proxy server.");
            }
            if (method == 0x00) {
            } else if (method == 0x02) {
                authenticateUserPassword(is, os, buf);
            } else {
                throw new IOException("Unknown authentication method selected by Socks5 proxy server.");
            }
            len = 0;
            buf[len++] = (byte) 0x05;
            buf[len++] = (byte) 0x01;
            buf[len++] = (byte) 0x00;
            buf[len++] = (byte) 0x01;
            len = IOUtil.serializeIP(host, buf, len);
            buf[len++] = (byte) (port >> 8);
            buf[len++] = (byte) (port);
            os.write(buf, 0, len);
            int version = is.read();
            int status = is.read();
            is.read();
            int atype = is.read();
            if (atype == 1) {
                is.read();
                is.read();
                is.read();
                is.read();
            } else if (atype == 3) {
                len = is.read();
                if (len < 0) len += 256;
                while (len > 0) {
                    is.read();
                    len--;
                }
            } else if (atype == 4) {
                for (int i = 0; i < 16; i++) is.read();
            } else {
                throw new IOException("Invalid return address type for Socks5");
            }
            is.read();
            is.read();
            if (version == 0x05 && status == 0) {
                return sock;
            }
            throw new IOException("Connection failed via Socks5 proxy server.  Proxy status=" + status);
        } catch (Exception e) {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e2) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e2) {
                }
            }
            if (sock != null) {
                try {
                    sock.close();
                } catch (Exception e2) {
                }
            }
            throw new IOException("Proxy server: " + e.getMessage());
        }
    }

    private static void authenticateUserPassword(InputStream is, OutputStream os, byte[] buf) throws IOException {
        int len = 0;
        buf[len++] = (byte) 0x01;
        buf[len++] = (byte) ServiceManager.sCfg.mProxyUserName.length();
        len = IOUtil.serializeString(ServiceManager.sCfg.mProxyUserName, buf, len);
        buf[len++] = (byte) ServiceManager.sCfg.mProxyPassword.length();
        len = IOUtil.serializeString(ServiceManager.sCfg.mProxyPassword, buf, len);
        os.write(buf, 0, len);
        if (is.read() == 1 && is.read() == 0) {
            return;
        }
        throw new IOException("Proxy server authentication failed.");
    }

    public static void setSocketTimeout(Socket socket, byte[] ip) throws IOException {
        {
            socket.setSoTimeout(ServiceManager.sCfg.mSocketTimeout);
        }
    }

    private static class SocketFactory {

        private String host;

        private int port;

        /**
         * Timeout in millis.
         */
        private int timeout;

        /**
         * @param timeout - Timeout in millis.
         */
        public SocketFactory(String aHost, int aPort, int aTimeout) {
            host = aHost;
            port = aPort;
            timeout = aTimeout;
        }

        public synchronized Socket createSocket() throws IOException {
            if (timeout == 0) {
                Socket socket = new Socket(host, port);
                setSocketTimeout(socket, socket.getInetAddress().getAddress());
                return socket;
            }
            OpenSocketWorker openSocketWorker = new OpenSocketWorker(host, port, this);
            ThreadPool.getInstance().addJob(openSocketWorker, "OpenSocketWorker-" + Integer.toHexString(openSocketWorker.hashCode()));
            try {
                this.wait(timeout);
            } catch (InterruptedException exp) {
                openSocketWorker.setTimedOut(true);
                throw new IOException("Timeout while opening Socket");
            }
            Socket socket = openSocketWorker.acquireSocketOrTimeout();
            if (socket == null) {
                throw new IOException("Timeout while opening Socket");
            }
            setSocketTimeout(socket, socket.getInetAddress().getAddress());
            return socket;
        }
    }

    private static class OpenSocketWorker implements Runnable {

        private String host;

        private int port;

        private boolean isTimedOut;

        private Socket socket;

        private SocketFactory notifyFactory;

        private IOException connectException;

        /**
         * @param timeout - Timeout in millis.
         * @param factory - The SocketFactory to notify when finished creating socket.
         *        Also used for locking.
         */
        public OpenSocketWorker(String aHost, int aPort, SocketFactory factory) {
            host = aHost;
            port = aPort;
            notifyFactory = factory;
        }

        public Socket acquireSocketOrTimeout() throws IOException {
            synchronized (notifyFactory) {
                if (connectException != null) {
                    throw connectException;
                }
                if (socket != null) {
                    return socket;
                }
                setTimedOut(true);
            }
            return null;
        }

        public void setTimedOut(boolean state) {
            synchronized (notifyFactory) {
                isTimedOut = state;
                if (socket == null) {
                    return;
                }
                if (isTimedOut) {
                    closeSocket();
                }
            }
        }

        public void run() {
            Socket tempSocket = null;
            try {
                tempSocket = new Socket(host, port);
            } catch (IOException exp) {
                connectException = exp;
            }
            synchronized (notifyFactory) {
                socket = tempSocket;
                if (isTimedOut) {
                    closeSocket();
                } else {
                    notifyFactory.notify();
                }
            }
        }

        private void closeSocket() {
            if (socket == null) {
                return;
            }
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}
