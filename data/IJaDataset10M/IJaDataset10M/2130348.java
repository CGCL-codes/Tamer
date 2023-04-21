package net.teqlo.bus.messages.user;

import net.teqlo.TeqloException;
import net.teqlo.bus.messages.ConsumerMessage;

public class RemoveUser extends UserMessage implements ConsumerMessage {

    private String href;

    public RemoveUser(String userFqn, String href) {
        super(userFqn);
        this.href = href;
    }

    public Object consumeMessage() throws TeqloException {
        getUser().removeUser(href);
        return voidReply;
    }
}
