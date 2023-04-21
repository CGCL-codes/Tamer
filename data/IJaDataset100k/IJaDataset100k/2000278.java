package scheme4j.gui.actions.interprete;

import javax.swing.Action;
import scheme4j.storage.ResourceLoader;

public class PlayStepAction extends AbstractExecutionAction {

    private static Action instance;

    public static Action getInstance() {
        if (instance == null) instance = new PlayStepAction();
        return instance;
    }

    private PlayStepAction() {
        putValue(Action.NAME, "Play Step");
        putValue(Action.SMALL_ICON, ResourceLoader.getImageIconResource("executestep.gif"));
        putValue(Action.SHORT_DESCRIPTION, "Execute a step");
        putValue(Action.LONG_DESCRIPTION, "Execute a step");
    }
}
