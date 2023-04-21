package org.intelligentsia.utility.jpa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import org.intelligentsia.utility.jpa.TimeStamp;
import org.intelligentsia.utility.jpa.TimeStamped;

/**
 * a page included in our site entity
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com">Jérôme Guibert</a>
 * @version 1.0.0
 */
@Entity
@Table(name = "PAGE")
public class Page implements Serializable, TimeStamped {

    private static final long serialVersionUID = -773904423928687747L;

    /**
     * Automatic version number
     */
    @Version
    protected Long version;

    /**
     * Identity with an automatic sequence
     */
    @Id
    @GeneratedValue(generator = "SEQ_PAGE_ID")
    @SequenceGenerator(name = "SEQ_PAGE_ID", sequenceName = "SEQ_PAGE_ID")
    @Column(name = "PAGE_ID")
    private Long id;

    /**
     * Name of  page
     */
    @Column(name = "NAME", length = 48, nullable = false)
    private String name;

    /**
     * Time stamp data embedded in this entity
     */
    @Embedded
    private TimeStamp timeStamp = new TimeStamp();

    /**
     * An example of many to many association
     */
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "pages", targetEntity = org.intelligentsia.utility.jpa.model.Tag.class)
    private List<Tag> tags = new ArrayList<Tag>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public TimeStamp getTimeStamp() {
        return timeStamp;
    }
}
