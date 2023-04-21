package org.intellij.idea.plugins.xplanner.model;

import org.xplanner.XPlanner;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * User: karpov Date: 02.02.2004 Time: 16:23:58
 */
public class UserStoryImpl implements UserStory {

    private final XPlanner xplanner;

    private org.xplanner.UserStory userStoryData;

    private final Set<Task> tasks = new HashSet<Task>();

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public UserStoryImpl(final XPlanner xplanner, final org.xplanner.UserStory userStoryData) {
        this.xplanner = xplanner;
        this.userStoryData = userStoryData;
    }

    public int getId() {
        return userStoryData.getId();
    }

    public double getActualHours() {
        double hours = 0;
        for (final Task task : tasks) {
            hours += task.getActualHours();
        }
        return hours;
    }

    public boolean isCompleted() {
        if (tasks.isEmpty()) {
            return false;
        }
        for (final Task task : tasks) {
            if (!task.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    public String getDescription() {
        return userStoryData.getDescription();
    }

    public void setDescription(final String description) throws RemoteException {
        final String oldValue = getDescription();
        userStoryData.setDescription(description);
        xplanner.update(userStoryData);
        propertyChangeSupport.firePropertyChange(DESCRIPTION_PROPERTY, oldValue, description);
    }

    public double getEstimatedHours() {
        double hours = 0;
        for (final Task task : tasks) {
            hours += task.getEstimatedHours();
        }
        return hours;
    }

    public String getName() {
        return userStoryData.getName();
    }

    public int getPriority() {
        return userStoryData.getPriority();
    }

    public double getRemainingHours() {
        return userStoryData.getRemainingHours();
    }

    public Set<Task> getTasks() {
        return Collections.unmodifiableSet(tasks);
    }

    public boolean notEstimated() {
        for (final Task task : tasks) {
            if (task.notEstimated()) {
                return true;
            }
        }
        return false;
    }

    public boolean isStarted() {
        for (final Task task : tasks) {
            if (task.isStarted()) {
                return true;
            }
        }
        return false;
    }

    public int getPercent() {
        if (isCompleted()) {
            return 100;
        } else if (!notEstimated()) {
            return (int) Math.round(getActualHours() * 100 / getEstimatedHours());
        } else {
            return 0;
        }
    }

    void addTask(final Task task) {
        tasks.add(task);
    }

    void removeTask(final Task task) {
        tasks.remove(task);
    }

    public String toString() {
        return userStoryData.getName();
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public void updateStoryData(final org.xplanner.UserStory storyData) throws RemoteException {
        userStoryData = storyData;
    }
}
