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
 * AbstractCoResponseCompleteE1 entity provides the base persistence definition
 * of the CoResponseCompleteE1 entity.
 * 
 * @author LingWeb
 */
@MappedSuperclass
public abstract class AbstractCoResponseCompleteE1 implements java.io.Serializable {

    private CoResponseCompleteE1Id id;

    private MaUser maUser;

    private CoCompleteE1 coCompleteE1;

    private String response;

    /** default constructor */
    public AbstractCoResponseCompleteE1() {
    }

    /** full constructor */
    public AbstractCoResponseCompleteE1(CoResponseCompleteE1Id id, MaUser maUser, CoCompleteE1 coCompleteE1, String response) {
        this.id = id;
        this.maUser = maUser;
        this.coCompleteE1 = coCompleteE1;
        this.response = response;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "completeE1Id", column = @Column(name = "complete_e1_id", unique = false, nullable = false, insertable = true, updatable = true, precision = 15, scale = 0)), @AttributeOverride(name = "userId", column = @Column(name = "user_id", unique = false, nullable = false, insertable = true, updatable = true, precision = 15, scale = 0)) })
    public CoResponseCompleteE1Id getId() {
        return this.id;
    }

    public void setId(CoResponseCompleteE1Id id) {
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
    @JoinColumn(name = "complete_e1_id", unique = false, nullable = false, insertable = false, updatable = false)
    public CoCompleteE1 getCoCompleteE1() {
        return this.coCompleteE1;
    }

    public void setCoCompleteE1(CoCompleteE1 coCompleteE1) {
        this.coCompleteE1 = coCompleteE1;
    }

    @Column(name = "response", unique = false, nullable = false, insertable = true, updatable = true)
    public String getResponse() {
        return this.response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
