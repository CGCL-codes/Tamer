package scrum.client.estimation;

public class ActivateRequirementEstimationVotingServiceCall extends scrum.client.core.AServiceCall {

    private String requirementId;

    public ActivateRequirementEstimationVotingServiceCall(String requirementId) {
        this.requirementId = requirementId;
    }

    public void execute(Runnable returnHandler) {
        serviceCaller.onServiceCall(this);
        serviceCaller.getService().activateRequirementEstimationVoting(serviceCaller.getConversationNumber(), requirementId, new DefaultCallback(this, returnHandler));
    }

    @Override
    public String toString() {
        return "ActivateRequirementEstimationVoting";
    }
}
