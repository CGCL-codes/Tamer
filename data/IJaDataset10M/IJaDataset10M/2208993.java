package jccr;

import java.lang.Iterable;

public interface Task {

    /**
	 * Executes the task, any subtasks return will be enqueued
	 * to the thread-pool and executed later.
	 * @return subtasks or null.
	 */
    Iterable<Task> execute();
}
