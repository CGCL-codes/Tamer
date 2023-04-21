package net.zcarioca.zscrum.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * An allocation of time devoted to a given sprint from a member of the project team.
 * 
 * @author zcarioca
 */
@Embeddable
public class SprintAllocation {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "hours_available")
    private float hoursAvailable;

    public SprintAllocation() {
        super();
    }

    public SprintAllocation(User user, float hoursAvailable) {
        this.user = user;
        this.hoursAvailable = hoursAvailable;
    }

    /**
    * Gets the user.
    * 
    * @return Returns the user.
    */
    public User getUser() {
        return this.user;
    }

    /**
    * Sets the user.
    * 
    * @param user The user.
    */
    public void setUser(User user) {
        this.user = user;
    }

    /**
    * The number of hours the user is available during the sprint. Used for capacity planning.
    * 
    * @return Returns the user's availability in hours.
    */
    public float getHoursAvailable() {
        return this.hoursAvailable;
    }

    /**
    * Sets the user's availability in hours.
    * 
    * @param hoursAvailable The user's availability in hours.
    */
    public void setHoursAvailable(float hoursAvailable) {
        this.hoursAvailable = hoursAvailable;
    }

    /**
    * {@inheritDoc}
    * 
    * @see java.lang.Object#hashCode()
    */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(hoursAvailable);
        result = prime * result + ((user == null) ? 0 : user.getUserId());
        return result;
    }

    /**
    * {@inheritDoc}
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SprintAllocation other = (SprintAllocation) obj;
        if (Float.floatToIntBits(hoursAvailable) != Float.floatToIntBits(other.hoursAvailable)) {
            return false;
        }
        if (getUser() == null) {
            if (other.getUser() != null) {
                return false;
            }
        } else if (getUser().getUserId() != other.getUser().getUserId()) {
            return false;
        }
        if (getHoursAvailable() != other.getHoursAvailable()) {
            return false;
        }
        return true;
    }
}
