package org.mybugs.tables;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

/**
 *
 * @author Zsolt Borcsok
 */
@Entity
@Table(name = "test_events")
@NamedQueries({  })
public class TestEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "eventid", nullable = false)
    private Short eventid;

    @Column(name = "name")
    private String name;

    public TestEvent() {
    }

    public TestEvent(Short eventid) {
        this.eventid = eventid;
    }

    public Short getEventid() {
        return eventid;
    }

    public void setEventid(Short eventid) {
        this.eventid = eventid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventid != null ? eventid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestEvent)) {
            return false;
        }
        TestEvent other = (TestEvent) object;
        if ((this.eventid == null && other.eventid != null) || (this.eventid != null && !this.eventid.equals(other.eventid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.mybugs.tables.TestEvent[eventid=" + eventid + "]";
    }
}
