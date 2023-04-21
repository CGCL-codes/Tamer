package scotlandyard.servlets.players;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import scotlandyard.engine.impl.Engine;
import scotlandyard.engine.spec.IGame;
import scotlandyard.engine.spec.IPlayer;

/**
 * gets the legal moves that a player can do from his current position, based on the number of tickets and game rules
 * @author Hussain Al-Mutawa
 * @version 3.0
 */
public class legal_moves_json extends HttpServlet {

    private static final long serialVersionUID = 1234234L;

    private String gameId, xhash;

    public legal_moves_json() {
        super();
    }

    public legal_moves_json(String gameId, String xhash) {
        super();
        this.gameId = gameId;
        this.xhash = xhash;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final PrintWriter out = response.getWriter();
        response.setHeader("Content-Type", "text/plain");
        try {
            if (request.getParameter("xhash") != null) {
                xhash = request.getParameter("xhash");
            }
            if (xhash == null || "".equals(xhash)) {
                throw new Exception("Unknown HASH");
            }
            if (request.getParameter("selected_game") != null) {
                gameId = request.getParameter("selected_game");
            }
            if (gameId == null || "".equals(gameId)) {
                throw new Exception("Game Id is unknown");
            }
            final IGame game = Engine.instance().games.get(gameId);
            if (game == null) {
                throw new Exception("Game is unknown");
            }
            final IPlayer player = game.getPlayer(xhash);
            if (player == null) {
                throw new Exception("player is unknown");
            }
            final String moves = Arrays.toString(game.getLegalMoves(player.getEmail())).replace("{", "[").replace("}", "]").replace("null", "0");
            final IPlayer mrx = game.getMrX();
            if (mrx == null) {
                throw new Exception("MR X Is not there");
            }
            String position = "-1";
            if (game.isRoundExposingMrX(game.getRound())) {
                position = mrx.getPosition() + "";
            }
            out.print("{\"msg\" : " + moves + ", \"mrx\" : { \"position\" : " + position + ", \"face\" : \"" + mrx.getIcon() + "\", \"name\" : \"" + mrx.getName() + "\"} }");
        } catch (Exception e) {
            out.print("{\"msg\" : \"EXCEPTION : " + (e.getMessage() + "").replace("\"", "'") + "\", \"className\" : \"" + getClass().getName() + "\"}");
        }
    }
}
