package phex.net.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.channels.Channels;
import phex.common.address.DefaultDestAddress;
import phex.common.address.DestAddress;
import phex.net.repres.PresentationManager;
import phex.net.repres.SocketFacade;
import phex.prefs.core.NetworkPrefs;
import phex.prefs.core.ProxyPrefs;
import phex.utils.IOUtil;

public final class SocketFactory {

    private static final Object LOCK = new Object();

    private static int concurrentConnectAttempts = 0;

    private SocketFactory() {
    }

    /**
     * @param address
     * @return
     * @throws IOException 
     */
    public static SocketFacade connect(DestAddress address) throws IOException {
        return connect(address, NetworkPrefs.TcpConnectTimeout.get().intValue(), null);
    }

    /**
     * @param address
     * @return
     * @throws IOException
     */
    public static SocketFacade connect(DestAddress address, Runnable acquireCallback) throws IOException {
        return connect(address, NetworkPrefs.TcpConnectTimeout.get().intValue(), acquireCallback);
    }

    /**
     * The operation might block to acquire a concurrentConnectAttempts lock 
     * before starting the also blocking connect operation.
     *  
     * @param address
     * @param timeout
     * @return
     * @throws IOException
     * @throws InterruptedException 
     */
    public static SocketFacade connect(DestAddress address, int timeout) throws IOException {
        return connect(address, timeout, null);
    }

    public static SocketFacade connect(DestAddress address, int timeout, Runnable acquireCallback) throws IOException {
        if (!address.isValidAddress()) {
            throw new IOException("Invalid DestAddress: " + address);
        }
        if (ProxyPrefs.UseSocks5.get().booleanValue()) {
            return connectSock5(address, acquireCallback);
        }
        SocketFacade socket = createSocket(address, timeout, acquireCallback);
        return socket;
    }

    private static SocketFacade connectSock5(DestAddress address, Runnable acquireCallback) throws IOException {
        SocketFacade socket = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            socket = createSocket(new DefaultDestAddress(ProxyPrefs.Socks5Host.get(), ProxyPrefs.Socks5Port.get().intValue()), NetworkPrefs.TcpConnectTimeout.get().intValue(), acquireCallback);
            is = Channels.newInputStream(socket.getChannel());
            os = Channels.newOutputStream(socket.getChannel());
            byte[] header;
            if (ProxyPrefs.Socks5Authentication.get().booleanValue() && ProxyPrefs.Socks5User.get().length() > 0) {
                header = new byte[4];
                header[0] = (byte) 0x05;
                header[1] = (byte) 0x02;
                header[2] = (byte) 0x00;
                header[3] = (byte) 0x02;
            } else {
                header = new byte[3];
                header[0] = (byte) 0x05;
                header[1] = (byte) 0x01;
                header[2] = (byte) 0x00;
            }
            os.write(header, 0, header.length);
            int servVersion = is.read();
            if (servVersion != 0x05) {
                throw new IOException("Invalid SOCKS server version: " + servVersion);
            }
            byte servMethod = (byte) is.read();
            if (servMethod == (byte) 0xFF) {
                throw new IOException("SOCKS: No acceptable authentication.");
            }
            if (servMethod == 0x00) {
            } else if (servMethod == 0x02) {
                authenticateUserPassword(is, os);
            } else {
                throw new IOException("Unknown SOCKS5 authentication method required.");
            }
            String host = address.getHostName();
            int port = address.getPort();
            byte[] request = new byte[10];
            request[0] = (byte) 0x05;
            request[1] = (byte) 0x01;
            request[2] = (byte) 0x00;
            request[3] = (byte) 0x01;
            IOUtil.serializeIP(host, request, 4);
            request[8] = (byte) (port >> 8);
            request[9] = (byte) (port);
            os.write(request, 0, request.length);
            int version = is.read();
            int status = is.read();
            switch(status) {
                case 0x01:
                    throw new IOException("SOCKS: General SOCKS server failure");
                case 0x02:
                    throw new IOException("SOCKS: Connection not allowed by ruleset");
                case 0x03:
                    throw new IOException("SOCKS: Network unreachable");
                case 0x04:
                    throw new SocketException("SOCKS: Host unreachable");
                case 0x05:
                    throw new SocketException("SOCKS: Connection refused");
                case 0x06:
                    throw new IOException("SOCKS: TTL expired");
                case 0x07:
                    throw new IOException("SOCKS: Command not supported");
                case 0x08:
                    throw new IOException("SOCKS: Address type not supported");
            }
            if (status != 0x00) {
                throw new IOException("SOCKS: Unknown status response: " + status);
            }
            is.read();
            int atype = is.read();
            if (atype == 1) {
                is.read();
                is.read();
                is.read();
                is.read();
            } else if (atype == 3) {
                int len = is.read();
                if (len < 0) len += 256;
                while (len > 0) {
                    is.read();
                    len--;
                }
            } else if (atype == 4) {
                for (int i = 0; i < 16; i++) is.read();
            } else {
                throw new IOException("Invalid return address type for SOCKS5: " + atype);
            }
            is.read();
            is.read();
            if (version != 0x05) {
                throw new IOException("Invalid SOCKS server version: " + version);
            }
            return socket;
        } catch (Exception exp) {
            IOUtil.closeQuietly(is);
            IOUtil.closeQuietly(os);
            IOUtil.closeQuietly(socket);
            if (exp instanceof IOException) {
                throw (IOException) exp;
            } else {
                throw new IOException("Error: " + exp.getMessage());
            }
        }
    }

    private static void authenticateUserPassword(InputStream is, OutputStream os) throws IOException {
        String userName = ProxyPrefs.Socks5User.get();
        String password = ProxyPrefs.Socks5Password.get();
        byte[] buffer = new byte[3 + userName.length() + password.length()];
        int pos = 0;
        buffer[pos++] = (byte) 0x01;
        buffer[pos++] = (byte) userName.length();
        pos = IOUtil.serializeString(userName, buffer, pos);
        buffer[pos++] = (byte) password.length();
        pos = IOUtil.serializeString(password, buffer, pos);
        os.write(buffer, 0, pos);
        if (is.read() == 1 && is.read() == 0) {
            return;
        }
        throw new IOException("Proxy server authentication failed.");
    }

    /**
     * @param host
     * @param port
     * @param timeout
     * @return
     * @throws IOException
     * @throws SocketException
     * @throws InterruptedException 
     */
    private static SocketFacade createSocket(DestAddress address, int connectTimeout, Runnable acquireCallback) throws IOException, SocketException {
        synchronized (LOCK) {
            while (concurrentConnectAttempts >= NetworkPrefs.MaxConcurrentConnectAttempts.get().intValue()) {
                try {
                    LOCK.wait();
                } catch (InterruptedException exp) {
                    throw new SocketException(exp.getMessage());
                }
            }
            concurrentConnectAttempts++;
        }
        try {
            if (acquireCallback != null) {
                acquireCallback.run();
            }
            SocketFacade socket = PresentationManager.getInstance().createSocket(address, connectTimeout);
            socket.setSoTimeout(NetworkPrefs.TcpRWTimeout.get().intValue());
            return socket;
        } finally {
            synchronized (LOCK) {
                concurrentConnectAttempts--;
                LOCK.notifyAll();
            }
        }
    }
}
