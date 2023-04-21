package com.xsm.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Sony Mathew
 */
public class Proxy {

    /**
     * author Sony Mathew
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Not enough args");
        }
        int localPort = toIntPort(args[0]);
        String remoteHost = toCleanStr(args[1], "remoteHost");
        int remotePort = toIntPort(args[2]);
        createProxy(localPort, remoteHost, remotePort);
    }

    private static String toCleanStr(String str, String strMeta) {
        if (str == null || (str = str.trim()).length() == 0) {
            throw new IllegalArgumentException("Bad " + strMeta + "[" + str + "]");
        }
        return str;
    }

    private static int toIntPort(String portStr) {
        int port = -1;
        portStr = toCleanStr(portStr, "port");
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException nfx) {
            throw new IllegalArgumentException("Bad port [" + portStr + "]");
        }
        if (port < 0) {
            throw new IllegalStateException("unexpected port value [" + portStr + "]");
        }
        return port;
    }

    /**
     * author: Sony Mathew
     * 
     * Transfer bytes from in to out
     * @throws RuntimeException
     */
    public static void copyBytes(InputStream in, OutputStream out) throws RuntimeException {
        try {
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException iox) {
            throw new RuntimeException(iox);
        }
    }

    public static void createProxy(int localPort, String remoteHost, int remotePort) {
        try {
            createProxyImpl(localPort, remoteHost, remotePort);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * author Sony Mathew
     */
    public static void createProxyImpl(int localPort, String remoteHost, int remotePort) throws Exception {
        ServerSocket serverSocket = new ServerSocket(localPort);
        while (true) {
            Socket localSocket = serverSocket.accept();
            Socket remoteSocket = null;
            try {
                remoteSocket = new Socket(remoteHost, remotePort);
            } catch (Exception x) {
                close(localSocket);
                throw x;
            }
            ProxyConnectionMgr proxyConnMgr = new ProxyConnectionMgr(localSocket, remoteSocket);
            proxyConnMgr.start();
        }
    }

    public static void close(Socket s) {
        if (s != null) {
            if (!s.isClosed()) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class ProxyConnectionMgr extends Thread {

        private final Socket localSocket;

        private final Socket remoteSocket;

        private final ProxyConnection localToRemoteProxyConn;

        private final ProxyConnection remoteToLocalProxyConn;

        public ProxyConnectionMgr(Socket localSocket, Socket remoteSocket) {
            super("ProxyConnectionMgr");
            this.localSocket = localSocket;
            this.remoteSocket = remoteSocket;
            this.localToRemoteProxyConn = new ProxyConnection(localSocket, remoteSocket);
            this.remoteToLocalProxyConn = new ProxyConnection(remoteSocket, localSocket);
        }

        @Override
        public void run() {
            try {
                localToRemoteProxyConn.start();
                remoteToLocalProxyConn.start();
                localToRemoteProxyConn.join();
                remoteToLocalProxyConn.join();
            } catch (Exception x) {
                x.printStackTrace();
            } finally {
                close(localSocket);
                close(remoteSocket);
            }
        }
    }

    static class ProxyConnection extends Thread {

        private final Socket inputSocket;

        private final Socket outputSocket;

        public ProxyConnection(Socket inputSocket, Socket outputSocket) {
            super("ProxyConnection");
            this.inputSocket = inputSocket;
            this.outputSocket = outputSocket;
        }

        public void run() {
            try {
                runImpl();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void runImpl() throws Exception {
            InputStream in = inputSocket.getInputStream();
            OutputStream out = outputSocket.getOutputStream();
            copyBytes(in, out);
        }
    }
}
