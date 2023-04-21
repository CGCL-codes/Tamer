package eq.common.entity;

public class TaggedRecurringItem {

    private int tag;

    private int recItem;

    public TaggedRecurringItem() {
    }

    public TaggedRecurringItem(int tag, int recItem) {
        this.tag = tag;
        this.recItem = recItem;
    }

    /**
	 * @param tag the tag to set
	 */
    public void setTag(int tag) {
        this.tag = tag;
    }

    /**
	 * @return the tag
	 */
    public int getTag() {
        return tag;
    }

    /**
	 * @param recItem the recItem to set
	 */
    public void setRecItem(int recItem) {
        this.recItem = recItem;
    }

    /**
	 * @return the recItem
	 */
    public int getRecItem() {
        return recItem;
    }
}
