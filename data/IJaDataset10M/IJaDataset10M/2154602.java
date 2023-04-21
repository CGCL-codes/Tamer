package org.encuestame.persistence.domain;

import java.util.Calendar;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Attachment.
 * @author Morales, Diana Paola paolaATencuestame.org
 * @since Mar 28, 2011
 */
@Entity
@Table(name = "attachment")
public class Attachment {

    /** Attachment id**/
    private Long attachmentId;

    /** Filename**/
    private String filename;

    /** Attachment upload date. **/
    private Date uploadDate = Calendar.getInstance().getTime();

    /** Project. **/
    private Project projectAttachment;

    /**
    * @return the attachmentId.
    */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "attachment_id", unique = true, nullable = false)
    public Long getAttachmentId() {
        return attachmentId;
    }

    /**
    * @param attachmentId the attachmentId to set.
    */
    public void setAttachmentId(final Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    /**
     * @return the filename.
     */
    @Column(name = "filename", nullable = false)
    public String getFilename() {
        return filename;
    }

    /**
    * @param filename the filename to set.
    */
    public void setFilename(final String filename) {
        this.filename = filename;
    }

    /**
    * @return the uploadDate
    */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "uploadDate", nullable = true)
    public Date getUploadDate() {
        return uploadDate;
    }

    /**
    * @param uploadDate the uploadDate to set.
    */
    public void setUploadDate(final Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    /**
    * @return the projectAttachment
    */
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "project_id", nullable = false)
    public Project getProjectAttachment() {
        return projectAttachment;
    }

    /**
    * @param projectAttachment the projectAttachment to set
    */
    public void setProjectAttachment(final Project projectAttachment) {
        this.projectAttachment = projectAttachment;
    }
}
