package org.nightlabs.rmissl.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import org.apache.log4j.Logger;
import org.nightlabs.rmissl.Config;
import org.nightlabs.rmissl.socket.SSLCompressionServerSocketFactory;

/**
 * <p>Title: NightLabsServer</p>
 * <p>Description: NightLabs Server Framework</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: NightLabs GmbH</p>
 * @author unascribed
 * @version 1.0
 */
public class SSLServer extends Thread {

    private static Logger logger = Logger.getLogger(SSLServer.class);

    private int serverSocketPort;

    public SSLServer(int serverSocketPort) {
        this.serverSocketPort = serverSocketPort;
        start();
    }

    @Override
    public void run() {
        try {
            File keystoreFile = new File("jfire-server.keystore");
            if (!keystoreFile.exists()) throw new IllegalStateException("Please create a keystore file with this name and path: " + keystoreFile.getAbsolutePath());
            InputStream in = new BufferedInputStream(new FileInputStream(keystoreFile));
            SSLServerSocketFactory srvSocketFactory = new SSLCompressionServerSocketFactory(in, "nightlabs".toCharArray(), null, "nightlabs".toCharArray(), Config.compressionEnabled);
            in.close();
            SSLServerSocket srvSocket;
            srvSocket = (SSLServerSocket) srvSocketFactory.createServerSocket(serverSocketPort);
            try {
                srvSocket.setNeedClientAuth(false);
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Socket clientSocket = srvSocket.accept();
                        logger.info("Client connected!");
                        new ClientThread(clientSocket);
                    } catch (IOException x) {
                        logger.error("Error while client connected!", x);
                    }
                }
            } finally {
                srvSocket.close();
            }
        } catch (Throwable t) {
            logger.error("Error in SSLServer!", t);
        }
    }
}
