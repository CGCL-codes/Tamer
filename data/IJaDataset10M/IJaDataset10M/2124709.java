package brainlink.core.model;

/**
 * The generic superclass of the main model elements in
 * NTE, namely the block and the line. This class encapsulates
 * the common data components both elements have, such as
 * a globally unique ID, modification time and so on.
 * @author Iain McGinniss
 */
public abstract class ModelElement {

    /**
	 * The globally unique identifier of this element.
	 */
    private NetworkID elementID;

    /**
	 * The last time that this element was modified
	 */
    private NetworkTime lastModified;

    /**
	 * The user who last modified this element.
	 */
    private User modifier;

    /**
	 * The element that precedes this one. The meaning of
	 * this is defined by the subtype.
	 */
    private NetworkID prevID;

    /**
	 * The element that follows on from this one. The meaning
	 * of this is defined by the subtype.
	 */
    private NetworkID nextID;

    /**
	 * Creates a model element with the specified details.
	 * @param networkID the id of this element, most likely extracted
	 * from the packet indicating it's existence or modification.
	 * @param modifier The user who last modified this element. As
	 * this element is most likely a new element, this would be the
	 * user who created this element.
	 * @param modifiedTime the last time at which this element was
	 * modified.
	 * @param prev the ID of the next element in the sequence (i.e.
	 * the next block or line, depending on what this element is).
	 * @param next the ID of the previous element in the sequence (i.e.
	 * the previous block or line, depending on what this element is).
	 */
    public ModelElement(NetworkID networkID, User modifier, NetworkTime modifiedTime, NetworkID prevID, NetworkID nextID) {
        assert (networkID != null) : "element ID is null";
        assert (!networkID.isNullID()) : "element ID is the null network ID";
        assert (modifier != null) : "modifier is null";
        assert (modifiedTime != null) : "last modified time is null";
        assert (prevID != null) : "previous id is null";
        assert (nextID != null) : "next id is null";
        this.elementID = networkID;
        this.modifier = modifier;
        this.lastModified = modifiedTime;
        this.prevID = prevID;
        this.nextID = nextID;
    }

    public NetworkID getElementID() {
        return elementID;
    }

    public NetworkTime getLastModified() {
        return lastModified;
    }

    public User getModifier() {
        return modifier;
    }

    public NetworkID getNextID() {
        return nextID;
    }

    public NetworkID getPrevID() {
        return prevID;
    }

    public void setLastModified(NetworkTime time) {
        assert (time != null);
        lastModified = time;
    }

    public void setModifier(User user) {
        assert (user != null);
        modifier = user;
    }

    public void setNextID(NetworkID networkID) {
        assert (networkID != null);
        nextID = networkID;
    }

    public void setPrevID(NetworkID networkID) {
        assert (networkID != null);
        prevID = networkID;
    }

    /**
	 * Model elements inherit their hashcode from their network ID.
	 */
    public int hashCode() {
        return elementID.hashCode();
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    public boolean equals(Object o) {
        if (o instanceof ModelElement) {
            return ((ModelElement) o).getElementID().equals(this.getElementID());
        } else {
            return false;
        }
    }
}
