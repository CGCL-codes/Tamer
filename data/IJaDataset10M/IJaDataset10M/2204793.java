package org.systemsbiology.apps.corragui.client.widget.project;

import org.systemsbiology.apps.corragui.client.constants.ProjectSubMenuCategory;
import org.systemsbiology.apps.corragui.client.data.Model;
import org.systemsbiology.apps.corragui.client.widget.IGuiMediator;
import org.systemsbiology.apps.corragui.client.widget.WidgetsMediator;
import org.systemsbiology.apps.corragui.domain.Project;

/**
 * Mediator class for passing messages between a ProjectMenu widget and its related 
 * ProjectDetails widget.
 * @author Vagisha Sharma
 * @version 1.0
 */
public class ProjectWidgetsMediator extends WidgetsMediator {

    private Model model;

    public ProjectWidgetsMediator(IGuiMediator guiMediator) {
        super(guiMediator);
    }

    public void setModel(Model model) {
        this.model = model;
    }

    private ProjectMenu getProjectMenu() {
        return (ProjectMenu) super.getMenu();
    }

    private ProjectDetails getProjectDetails() {
        return (ProjectDetails) super.getDetails();
    }

    /**
	 * Updates the ProjectMenu to select the pipeline step (corresponding to the sub-menu
	 * category) for the currently selected project.
	 */
    void onPipelineStepSelected(ProjectSubMenuCategory step) {
        if (!canChangeSelectedStep()) return;
        getProjectMenu().selectSubMenu(step);
        getProjectDetails().showDetails(step);
    }

    /**
	 * Reverts to the last saved state.
	 */
    void onEditCanceled() {
        Project proj = model.getProjModel().getProject();
        if (!(Project.isValidProject(proj))) {
            model.getProjModel().removeProject();
        } else {
            getProjectDetails().cancelEditing();
        }
    }

    boolean canChangeSelectedStep() {
        return getProjectDetails().canChangeSelectedStep();
    }

    /**
	 * Tells the ProjectDetails widget to set the currently selected details widget
	 * to its editable state.
	 */
    void onEdit() {
        getProjectDetails().editProject();
    }

    /**
	 * Tells the ProjectDetails to save the current setup.
	 * @return <code>true</code> if the request to save the setup was made successfully.
	 */
    boolean onSave() {
        return getProjectDetails().saveProject();
    }

    /**
	 * Tells the ProjectDetails to reset the Save/Cancel button to Edit state. 
	 */
    public void onExecuteClicked() {
        getProjectDetails().disableEditButton();
    }
}
