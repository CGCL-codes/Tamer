package net.sourceforge.javabits.task;

import java.util.Set;

/**
 * @author Jochen Kuhnle
 */
public interface TaskNode {

    public Set<Task> submit(TaskRunner runner, Set<Task> predecessorSet);
}
