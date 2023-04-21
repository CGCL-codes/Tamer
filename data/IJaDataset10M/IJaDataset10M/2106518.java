package org.tolven.ctom.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Column;

/**
 * A written description of a change(s) to or formal clarification of a study.
 * 
 * BRIDG: A written description of a change(s) to or formal clarification of a
 * protocol.
 * @version 1.0
 * @created 27-Sep-2006 9:50:28 AM
 */
@Entity
@Table
public class Amendment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * The system generated unique identifier.
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CTOM_SEQ_GEN")
    private long id;

    /**
	 * Identifier to uniquely represent an amendment (version) of a protocol.
	 */
    private long identifier;

    /**
	 * Date of protocol amendment approval by IRB.  Amendments are versions of the
	 * original protocol document, each version with an eligibility checklist,
	 * informed consent, and complete revised protocol. 
	 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private java.util.Date irbApprovalDate;

    @ManyToOne
    private Study study;

    public Amendment() {
    }

    public void finalize() throws Throwable {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(long identifier) {
        this.identifier = identifier;
    }

    public java.util.Date getIrbApprovalDate() {
        return irbApprovalDate;
    }

    public void setIrbApprovalDate(java.util.Date irbApprovalDate) {
        this.irbApprovalDate = irbApprovalDate;
    }

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }
}
