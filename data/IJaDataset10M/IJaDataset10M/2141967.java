package gameserver.network.loginserver;

import gameserver.network.factories.LsPacketHandlerFactory;
import gameserver.network.loginserver.serverpackets.SM_GS_AUTH;
import gameserver.utils.ThreadPoolManager;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Deque;
import org.apache.log4j.Logger;
import commons.network.AConnection;
import commons.network.Dispatcher;

/**
 * Object representing connection between LoginServer and GameServer.
 */
public class LoginServerConnection extends AConnection {

    /**
	* Logger for this class.
	*/
    private static final Logger log = Logger.getLogger(LoginServerConnection.class);

    /**
	* Possible states of GsConnection
	*/
    public static enum State {

        /**
		* game server just connect
		*/
        CONNECTED, /**
		* game server is authenticated
		*/
        AUTHED
    }

    /**
	* Server Packet "to send" Queue
	*/
    private final Deque<LsServerPacket> sendMsgQueue = new ArrayDeque<LsServerPacket>();

    /**
	* Current state of this connection
	*/
    private State state;

    private LsPacketHandler lsPacketHandler;

    /**
	* Constructor.
	* 
	* @param sc
	* @param d
	* @throws IOException
	*/
    public LoginServerConnection(SocketChannel sc, Dispatcher d) throws IOException {
        super(sc, d);
        LsPacketHandlerFactory lsPacketHandlerFactory = LsPacketHandlerFactory.getInstance();
        this.lsPacketHandler = lsPacketHandlerFactory.getPacketHandler();
        state = State.CONNECTED;
        log.info("Connected to LoginServer!");
        this.sendPacket(new SM_GS_AUTH());
    }

    /**
	* Called by Dispatcher. ByteBuffer data contains one packet that should be processed.
	* 
	* @param data
	* @return True if data was processed correctly, False if some error occurred and connection should be closed NOW.
	*/
    @Override
    public boolean processData(ByteBuffer data) {
        LsClientPacket pck = lsPacketHandler.handle(data, this);
        log.info("recived packet: " + pck);
        if (pck != null && pck.read()) ThreadPoolManager.getInstance().executeLsPacket(pck);
        return true;
    }

    /**
	* This method will be called by Dispatcher, and will be repeated till return false.
	* 
	* @param data
	* @return True if data was written to buffer, False indicating that there are not any more data to write.
	*/
    @Override
    protected final boolean writeData(ByteBuffer data) {
        synchronized (guard) {
            LsServerPacket packet = sendMsgQueue.pollFirst();
            if (packet == null) return false;
            packet.write(this, data);
            return true;
        }
    }

    /**
	* This method is called by Dispatcher when connection is ready to be closed.
	* 
	* @return time in ms after witch onDisconnect() method will be called. Always return 0.
	*/
    @Override
    protected final long getDisconnectionDelay() {
        return 0;
    }

    /**
	* {@inheritDoc}
	*/
    @Override
    protected final void onDisconnect() {
        LoginServer.getInstance().loginServerDown();
    }

    /**
	* {@inheritDoc}
	*/
    @Override
    protected final void onServerClose() {
        close(true);
    }

    /**
	* Sends GsServerPacket to this client.
	* 
	* @param bp
	*            GsServerPacket to be sent.
	*/
    public final void sendPacket(LsServerPacket bp) {
        synchronized (guard) {
            if (isWriteDisabled()) return;
            log.info("sending packet: " + bp);
            sendMsgQueue.addLast(bp);
            enableWriteInterest();
        }
    }

    /**
	* Its guaranted that closePacket will be sent before closing connection, but all past and future packets wont.
	* Connection will be closed [by Dispatcher Thread], and onDisconnect() method will be called to clear all other
	* things. forced means that server shouldn't wait with removing this connection.
	* 
	* @param closePacket
	*            Packet that will be send before closing.
	* @param forced
	*            have no effect in this implementation.
	*/
    public final void close(LsServerPacket closePacket, boolean forced) {
        synchronized (guard) {
            if (isWriteDisabled()) return;
            log.info("sending packet: " + closePacket + " and closing connection after that.");
            pendingClose = true;
            isForcedClosing = forced;
            sendMsgQueue.clear();
            sendMsgQueue.addLast(closePacket);
            enableWriteInterest();
        }
    }

    /**
	* @return Current state of this connection.
	*/
    public State getState() {
        return state;
    }

    /**
	* @param state
	*            Set current state of this connection.
	*/
    public void setState(State state) {
        this.state = state;
    }

    /**
	* @return String info about this connection
	*/
    @Override
    public String toString() {
        return "LoginServer " + getIP();
    }
}
