package edu.univalle.lingweb.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CoSequenceUserHistoryId entity provides the base persistence definition of
 * the ceUserHistoryId entity.
 * 
 * @author LingWeb
 */
@Embeddable
public class CoSequenceUserHistoryId implements java.io.Serializable {

    private Long sequenceId;

    private Long userId;

    /** default constructor */
    public CoSequenceUserHistoryId() {
    }

    /** full constructor */
    public CoSequenceUserHistoryId(Long sequenceId, Long userId) {
        this.sequenceId = sequenceId;
        this.userId = userId;
    }

    @Column(name = "sequence_id", unique = false, nullable = false, insertable = true, updatable = true, precision = 15, scale = 0)
    public Long getSequenceId() {
        return this.sequenceId;
    }

    public void setSequenceId(Long sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Column(name = "user_id", unique = false, nullable = false, insertable = true, updatable = true, precision = 15, scale = 0)
    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof CoSequenceUserHistoryId)) return false;
        CoSequenceUserHistoryId castOther = (CoSequenceUserHistoryId) other;
        return ((this.getSequenceId() == castOther.getSequenceId()) || (this.getSequenceId() != null && castOther.getSequenceId() != null && this.getSequenceId().equals(castOther.getSequenceId()))) && ((this.getUserId() == castOther.getUserId()) || (this.getUserId() != null && castOther.getUserId() != null && this.getUserId().equals(castOther.getUserId())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getSequenceId() == null ? 0 : this.getSequenceId().hashCode());
        result = 37 * result + (getUserId() == null ? 0 : this.getUserId().hashCode());
        return result;
    }
}
