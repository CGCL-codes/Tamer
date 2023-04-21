package at.fhj.itm.model;

/**
 * Represents a Waypoints of a trip
 * 
 * @author Seuchter
 *
 */
public class Waypoint {

    private int id;

    private Location fromLocation;

    private Location toLocation;

    private User user;

    private String comment;

    private boolean active;

    /**
	 * Creates an new waypoint
	 * @param id id of the waypoint
	 * @param from_location "from"-location
	 * @param to_location "to"-location
	 * @param label label of the waypoint
	 * @param comment comment about the waypoint 
	 * @param active indication wether the waypoint is active
	 */
    public Waypoint(int id, Location from_location, Location to_location, User user, String comment, boolean active) {
        super();
        setId(id);
        setFrom_location(from_location);
        setToLocation(to_location);
        setUser(user);
        setComment(comment);
        setActive(active);
    }

    /**
	 * Creates an new waypoint with id -1
	 * @param id id of the waypoint
	 * @param from_location "from"-location
	 * @param to_location "to"-location
	 * @param label label of the waypoint
	 * @param comment comment about the waypoint 
	 * @param active indication wether the waypoint is active
	 */
    public Waypoint(Location from_location, Location to_location, User user, String comment, boolean active) {
        this(-1, from_location, to_location, user, comment, active);
    }

    /**
	 * Get's the waypoint's id
	 * @return
	 */
    public final int getId() {
        return id;
    }

    /**
	 * Set's a new id for the waypoint
	 * @param id id which is about to be set.
	 */
    public final void setId(int id) {
        this.id = id;
    }

    /**
	 * Gets the comment for the waypoint
	 * @return
	 */
    public final String getComment() {
        return comment;
    }

    /**
	 * Sets a comment for the waypoint.
	 * @param comment new comment which is to be set.
	 */
    public final void setComment(String comment) {
        this.comment = comment;
    }

    /**
	 * Indicates if the waypoint is in use
	 * @return true if the waypoint is active otherwise false.
	 */
    public final boolean isActive() {
        return active;
    }

    /**
	 * Sets if the waypoint is active
	 * @param active toggles the waypoint active or inactive
	 */
    public final void setActive(boolean active) {
        this.active = active;
    }

    public final Location getFromLocation() {
        return fromLocation;
    }

    public final void setFrom_location(Location fromLocation) {
        if (fromLocation == null) {
            throw new NullPointerException("fromLocation can't be null");
        }
        this.fromLocation = fromLocation;
    }

    public final Location getToLocation() {
        return toLocation;
    }

    public final void setToLocation(Location toLocation) {
        if (toLocation == null) {
            throw new NullPointerException("toLocation can't be null");
        }
        this.toLocation = toLocation;
    }

    public final User getUser() {
        return user;
    }

    public final void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Waypoint [id=" + id + ", from-location=" + fromLocation.toString() + ", to-location=" + toLocation.toString() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (active ? 1231 : 1237);
        result = prime * result + ((comment == null) ? 0 : comment.hashCode());
        result = prime * result + ((fromLocation == null) ? 0 : fromLocation.hashCode());
        result = prime * result + id;
        result = prime * result + ((toLocation == null) ? 0 : toLocation.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Waypoint other = (Waypoint) obj;
        if (active != other.active) return false;
        if (comment == null) {
            if (other.comment != null) return false;
        } else if (!comment.equals(other.comment)) return false;
        if (fromLocation == null) {
            if (other.fromLocation != null) return false;
        } else if (!fromLocation.equals(other.fromLocation)) return false;
        if (id != other.id) return false;
        if (toLocation == null) {
            if (other.toLocation != null) return false;
        } else if (!toLocation.equals(other.toLocation)) return false;
        if (user == null) {
            if (other.user != null) return false;
        } else if (!user.equals(other.user)) return false;
        return true;
    }
}
