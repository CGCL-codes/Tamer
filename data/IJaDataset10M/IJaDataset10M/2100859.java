package egs.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import egs.games.CheckersGame;
import egs.games.Game;
import egs.games.TicTacToeGame;
import egs.packets.Packet;

public class GameManager {

    public class GameKey {

        private short gameName;

        private short gameVersion;

        public GameKey(short gameName, short gameVersion) {
            this.gameName = gameName;
            this.gameVersion = gameVersion;
        }

        public short getGameName() {
            return gameName;
        }

        public void setGameName(short gameName) {
            this.gameName = gameName;
        }

        public short getGameVersion() {
            return gameVersion;
        }

        public void setGameVersion(short gameVersion) {
            this.gameVersion = gameVersion;
        }

        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof GameKey)) return false;
            GameKey key = (GameKey) obj;
            return (gameName == key.gameName && gameVersion == key.gameVersion);
        }

        public int hashCode() {
            int hash = 1;
            hash = hash * 31 + Short.valueOf(gameName).hashCode();
            hash = hash * 31 + Short.valueOf(gameVersion).hashCode();
            return hash;
        }
    }

    private final List<Short> knownGames;

    private final List<GameKey> supportedGames;

    private Map<GameKey, ClientManager> clientManagerMap;

    public GameManager() {
        knownGames = new ArrayList<Short>();
        knownGames.add(Packet.GAME_NAME_CHECKERS);
        knownGames.add(Packet.GAME_NAME_BLACKJACK);
        knownGames.add(Packet.GAME_NAME_TICTACTOE);
        supportedGames = new ArrayList<GameKey>();
        supportedGames.add(new GameKey(Packet.GAME_NAME_CHECKERS, (short) 1));
        supportedGames.add(new GameKey(Packet.GAME_NAME_TICTACTOE, (short) 1));
        clientManagerMap = new HashMap<GameKey, ClientManager>();
        for (GameKey key : supportedGames) clientManagerMap.put(key, new ClientManager());
    }

    public GameKey getGameKey(short gameName, short gameVersion) {
        return new GameKey(gameName, gameVersion);
    }

    public synchronized boolean isKnownGame(short gameName, short gameVersion) {
        return isKnownGame(getGameKey(gameName, gameVersion));
    }

    public synchronized boolean isKnownGame(GameKey key) {
        return knownGames.contains(key.getGameName());
    }

    public synchronized boolean isSupportedGame(short gameName, short gameVersion) {
        return isSupportedGame(getGameKey(gameName, gameVersion));
    }

    public synchronized boolean isSupportedGame(GameKey key) {
        return supportedGames.contains(key);
    }

    public Game buildGame(short gameName, short gameVersion) {
        return buildGame(getGameKey(gameName, gameVersion));
    }

    public Game buildGame(GameKey key) {
        Game result = null;
        switch(key.getGameName()) {
            case Packet.GAME_NAME_CHECKERS:
                switch(key.getGameVersion()) {
                    case 1:
                        int board[][] = CheckersGame.getInitGameStateP1Top();
                        result = new CheckersGame(board);
                        break;
                    default:
                        break;
                }
                break;
            case Packet.GAME_NAME_BLACKJACK:
                break;
            case Packet.GAME_NAME_TICTACTOE:
                switch(key.getGameVersion()) {
                    case 1:
                        result = new TicTacToeGame();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return result;
    }

    public synchronized ClientManager getClientManager(short gameName, short gameVersion) {
        return getClientManager(getGameKey(gameName, gameVersion));
    }

    public synchronized ClientManager getClientManager(GameKey key) {
        if (clientManagerMap.containsKey(key)) {
            return clientManagerMap.get(key);
        }
        return null;
    }
}
