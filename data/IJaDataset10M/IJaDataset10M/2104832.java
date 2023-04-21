package fi.arcusys.qnet.common.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.*;

/**
 * 
 *  @todo FIXME documentation
 * @author mikko
 *
 */
@Entity
@Proxy(lazy = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private UserAccount createdBy;

    private Date createdDate;

    private Note previousVersion;

    private String content;

    private List<ResourceFile> attachments;

    public Note() {
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public UserAccount getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserAccount createdBy) {
        this.createdBy = createdBy;
    }

    @Basic
    @Column(nullable = false)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @OneToOne
    @JoinColumn(nullable = true)
    public Note getPreviousVersion() {
        return previousVersion;
    }

    public void setPreviousVersion(Note previousVersion) {
        this.previousVersion = previousVersion;
    }

    @Basic
    @Column(length = 16384)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @OneToMany(cascade = { CascadeType.ALL })
    @OrderBy("name")
    public List<ResourceFile> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ResourceFile> attachments) {
        this.attachments = attachments;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        } else if (this == obj) {
            return true;
        } else {
            Note that = (Note) obj;
            return new EqualsBuilder().append(this.getCreatedBy(), that.getCreatedBy()).append(this.getCreatedDate(), that.getCreatedDate()).append(this.getContent(), that.getContent()).append(this.getPreviousVersion(), that.getPreviousVersion()).isEquals();
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getCreatedBy()).append(getCreatedDate()).append(getContent()).append(getPreviousVersion()).toHashCode();
    }
}
