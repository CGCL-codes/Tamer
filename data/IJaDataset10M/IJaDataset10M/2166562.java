package org.amse.bomberman.server.gameservice;

import org.amse.bomberman.server.gameservice.impl.Game;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.amse.bomberman.server.gameservice.impl.GameCreator;
import org.amse.bomberman.server.gameservice.listeners.GameChangeListener;

/**
 *
 * @author Kirilchuk V.E.
 */
public class GameStorage implements GameChangeListener {

    private final GameCreator gameCreator = new GameCreator();

    private final List<Game> games = new CopyOnWriteArrayList<Game>();

    private final List<GameStorageListener> listeners = new CopyOnWriteArrayList<GameStorageListener>();

    /**
     * Creates game.
     * @param gameMapName name of gameMap.
     * @param gameName name of game.
     * @param maxPlayers maxPlayers parameter of game.
     * @return created game.
     * @throws FileNotFoundException if gameMap with defined name was not finded.
     * @throws IOException if IO errors occurs while creating gameMap.
     */
    public Game createGame(GamePlayer creator, String gameMapName, String gameName, int maxPlayers) throws FileNotFoundException, IOException {
        Game game = gameCreator.createGame(creator, gameMapName, gameName, maxPlayers);
        game.addGameChangeListener(this);
        addGame(game);
        return game;
    }

    public void addGame(Game gameToAdd) {
        games.add(gameToAdd);
        fireGamesChanged();
    }

    public void removeGame(Game gameToRemove) {
        if (games.remove(gameToRemove)) {
            System.out.println("GameStorage: game removed.");
        } else {
            System.err.println("GameStorage: removeGame warning. No specified game found.");
        }
    }

    public Game getGame(int n) {
        Game game = null;
        try {
            game = games.get(n);
        } catch (IndexOutOfBoundsException ex) {
            System.err.println("GameStorager: getGame warning. Tryed to get game with illegal ID.");
        }
        return game;
    }

    public List<Game> getGamesList() {
        return Collections.unmodifiableList(games);
    }

    public void clearGames() {
        games.clear();
    }

    public void addListener(GameStorageListener listener) {
        listeners.add(listener);
    }

    public void removeListener(GameStorageListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void parametersChanged(Game game) {
        fireGamesChanged();
    }

    @Override
    public void gameStarted(Game game) {
        fireGamesChanged();
    }

    @Override
    public void gameTerminated(Game game) {
        removeGame(game);
        fireGamesChanged();
    }

    @Override
    public void newChatMessage(String message) {
    }

    @Override
    public void fieldChanged() {
    }

    @Override
    public void gameEnded(Game game) {
    }

    @Override
    public void statsChanged(Game game) {
    }

    private void fireGamesChanged() {
        for (GameStorageListener listener : listeners) {
            listener.gamesChanged();
        }
    }
}
