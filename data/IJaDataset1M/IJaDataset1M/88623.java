package net.teqlo.bus.messages.user;

import net.teqlo.TeqloException;
import net.teqlo.bus.messages.ConsumerMessage;

public class CopySourceAndActivate extends UserMessage implements ConsumerMessage {

    private String sourceHref;

    private String targetHref;

    public CopySourceAndActivate(String userFqn, String sourceHref, String targetHref) {
        super(userFqn);
        this.sourceHref = sourceHref;
        this.targetHref = targetHref;
    }

    public Object consumeMessage() throws TeqloException {
        getUser().copySourceAndActivate(sourceHref, targetHref);
        return voidReply;
    }
}
