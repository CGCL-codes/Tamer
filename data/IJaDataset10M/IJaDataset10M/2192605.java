package net.sf.brightside.instantevents.core.beans;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseBean implements Serializable, Identifiable {

    private static final long serialVersionUID = 1685615540034161333L;

    private Long _id;

    public BaseBean() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long get_id() {
        return _id;
    }

    public void set_id(Long id) {
        this._id = id;
    }

    public void set_id(Serializable id) {
        this.set_id((Long) id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Identifiable)) {
            return false;
        }
        Identifiable other = (Identifiable) o;
        if (get_id() == null) return false;
        return get_id().equals(other.get_id());
    }
}
