package org.endeavour.mgmt.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.endeavour.mgmt.controller.ci.CIMaintenance;
import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.model.ProjectMember;
import org.endeavour.mgmt.model.WorkProduct;
import org.endeavour.mgmt.model.persistence.PersistenceManager;

@SuppressWarnings("unchecked")
public class ProjectMaintenance extends CIMaintenance {

    private Project project = null;

    private List<String> errors = null;

    public static final String PROJECT_MEMBERS = WorkProduct.PROJECT_MEMBERS;

    public static final String DESCRIPTION = Project.DESCRIPTION;

    public static final String STATUS = Project.STATUS;

    public List<Project> getProjects() {
        SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
        ProjectMember theLoggedUser = theSecurityMaintenance.getLoggedUser();
        List<Project> theAllProjects = PersistenceManager.getInstance().findAllBy("select project from " + Project.class.getSimpleName() + " project order by project.name");
        List<Project> theProjects = new ArrayList<Project>();
        if (theLoggedUser.isAdministrator()) {
            theProjects = theAllProjects;
        } else {
            for (Project theProject : theAllProjects) {
                if (theLoggedUser.isAssigned(theProject)) {
                    theProjects.add(theProject);
                }
            }
        }
        return theProjects;
    }

    public Map<String, Object> getProjectDataBy(Integer aProjectId) {
        this.project = this.retrieveProjectBy(aProjectId);
        Map<String, Object> theData = this.project.getData();
        return theData;
    }

    public Project retrieveProjectBy(Integer aProjectId) {
        Project theProject = (Project) PersistenceManager.getInstance().findById(Project.class, aProjectId);
        return theProject;
    }

    public void processProjectMemberIds(Map<String, Object> aData) {
        List<ProjectMember> theProjectMembers = new ArrayList<ProjectMember>();
        List<Integer> theProjectMemberIds = (List) aData.get(PROJECT_MEMBERS);
        for (Integer theProjectMemberId : theProjectMemberIds) {
            ProjectMember theProjectMember = (ProjectMember) PersistenceManager.getInstance().findById(ProjectMember.class, theProjectMemberId);
            theProjectMembers.add(theProjectMember);
        }
        aData.put(PROJECT_MEMBERS, theProjectMembers);
    }

    public List<String> saveProject(Map<String, Object> aData) {
        if (this.project == null) {
            this.project = new Project();
        }
        this.processProjectMemberIds(aData);
        if (this.isValid(aData)) {
            String theName = this.project.getName();
            String theDescription = this.project.getDescription();
            this.project.save(aData);
            if (theName == null) {
                super.createCIProject(this.project.getName(), this.project.getDescription());
            } else if (theName != null && !theName.equals(this.project.getName())) {
                super.renameCIProject(theName, this.project.getName());
            }
            if (theDescription != null && !theDescription.equals(this.project.getDescription())) {
                super.changeCIProjectDescription(this.project.getName(), this.project.getDescription());
            }
            setChanged();
            notifyObservers();
        }
        return this.errors;
    }

    public void deleteProject(Integer aProjectId) {
        this.project = this.retrieveProjectBy(aProjectId);
        this.project.delete();
        super.deleteCIProject(this.project.getName());
        this.project = null;
    }

    private boolean isValid(Map<String, Object> aData) {
        this.errors = this.project.validate(aData);
        return this.errors.isEmpty();
    }

    public List<ProjectMember> getAllUnassignedProjectMembers() {
        List<ProjectMember> theUnAssignedProjectMembers = ProjectMember.getUnassignedProjectMembersFor(this.project, null);
        if (this.project != null) {
            for (ProjectMember theProjectMember : this.project.getProjectMembers()) {
                if (theProjectMember != null) {
                    theUnAssignedProjectMembers.remove(theProjectMember);
                }
            }
        }
        return theUnAssignedProjectMembers;
    }

    public void reset() {
        this.project = null;
    }

    public Integer getSelectedProjectId() {
        Integer theProjectId = null;
        if (this.project != null) {
            theProjectId = this.project.getId();
        }
        return theProjectId;
    }
}
