package scrum.client.sprint;

import scrum.client.project.AddRequirementToCurrentSprintAction;
import scrum.client.project.Requirement;

public class PullNextRequirementAction extends GPullNextRequirementAction {

    public PullNextRequirementAction(scrum.client.sprint.Sprint sprint) {
        super(sprint);
    }

    @Override
    protected void updateTooltip(scrum.client.common.TooltipBuilder tb) {
        Requirement req = getCurrentProject().getNextProductBacklogRequirement();
        if (req != null) tb.setText("Pull " + req.getReferenceAndLabel() + " to current Sprint.");
    }

    @Override
    public String getLabel() {
        return "Pull next Story";
    }

    @Override
    public boolean isExecutable() {
        AddRequirementToCurrentSprintAction addAction = getAddAction();
        if (addAction == null) return false;
        return addAction.isExecutable();
    }

    @Override
    public boolean isPermitted() {
        AddRequirementToCurrentSprintAction addAction = getAddAction();
        if (addAction == null) return false;
        return addAction.isPermitted();
    }

    @Override
    protected void onExecute() {
        AddRequirementToCurrentSprintAction addAction = getAddAction();
        if (addAction != null) addAction.onExecute();
    }

    private AddRequirementToCurrentSprintAction getAddAction() {
        Requirement req = getCurrentProject().getNextProductBacklogRequirement();
        if (req == null) return null;
        return new AddRequirementToCurrentSprintAction(req);
    }
}
