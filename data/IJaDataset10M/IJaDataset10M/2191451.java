package cz.muni.fi.rum.sender.command;

import cz.muni.fi.rum.sender.Factory;
import cz.muni.fi.rum.sender.senders.MessageSender;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pmikulasek
 */
public class Request implements RapRequest {

    private final Long messageId;

    private final String value;

    private MessageSender sender;

    private static final Logger logger = LoggerFactory.getLogger(Request.class);

    public Request(String value) {
        if (value == null) {
            throw new NullPointerException("request value");
        }
        messageId = counter.getAndIncrement();
        this.value = value;
        this.sender = Factory.getSenderFactory().getPresentMessageSender();
    }

    @Override
    public void execute() {
        try {
            Factory.getHistoryManager().add(this, Factory.getCommandFactory().createResponse(sender.send(value)));
        } catch (IOException ex) {
            logger.error("Nepodarilo se odeslat Request.", ex);
        } finally {
            this.sender = null;
        }
    }

    @Override
    public Long getMessageId() {
        return messageId;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Request ID : ");
        buf.append(messageId);
        buf.append(", Value : ");
        buf.append(value);
        return buf.toString();
    }
}
