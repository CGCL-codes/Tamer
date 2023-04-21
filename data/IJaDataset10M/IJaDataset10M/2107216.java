package edu.univalle.lingweb.persistence;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractAgStatusExercises1 entity provides the base persistence definition of
 * the AgStatusExercises1 entity.
 * 
 * @author LingWeb
 */
@MappedSuperclass
public abstract class AbstractAgStatusExercises1 implements java.io.Serializable {

    private AgStatusExercises1Id id;

    private MaUser maUser;

    private CoExercises1 coExercises1;

    private AgStatus agStatus;

    /** default constructor */
    public AbstractAgStatusExercises1() {
    }

    /** full constructor */
    public AbstractAgStatusExercises1(AgStatusExercises1Id id, MaUser maUser, CoExercises1 coExercises1, AgStatus agStatus) {
        this.id = id;
        this.maUser = maUser;
        this.coExercises1 = coExercises1;
        this.agStatus = agStatus;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "exerciseId", column = @Column(name = "exercise_id", unique = false, nullable = false, insertable = true, updatable = true, precision = 15, scale = 0)), @AttributeOverride(name = "userId", column = @Column(name = "user_id", unique = false, nullable = false, insertable = true, updatable = true, precision = 15, scale = 0)) })
    public AgStatusExercises1Id getId() {
        return this.id;
    }

    public void setId(AgStatusExercises1Id id) {
        this.id = id;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = false, nullable = false, insertable = false, updatable = false)
    public MaUser getMaUser() {
        return this.maUser;
    }

    public void setMaUser(MaUser maUser) {
        this.maUser = maUser;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", unique = false, nullable = false, insertable = false, updatable = false)
    public CoExercises1 getCoExercises1() {
        return this.coExercises1;
    }

    public void setCoExercises1(CoExercises1 coExercises1) {
        this.coExercises1 = coExercises1;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", unique = false, nullable = false, insertable = true, updatable = true)
    public AgStatus getAgStatus() {
        return this.agStatus;
    }

    public void setAgStatus(AgStatus agStatus) {
        this.agStatus = agStatus;
    }
}
