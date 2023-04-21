package com.rarebrick.tools.dao.http;

import java.util.concurrent.Callable;
import com.rarebrick.tools.model.ClientUpdate;

public abstract class ClientCallable implements Callable<ClientUpdate> {

    private final ClientUpdate clientUpdate;

    public ClientCallable(ClientUpdate client) {
        this.clientUpdate = client;
    }

    public ClientUpdate getClientUpdate() {
        return clientUpdate;
    }
}
