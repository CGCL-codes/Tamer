package cz.muni.fi.rum.sender.command;

import cz.muni.fi.rum.sender.command.parser.RapResponseParser;

/**
 *
 * @author pmikulasek
 */
public class Response implements RapResponse {

    private final Long messageId;

    private final String value;

    public Response(String value) {
        if (value == null) {
            throw new NullPointerException("value value");
        }
        this.messageId = counter.getAndIncrement();
        this.value = value;
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
    public void parse(RapResponseParser parser) {
        parser.parse(value);
    }
}
