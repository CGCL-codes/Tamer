package tinyuser.security;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Implementation of {@link java.security.Principal}.
 * 
 * @author Martin Algesten
 * 
 */
@SuppressWarnings("serial")
@Inheritance(strategy = InheritanceType.JOINED)
@Entity(name = "Principal")
public abstract class AbstractPrincipal implements java.security.Principal, Serializable, Comparable<java.security.Principal> {

    private String name;

    public AbstractPrincipal() {
    }

    /**
	 * Constructs a new object by setting the name.
	 * 
	 * @param name
	 */
    public AbstractPrincipal(String name) {
        if (name == null) throw new IllegalArgumentException("name can't be null");
        this.name = name;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    @Id
    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(java.security.Principal o) {
        String otherName = o.getName();
        if (name == null || otherName == null) return 0;
        return name.compareTo(otherName);
    }

    @Override
    public String toString() {
        return "Principal[" + name + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AbstractPrincipal other = (AbstractPrincipal) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }
}
