package org.netbeans.cubeon.gcode.tasks.actions;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.netbeans.cubeon.gcode.tasks.GCodeTask;
import org.netbeans.cubeon.tasks.spi.task.TaskElement;
import org.netbeans.cubeon.tasks.spi.task.TaskElementActionsProvider;

/**
 *
 * @author Anuradha
 */
public class GCodeTaskActionsProviderImpl implements TaskElementActionsProvider {

    public Action[] getActions(TaskElement element) {
        List<Action> actions = new ArrayList<Action>();
        GCodeTask gCodeTask = element.getLookup().lookup(GCodeTask.class);
        if (gCodeTask != null) {
            actions.add(null);
            actions.add(new SubmitTaskAction(gCodeTask));
            actions.add(new RevertChangesAction(gCodeTask));
            actions.add(null);
        }
        return actions.toArray(new Action[actions.size()]);
    }
}
