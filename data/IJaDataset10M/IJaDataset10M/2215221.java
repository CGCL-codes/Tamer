package at.riemers.zero.base.model;

import at.riemers.zero.base.util.IdGenerator;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.apache.log4j.Logger;

@MappedSuperclass
public class PersistentUUID implements Serializable {

    private static final Logger log = Logger.getLogger(PersistentUUID.class);

    private String id = IdGenerator.createId();

    @Id
    @Column(length = 40)
    public String getId() {
        return id;
    }

    public void setId(String long1) {
        id = long1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        final PersistentUUID other = (PersistentUUID) obj;
        if (this.id == null || other.getId() == null) {
            return false;
        }
        if (this.id != other.getId() && (this.id == null || !this.id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
