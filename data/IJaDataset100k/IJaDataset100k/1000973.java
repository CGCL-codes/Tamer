package py.edu.ucom.cadira.game.war;

/**
 * 
 * @author WaterlooTeam
 *
 */
public class GameLogicException extends GameException {

    /**
     * 
     */
    private static final long serialVersionUID = -3061737061732066408L;

    public GameLogicException(String msg, Exception e) {
        super(msg, e);
    }
}
