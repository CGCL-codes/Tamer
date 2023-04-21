package org.photovault.taskscheduler;

/**
 Interface for classes that can execute background tasks created by 
 {@link TaskProducer}
 
 */
public interface TaskScheduler {

    /**
     Register a new producer that assigns tasks to this scheduler. The scheduler
     requests new tasks by calling producer's requestTask() method.
     @param c The new task producer
     @param priority Priority of the new task producer. THis can vary from 0 
     (highest) to MIN_PRIORITY (lowest).
     */
    public void registerTaskProducer(org.photovault.taskscheduler.TaskProducer c, int priority);
}
