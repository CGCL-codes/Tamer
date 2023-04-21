package org.vexi.tpgame;

import java.io.IOException;
import org.vexi.framework.DummyUserStore;
import org.vexi.framework.GenericSession;
import org.vexi.framework.GenericUser;
import org.vexi.framework.IUser;
import org.vexi.framework.IUserStore;

public class DummyTPGameServer extends TPGameServer {

    public DummyTPGameServer() throws IOException, ClassNotFoundException {
        super(new DummyUserStore(), 100);
    }

    @Override
    protected TPGame createGame(TPGameSession sesh) {
        return new DummyTPGame(sesh);
    }

    @Override
    protected GenericSession _createSession(IUser user) {
        return new TPGameSession(user);
    }

    @Override
    protected IUser _createUser(String username, String password) {
        return new GenericUser(username, password);
    }
}
