package net.teqlo.bus.messages.user;

import net.teqlo.TeqloException;
import net.teqlo.bus.messages.ConsumerMessage;

public class ListFolder extends UserMessage implements ConsumerMessage {

    private String href;

    public ListFolder(String userFqn, String href) {
        super(userFqn);
        this.href = href;
    }

    public Object consumeMessage() throws TeqloException {
        return getUser().listFolder(href);
    }
}
