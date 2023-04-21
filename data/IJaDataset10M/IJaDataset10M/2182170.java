package net.taylor.worklist.service;

import javax.ejb.Local;
import net.taylor.worklist.entity.Task;

@Local
public interface TaskTimeoutCallback {

    void createTimer(Task task);
}
