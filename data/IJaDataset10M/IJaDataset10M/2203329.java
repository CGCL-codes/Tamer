package com.vmix.simplemq.daemon.handlers;

import org.apache.log4j.Logger;
import com.vmix.simplemq.daemon.*;
import com.vmix.simplemq.daemon.config.QueueConfig;
import com.vmix.simplemq.daemon.status.QueueInfo;

public class PullQueue extends PersistentQueue implements MessageProvider {

    private static final int PEEK_WAIT = 5000;

    private static Logger logger;

    private long peekId = 0;

    private Message peekMessage = null;

    private long peekTime = 0;

    public PullQueue(QueueConfig config) {
        super(config);
        if (logger == null) {
            logger = Logger.getLogger(getClass());
        }
    }

    public QueueInfo getHandlerStatus() {
        QueueInfo info = super.getHandlerStatus();
        return info;
    }

    public void accept(Message message) throws MessageIngestionException {
        logger.info("storing pull message (" + message.type() + ") '" + config.name + "'");
        super.accept(message);
    }

    public Message peek() throws MessageProviderBusyException {
        if (peekMessage != null) {
            if (System.currentTimeMillis() > peekTime + PEEK_WAIT) {
                logger.debug("resetting '" + config.name + "' head");
                peekTime = 0;
                peekMessage = null;
            } else {
                throw new MessageProviderBusyException();
            }
        }
        while (true) {
            synchronized (this) {
                Long nextId = queue.peek();
                if (nextId == null) {
                    return null;
                }
                peekId = nextId;
                try {
                    peekMessage = retrieveMessage(peekId);
                } catch (MessagePersistenceException e) {
                    logger.debug("message with id " + peekId + " couldn't be deserialized.. discarding and trying next");
                    queue.remove();
                    continue;
                }
            }
            peekTime = System.currentTimeMillis();
            return peekMessage;
        }
    }

    public Message remove(Guid messageGuid) throws MessageGoneException, InvalidMessageGuidException, IllegalStateException {
        synchronized (this) {
            if (peekMessage == null) {
                throw new MessageGoneException();
            } else if (!peekMessage.guid().equals(messageGuid)) {
                throw new InvalidMessageGuidException();
            } else if (peekId != queue.peek()) {
                throw new IllegalStateException();
            }
            Message message = peekMessage;
            deleteMessage(peekId);
            queue.remove();
            peekMessage = null;
            peekTime = 0;
            return message;
        }
    }
}
