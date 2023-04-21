package net.teqlo.bus.messages.user;

import net.teqlo.TeqloException;
import net.teqlo.bus.messages.ConsumerMessage;

public class PutFile extends UserMessage implements ConsumerMessage {

    private String href;

    private byte[] content;

    private boolean external;

    public PutFile(String userFqn, String href, byte[] content, boolean external) {
        super(userFqn);
        this.href = href;
        this.content = content;
        this.external = external;
    }

    public Object consumeMessage() throws TeqloException {
        getUser().putFile(href, content, external);
        return voidReply;
    }
}
