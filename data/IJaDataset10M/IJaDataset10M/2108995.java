package org.reward4j.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A {@code RateableAction} describes an action, that will be payed for, when
 * the action is executed. An action is always executed by an {@link User}.
 *
 * @author Peter Kehren <mailto:kehren@eyeslide.de>
 */
@Entity
public class RateableAction implements Serializable {

    private static final long serialVersionUID = -2900499255363027395L;

    /** name of the rateable action that is also used as unique identifier */
    @Id
    @Column(nullable = false)
    private String name;

    /**
   * Constructor.
   */
    protected RateableAction() {
    }

    public RateableAction(final String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", this.name).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.name).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        RateableAction rhs = (RateableAction) obj;
        return new EqualsBuilder().append(this.name, rhs.name).isEquals();
    }
}
