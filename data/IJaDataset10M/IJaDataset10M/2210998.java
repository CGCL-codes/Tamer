package net.kano.joustsim.oscar.oscar.service.icbm;

public class SimpleMessage implements Message {

    private final String messageBody;

    private final boolean autoResponse;

    protected SimpleMessage() {
        messageBody = null;
        autoResponse = false;
    }

    public SimpleMessage(String messageBody) {
        this(messageBody, false);
    }

    public SimpleMessage(boolean autoresponse) {
        this(null, autoresponse);
    }

    public SimpleMessage(String messageBody, boolean autoResponse) {
        this.messageBody = messageBody;
        this.autoResponse = autoResponse;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public boolean isAutoResponse() {
        return autoResponse;
    }
}
