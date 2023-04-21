package woko.tracker.model;

import org.hibernate.validator.NotNull;
import org.hibernate.validator.Length;
import org.hibernate.validator.Min;
import org.hibernate.validator.Max;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.SearchableComponent;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import static com.mongus.beans.validation.BeanValidator.validate;

@Searchable
@Entity
public class Ticket implements TrackerObject {

    @SearchableId
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SearchableProperty
    @NotNull
    @Length(max = 50)
    private String name;

    @SearchableProperty
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    private Tracker tracker;

    @ManyToOne(fetch = FetchType.LAZY)
    private TrackerUser createdBy;

    @SearchableProperty
    @NotNull
    private TicketType type = TicketType.UNKNOWN;

    @SearchableProperty
    @Length(max = 5000)
    private String richText;

    @SearchableComponent
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ticket")
    private List<Comment> comments;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated = new Date();

    @SearchableProperty
    @NotNull
    private TicketStatus status = TicketStatus.OPEN;

    @NotNull
    @Max(10)
    @Min(1)
    private Integer priority = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    private TrackerUser updatedBy;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Attachment> attachments;

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
        validate(name);
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        validate(creationDate);
        this.creationDate = creationDate;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    public TrackerUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(TrackerUser createdBy) {
        this.createdBy = createdBy;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        validate(type);
        this.type = type;
    }

    public String getRichText() {
        return richText;
    }

    public void setRichText(String richText) {
        validate(richText);
        this.richText = richText;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        validate(status);
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        validate(priority);
        this.priority = priority;
    }

    public TrackerUser getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(TrackerUser updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        if (getId() != null ? !getId().equals(ticket.getId()) : ticket.getId() != null) return false;
        return true;
    }

    public int hashCode() {
        return (getId() != null ? getId().hashCode() : 0);
    }

    public boolean isOwnedBy(TrackerUser user) {
        TrackerUser owner = getCreatedBy();
        if (owner == null) {
            return true;
        } else {
            return owner.equals(user);
        }
    }
}
