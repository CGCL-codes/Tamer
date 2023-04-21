package com.agical.buildmonitor;

import com.agical.buildmonitor.listeners.Listener;
import com.agical.buildmonitor.listeners.Project;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * A manager that will handle all listeners
 */
public class ListenerManager {

    private List<Listener> allListeners;

    public ListenerManager(List<Listener> allListeners) {
        this.allListeners = allListeners;
    }

    /**
     * Check all build for all listeners
     */
    public void checkBuilds() {
        for (Listener listener : allListeners) {
            if (isItTimeToCheck(listener)) {
                listener.checkBuildStatus();
            }
        }
    }

    private boolean isItTimeToCheck(Listener listener) {
        int monitorInterval = listener.getMonitorInterval();
        Date lastMonitorTime = listener.getLastMonitorTime();
        Date newMonitorTime = new Date(lastMonitorTime.getTime() + (monitorInterval * 1000));
        Date now = new Date();
        return newMonitorTime.before(now);
    }

    /**
     * Return all projects that currently has failed.
     *
     * @return all projects that currently has failed
     */
    public List<Project> getSuccessfulProjects() {
        List<Project> allProjects = new LinkedList<Project>();
        for (Listener listener : allListeners) {
            allProjects.addAll(listener.getSuccessfulProjects());
        }
        return allProjects;
    }

    /**
     * Return all projects that currently has failed.
     *
     * @return all projects that currently has failed
     */
    public List<Project> getFailedProjects() {
        List<Project> allProjects = new LinkedList<Project>();
        for (Listener listener : allListeners) {
            allProjects.addAll(listener.getFailedProjects());
        }
        return allProjects;
    }
}
