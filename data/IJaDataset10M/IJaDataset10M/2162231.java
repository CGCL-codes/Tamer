package scrum.client.sprint;

public class PullStoryToSprintServiceCall extends scrum.client.core.AServiceCall {

    private String storyId;

    public PullStoryToSprintServiceCall(String storyId) {
        this.storyId = storyId;
    }

    public void execute(Runnable returnHandler) {
        serviceCaller.onServiceCall(this);
        serviceCaller.getService().pullStoryToSprint(serviceCaller.getConversationNumber(), storyId, new DefaultCallback(this, returnHandler));
    }

    @Override
    public String toString() {
        return "PullStoryToSprint";
    }
}
