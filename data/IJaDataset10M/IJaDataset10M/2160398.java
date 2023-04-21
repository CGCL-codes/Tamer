package org.systemsbiology.apps.corragui.client.data.project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.systemsbiology.apps.corragui.client.constants.PipelineStep;
import org.systemsbiology.apps.corragui.client.data.ProjectModelMediator;
import org.systemsbiology.apps.corragui.client.data.Status;
import org.systemsbiology.apps.corragui.client.data.corraSettings.ICorraSettingsModel;
import org.systemsbiology.apps.corragui.client.data.inclusionList.InclusionListSummary;
import org.systemsbiology.apps.corragui.client.data.inclusionList.Segments;
import org.systemsbiology.apps.corragui.client.data.inclusionList.SegmentsSummary;
import org.systemsbiology.apps.corragui.domain.AlignmentSetup;
import org.systemsbiology.apps.corragui.domain.FeatAnnoSetup;
import org.systemsbiology.apps.corragui.domain.FeaturePickingSetup;
import org.systemsbiology.apps.corragui.domain.InclusionListSummaries;
import org.systemsbiology.apps.corragui.domain.Project;
import org.systemsbiology.apps.corragui.domain.ProjectSetup;
import org.systemsbiology.apps.corragui.domain.ProjectSummary;
import org.systemsbiology.apps.corragui.domain.SampleInformation;
import org.systemsbiology.apps.corragui.domain.StatsSetup;
import com.google.gwt.user.client.Window;

public class ProjectModel implements IProjectModel {

    private Project currentProject;

    private List<IProjectModelListener> listeners;

    private final ICorraSettingsModel settingsModel;

    private ProjectModelMediator mediator;

    public ProjectModel(ICorraSettingsModel settingsModel) {
        this.listeners = new ArrayList<IProjectModelListener>();
        this.settingsModel = settingsModel;
    }

    /**
     * Sets the mediator for keeping the ProjectModel and ProjectListModel in sync.
     * @param mediator
     */
    public void setModelMediator(ProjectModelMediator mediator) {
        this.mediator = mediator;
    }

    public ProjectModelMediator getModelMediator() {
        return this.mediator;
    }

    /**
     * Returns the current project
     */
    public Project getProject() {
        return this.currentProject;
    }

    /**
     * Returns the name of the current project.  
     * Returns the empty string "" if the current project is <code>null</code>.
     */
    public String getProjectName() {
        if (this.currentProject != null) return this.currentProject.getProjectName(); else return "";
    }

    /**
     * Set the current project to <code>Project newProj</code> if it is not null;
     * Notifies listeners of change.
     */
    public void setProject(Project newProj) {
        if (newProj == null) {
            Window.alert("Project is null");
            return;
        }
        this.currentProject = newProj;
        if (mediator != null) {
            mediator.setCurrentProjectInList(newProj.getProjectSummary());
        }
        this.fireProjectChanged();
    }

    /**
     * Sets the current project to a default project, i.e. a project with default setup and status
     * values. The project name is set to <code>projName</code>.
     * Notifies listeners of change.
     */
    public void setDefaultProject(String projName) {
        Project proj = defaultProject();
        if (projName != null) proj.setProjectName(projName); else proj.setProjectName("");
        this.setProject(proj);
    }

    private Project defaultProject() {
        Project proj = new Project(new ProjectSetup(), new FeaturePickingSetup(), new AlignmentSetup(), new StatsSetup(), new FeatAnnoSetup(), Status.defaultStatus(), Status.defaultStatus(), Status.defaultStatus(), Status.defaultStatus(), Status.defaultStatus(), new InclusionListSummaries());
        return proj;
    }

    /**
     * Updates the status of the pipeline steps for the current project, ONLY if 
     * the <code>projSummary</code> has the same project name as the current project.
     * Notifies listeners of the change in project status.
     */
    public void updateProjectStatus(ProjectSummary projSummary, boolean broadcast) {
        if (projSummary.getProjectName().equals(this.currentProject.getProjectName())) {
            this.currentProject.setFeaturePickingStatus(projSummary.getFpStatus());
            this.currentProject.setAlignmentStatus(projSummary.getAlignStatus());
            this.currentProject.setStatsStatus(projSummary.getStatsStatus());
            this.currentProject.setInclStatus(projSummary.getInclListsStatus());
            this.currentProject.setFeatAnnoStatus(projSummary.getFeatAnnoStatus());
            if (currentProject.getInclStatus().notStarted()) deleteAllInclusionLists();
            this.fireProjectStatusChanged(projSummary);
        }
        if (mediator != null && broadcast) {
            mediator.updateProjectStatusInList(projSummary);
        }
    }

    /**
     * Sets the current project to <code>null</code>, if its name matches <code>projName</code>.
     * If this class contains a mediator it is notfied of the change.
     * Notifies listeners. 
     */
    public void removeProject(String projName) {
        if (currentProject == null) return;
        reset(projName);
        if (mediator != null) {
            mediator.removeProjectFromList(projName);
        }
    }

    /**
     * Sets the current project to <code>null</code>.
     * If the class contains a mediator it is notified of the change.
     * Notifies listeners.
     */
    public void removeProject() {
        removeProject(getProjectName());
    }

    /**
     * Sets the current project to <code>null</code>
     * Notifies listeners.
     */
    public void reset() {
        if (currentProject == null) return;
        reset(currentProject.getProjectName());
    }

    private void reset(String projName) {
        if (currentProject.getProjectName().equals(projName)) {
            currentProject = null;
            fireProjectDeleted(projName);
        }
    }

    public void setImportedSampleInformation(String projectName, SampleInformation sInfo) {
        if (projectName.equals(currentProject.getProjectName())) {
            fireSampleInfoImported(sInfo);
        }
    }

    public void setImportedStepOptions(String projectName, PipelineStep step, List<ISetupOption> options) {
        if (projectName.equals(currentProject.getProjectName())) {
            fireStepParamsImported(options, step);
        }
    }

    public void addInclusionListSummary(InclusionListSummary summary) {
        if (summary.getProjectName().equals(currentProject.getProjectName())) {
            currentProject.addInclusionListSummary(summary);
            fireInclusionListAdded(summary);
            if (currentProject.getInclStatus().notStarted()) {
                currentProject.setInclStatus(Status.DONE);
                updateProjectStatus(currentProject.getProjectSummary(), true);
            }
        }
    }

    private void deleteAllInclusionLists() {
        currentProject.deleteAllInclusionLists();
        currentProject.setInclStatus(Status.NOTSTARTED);
    }

    public void deleteInclusionList(String projName, String listName) {
        if (projName.equals(currentProject.getProjectName())) {
            InclusionListSummary deleted = currentProject.deleteInclusionList(listName);
            fireInclusionListDeleted(deleted);
            if (currentProject.getInclListSummaries().getSummaryCount() == 0) {
                currentProject.setInclStatus(Status.NOTSTARTED);
                updateProjectStatus(currentProject.getProjectSummary(), true);
            }
        }
    }

    public void setInclusionListSegments(Segments segments, String projName) {
        if (projName.equals(currentProject.getProjectName())) {
            fireSegmentsRead(segments);
        }
    }

    public void setInclusionListSegmentsSummary(SegmentsSummary segSummary, String projName, String listName) {
        if (projName.equals(currentProject.getProjectName())) {
            if (currentProject.addSegmentsSummary(segSummary)) fireSegmentsCreated(segSummary);
        }
    }

    public boolean isValidInclusionListName(String listName) {
        if (currentProject.getInclListSummaries().getSummaryByListName(listName) != null) return false;
        return true;
    }

    public boolean inclusionListExists(String projName, String listName) {
        return currentProject.getInclListSummaries().getSummaryByListName(listName) != null;
    }

    public boolean segmentsExist(String projName, String listName) {
        InclusionListSummary summary = currentProject.getInclListSummaries().getSummaryByListName(listName);
        if (summary == null) return false;
        return summary.getSegmentsSummary() != null;
    }

    /**
     * Returns the summary for the current project. 
     * Returns <code>null</code> if the current project is null.
     */
    public ProjectSummary getProjectSummary() {
        if (this.currentProject != null) return this.currentProject.getProjectSummary(); else return null;
    }

    /**
     * Returns the project setup for the current project. 
     * Returns <code>null</code> if the current project is null.
     */
    public ProjectSetup getProjectSetup() {
        if (this.currentProject != null) return this.currentProject.getProjSetup(); else return null;
    }

    /**
     * If the given setup is for the currently displayed project, it is updated.
     */
    public void setProjectSetup(ProjectSetup setup) {
        if (setup.getProjectName().equals(currentProject.getProjectName())) {
            currentProject.setProjSetup(setup);
        }
    }

    /**
     * Returns the feature picking setup for the current project. 
     * Returns <code>null</code> if the current project is null.
     */
    public FeaturePickingSetup getFeaturePickingSetup() {
        if (this.currentProject != null) return this.currentProject.getFpSetup(); else return null;
    }

    /**
     * Returns the feature picking status for the current project. 
     * Returns <code>null</code> if the current project is null.
     */
    public Status getFeaturePickingStatus() {
        if (this.currentProject != null) return this.currentProject.getFeaturePickingStatus(); else return null;
    }

    public void setFeaturePickingSetup(String projName, FeaturePickingSetup setup) {
        if (projName.equals(currentProject.getProjectName())) {
            currentProject.setFpSetup(setup);
        }
    }

    /**
     * Returns the alignment setup for the current project. 
     * Returns <code>null</code> if the current project is null.
     */
    public AlignmentSetup getAlignmentSetup() {
        if (this.currentProject != null) return this.currentProject.getAlignSetup(); else return null;
    }

    /**
     * Returns the alignment status for the current project. 
     * Returns <code>null</code> if the current project is null.
     */
    public Status getAlignmentStatus() {
        return this.currentProject.getAlignmentStatus();
    }

    public void setAlignmentSetup(String projName, AlignmentSetup setup) {
        if (projName.equals(currentProject.getProjectName())) {
            currentProject.setAlignSetup(setup);
        }
    }

    /**
     * Returns the statistical analysis setup for the current project. 
     * Returns <code>null</code> if the current project is null.
     */
    public StatsSetup getStatsSetup() {
        if (this.currentProject != null) return this.currentProject.getStatsSetup(); else return null;
    }

    /**
     * Returns the statistical analysis status for the current project. 
     * Returns <code>null</code> if the current project is null.
     */
    public Status getStatsStatus() {
        if (this.currentProject != null) return this.currentProject.getStatsStatus(); else return null;
    }

    public void setStatsSetup(String projName, StatsSetup setup) {
        if (projName.equals(currentProject.getProjectName())) {
            currentProject.setStatsSetup(setup);
        }
    }

    /**
     * Returns the Target Feature Annotation analysis setup for the current project. 
     * Returns <code>null</code> if the current project is null.
     */
    public FeatAnnoSetup getFeatureAnnotationSetup() {
        if (this.currentProject != null) return this.currentProject.getFeatAnnoSetup(); else return null;
    }

    /**
     * Returns the Target Feature Annotation status for the current project. 
     * Returns <code>null</code> if the current project is null.
     */
    public Status getFeatureAnnotationStatus() {
        if (this.currentProject != null) return this.currentProject.getFeatAnnoStatus(); else return null;
    }

    public void setFeatureAnnotationSetup(String projName, FeatAnnoSetup setup) {
        if (projName.equals(currentProject.getProjectName())) {
            currentProject.setFeatAnnoSetup(setup);
        }
    }

    public SampleInformation getSampleInformation() {
        if (this.currentProject != null) return currentProject.getProjSetup().getSampleInformation(); else return null;
    }

    public void addListener(IProjectModelListener listener) {
        if (listeners.contains(listener)) return;
        listeners.add(listener);
    }

    public void removeAllListners() {
        this.listeners.clear();
    }

    public void removeListener(IProjectModelListener listener) {
        this.listeners.remove(listener);
    }

    private void fireProjectChanged() {
        for (Iterator<IProjectModelListener> it = listeners.iterator(); it.hasNext(); ) {
            it.next().onProjectChanged(this.currentProject);
        }
    }

    private void fireProjectStatusChanged(ProjectSummary projSummary) {
        for (Iterator<IProjectModelListener> it = listeners.iterator(); it.hasNext(); ) {
            it.next().onProjectStatusChanged(projSummary);
        }
    }

    private void fireProjectDeleted(String projName) {
        for (Iterator<IProjectModelListener> it = listeners.iterator(); it.hasNext(); ) {
            it.next().onProjectDeleted(projName);
        }
    }

    private void fireInclusionListAdded(InclusionListSummary added) {
        for (Iterator<IProjectModelListener> it = listeners.iterator(); it.hasNext(); ) {
            it.next().onInclusionListAdded(added);
        }
    }

    private void fireInclusionListDeleted(InclusionListSummary deleted) {
        for (Iterator<IProjectModelListener> it = listeners.iterator(); it.hasNext(); ) {
            it.next().onInclusionListDeleted(deleted);
        }
    }

    private void fireSegmentsCreated(SegmentsSummary segSummary) {
        for (Iterator<IProjectModelListener> it = listeners.iterator(); it.hasNext(); ) {
            it.next().onSegmentsCreated(segSummary);
        }
    }

    private void fireSegmentsRead(Segments segments) {
        for (Iterator<IProjectModelListener> it = listeners.iterator(); it.hasNext(); ) {
            it.next().onGetSegments(segments);
        }
    }

    private void fireSampleInfoImported(SampleInformation sInfo) {
        for (Iterator<IProjectModelListener> it = listeners.iterator(); it.hasNext(); ) {
            ((IProjectModelListener) it.next()).onSampleInfoImported(sInfo);
        }
    }

    private void fireStepParamsImported(List<ISetupOption> setupOptions, PipelineStep step) {
        for (Iterator<IProjectModelListener> it = listeners.iterator(); it.hasNext(); ) {
            ((IProjectModelListener) it.next()).onStepOptionsImported(setupOptions, step);
        }
    }
}
