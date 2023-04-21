package com.cvo.scrumtoolkit.client.model;

import java.io.Serializable;
import java.util.LinkedList;
import com.cvo.scrumtoolkit.client.entities.Project;

public class ProjectStore implements Serializable {

    private static ProjectStore projectStore;

    private LinkedList<Project> projects;

    protected ProjectStore() {
        projects = new LinkedList<Project>();
    }

    public static ProjectStore getProjectStore() {
        if (projectStore == null) {
            projectStore = new ProjectStore();
        }
        return projectStore;
    }

    public LinkedList<Project> getProjects() {
        return projects;
    }

    public void addProject(Project project) {
        projects.add(project);
    }

    public Project getProjectById(Long Id) {
        Project project = null;
        for (Project getproject : projects) {
            if (Id == getproject.getId()) {
                project = getproject;
            }
        }
        return project;
    }
}
