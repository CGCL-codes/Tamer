package uk.ac.ebi.intact.dataexchange.imex.repository.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * TODO comment this
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: RepoEntity.java 10281 2007-11-05 15:14:38Z baranda $
 */
@MappedSuperclass
public class RepoEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime created;

    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime updated;

    @PreUpdate
    public void setUpdatedDate() {
        setUpdated(new DateTime());
    }

    @PrePersist
    public void setCreatedDate() {
        DateTime date = new DateTime();
        setCreated(date);
        setUpdated(date);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public DateTime getUpdated() {
        return updated;
    }

    public void setUpdated(DateTime updated) {
        this.updated = updated;
    }
}
