package net.sf.dlcdb.model.db.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *  POJO class DocumentModificationHistory used for storage of information
 *  about subsequent document changes
 *
 *  @author $Author: gregor8003 $
 *  @version $Rev: 46 $
 */
@Entity
@Table(name = "DOCUMENTMODIFICATIONHISTORY")
public class DocumentModificationHistory implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    /** Id of the object */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DOCUMENTMODIFICATIONHISTORY_ID")
    private Long id;

    /** Binary data of the document before change */
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @Column(name = "DOCUMENTMODIFICATIONHISTORY_DOCUMENTDATABEFORECHANGE")
    private DocumentData documentDataBeforeChange;

    /** Binary data of the document after change */
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @Column(name = "DOCUMENTMODIFICATIONHISTORY_DOCUMENTDATAADTERCHANGE")
    private DocumentData documentDataAfterChange;

    /** User who is the last modifier of this document */
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @Column(name = "DOCUMENTMODIFICATIONHISTORY_MODIFIER")
    private DocumentUser modifier;

    /** Date of last modification of this document */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DOCUMENTMODIFICATIONHISTORY_MODIFICATIONDATE")
    private Date modificationDate;

    /** Status of this document before change */
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @Column(name = "DOCUMENTMODIFICATIONHISTORY_STATUSBEFORECHANGE")
    private DocumentStatus statusBeforeChange;

    /** Status of this document after change */
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @Column(name = "DOCUMENTMODIFICATIONHISTORY_STATUSAFTERCHANGE")
    private DocumentStatus statusAfterChange;

    /** Major revision of this document before change */
    @Column(name = "DOCUMENTMODIFICATIONHISTORY_MAJORREVISIONBEFORECHANGE")
    private Integer majorRevisionBeforeChange;

    /** Major revision of this document after change */
    @Column(name = "DOCUMENTMODIFICATIONHISTORY_MAJORREVISIONAFTERCHANGE")
    private Integer majorRevisionAfterChange;

    /** Minor revision of this document before change */
    @Column(name = "DOCUMENTMODIFICATIONHISTORY_MINORREVISIONBEFORECHANGE")
    private Integer minorRevisionBeforeChange;

    /** Minor revision of this document after change */
    @Column(name = "DOCUMENTMODIFICATIONHISTORY_MINORREVISIONAFTERCHANGE")
    private Integer minorRevisionAfterChange;

    /**
     *  Creates a new DocumentModificationHistory object.
     */
    public DocumentModificationHistory() {
    }

    /**
	 *  Return binary data of the document after change
	 *
	 *  @return Binary data of the document after change
	 */
    public DocumentData getDocumentDataAfterChange() {
        return documentDataAfterChange;
    }

    /**
	 *  Return binary data of the document before change
	 *
	 *  @return Binary data of the document before change
	 */
    public DocumentData getDocumentDataBeforeChange() {
        return documentDataBeforeChange;
    }

    /**
	 *  Return ID of the object
	 *
	 *  @return ID of the object
	 */
    public Long getId() {
        return id;
    }

    /**
	 *  Return major revision of this document after change
	 *
	 *  @return Major revision of this document after change
	 */
    public Integer getMajorRevisionAfterChange() {
        return majorRevisionAfterChange;
    }

    /**
	 *  Return major revision of this document before change
	 *
	 *  @return Major revision of this document before change
	 */
    public Integer getMajorRevisionBeforeChange() {
        return majorRevisionBeforeChange;
    }

    /**
	 *  Return minor revision of this document after change
	 *
	 *  @return Minor revision of this document after change
	 */
    public Integer getMinorRevisionAfterChange() {
        return minorRevisionAfterChange;
    }

    /**
	 *  Return minor revision of this document before change
	 *
	 *  @return Minor revision of this document before change
	 */
    public Integer getMinorRevisionBeforeChange() {
        return minorRevisionBeforeChange;
    }

    /**
	 *  Return last modification date of this document
	 *
	 *  @return Last modification date of this document
	 */
    public Date getModificationDate() {
        return modificationDate;
    }

    /**
	 *  Return last modifier of this document
	 *
	 *  @return Last modifier of this document
	 */
    public DocumentUser getModifier() {
        return modifier;
    }

    /**
	 *  Return status of this document after change
	 *
	 *  @return Status of this document after change
	 */
    public DocumentStatus getStatusAfterChange() {
        return statusAfterChange;
    }

    /**
	 *  Return status of this document before change
	 *
	 *  @return Status of this document before change
	 */
    public DocumentStatus getStatusBeforeChange() {
        return statusBeforeChange;
    }

    /**
	 *  Set binary data of the document after change
	 *
	 *  @param documentDataAfterChange Binary data of the document after change
	 *  	   to set
	 */
    public void setDocumentDataAfterChange(DocumentData documentDataAfterChange) {
        this.documentDataAfterChange = documentDataAfterChange;
    }

    /**
	 *  Set binary data of the document before change
	 *
	 *  @param documentDataBeforeChange Binary data of the document before
	 *  	   change to set
	 */
    public void setDocumentDataBeforeChange(DocumentData documentDataBeforeChange) {
        this.documentDataBeforeChange = documentDataBeforeChange;
    }

    /**
	 *  Set ID of the object
	 *
	 *  @param id ID of the object to set
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 *  Set major revision of this document after change
	 *
	 *  @param majorRevisionAfterChange Major revision of this document after
	 *  	   change to set
	 */
    public void setMajorRevisionAfterChange(Integer majorRevisionAfterChange) {
        this.majorRevisionAfterChange = majorRevisionAfterChange;
    }

    /**
	 *  Set major revision of this document before change
	 *
	 *  @param majorRevisionBeforeChange Major revision of this document before
	 *  	   change to set
	 */
    public void setMajorRevisionBeforeChange(Integer majorRevisionBeforeChange) {
        this.majorRevisionBeforeChange = majorRevisionBeforeChange;
    }

    /**
	 *  Set minor revision of this document after change
	 *
	 *  @param minorRevisionAfterChange Minor revision of this document after
	 *  	   change to set
	 */
    public void setMinorRevisionAfterChange(Integer minorRevisionAfterChange) {
        this.minorRevisionAfterChange = minorRevisionAfterChange;
    }

    /**
	 *  Set minor revision of this document before change
	 *
	 *  @param minorRevisionBeforeChange Minor revision of this document before
	 *  	   change to set
	 */
    public void setMinorRevisionBeforeChange(Integer minorRevisionBeforeChange) {
        this.minorRevisionBeforeChange = minorRevisionBeforeChange;
    }

    /**
	 *  Set date of last modification of this document
	 *
	 *  @param modificationDate Date of last modification of this document to
	 *  	   set
	 */
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    /**
	 *  Set user who is the last modifier of this document
	 *
	 *  @param modifier User who is the last modifier of this document to set
	 */
    public void setModifier(DocumentUser modifier) {
        this.modifier = modifier;
    }

    /**
	 *  Set status of this document after change
	 *
	 *  @param statusAfterChange Status of this document after change to set
	 */
    public void setStatusAfterChange(DocumentStatus statusAfterChange) {
        this.statusAfterChange = statusAfterChange;
    }

    /**
	 *  Set status of this document before change
	 *
	 *  @param statusBeforeChange Status of this document before change to set
	 */
    public void setStatusBeforeChange(DocumentStatus statusBeforeChange) {
        this.statusBeforeChange = statusBeforeChange;
    }
}
