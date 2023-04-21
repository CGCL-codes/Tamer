package net.teqlo.bus.messages.user;

import net.teqlo.TeqloException;
import net.teqlo.bus.messages.ConsumerMessage;

public class ExistsSource extends UserMessage implements ConsumerMessage {

    private String href;

    public ExistsSource(String userFqn, String href) {
        super(userFqn);
        this.href = href;
    }

    public Object consumeMessage() throws TeqloException {
        return new Boolean(getUser().existsSource(href));
    }
}
