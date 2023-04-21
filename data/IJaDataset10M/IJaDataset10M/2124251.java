package org.osll.tictactoe.transport.corba.server;

import org.osll.tictactoe.engine.GameStorageImpl;
import org.osll.tictactoe.game.Game;
import org.osll.tictactoe.transport.corba.service.ServerConnectionPOA;
import org.osll.tictactoe.transport.corba.service.Team;
import org.osll.tictactoe.transport.corba.Adapter;

public class ServerConnectionServant extends ServerConnectionPOA {

    @Override
    public String connect(String name, Team team) {
        Game g = GameStorageImpl.getInstance().register(name, Adapter.convertTeam(team));
        return g.getTransport().getCorbaService();
    }
}
