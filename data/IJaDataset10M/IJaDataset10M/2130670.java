package org.escapek.core.logging.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.escapek.core.logging.EventLevel;
import org.escapek.core.utils.StringTools;

@Entity
@Table(name = "LOG_EVENT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "EVENT_TYPE", discriminatorType = DiscriminatorType.STRING, length = 20)
public class Event implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 517029071598911967L;

    private Long id;

    private Date date;

    private Integer level;

    private Properties source;

    private String message;

    private Event parentEvent;

    private Set<Event> childrenEvent;

    public Event() {
        setDate(new Date());
        childrenEvent = new HashSet<Event>();
        source = new Properties();
        initSource();
    }

    public Event(Integer level, String message) {
        this();
        setLevel(level);
        setSource(source);
        setMessage(message);
    }

    public Event(Integer level, String message, Event parent) {
        this(level, message);
        if (parent != null) parent.addChild(this);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EVENT_ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "EVENT_DATE", columnDefinition = "TIMESTAMP(3)")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "EVENT_LEVEL")
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Column(name = "EVENT_SOURCE")
    @Lob
    public Properties getSource() {
        return source;
    }

    public void setSource(Properties source) {
        this.source = source;
    }

    @Column(name = "EVENT_MESSAGE")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    public Event getParentEvent() {
        return parentEvent;
    }

    public void setParentEvent(Event parentEvent) {
        this.parentEvent = parentEvent;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "parentEvent")
    @OrderBy("date DESC")
    public Set<Event> getChildrenEvent() {
        return childrenEvent;
    }

    public void setChildrenEvent(Set<Event> childrenEvent) {
        this.childrenEvent = childrenEvent;
    }

    public void addChild(Event anEvent) {
        anEvent.setParentEvent(this);
        getChildrenEvent().add(anEvent);
    }

    private void initSource() {
        try {
            source.put("java.version", System.getProperty("java.version"));
            source.put("os.arch", System.getProperty("os.arch"));
            source.put("os.name", System.getProperty("os.name"));
            source.put("os.version", System.getProperty("os.version"));
            source.put("user.name", System.getProperty("user.name"));
            source.put("host.name", java.net.InetAddress.getLocalHost().getHostName());
        } catch (Exception e) {
        }
    }

    public String toString() {
        String msg = StringTools.toStringNull(getMessage());
        if (msg.length() > 20) msg = msg.substring(0, 17).concat("...");
        StringBuffer buffer = new StringBuffer("(");
        buffer.append(this.getClass().getSimpleName()).append("#").append(StringTools.toStringNull(getId())).append(":[").append(EventLevel.getStringLevel(getLevel())).append("]").append(msg).append(")");
        return buffer.toString();
    }

    @Override
    public boolean equals(Object arg0) {
        if (arg0 == this) return true;
        if (arg0 instanceof Event) return id.equals(((Event) arg0).getId());
        return false;
    }
}
