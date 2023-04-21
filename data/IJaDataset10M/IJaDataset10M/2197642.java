package goldengate.ftp.core.data.handler;

import goldengate.ftp.core.session.FtpSession;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ExceptionEvent;

/**
 * This class is to be implemented in order to allow Business actions according
 * to FTP service
 *
 * @author Frederic Bregier
 *
 */
public abstract class DataBusinessHandler {

    /**
     * NettyHandler that holds this DataBusinessHandler
     */
    private DataNetworkHandler dataNetworkHandler = null;

    /**
     * Ftp SessionInterface
     */
    private FtpSession session = null;

    /**
     * Constructor with no argument (mandatory)
     *
     */
    public DataBusinessHandler() {
    }

    /**
     * Call when the DataNetworkHandler is created
     *
     * @param dataNetworkHandler
     *            the dataNetworkHandler to set
     */
    public void setDataNetworkHandler(DataNetworkHandler dataNetworkHandler) {
        this.dataNetworkHandler = dataNetworkHandler;
    }

    /**
     * @return the dataNetworkHandler
     */
    public DataNetworkHandler getDataNetworkHandler() {
        return dataNetworkHandler;
    }

    /**
     * Called when the connection is opened
     *
     * @param session
     *            the session to set
     */
    public void setFtpSession(FtpSession session) {
        this.session = session;
    }

    /**
     *
     * @return the ftpSession
     */
    public FtpSession getFtpSession() {
        return session;
    }

    /**
     * Is executed when the channel is closed, just before the test on the
     * finish status.
     */
    public abstract void executeChannelClosed();

    /**
     * To Clean the session attached objects for Data Network
     */
    protected abstract void cleanSession();

    /**
     * Clean the DataBusinessHandler
     *
     */
    public void clear() {
        cleanSession();
    }

    /**
     * Is executed when the channel is connected after the handler is on, before
     * answering OK or not on connection, except if the global service is going
     * to shutdown.
     *
     * @param channel
     */
    public abstract void executeChannelConnected(Channel channel);

    /**
     * Run when an exception is get before the channel is closed.
     *
     * @param e
     */
    public abstract void exceptionLocalCaught(ExceptionEvent e);
}
