package org.netbeans.cubeon.gcode.repository;

import java.util.ArrayList;
import java.util.List;
import org.netbeans.cubeon.gcode.tasks.GCodeTask;
import org.netbeans.cubeon.tasks.spi.task.TaskElement;
import org.netbeans.cubeon.tasks.spi.task.TaskPriority;
import org.netbeans.cubeon.tasks.spi.repository.TaskPriorityProvider;

/**
 *
 * @author Anuradha G
 */
public class GCodeTaskPriorityProvider implements TaskPriorityProvider {

    private List<TaskPriority> prioritys = new ArrayList<TaskPriority>();

    public GCodeTaskPriorityProvider() {
    }

    public List<TaskPriority> getTaskPriorities() {
        return new ArrayList<TaskPriority>(prioritys);
    }

    public TaskPriority getTaskPriorityById(String priority) {
        for (TaskPriority tp : getTaskPriorities()) {
            if (tp.getId().equals(priority)) {
                return tp;
            }
        }
        return null;
    }

    public TaskPriority getPrefredPriority() {
        int size = prioritys.size();
        if (size > 0) {
            return prioritys.get(size / 2);
        }
        return null;
    }

    public void setPriorities(List<TaskPriority> prioritys) {
        this.prioritys = new ArrayList<TaskPriority>(prioritys);
    }

    public TaskPriority getTaskPriority(TaskElement element) {
        GCodeTask codeTask = element.getLookup().lookup(GCodeTask.class);
        assert codeTask != null;
        return codeTask.getPriority();
    }

    public void setTaskPriority(TaskElement element, TaskPriority priority) {
        GCodeTask codeTask = element.getLookup().lookup(GCodeTask.class);
        assert codeTask != null;
        codeTask.setPriority(priority);
    }
}
