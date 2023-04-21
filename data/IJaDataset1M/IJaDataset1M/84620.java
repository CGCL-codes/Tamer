package ca.ucalgary.cpsc.agilePlanner.persister.impl.data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import ca.ucalgary.cpsc.agilePlanner.persister.IndexCardWithChild;
import ca.ucalgary.cpsc.agilePlanner.persister.StoryCard;

public class IndexCardWithChildDataObject extends IndexCardDataObject implements IndexCardWithChild {

    private static final long serialVersionUID = 1384807442340718616L;

    protected Timestamp startdate, enddate;

    protected float availableEffort;

    List<StoryCard> storycards = new ArrayList<StoryCard>();

    public IndexCardWithChildDataObject() {
        super();
        this.availableEffort = 0f;
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        this.startdate = ts;
        this.enddate = ts;
    }

    public void addStoryCard(StoryCard storycard) {
        storycard.setParent(this.getId());
        this.storycards.add(storycard);
    }

    public List<StoryCard> getStoryCardChildren() {
        return this.storycards;
    }

    public void removeStoryCard(StoryCard storycard) {
        this.storycards.remove(storycard);
    }

    public void setStoryCardChildren(List<StoryCard> storycards) {
        this.storycards = storycards;
    }

    public Timestamp getStartDate() {
        return this.startdate;
    }

    public boolean isCompleted() {
        return this.status.equals(STATUS_COMPLETED);
    }

    public boolean isStarted() {
        return this.status.equals(STATUS_IN_PROGRESS);
    }

    public void setAvailableEffort(float availableEffort) {
        this.availableEffort = availableEffort;
    }

    public void setEndDate(Timestamp enddate) {
        this.enddate = enddate;
    }

    public void setStartDate(Timestamp startdate) {
        this.startdate = startdate;
    }

    public float getAvailableEffort() {
        return this.availableEffort;
    }

    public Timestamp getEndDate() {
        return this.enddate;
    }

    public float getRemainingEffort() {
        float sumStoryCard = 0;
        for (StoryCard storycard : getStoryCardChildren()) {
            sumStoryCard += storycard.getMostlikelyEstimate();
        }
        return (availableEffort - sumStoryCard);
    }
}
