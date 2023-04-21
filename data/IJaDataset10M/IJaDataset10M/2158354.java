package up2p.peer.jtella;

import java.io.IOException;
import org.apache.log4j.Logger;
import com.kenmccrary.jtella.NewConnectionEvent;
import com.kenmccrary.jtella.NewConnectionListener;
import com.kenmccrary.jtella.SearchMessage;

public class ConnectionReceiver implements NewConnectionListener {

    /** Name of Logger used by this adapter. */
    public static final String LOGGER = "up2p.peer.jtella.ConnectionReceiver";

    /** Logger used by this adapter. */
    private static Logger LOG = Logger.getLogger(LOGGER);

    public ConnectionReceiver() {
        JTellaAdapter.getConnection().getIncomingConnectionManager().addNewConnectionListener(this);
        JTellaAdapter.getConnection().getOutgoingConnectionManager().addNewConnectionListener(this);
    }

    public void onNewConnection(NewConnectionEvent e) {
        LOG.info("Received new connection from: " + e.getConnection().getConnectedServant());
        try {
            LOG.info("Sending a discover message to " + e.getConnection().getConnectedServant());
            e.getConnection().sendAndReceive(new SearchMessage("discover", 0), JTellaAdapter.getInstance());
        } catch (IOException e1) {
            LOG.debug("IOException in ConnectionReceiver: " + e1.getMessage());
        }
    }
}
