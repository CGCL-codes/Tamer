package org.xsocket.server.smtp.ssl;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;

/**
*
* @author grro@xsocket.org
*/
public final class SSLClient {

    private int port = 7795;

    private SSLContext sslContext = null;

    public static void main(String... args) throws Exception {
        new SSLClient().launch();
    }

    public void launch() throws Exception {
        for (int i = 0; i < 3; i++) {
            InputStream in = null;
            OutputStream out = null;
            try {
                sslContext = new SSLTestContextFactory().getSSLContext();
                SocketFactory socketFactory = sslContext.getSocketFactory();
                Socket socket = socketFactory.createSocket("127.0.0.1", port);
                in = socket.getInputStream();
                out = socket.getOutputStream();
                in.read();
                System.out.println("ready");
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception ignore) {
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception ignore) {
                    }
                }
            }
        }
    }
}
