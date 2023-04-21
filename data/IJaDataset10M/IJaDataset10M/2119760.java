package com.firescrum.core.service;

import java.util.List;
import com.firescrum.core.model.BacklogItem;
import com.firescrum.core.model.Product;
import com.firescrum.core.model.Sprint;
import com.firescrum.core.model.Task;
import com.firescrum.core.model.UserApp;

/**
 * IServiceTask represents the entity's services {@link Task}.
 * 
 * @author core
 */
public interface IServiceTask {

    /**
     * Add a {@link Task} on the system.
     * 
     * @param task the {@link Task}
     * @param user the {@link UserApp}
     * @return the {@link Task} added
     */
    Task addTask(Task task, UserApp user);

    /**
     * Update a {@link Task} on the system.
     * 
     * @param task the {@link Task}.
     * @param user the {@link UserApp}
     * @return the {@link Task} updated.
     */
    Task updateTask(Task task, UserApp user);

    /**
     * Delete a {@link Task} on the system.
     * 
     * @param task the {@link Task}
     * @param user the {@link UserApp}
     */
    void deleteTask(Task task, UserApp user);

    /**
     * Retrieve all {@link Task} of {@link BacklogItem}.
     * 
     * @param backlogItem the {@link BacklogItem}
     * @return a {@link List} of {@link Task}
     */
    List<Task> getAllTasksOfBacklogItem(BacklogItem backlogItem);

    /**
     * Retrieve all {@link Task} of {@link Sprint}.
     * 
     * @param sprint the {@link Sprint}
     * @return a {@link List} of {@link Task}
     */
    List<Task> getAllTasksOfSprint(Sprint sprint);

    /**
     * Retrieve all {@link Task} of the {@link UserApp}.
     * 
     * @param user the {@link UserApp}
     * @param product the {@link Product}
     * @param sprint the {@link Sprint}
     * @return a {@link List} of {@link Task}
     */
    List<Task> getAllTasksOfUser(UserApp user, Product product, Sprint sprint);

    /**
     * Retrieve a {@link Task} using your identifier.
     * 
     * @param id the identifier
     * @return a {@link List} of {@link Task}
     */
    Task getTaskById(Integer id);
}
