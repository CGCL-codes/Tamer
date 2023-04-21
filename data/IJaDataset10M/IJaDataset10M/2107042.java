package mudstrate.engine;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import mudstrate.init.MudEnvironment;
import mudstrate.util.Util;

/**
 * The <code>Server</code> waits for incoming connections on its specified port <br>
 * and upon accepting new <code>SocketChannels</code>, queues them with the <br>
 * <code>LoginHandler</code> to be processed for login.
 * @author Taylor
 *
 */
public class Server extends Thread {

    private static Server instance = new Server();

    public static synchronized Server getInstance() {
        return instance;
    }

    private Selector connectionSelector;

    private MudEnvironment environment = MudEnvironment.getInstance();

    private boolean keepRunning;

    private final int port = environment.getConnectionPort();

    private ServerSocketChannel ssc;

    /**
		 * Establishes a <code>ServerSocketChannel</code> for the MUD and begins listening for <br>
		 * incoming connections and sending them to be processed by the <code>LoginHandler</code>.
		 * @param port
		 * @param connectionSelector
		 */
    private Server() {
        super("Server");
        boolean success = false;
        try {
            this.ssc = ServerSocketChannel.open();
            this.ssc.configureBlocking(false);
            this.ssc.socket().bind(new InetSocketAddress(this.port));
            this.keepRunning = true;
            this.connectionSelector = Selector.open();
            this.ssc.register(this.connectionSelector, SelectionKey.OP_ACCEPT);
            success = true;
        } catch (IllegalBlockingModeException e) {
            Util.echo("Could not register with connectionSelector: server is configured to block.", this);
            success = false;
        } catch (BindException e) {
            Util.echo("Port " + this.port + " already in use.", this);
            success = false;
        } catch (ClosedChannelException e) {
            Util.echo("The ServerSocketChannel is closed, cannot register with Selector: " + e.getMessage(), this);
            success = false;
        } catch (IOException e) {
            Util.echo("Error in Server construction: " + e.getMessage(), this);
            success = false;
        }
        if (success) {
            Util.echo("Initialization successful.", this);
        } else {
            this.environment.shutdown();
        }
    }

    private void handleIncomingConnections() {
        Iterator<SelectionKey> keyIterator = this.connectionSelector.selectedKeys().iterator();
        while (keyIterator.hasNext()) {
            SelectionKey thisKey = keyIterator.next();
            keyIterator.remove();
            if (thisKey.isAcceptable() && thisKey.isValid()) {
                SelectableChannel serverChannel = thisKey.channel();
                if (serverChannel instanceof ServerSocketChannel) {
                    try {
                        SocketChannel incomingChannel = ((ServerSocketChannel) serverChannel).accept();
                        incomingChannel.configureBlocking(false);
                        Util.echo("Incoming SocketChannel received and configured to be non-blocking. IP: " + incomingChannel.socket().getInetAddress(), this);
                        LoginHandler.getInstance().storeNewChannel(incomingChannel);
                    } catch (IOException e) {
                        Util.echo("Could not accept incoming SocketChannel(" + e.getMessage() + ").", this);
                    }
                }
            }
        }
    }

    public boolean isOnline() {
        return this.keepRunning;
    }

    @Override
    public void run() {
        while (this.keepRunning == true) {
            try {
                Util.echo("Listening for connections on port " + this.port + "...", this);
                this.connectionSelector.select();
                this.handleIncomingConnections();
            } catch (IOException e) {
                Util.echo("Cannot handle connections: " + e.getMessage(), this);
                this.environment.shutdown();
                return;
            }
        }
    }

    public void shutdown() {
        this.keepRunning = false;
        Util.echo("Shutting down...", this);
    }
}
