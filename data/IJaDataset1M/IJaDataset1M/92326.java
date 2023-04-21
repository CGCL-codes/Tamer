package physis.core.task;

import java.util.*;

public class TaskGroup {

    private String name;

    private TaskIterator taskiterator;

    public TaskGroup(String name, Vector tasks) {
        this.name = name;
        Task[] tasksarray = new Task[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            tasksarray[i] = (Task) tasks.elementAt(i);
        }
        taskiterator = new TaskIterator(tasksarray);
    }

    public String getGroupName() {
        return name;
    }

    public TaskIterator getTasks() {
        return taskiterator;
    }
}
