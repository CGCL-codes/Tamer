package hotgammon.domain;

/** This interface defines the Observer role in the observer pattern where
 *  the Subject role is the Game role.
 * 
 * Author Henrik B�rbak Christensen 
 */
public interface GameListener {

    /** This method is invoked whenever a move has been made on the board */
    public void boardChange();

    /** This method is invoked whenever the dice have been rolled */
    public void diceRolled();
}
