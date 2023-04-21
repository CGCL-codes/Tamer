package bookshepherd.model;

/**
 * Describe many to many association Item to Group and Group to Item.
 */
public class ReferenceItemGroup extends Element {

    private long id;

    private ReferenceItem item;

    private ReferenceGroup group;

    /**
	 * Constructor.
	 */
    protected ReferenceItemGroup() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ReferenceItem getItem() {
        return item;
    }

    public void setItem(ReferenceItem item) {
        this.item = item;
    }

    public ReferenceGroup getGroup() {
        return group;
    }

    public void setGroup(ReferenceGroup group) {
        this.group = group;
    }
}
