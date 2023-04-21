package elapse.domain;

import java.awt.*;
import java.util.Stack;

public class TaskList extends ListWithEvents<Task> {

    private DefaultTasks defaultTasks;

    private Stack<Task> trashedTasks;

    private boolean recycle;

    public TaskList(boolean recycle) {
        defaultTasks = new DefaultTasks();
        trashedTasks = new Stack<Task>();
        this.recycle = recycle;
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public Task getTaskById(String taskIdString) {
        for (Task task : this) {
            if (task.getIdString().equals(taskIdString)) {
                return task;
            }
        }
        return null;
    }

    public Task getTaskByTitle(String title) {
        Task result = null;
        for (Task task : this) {
            if (task.getTitle().equals(title)) {
                result = task;
            }
        }
        return result;
    }

    public Task addNew() {
        Task task;
        if (recycle && !trashedTasks.isEmpty()) {
            task = trashedTasks.pop();
        } else {
            task = new Task();
        }
        add(task);
        int iTask = size();
        float hue = Utils.indexToHue(iTask);
        int rgb = Color.HSBtoRGB(hue, 0.15f, 0.85f);
        task.setColor(new Color(rgb));
        return task;
    }

    public boolean isBreakTask(Task task) {
        return task == getDefaultTasks().breakTask;
    }

    public boolean isDefaultTask(Task task) {
        return getDefaultTasks().contains(task);
    }

    public boolean isNotLoggingTask(Task task) {
        return task == getDefaultTasks().notLoggingTask;
    }

    public DefaultTasks getDefaultTasks() {
        return defaultTasks;
    }

    @Override
    public void clear() {
        while (!isEmpty()) remove(get(size() - 1));
    }

    @Override()
    public boolean remove(Object task) {
        if (super.remove(task)) {
            if (recycle) trashedTasks.push((Task) task);
            return true;
        }
        return false;
    }

    @Override()
    public Task remove(int index) {
        Task task = super.remove(index);
        if (task != null && recycle) trashedTasks.push(task);
        return task;
    }

    public TaskList getTrashedTasks() {
        TaskList result = new TaskList(false);
        for (Task task : trashedTasks) {
            result.add(task);
        }
        return result;
    }
}
