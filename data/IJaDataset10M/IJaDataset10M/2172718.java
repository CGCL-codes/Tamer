package scrum.client.project;

import scrum.client.common.TooltipBuilder;

public class CloseRequirementEstimationVotingAction extends GCloseRequirementEstimationVotingAction {

    public CloseRequirementEstimationVotingAction(scrum.client.project.Requirement requirement) {
        super(requirement);
    }

    @Override
    public String getLabel() {
        return "Close Planning Poker";
    }

    @Override
    protected void updateTooltip(TooltipBuilder tb) {
        tb.setText("Close the Planning Poker table for this Story.");
        if (!requirement.getProject().isTeamMember(getCurrentUser())) tb.addRemark(TooltipBuilder.NOT_TEAM);
    }

    @Override
    public boolean isPermitted() {
        if (!requirement.getProject().isTeamMember(getCurrentUser())) return false;
        return true;
    }

    @Override
    public boolean isExecutable() {
        if (!requirement.isWorkEstimationVotingActive()) return false;
        return true;
    }

    @Override
    protected void onExecute() {
        requirement.deactivateWorkEstimationVoting();
    }
}
