package org.robocup.msl.refbox.communication;

import junit.framework.TestCase;
import org.robocup.msl.refbox.GameControl;
import org.robocup.msl.refbox.constants.ConnectionChangeReason;
import org.robocup.msl.refbox.constants.GameEvent;
import org.robocup.msl.refbox.protocol.Protocol2010;

public class TestCaseConnections extends TestCase {

    public void testMultiCastHandler() {
        final GameControl gameControl = new GameControl();
        final MultiCastSendConnectionHandler handler2010 = new MultiCastSendConnectionHandler(new Protocol2010(gameControl), 30000, "230.0.0.1");
        handler2010.addNumberOfConnectionsChangedListener(null);
        handler2010.removeNumberOfConnectionsChangedListener(null);
        handler2010.sendMessage(GameEvent.CANCEL, null);
        handler2010.sendConnectionChangedEvent(null, ConnectionChangeReason.NEW_CONNECTION);
        handler2010.sendConnectionChangedEvent(null, ConnectionChangeReason.CONNECTION_LOST);
        handler2010.sendConnectionChangedEvent(null, ConnectionChangeReason.RECONNECTED);
        handler2010.run();
        handler2010.dispose();
    }
}
