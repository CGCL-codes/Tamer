package nz.ac.massey.softwarec.group3.actions;

import java.util.List;
import nz.ac.massey.softwarec.group3.game.Game;
import nz.ac.massey.softwarec.group3.game.Move;
import nz.ac.massey.softwarec.group3.game.Player;
import nz.ac.massey.softwarec.group3.session.SessionListener;
import nz.ac.massey.softwarec.group3.session.SessionTracker;

/**
 * DataGetter - Class for the object which gets data from the Server.
 * @version 1.0 Release
 * @since 1.0
 * @authors Natalie Eustace | Wanting Huang | Paul Smith | Craig Spence
 */
public final class DataGetter {

    private DataGetter() {
    }

    /**
     * Adds all game and user information to a JSON string gameJSON, which will be returned to the client.
     * @param game - The Game for which to get the data.
     * @return String gameJSON - JSON string representing the game.
     */
    public static String getGameData(final Game game) {
        final SessionTracker sessionTracker = SessionListener.getSessionTracker();
        final StringBuilder gameJSON = new StringBuilder();
        gameJSON.append("{\"game\" : ");
        gameJSON.append("{\"players\" : [");
        for (Player player : game.getPlayers()) {
            gameJSON.append("{\"email\" : \"").append(player.getPlayerEmail()).append("\",");
            gameJSON.append("\"name\" : \"").append(sessionTracker.getUserNameFromEmail(player.getPlayerEmail())).append("\",");
            gameJSON.append("\"location\" : ").append(player.getCurrentLocation().getStationNumber()).append(",");
            gameJSON.append("\"taxiTickets\" : ").append(player.getTaxiTokens()).append(",");
            gameJSON.append("\"busTickets\" : ").append(player.getBusTokens()).append(",");
            gameJSON.append("\"undergroundTickets\" : ").append(player.getUndergroundTokens()).append(",");
            gameJSON.append("\"mrXTickets\" : ").append(player.getMrXTokens()).append(",");
            gameJSON.append("\"doubleTickets\" : ").append(player.getDoubleMoveTokens()).append(",");
            gameJSON.append("\"playerNumber\" : ").append(game.getPlayers().indexOf(player)).append(",");
            gameJSON.append("\"isMrX\" : ").append(player.isMrX()).append("},");
        }
        if (game.getPlayers().size() > 0) {
            gameJSON.deleteCharAt(gameJSON.length() - 1);
        }
        gameJSON.append("], \"whosTurn\" : ").append(game.getWhosTurn()).append(", ");
        gameJSON.append("\"turnCount\" : ").append(game.getMrX().getMoveNumber()).append(", ");
        gameJSON.append("\"gameCreator\" : \"").append(game.getCreatorEmail()).append("\", ");
        gameJSON.append("\"mrXMoves\" : [");
        for (Move move : game.getMrXMoves()) {
            gameJSON.append("{\"moveType\" : \"").append(move.getTicketType()).append("\"},");
        }
        if (game.getMrXMoves().size() > 0) {
            gameJSON.deleteCharAt(gameJSON.length() - 1);
        }
        gameJSON.append("] } }");
        return gameJSON.toString();
    }

    /**
     * Gets information for lobby and adds to a JSON string lobbyJSON, which will be returned to the client.
     * @return String lobbyJSON - JSON string representing the lobby user data.
     */
    public static String getLobbyData() {
        final SessionTracker sessionTracker = SessionListener.getSessionTracker();
        final List<String> currentlyOnlineUsersEmails = sessionTracker.getListOfUsersWhoAreCurrentlyOnline();
        final StringBuilder lobbyJSON = new StringBuilder();
        lobbyJSON.append("{\"players\": [");
        for (String userEmail : currentlyOnlineUsersEmails) {
            lobbyJSON.append("{\"name\" : \"").append(sessionTracker.getUserNameFromEmail(userEmail)).append("\"},");
        }
        lobbyJSON.deleteCharAt(lobbyJSON.length() - 1);
        lobbyJSON.append("] }");
        return lobbyJSON.toString();
    }

    /**
     * Gets all of the current game information and adds it to a JSON string gamesJSON, which will be returned to the client.
     * @return Sting gamesJSON - JSON string representing the lobby games data.
     */
    public static String getGamesData() {
        final SessionTracker sessionTracker = SessionListener.getSessionTracker();
        final List<Game> currentlyAvailableGames = sessionTracker.getGameTracker().getAvailableGames();
        final StringBuilder gamesJSON = new StringBuilder();
        gamesJSON.append("{\"games\": [");
        for (Game game : currentlyAvailableGames) {
            if (!game.isPlaying()) {
                gamesJSON.append("{\"players\": [");
                for (Player player : game.getPlayers()) {
                    gamesJSON.append("{\"name\": \"").append(sessionTracker.getUserNameFromEmail(player.getPlayerEmail())).append("\"},");
                }
                gamesJSON.deleteCharAt(gamesJSON.length() - 1);
                gamesJSON.append("] , \"creatorName\": \"").append(game.getCreatorName()).append("\",");
                gamesJSON.append("\"creatorEmail\": \"").append(game.getCreatorEmail()).append("\",");
                gamesJSON.append("\"gameID\": \"").append(game.getGameID()).append("\"},");
            }
        }
        if (currentlyAvailableGames.size() > 0) {
            gamesJSON.deleteCharAt(gamesJSON.length() - 1);
        }
        gamesJSON.append("] }");
        return gamesJSON.toString();
    }

    /**
     * Gets the game players information and adds it to a JSON string gamePlayersJSON, which will be returned to the client.
     * @param game - The Game to get player information for.
     * @return String gamePlayersJSON - JSON string representing the players in a game.
     */
    public static String getGamePlayersData(final Game game) {
        final SessionTracker sessionTracker = SessionListener.getSessionTracker();
        final List<Player> gamePlayers = game.getPlayers();
        final StringBuilder gamePlayersJSON = new StringBuilder();
        gamePlayersJSON.append("{\"players\": [");
        for (Player player : gamePlayers) {
            gamePlayersJSON.append("{\"name\" : \"").append(sessionTracker.getUserNameFromEmail(player.getPlayerEmail())).append("\",");
            gamePlayersJSON.append("\"ready\" : ").append(player.isReady()).append("},");
        }
        gamePlayersJSON.deleteCharAt(gamePlayersJSON.length() - 1);
        gamePlayersJSON.append("] }");
        return gamePlayersJSON.toString();
    }

    /**
     * Gets current openTokSession information from a game and adds it to a JSON string openTokJSON, which will be returned to the client.
     * @param game - The Game to get openTok information for.
     * @return String openTokJSON - JSON string representing the openTok information of the game.
     */
    public static String getOpenTokData(final Game game) {
        final StringBuilder openTokJSON = new StringBuilder();
        openTokJSON.append("{\"apiKey\" : \"").append(game.getOpenTokAPIKey()).append("\",");
        openTokJSON.append("\"sessionId\" : \"").append(game.getOpenTokSessionId()).append("\",");
        openTokJSON.append("\"token\" : \"").append(game.getOpenTokIdToken()).append("\"}");
        return openTokJSON.toString();
    }

    /**
     * Gets the already created mapJSON file from a game.
     * @param game - The game to get the mapJSON from.
     * @return MapJSO - JSON string representing the map of the game.
     */
    public static String getMapData(final Game game) {
        return game.getMap().getMapJSON();
    }
}
