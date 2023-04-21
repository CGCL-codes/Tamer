package client.game.state.actions;

import java.util.Hashtable;
import client.game.task.gameState.ChangeStateTask;
import client.game.task.gameState.ChangeToApp;
import client.manager.TaskManager;

public class SpotAction extends AAction {

    public SpotAction(String actionIdentifier, String actionName) {
        super(actionIdentifier, actionName);
    }

    public void performAction(Hashtable<String, Object> configuration) {
        ChangeStateTask task = new ChangeToApp(this.actionName, configuration);
        TaskManager.getInstance().enqueue(task);
    }
}
