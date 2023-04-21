package net.teqlo.bus.messages.user;

import net.teqlo.TeqloException;
import net.teqlo.bus.messages.ConsumerMessage;

public class DeactivateSource extends UserMessage implements ConsumerMessage {

    private String href;

    private boolean fussy;

    public DeactivateSource(String userFqn, String href, boolean fussy) {
        super(userFqn);
        this.href = href;
        this.fussy = fussy;
    }

    public Object consumeMessage() throws TeqloException {
        getUser().deactivateSource(href, fussy);
        return voidReply;
    }
}
