package megamek.common.event;

import megamek.common.IGame;

/**
 * Instances of this class are sent when Game phase changes
 */
public class GamePhaseChangeEvent extends GameEvent {

    /**
     * 
     */
    private static final long serialVersionUID = 5589252062756476819L;

    /**
     * Old phase
     */
    private IGame.Phase oldPhase;

    /**
     * new phase
     */
    private IGame.Phase newPhase;

    /**
     * Constructs new <code>GamePhaseChangeEvent</code>
     * 
     * @param source Event source
     * @param oldPhase
     * @param newPhase
     */
    public GamePhaseChangeEvent(Object source, IGame.Phase oldPhase, IGame.Phase newPhase) {
        super(source, GAME_PHASE_CHANGE);
        this.oldPhase = oldPhase;
        this.newPhase = newPhase;
    }

    /**
     * Returns the newPhase.
     * 
     * @return the newPhase.
     */
    public IGame.Phase getNewPhase() {
        return newPhase;
    }

    /**
     * Returns the oldPhase.
     * 
     * @return the oldPhase.
     */
    public IGame.Phase getOldPhase() {
        return oldPhase;
    }
}
