package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author mika
 */
@Entity
@Table(name = "orderedPreferencesEvents")
public class OrderedPreferenceEvent {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "orderedPreferencesEvents_id_seq")
    @GeneratedValue(generator = "sequence")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "orderedID", nullable = false)
    private Integer orderedId;

    @JoinColumn(name = "eventID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Event event;

    @Column(name = "firstPosition", nullable = false)
    private Boolean firstPosition;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Boolean getFirstPosition() {
        return firstPosition;
    }

    public void setFirstPosition(Boolean firstPosition) {
        this.firstPosition = firstPosition;
    }

    public Integer getOrderedId() {
        return orderedId;
    }

    public void setOrderedId(Integer orderedId) {
        this.orderedId = orderedId;
    }
}
