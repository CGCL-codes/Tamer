package nz.ac.massey.softwarec.group3.reverseAJAX;

import javax.servlet.http.HttpSession;
import nz.ac.massey.softwarec.group3.game.Game;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContextFactory;

/**
 * ScriptSessionManager - Class which manages all the ReverseAJAX ScriptSessions.
 * @version 1.0 Release
 * @since 1.0
 * @authors Natalie Eustace | Wanting Huang | Paul Smith | Craig Spence
 */
public class ScriptSessionManager implements ScriptSessionManagerInterface {

    /**
     * Sets the script session for the game.
     */
    @Override
    public void setScriptSessionGame() {
        final ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
        final HttpSession session = WebContextFactory.get().getSession();
        final Game game = (Game) session.getAttribute("game");
        final String email = (String) session.getAttribute("email");
        scriptSession.setAttribute("game", game);
        scriptSession.setAttribute("email", email);
    }
}
