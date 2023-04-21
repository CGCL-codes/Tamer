package com.angel.resourceBundle.internationalizer;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;
import com.angel.architecture.persistence.base.PersistentObject;

/**
 *
 * @author William
 * @since 17/April/2009
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "discriminatorInternationalizer")
@DiscriminatorValue("Internationalizer")
public abstract class Internationalizer extends PersistentObject implements Comparable<Object> {

    private static final long serialVersionUID = -140255376427134070L;

    @Column(length = 25, nullable = false, updatable = false)
    private String name;

    @Transient
    private Boolean current;

    public abstract Object getOwner();

    public abstract void setOwner(Object owner);

    public Internationalizer() {
        super();
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the current
	 */
    public Boolean getCurrent() {
        return current;
    }

    /**
	 * @param current the current to set
	 */
    public void setCurrent(Boolean current) {
        this.current = current;
    }
}
