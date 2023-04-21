package civquest.swing.game;

import civquest.group.Group;
import civquest.group.gameChange.UnmarkAllFields;
import swifu.main.AbstractFunctionAction;
import swifu.main.FunctionActionEvent;

/**
 * FunctionAction for unmarking all fields.
 */
public class UnmarkAllFieldsAction extends AbstractFunctionAction {

    private Group group;

    public UnmarkAllFieldsAction(Group group) {
        this.group = group;
    }

    public void actionPerformed(FunctionActionEvent e) {
        UnmarkAllFields unmark = new UnmarkAllFields(this.group, this.group.getGameData());
        unmark.execute();
    }
}
