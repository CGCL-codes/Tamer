package megamek.common.event;

/**
 * Instances of this class are sent when game board changed - added/removed
 * minefield and so on
 * 
 * @see GameListener
 */
public class GameBoardChangeEvent extends GameEvent {

    /**
     * 
     */
    private static final long serialVersionUID = -6307225739747874155L;

    /**
     * @param source event source
     */
    public GameBoardChangeEvent(Object source) {
        super(source, GAME_BOARD_CHANGE);
    }
}
