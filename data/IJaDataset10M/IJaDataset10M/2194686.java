package tjger.game;

/**
 * A move that can return if this is just a part of a move or if the move is completed.
 * 
 * @author hagru
 */
public interface SplitableMove extends MoveInformation {

    /**
     * @return True if the move is complete, false if this ist just a part of a move.
     */
    public boolean isMoveComplete();
}
