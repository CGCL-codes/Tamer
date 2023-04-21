package ch.squix.net.nataware.common;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;

public abstract class AbstractMessageHandler extends IoHandlerAdapter implements MessageHandler {

    private ArrayList<Long> knownSessions = new ArrayList<Long>();

    private static Logger logger = Logger.getLogger(AbstractMessageHandler.class);

    public abstract void messageReceived(IoSession arg0, Object arg1) throws Exception;

    protected boolean isKnownSession(AbstractMessage message) {
        return knownSessions.contains(message.getSessionID());
    }

    protected void addToKnownSessions(AbstractMessage message) {
        if (!isKnownSession(message)) {
            knownSessions.add(message.getSessionID());
        } else {
            logger.warn("This session is already known. Not added twice");
        }
    }
}
