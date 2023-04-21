package za.um.td.input.action;

import java.util.logging.Level;
import java.util.logging.Logger;
import za.um.td.state.BasicGameStateSingleton;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;

public class ExitLevelAction extends InputAction {

    private static final Logger LOG = Logger.getLogger(ExitLevelAction.class.getName());

    public void performAction(InputActionEvent evt) {
        BasicGameStateSingleton.game.shutdown();
    }
}
