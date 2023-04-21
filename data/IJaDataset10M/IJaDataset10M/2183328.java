package web.servlets;

import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.scotlandyard.engine.Game;
import org.scotlandyard.engine.GameException;
import org.scotlandyard.engine.Player;
import org.scotlandyard.engine.json.GamePlayersJsonContainer;
import org.scotlandyard.engine.json.JsonPlayer;
import org.scotlandyard.impl.engine.Detective;
import org.scotlandyard.impl.engine.GameEngine;

/**
 * TODO add a description for this class
 *
 *
 * @author Hussain Al-Mutawa
 * @version 2.0
 * @since Sun Sep 23, 2011
 */
public class GamePlayers extends AbstractServlet implements IRequestProcessor {

    private Game game;

    @Override
    public Object getOutput(final HttpServletRequest request, final GameEngine engine) throws GameException {
        final GamePlayersJsonContainer gpjc = new GamePlayersJsonContainer();
        if (game == null) {
            throw new GameException("game is null");
        }
        if (game.getMrX() != null) {
            gpjc.mrx = getJsonPlayer(game.getMrX(), game);
        }
        gpjc.detectives = new ArrayList<JsonPlayer>();
        for (Detective d : game.getDetectives()) {
            gpjc.detectives.add(getJsonPlayer(d, game));
        }
        return (gpjc.toJson());
    }

    public JsonPlayer getJsonPlayer(Player player, Game game) throws GameException {
        JsonPlayer jsonPlayer = new JsonPlayer();
        if (player == null) {
            throw new GameException("player can not be null");
        }
        if (game == null) {
            throw new GameException("game can not be null");
        }
        if (game.hasPlayer(player.getEmail()) == false) {
            throw new GameException("this player is not a memeber of the supplied game");
        }
        if (player.getPosition(game.getIdentifier()) == null) {
            throw new GameException("this player position is unnkown");
        }
        jsonPlayer.name = player.getName();
        jsonPlayer.email = player.getEmail();
        jsonPlayer.position = player.getPosition(game.getIdentifier());
        jsonPlayer.hasTurn = player.getEmail().equals(game.getWhoIsTurnPlayerEmail());
        return jsonPlayer;
    }

    @Override
    public void validateRequest(final HttpServletRequest request, final GameEngine engine) throws GameException {
        final String gameId = request.getParameter("gameId");
        if (gameId == null || "".equals(gameId.trim())) {
            throw new GameException("Game id can not be null");
        }
        game = GameEngine.instance().getLobby().getGame(gameId);
        if (game == null) {
            throw new GameException("Game [" + gameId + "] can not be found");
        }
    }
}
