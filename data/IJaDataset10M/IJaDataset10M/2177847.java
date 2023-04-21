package com.novasurv.turtle.backend.project;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a set of student submissions for a particular project.
 *
 * @author Jason Dobies
 */
public class GradeBook implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private List<Project> projects;

    /**
     * If this gradebook has been saved, this file will indicate where it was last saved.
     */
    private File lastSaveLocation;

    public GradeBook(String name, List<Project> projects) {
        this.name = name;
        this.projects = projects;
    }

    public String getName() {
        return name;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public File getLastSaveLocation() {
        return lastSaveLocation;
    }

    public void setLastSaveLocation(File lastSaveLocation) {
        this.lastSaveLocation = lastSaveLocation;
    }
}
