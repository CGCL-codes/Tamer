package net.sourceforge.iwii.db.dev.bo.project.artifact.phase1;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import net.sourceforge.iwii.db.dev.bo.IBusinessObject;
import net.sourceforge.iwii.db.dev.bo.project.artifact.ProjectArtifactDataBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.ProjectArtifactVersionBO;
import net.sourceforge.iwii.db.dev.common.TempIdGenerator;
import net.sourceforge.iwii.db.dev.common.enumerations.ArtifactStatuses;

/**
 * Class represents artifact data business object - fesibility study
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 * @version 1.00
 */
public class FeasibilityStudyArtifactDataBO extends ProjectArtifactDataBO {

    private String fullProjectName;

    private String workProjectName;

    private List<ProjectGoalBO> projectGoals = new LinkedList<ProjectGoalBO>();

    private List<ProjectScopeGroupBO> projectScopes = new LinkedList<ProjectScopeGroupBO>();

    private String potentialCustomer;

    private String customerCharacteristics;

    private List<CustomerExpectationGroupBO> customerExpectations = new LinkedList<CustomerExpectationGroupBO>();

    private CustomerImplementationEnvironmentBO customerImplementationEnvironment = new CustomerImplementationEnvironmentBO();

    private OwnImplementationEnvironmentBO ownImplementationEnvironment = new OwnImplementationEnvironmentBO();

    private SimilarProjectsExperienceBO projectExperience = new SimilarProjectsExperienceBO();

    private List<SimilarProjectBO> similarProjects = new LinkedList<SimilarProjectBO>();

    private Date projectStart;

    private Date projectEnd;

    private ProjectResourcesBO resources = new ProjectResourcesBO();

    private ProjectBudgetBO budget = new ProjectBudgetBO();

    private ProjectThreatsBO threats = new ProjectThreatsBO();

    private List<ContactBO> contacts = new LinkedList<ContactBO>();

    private String documentation;

    private String proposal;

    private ProjectArtifactVersionBO artifactVersion;

    public FeasibilityStudyArtifactDataBO() {
        this.setDatabaseId(TempIdGenerator.getInstance().generateId());
        this.projectExperience.setArtifactData(this);
        this.resources.setArtifactData(this);
        this.budget.setArtifactData(this);
        this.threats.setArtifactData(this);
        this.ownImplementationEnvironment.setArtifactData(this);
        this.customerImplementationEnvironment.setArtifactData(this);
        this.setStatus(ArtifactStatuses.Editing);
    }

    public ProjectBudgetBO getBudget() {
        return budget;
    }

    public void setBudget(ProjectBudgetBO budget) {
        this.budget = budget;
    }

    public List<ContactBO> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactBO> contacts) {
        this.contacts = contacts;
    }

    public String getCustomerCharacteristics() {
        return customerCharacteristics;
    }

    public void setCustomerCharacteristics(String customerCharacteristics) {
        this.customerCharacteristics = customerCharacteristics;
    }

    public List<CustomerExpectationGroupBO> getCustomerExpectations() {
        return customerExpectations;
    }

    public void setCustomerExpectations(List<CustomerExpectationGroupBO> customerExpectations) {
        this.customerExpectations = customerExpectations;
    }

    public CustomerImplementationEnvironmentBO getCustomerImplementationEnvironment() {
        return customerImplementationEnvironment;
    }

    public void setCustomerImplementationEnvironment(CustomerImplementationEnvironmentBO customerImplementationEnvironment) {
        this.customerImplementationEnvironment = customerImplementationEnvironment;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getFullProjectName() {
        return fullProjectName;
    }

    public void setFullProjectName(String fullProjectName) {
        this.fullProjectName = fullProjectName;
    }

    public OwnImplementationEnvironmentBO getOwnImplementationEnvironment() {
        return ownImplementationEnvironment;
    }

    public void setOwnImplementationEnvironment(OwnImplementationEnvironmentBO ownImplementationEnvironment) {
        this.ownImplementationEnvironment = ownImplementationEnvironment;
    }

    public String getPotentialCustomer() {
        return potentialCustomer;
    }

    public void setPotentialCustomer(String potentialCustomer) {
        this.potentialCustomer = potentialCustomer;
    }

    public Date getProjectEnd() {
        return projectEnd;
    }

    public void setProjectEnd(Date projectEnd) {
        this.projectEnd = projectEnd;
    }

    public SimilarProjectsExperienceBO getProjectExperience() {
        return projectExperience;
    }

    public void setProjectExperience(SimilarProjectsExperienceBO projectExperience) {
        this.projectExperience = projectExperience;
    }

    public List<ProjectGoalBO> getProjectGoals() {
        return projectGoals;
    }

    public void setProjectGoals(List<ProjectGoalBO> projectGoals) {
        this.projectGoals = projectGoals;
    }

    public List<ProjectScopeGroupBO> getProjectScopes() {
        return projectScopes;
    }

    public void setProjectScopes(List<ProjectScopeGroupBO> projectScopes) {
        this.projectScopes = projectScopes;
    }

    public Date getProjectStart() {
        return projectStart;
    }

    public void setProjectStart(Date projectStart) {
        this.projectStart = projectStart;
    }

    public String getProposal() {
        return proposal;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public ProjectResourcesBO getResources() {
        return resources;
    }

    public void setResources(ProjectResourcesBO resources) {
        this.resources = resources;
    }

    public List<SimilarProjectBO> getSimilarProjects() {
        return similarProjects;
    }

    public void setSimilarProjects(List<SimilarProjectBO> similarProjects) {
        this.similarProjects = similarProjects;
    }

    public ProjectThreatsBO getThreats() {
        return threats;
    }

    public void setThreats(ProjectThreatsBO threats) {
        this.threats = threats;
    }

    public String getWorkProjectName() {
        return workProjectName;
    }

    public void setWorkProjectName(String workProjectName) {
        this.workProjectName = workProjectName;
    }

    public ProjectArtifactVersionBO getArtifactVersion() {
        return artifactVersion;
    }

    public void setArtifactVersion(ProjectArtifactVersionBO artifactVersion) {
        this.artifactVersion = artifactVersion;
    }

    @Override
    public String toString() {
        return "business-object://convertable/" + this.getClass().getName() + "[databaseId=" + this.getDatabaseId() + "]";
    }

    @Override
    public void initWithOtherBO(IBusinessObject otherBO) {
        super.initWithOtherBO(otherBO);
        if (otherBO instanceof FeasibilityStudyArtifactDataBO) {
            FeasibilityStudyArtifactDataBO bo = (FeasibilityStudyArtifactDataBO) otherBO;
            this.setBudget(bo.getBudget());
            this.setContacts(bo.getContacts());
            this.setCustomerCharacteristics(bo.getCustomerCharacteristics());
            this.setCustomerExpectations(bo.getCustomerExpectations());
            this.setCustomerImplementationEnvironment(bo.getCustomerImplementationEnvironment());
            this.setDocumentation(bo.getDocumentation());
            this.setFullProjectName(bo.getFullProjectName());
            this.setOwnImplementationEnvironment(bo.getOwnImplementationEnvironment());
            this.setPotentialCustomer(bo.getPotentialCustomer());
            this.setProjectEnd(bo.getProjectEnd());
            this.setProjectExperience(bo.getProjectExperience());
            this.setProjectGoals(bo.getProjectGoals());
            this.setProjectScopes(bo.getProjectScopes());
            this.setProjectStart(bo.getProjectStart());
            this.setProposal(bo.getProposal());
            this.setResources(bo.getResources());
            this.setSimilarProjects(bo.getSimilarProjects());
            this.setThreats(bo.getThreats());
            this.setWorkProjectName(bo.getWorkProjectName());
            this.setArtifactVersion(bo.getArtifactVersion());
        }
    }
}
