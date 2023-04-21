package com.google.gdt.eclipse.gph.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Google Project Hosting user.
 * 
 * @see GPHProject
 */
public class GPHUser {

    private String userName;

    private String userId;

    private String repoPassword;

    private List<GPHProject> projects = new ArrayList<GPHProject>();

    /**
   * Create a new GPHUser.
   * 
   * @param userName the user's name
   * @param userId a unique ID for the user
   * @param repoPassword the user's repository password
   */
    public GPHUser(String userName, String userId, String repoPassword) {
        this.userName = userName;
        this.userId = userId;
        this.repoPassword = repoPassword;
    }

    /**
   * @return the list of projects this user is a member of
   */
    public List<GPHProject> getProjects() {
        return Collections.unmodifiableList(projects);
    }

    /**
   * @return the repository password for this user
   */
    public String getRepoPassword() {
        return repoPassword;
    }

    /**
   * @return the unique user identifier
   */
    public String getUserId() {
        return userId;
    }

    /**
   * @return the username
   */
    public String getUserName() {
        return userName;
    }

    /**
   * Add the given project to the list of projects for this user.
   * 
   * @param project the project to add
   */
    void addProject(GPHProject project) {
        projects.add(project);
    }
}
