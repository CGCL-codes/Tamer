package scrum.client.sprint;

import java.util.*;

public abstract class GReopenTaskAction extends scrum.client.common.AScrumAction {

    protected scrum.client.sprint.Task task;

    public GReopenTaskAction(scrum.client.sprint.Task task) {
        this.task = task;
    }

    @Override
    public boolean isExecutable() {
        return true;
    }

    @Override
    public String getId() {
        return ilarkesto.core.base.Str.getSimpleName(getClass()) + '_' + ilarkesto.core.base.Str.toHtmlId(task);
    }
}
