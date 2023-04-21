package org.itracker.services;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.itracker.model.Component;
import org.itracker.model.CustomField;
import org.itracker.model.Project;
import org.itracker.model.ProjectScript;
import org.itracker.model.User;
import org.itracker.model.Version;

public interface ProjectService {

    public Project getProject(Integer projectId);

    public List<Project> getAllProjects();

    public List<Project> getAllAvailableProjects();

    public Component getProjectComponent(Integer componentId);

    public Component updateProjectComponent(Component component);

    public Component addProjectComponent(Integer projectId, Component component);

    public boolean removeProjectComponent(Integer projectId, Integer componentId);

    public Version getProjectVersion(Integer versionId);

    public Version updateProjectVersion(Version version);

    public Version addProjectVersion(Integer projectId, Version version);

    public boolean removeProjectVersion(Integer projectId, Integer versionId);

    public List<User> getProjectOwners(Integer projectId);

    public List<User> getListOfProjectOwners(Integer projectId);

    public boolean setProjectOwners(Project project, Set<Integer> newOwners);

    public List<CustomField> getProjectFields(Integer projectId);

    public List<CustomField> getListOfProjectFields(Integer projectId);

    public List<CustomField> getProjectFields(Integer projectId, Locale locale);

    public boolean setProjectFields(Project project, Set<Integer> newFields);

    public ProjectScript getProjectScript(Integer scriptId);

    public List<ProjectScript> getProjectScripts();

    public ProjectScript updateProjectScript(ProjectScript projectScript);

    public ProjectScript addProjectScript(Integer projectId, ProjectScript projectScript);

    public boolean removeProjectScript(Integer projectId, Integer scriptId);

    /**
     * Counts the number of issues for a given component. 
     * 
     * @param componentId Id of the component to which the issues must be associated
     * @return 0 if the component has no issues or doesn't exist
     */
    public Long countIssuesByComponent(Integer componentId);

    public Long getTotalNumberIssuesByProject(Integer projectId);

    public Long getTotalNumberOpenIssuesByProject(Integer projectId);

    public Long getTotalNumberResolvedIssuesByProject(Integer projectId);

    /**
     * Counts the number of issues for a given version. 
     * 
     * @param versionId Id of the version to which the issues must be associated
     * @return 0 if the version has no issues or doesn't exist
     */
    public Long countIssuesByVersion(Integer versionId);

    /**
     * Returns the number of open and resolved issues in the given project. 
     * 
     * <p>PENDING: should use a class to hold statistics info to improve type-
     * safety. </p>
     * @deprecated count open/closed issues with new methods: getTotalNumberOpenIssuesByProject, getTotalNumberResolvedIssuesByProject
     * 
     * @return int[0] = open issues, int[1] = resolved issues
     */
    public Long[] getProjectStats(Integer projectId);

    public Date getLatestIssueUpdatedDateByProjectId(Integer projectId);

    /**
     * Creates a new issue in a project.
     * @param model an Issue representing the new issue information
     * @param projectId the projectId the issue belongs to
     * @param userId the id of registered creator of the new issue
     * @param createdById the id of the actual creator of the issue.  This would normally be the same as the userId.
     * @return an Issue containing the newly created issue, or null if the create failed
     */
    Project createProject(Project project, Integer userId);

    /**
     * update a project, as the user with userId
     * @param project
     * @param userId
     * @return the updated project from Persistence
     */
    Project updateProject(Project project, Integer userId);

    /**
     * Check if this project name is used by any of the projects which already exist in the database.
     * 
     * @param projectName
     * @param updatedProjectId The updated project which will be use the new name.
     * @return true if the name is unique.
     */
    Boolean isUniqueProjectName(String projectName, Integer updatedProjectId);
}
