package edu.univalle.lingweb.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * AgStatusExercises2Id entity provides the base persistence definition of the
 * Exercises2Id entity.
 * 
 * @author LingWeb
 */
@Embeddable
public class AgStatusExercises2Id implements java.io.Serializable {

    private Long exerciseId;

    private Long userId;

    /** default constructor */
    public AgStatusExercises2Id() {
    }

    /** full constructor */
    public AgStatusExercises2Id(Long exerciseId, Long userId) {
        this.exerciseId = exerciseId;
        this.userId = userId;
    }

    @Column(name = "exercise_id", unique = false, nullable = false, insertable = true, updatable = true, precision = 15, scale = 0)
    public Long getExerciseId() {
        return this.exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
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
        if (!(other instanceof AgStatusExercises2Id)) return false;
        AgStatusExercises2Id castOther = (AgStatusExercises2Id) other;
        return ((this.getExerciseId() == castOther.getExerciseId()) || (this.getExerciseId() != null && castOther.getExerciseId() != null && this.getExerciseId().equals(castOther.getExerciseId()))) && ((this.getUserId() == castOther.getUserId()) || (this.getUserId() != null && castOther.getUserId() != null && this.getUserId().equals(castOther.getUserId())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getExerciseId() == null ? 0 : this.getExerciseId().hashCode());
        result = 37 * result + (getUserId() == null ? 0 : this.getUserId().hashCode());
        return result;
    }
}
