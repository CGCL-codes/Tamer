package muvis.view.filters;

import utils.Util;

/**
 *
 * @author Ricardo
 */
public class MuVisMoodFilterNode extends MuVisFilterNode {

    private String mood;

    /**
     * @return the genre
     */
    public String getMood() {
        return mood;
    }

    /**
     * @param mood the mood to set
     */
    public void setMood(String mood) {
        this.mood = mood;
    }

    public MuVisMoodFilterNode() {
        super("Mood Filter");
    }

    protected MuVisMoodFilterNode(String filterName, MuVisFilterNode parent, ProgressStatus status) {
        super(filterName, parent, status);
    }

    @Override
    protected void buildTree(ProgressStatus status) {
        int k = 0;
        for (String vMood : Util.mood) {
            MuVisMoodFilterNode child = new MuVisMoodFilterNode(vMood, parent, status);
            child.setMood(vMood);
            child.setOrder(k++);
            addChild(child);
        }
    }
}
