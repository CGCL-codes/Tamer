package org.mangocms.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.mangocms.model.user.User;

/**
 * The base entity for content classes
 * 
 * @author <a href="tanordheim@gmail.com">Trond Arve Nordheim</a>
 * @version $Revision: 1.1 $
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "mango_content_entities")
public abstract class ContentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "title")
    @Length(max = 255)
    @NotNull
    private String title;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", referencedColumnName = "id")
    @NotNull
    private User createdBy;

    @Column(name = "created_time")
    @NotNull
    private Date createdTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by_id", referencedColumnName = "id")
    private User modifiedBy = null;

    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
	 * Create a new ContentEntity.
	 */
    protected ContentEntity() {
        createdTime = new Date();
    }

    /**
	 * Returns the internal name of the entity. Must be unique within the entire
	 * Mango scope.
	 * 
	 * @return
	 */
    public abstract String getEntityName();

    @Override
    public String toString() {
        return String.format("%s [#%d/%s]", getClass().getSimpleName(), getId(), getTitle());
    }

    /**
	 * Get the id.
	 * 
	 * @return the id.
	 */
    public Integer getId() {
        return id;
    }

    /**
	 * Set the id.
	 * 
	 * @param id The id to set.
	 */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * Get the title.
	 * 
	 * @return the title.
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * Set the title.
	 * 
	 * @param title The title to set.
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * Get the createdBy.
	 * 
	 * @return the createdBy.
	 */
    public User getCreatedBy() {
        return createdBy;
    }

    /**
	 * Set the createdBy.
	 * 
	 * @param createdBy The createdBy to set.
	 */
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    /**
	 * Get the createdTime.
	 * 
	 * @return the createdTime.
	 */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
	 * Set the createdTime.
	 * 
	 * @param createdTime The createdTime to set.
	 */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
	 * Get the modifiedBy.
	 * 
	 * @return the modifiedBy.
	 */
    public User getModifiedBy() {
        return modifiedBy;
    }

    /**
	 * Set the modifiedBy.
	 * 
	 * @param modifiedBy The modifiedBy to set.
	 */
    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
	 * Get the modifiedTime.
	 * 
	 * @return the modifiedTime.
	 */
    public Date getModifiedTime() {
        return modifiedTime;
    }

    /**
	 * Set the modifiedTime.
	 * 
	 * @param modifiedTime The modifiedTime to set.
	 */
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
