package org.horen.task.comparators;

import org.horen.task.Task;

public class TaskShortDescriptionComparator extends TaskComparator {

    @Override
    protected int compareFunction(Task t1, Task t2) {
        return t1.getShortDesc().compareTo(t2.getShortDesc());
    }

    @Override
    protected String getRessourceName() {
        return "Column.Name=Name";
    }
}
