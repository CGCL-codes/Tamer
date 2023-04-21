package net.xelnaga.screplay.actions;

import net.xelnaga.screplay.interfaces.Player;
import net.xelnaga.screplay.types.ActionType;

/**
 * Represents an ally action.
 *
 * @author Russell Wilson
 *
 */
public class AllyAction extends AbstractAction {

    private static final ActionType ACTION_TYPE = ActionType.ALLY;

    /**
     * Constructs an <code>AllyAction</code>.
     *
     * @param sequence the sequence number.
     * @param frame the frame number.
     * @param player the player.
     */
    public AllyAction(int sequence, int frame, Player player) {
        super(sequence, frame, player);
    }

    public ActionType getActionType() {
        return ACTION_TYPE;
    }
}
