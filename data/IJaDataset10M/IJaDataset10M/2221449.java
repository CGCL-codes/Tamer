package org.yass.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * @author svenduzont
 */
@Embeddable
public class UserBrowsingContextPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "TRACK_INFO_ID")
    private int trackInfoId;

    @Column(name = "USER_ID")
    private int userId;

    /**
     *
     */
    public UserBrowsingContextPK() {
        super();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof UserBrowsingContextPK)) return false;
        final UserBrowsingContextPK other = (UserBrowsingContextPK) o;
        return userId == other.userId && trackInfoId == other.trackInfoId;
    }

    /**
	 * 
	 * @return
	 */
    public int getTrackInfoId() {
        return trackInfoId;
    }

    /**
	 * 
	 * @return
	 */
    public int getUserId() {
        return userId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + userId;
        hash = hash * prime + trackInfoId;
        return hash;
    }

    /**
	 * 
	 * @param trackInfoId
	 */
    public void setTrackInfoId(final int trackInfoId) {
        this.trackInfoId = trackInfoId;
    }

    /**
	 * 
	 * @param userId
	 */
    public void setUserId(final int userId) {
        this.userId = userId;
    }
}
