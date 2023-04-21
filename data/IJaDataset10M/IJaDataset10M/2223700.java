package net.teqlo.bus.messages.database;

import net.teqlo.TeqloException;

public class GetFile extends DatabaseMessage {

    private String href;

    private boolean external;

    public GetFile(String href, boolean external) {
        this.href = href;
        this.external = external;
    }

    public Object consumeMessage() throws TeqloException {
        return getDb().getFile(href, external);
    }
}
