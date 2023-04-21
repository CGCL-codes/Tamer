package edu.princeton.wordnet.pojos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * WordNetPojos @author Bernard Bou
 */
@Entity
@Table(name = "vframes")
public class VerbFrame implements java.io.Serializable {

    private short id;

    private String frame;

    public VerbFrame() {
    }

    public VerbFrame(short id) {
        this.id = id;
    }

    public VerbFrame(short id, String frame) {
        this.id = id;
        this.frame = frame;
    }

    @Id
    @Column(name = "frameid", nullable = false)
    public short getId() {
        return this.id;
    }

    public void setId(short id) {
        this.id = id;
    }

    @Column(name = "frame", length = 50)
    public String getFrame() {
        return this.frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }
}
