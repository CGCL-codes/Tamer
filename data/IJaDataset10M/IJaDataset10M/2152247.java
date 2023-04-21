package starcraft.gamemodel.remoteservices;

import java.util.List;
import starcraft.gamemodel.GameException;
import starcraft.gamemodel.GameOverview;
import starcraft.gamemodel.references.GameRef;

public interface AccountAndGameManagement {

    /**
	 * Creates an account with the given parameters. If there is already an account
	 * with the given user name an GameException is thrown.
	 */
    public void createAccount(String username, String password, String displayName) throws GameException;

    /**
	 * Returns a clientID that is used to identify objects that belong to one
	 * client such as a Profile or are connected with a client. Similar to a
	 * session ID. This provides the possibility for a reconnect after a
	 * connection failure.
	 */
    public String login(String username, String password) throws GameException;

    public void logout(String clientID);

    /**
	 * Creates a game with the given name. Returns the new gameID.
	 */
    public GameRef createGame(String clientID, String gameName) throws GameException;

    public void createGame(String clientID, GameRef game, String gameName) throws GameException;

    /**
	 * A client that is part of the game can close the game. This clears all game state.
	 */
    public void closeGame(String clientID, GameRef game) throws GameException;

    public List<GameOverview> getGames();

    public List<GameOverview> getMyGames(String clientID);
}
