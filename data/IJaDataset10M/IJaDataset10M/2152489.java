package org.osll.tictactoe.engine;

import java.util.ArrayList;
import org.osll.tictactoe.Team;
import org.osll.tictactoe.game.Game;
import org.osll.tictactoe.game.GameStorage;

public class GameStorageImpl implements GameStorage {

    private static GameStorageImpl instance = null;

    private ArrayList<Game> list = new ArrayList<Game>();

    private GameStorageImpl() {
    }

    public static GameStorage getInstance() {
        if (instance == null) instance = new GameStorageImpl();
        return instance;
    }

    @Override
    public synchronized void addGame(Game game) {
        list.add(game);
    }

    @Override
    public synchronized Game register(String name, Team team) {
        for (Game it : list) {
            if (it.isAvail(team)) {
                it.addPlayer(name, team);
                return it;
            }
        }
        GameImpl game = new GameImpl(30, 30, 300000);
        game.addPlayer(name, team);
        list.add(game);
        return game;
    }
}
