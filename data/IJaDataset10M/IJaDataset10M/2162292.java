package org.zeroexchange.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Persistent with integer primary key.
 * 
 * @author black
 *
 */
@MappedSuperclass
public class IntegerPKPersistent extends Persistent implements Identifiable<Integer> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        IntegerPKPersistent other = (IntegerPKPersistent) obj;
        if (getId() == null || other.getId() == null) {
            return false;
        } else if (!getId().equals(other.getId())) return false;
        return true;
    }
}
