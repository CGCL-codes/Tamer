package com.firescrum.bugtracking.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.firescrum.core.model.UserApp;
import com.firescrum.infrastructure.model.PersistentEntity;

/**
 * Attachment represents a file that has been attached to a given bug.
 * 
 * @author emanoelbarreiros
 */
@Entity
public final class Attachment extends PersistentEntity {

    /**
	 * The serial version UID
	 */
    private static final long serialVersionUID = -7502923582035743663L;

    /**
	 * Represents the unique identifier of the note.
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
	 * 
	 */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bugid")
    private Bug bug;

    /**
	 * Represents the date when the note was added.
	 */
    private Date createDate;

    /**
	 * A brief description of the file.
	 */
    private String description;

    /**
	 * The file name before uploading.
	 */
    private String fileName;

    /**
	 * 
	 */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userid")
    private UserApp user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Bug getBug() {
        return bug;
    }

    public void setBug(Bug bug) {
        this.bug = bug;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public UserApp getUser() {
        return user;
    }

    public void setUser(UserApp user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
