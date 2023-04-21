package fr.devcoop.jee.tp6;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

/**
 *
 * @author lfo
 */
@Entity
@DiscriminatorValue("D")
@NamedQuery(name = "allDVDs", query = "select d from DVD d")
public class DVD extends Media implements Serializable {

    public DVD() {
    }

    public DVD(String title) {
        super(title);
    }

    public DVD(long duration) {
        this.duration = duration;
    }

    public DVD(String title, long duration) {
        super(title);
        this.duration = duration;
    }

    private long duration;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
