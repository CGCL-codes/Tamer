package de.etqw.openranked.service.impl;

import java.io.IOException;
import java.security.InvalidParameterException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import de.etqw.openranked.service.ConfigService;
import de.etqw.openranked.service.ServerService;
import de.etqw.openranked.service.SocketService;
import de.etqw.openranked.service.ssl.SingleSslConnectionThread;

/**
 * Listen to given port defined in {@link SslServerInstanceProvider} and creates
 * a new threaded {@link SingleSslConnectionThread} for every new connection.
 * 
 * @author ckl
 * 
 */
public class SocketServiceImpl implements Runnable, ApplicationContextAware, SocketService {

    private static final Logger log = Logger.getLogger(SocketServiceImpl.class);

    private SSLServerSocket sslServerSocket;

    private boolean isRunning = true;

    private ServerService serverService;

    private ApplicationContext applicationContext;

    public SocketServiceImpl(SSLServerSocket _sslServerSocket) {
        setSslServerSocket(_sslServerSocket);
    }

    public SocketServiceImpl() {
    }

    public void run() {
        if (getSslServerSocket() == null) {
            throw new InvalidParameterException("getSslServerSocket() returns null but instance of SSLServerSocket was expected");
        }
        while (isRunning()) {
            try {
                log.debug("waiting for incoming connection...");
                SSLSocket socket = (SSLSocket) getSslServerSocket().accept();
                if ((getServerService() != null) && getServerService().isClientAllowed(socket)) {
                    SingleSslConnectionThread sslConnectionThread = (SingleSslConnectionThread) applicationContext.getBean(ConfigService.BEAN_PROTOTYPE_SSL_CONNECTION);
                    sslConnectionThread.setSslSocket(socket);
                    log.debug("spawning new thread for incoming connection ...");
                    sslConnectionThread.start();
                } else {
                    socket.close();
                }
            } catch (IOException e) {
                log.error("failed to handle SSL server socket: " + e.getMessage());
                throw new InvalidParameterException("Failed to handle SSL server socket: " + e.getMessage());
            }
        }
    }

    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        applicationContext = arg0;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }

    public ServerService getServerService() {
        return serverService;
    }

    public void setSslServerSocket(SSLServerSocket _sslServerSocket) {
        sslServerSocket = _sslServerSocket;
    }

    public SSLServerSocket getSslServerSocket() {
        return sslServerSocket;
    }
}
