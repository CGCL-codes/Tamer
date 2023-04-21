package civquest.swing.game;

import civquest.group.Group;
import civquest.group.gameChange.UnmarkAllUnits;
import swifu.main.AbstractFunctionAction;
import swifu.main.FunctionActionEvent;

/**
 * FunctionAction for unmarking all units.
 */
public class UnmarkAllUnitsAction extends AbstractFunctionAction {

    private Group group;

    public UnmarkAllUnitsAction(Group group) {
        this.group = group;
    }

    public void actionPerformed(FunctionActionEvent e) {
        UnmarkAllUnits unmark = new UnmarkAllUnits(this.group, this.group.getGameData());
        unmark.execute();
    }
}
