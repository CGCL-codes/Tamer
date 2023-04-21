package ucalgary.ebe.webui.client.data;

import com.google.gwt.user.client.rpc.IsSerializable;

public class StoryCardWeb implements IsSerializable {

    public static final String STATUS_DEFINED = "Defined";

    public static final String STATUS_TODO = "ToDo";

    public static final String STATUS_IN_PROGRESS = "In Progress";

    public static final String STATUS_COMPLETED = "Completed";

    public static final String DEFAULT_NAME = "Default Storycard";

    public static final String DEFAULT_DESC = "no description";

    public static final float DEFAULT_ESTIMATE = 0f;

    private long id, parent;

    private float actualEffort, bestCaseEstimate, mostLikelyEstimate, worstCaseEstimate;

    private String name, description, status;

    public StoryCardWeb() {
        this.id = 0;
        this.parent = 0;
        this.name = DEFAULT_NAME;
        this.description = DEFAULT_DESC;
        this.bestCaseEstimate = DEFAULT_ESTIMATE;
        this.mostLikelyEstimate = DEFAULT_ESTIMATE;
        this.worstCaseEstimate = DEFAULT_ESTIMATE;
        this.actualEffort = DEFAULT_ESTIMATE;
        this.status = STATUS_DEFINED;
    }

    public StoryCardWeb(long id, long parentid, String name, String description, float bestCaseEstimate, float mostlikelyEstimate, float worstCaseEstimate, float actualEffort, String status) {
        this.id = id;
        this.parent = parentid;
        this.name = name;
        this.description = description;
        this.bestCaseEstimate = bestCaseEstimate;
        this.mostLikelyEstimate = mostlikelyEstimate;
        this.worstCaseEstimate = worstCaseEstimate;
        this.actualEffort = actualEffort;
        this.status = status;
    }

    public StoryCardWeb(long parentid, String name, String description, float bestCaseEstimate, float mostlikelyEstimate, float worstCaseEstimate, float actualEffort, String status) {
        this.id = 0;
        this.parent = parentid;
        this.name = name;
        this.description = description;
        this.bestCaseEstimate = bestCaseEstimate;
        this.mostLikelyEstimate = mostlikelyEstimate;
        this.worstCaseEstimate = worstCaseEstimate;
        this.actualEffort = actualEffort;
        this.status = status;
    }

    public boolean equals(Object o) {
        if (o instanceof StoryCardWeb) {
            StoryCardWeb storycard = (StoryCardWeb) o;
            return storycard.getId() == this.getId();
        } else return false;
    }

    public float getActualEffort() {
        return this.actualEffort;
    }

    public float getBestCaseEstimate() {
        return this.bestCaseEstimate;
    }

    public String getDescription() {
        return this.description;
    }

    public long getId() {
        return this.id;
    }

    public float getMostlikelyEstimate() {
        return this.mostLikelyEstimate;
    }

    public String getName() {
        return this.name;
    }

    public long getParent() {
        return this.parent;
    }

    public float getWorstCaseEstimate() {
        return this.worstCaseEstimate;
    }

    public int hashCode() {
        return (int) getId();
    }

    public boolean isCompleted() {
        return this.status.equals(STATUS_COMPLETED);
    }

    public boolean isStarted() {
        return this.status.equals(STATUS_IN_PROGRESS);
    }

    public void setActualEffort(float actual) {
        this.actualEffort = actual;
    }

    public void setBestCaseEstimate(float bestcase) {
        this.bestCaseEstimate = bestcase;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMostlikelyEstimate(float mostlikely) {
        this.mostLikelyEstimate = mostlikely;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(long id) {
        this.parent = id;
    }

    public void setWorstCaseEstimate(float worstcase) {
        this.worstCaseEstimate = worstcase;
    }

    public String toString() {
        String storycard = "###################################\n" + "#           StoryCard             #\n" + "###################################\n\n" + "ID:\t\t" + this.id + "\n" + "Name:\t\t" + this.name + "\n" + "Parent:\t\t" + this.parent + "\n" + "Best case:\t" + this.bestCaseEstimate + "\n" + "Most likely:\t" + this.mostLikelyEstimate + "\n" + "Worst case:\t" + this.worstCaseEstimate + "\n" + "Actual:\t\t" + this.actualEffort + "\n" + "Completed:\t" + this.status + "\n\n";
        return storycard;
    }
}
