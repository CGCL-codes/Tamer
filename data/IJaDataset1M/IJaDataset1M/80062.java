package net.wsnware.template.tinyos.app;

import net.wsnware.core.MessageListener;
import net.wsnware.core.MessageSource;
import net.wsnware.core.utils.MessageIdealChannel;
import net.wsnware.network.proxy.tcp.NetworkIO;
import net.wsnware.platform.tinyos2.TinyMessageIO;
import net.wsnware.template.tinyos.app.protocol.MyMessageAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handle the Application flow and its modules.
 * Used and configured by provided runners:
 *  - RunnerConsole
 *  - RunnerGui
 * 
 * All layers (until TinyOS internals) are event-driven,
 * the most important entry points are:
 *  - the configuration steps (initialization, finalization)
 *  - close (shutdown) hanlder
 * 
 * 
 * @author  Alessandro Polo <contact@alessandropolo.name>
 * @version 1.0.0
 * @date    2011-09-19
 */
public class TinyApp {

    public static final Logger logger = Logger.getLogger("TinyApp");

    protected final TinyMessageIO tinyIO = new TinyMessageIO(logger);

    protected final MyMessageAdapter tinyMessageAdapter = new MyMessageAdapter();

    protected final MessageIdealChannel wsnOutChannel = new MessageIdealChannel();

    protected final MessageIdealChannel wsnInChannel = new MessageIdealChannel();

    protected NetworkIO networkChannel = null;

    public TinyApp() {
        tinyIO.setMessageAdapter(tinyMessageAdapter);
        wsnOutChannel.addMessageListener(tinyIO);
        tinyIO.addMessageListener(wsnInChannel);
    }

    public void setupTinyOS(String tinyPort, String tinyString) {
        if (tinyIO.isOpened()) {
            tinyIO.close();
        }
        if (tinyString != null) tinyIO.forcePhoenixString(tinyString); else tinyIO.getPhoenixHandler().getParameters().port_id = tinyPort;
        logger.log(Level.FINE, "TinyOS Configuration:\n{0}", tinyIO.getPhoenixHandler().getParameters().dump());
        tinyIO.open();
    }

    public void setupAsNetworkServer(int port) {
        boolean link = false;
        if (networkChannel == null) {
            logger.log(Level.FINEST, "Creating Network channel..");
            networkChannel = new NetworkIO();
            link = true;
        } else if (networkChannel.isOpened()) {
            logger.log(Level.FINEST, "Closing Network channel..");
            networkChannel.close();
        }
        logger.log(Level.FINEST, "Opening Network channel (server: {0})..", port);
        networkChannel.setBindPort(port);
        networkChannel.open();
        if (!networkChannel.isOpened()) throw new RuntimeException("Server Binding Failed! " + port);
        if (link) {
            networkChannel.addMessageListener(wsnOutChannel);
            wsnInChannel.addMessageListener(networkChannel);
        }
    }

    public void setupAsNetworkClient(String address, int port) {
        if (networkChannel.isOpened()) {
            logger.log(Level.FINEST, "Closing Network channel..");
            networkChannel.close();
        }
        logger.log(Level.FINEST, "Opening Network channel (client: {0} {1})..", new Object[] { address, port });
        networkChannel.setConnectAddress(address, port);
        networkChannel.open();
        if (!networkChannel.isOpened()) throw new RuntimeException("Connection Failed! " + address + " at " + port);
    }

    public void closeApplication() {
        TinyApp.logger.info("Closing application..");
        if (networkChannel != null) {
            networkChannel.close();
        }
        tinyIO.close();
    }

    public MessageIdealChannel getWsnOutgoingChannel() {
        return wsnOutChannel;
    }

    public MessageIdealChannel getWsnIncomingChannel() {
        return wsnInChannel;
    }

    public TinyMessageIO getTinyIO() {
        return tinyIO;
    }

    public MyMessageAdapter getTinyMessageAdapter() {
        return tinyMessageAdapter;
    }

    public NetworkIO getNetworkChannel() {
        return networkChannel;
    }

    public void addSink(MessageListener sink) {
        wsnInChannel.addMessageListener(sink);
    }

    public void addSource(MessageSource source) {
        source.addMessageListener(wsnOutChannel);
    }
}
