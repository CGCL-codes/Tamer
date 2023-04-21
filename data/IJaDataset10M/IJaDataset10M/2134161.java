package de.hbrs.inf.atarrabi.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Class representing a technical quality check with filled fields like name, submitted, status, comment. 
 *
 * @author Florian Quadt
 */
@Entity
@XStreamAlias("TQA_CHECK")
public class TQACheck implements Serializable {

    private static final long serialVersionUID = 3415538900825467077L;

    @Id
    @GeneratedValue
    @XStreamOmitField
    private Long id;

    @XStreamAlias("NAME")
    private String name;

    @XStreamAlias("SUBMITTED")
    private boolean submitted;

    @XStreamAlias("STATUS")
    private String status;

    @XStreamAlias("COMMENT")
    private String comment;

    public TQACheck() {
    }

    public TQACheck(String checkName) {
        this.name = checkName;
        this.submitted = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
