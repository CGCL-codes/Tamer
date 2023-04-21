package org.mangocms.core.entities.structure;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.mangocms.core.entities.user.User;

/**
 * Describes a category of the Mango site, beneath a section, containing
 * content
 * 
 * @author trond
 */
@Entity
@Table(name = "categories")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "category_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "category_sequence")
    @SequenceGenerator(name = "category_sequence", sequenceName = "seq_categories")
    private Integer categoryId = null;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    @NotNull
    private Section section = null;

    @Column(name = "title", nullable = false, length = 255)
    @Length(max = 255)
    @NotNull
    private String title = null;

    @Column(name = "description", nullable = true)
    private String description = null;

    @Column(name = "published", nullable = false)
    private Boolean published = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "user_id", nullable = false)
    @NotNull
    private User createdBy = null;

    @Column(name = "created_time", nullable = false)
    @NotNull
    private Date createdTime = new Date();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by", referencedColumnName = "user_id", nullable = true)
    private User lastModifiedBy = null;

    @Column(name = "last_modified_time", nullable = true)
    private Date lastModifiedTime = null;

    /**
	 * Construct a new category
	 */
    public Category() {
    }

    /**
	 * Construct a new category
	 * @param section
	 */
    public Category(Section section) {
        this.section = section;
    }

    /**
	 * @return the categoryId
	 */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
	 * @param categoryId the categoryId to set
	 */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
	 * @return the section
	 */
    public Section getSection() {
        return section;
    }

    /**
	 * @param section the section to set
	 */
    public void setSection(Section section) {
        this.section = section;
    }

    /**
	 * @return the title
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * @param title the title to set
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description the description to set
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return the published
	 */
    public Boolean getPublished() {
        return published;
    }

    /**
	 * @param published the published to set
	 */
    public void setPublished(Boolean published) {
        this.published = published;
    }

    /**
	 * @return the createdBy
	 */
    public User getCreatedBy() {
        return createdBy;
    }

    /**
	 * @param createdBy the createdBy to set
	 */
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    /**
	 * @return the createdTime
	 */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
	 * @param createdTime the createdTime to set
	 */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
	 * @return the lastModifiedBy
	 */
    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
	 * @param lastModifiedBy the lastModifiedBy to set
	 */
    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
	 * @return the lastModifiedTime
	 */
    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    /**
	 * @param lastModifiedTime the lastModifiedTime to set
	 */
    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
}
