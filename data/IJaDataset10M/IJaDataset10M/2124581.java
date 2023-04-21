package org.fxplayer.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The Class TrackStatPK.
 */
@Embeddable
public class TrackStatPK implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8604016058093949865L;

    /** The track id. */
    @Column(name = "TRACK_ID")
    public int trackId;

    /** The user id. */
    @Column(name = "USER_ID")
    public int userId;

    /**
	 * Instantiates a new track stat pk.
	 */
    public TrackStatPK() {
        super();
    }

    /**
	 * Instantiates a new track stat pk.
	 * @param user
	 *          the user
	 * @param trackId
	 *          the track id
	 */
    public TrackStatPK(final User user, final int trackId) {
        super();
        this.trackId = trackId;
        userId = user.getId();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        return ((TrackStatPK) obj).trackId == trackId && ((TrackStatPK) obj).userId == userId;
    }

    @Override
    public int hashCode() {
        return (32 + userId) * 31 + trackId;
    }
}
