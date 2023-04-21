package org.helianto.document;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.helianto.core.base.AbstractAssociation;

/**
 * Function parent-child associations.
 * 
 * @author Mauricio Fernandes de Castro
 */
@javax.persistence.Entity
@Table(name = "doc_funcassoc", uniqueConstraints = { @UniqueConstraint(columnNames = { "parentId", "childId" }) })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("A")
public class FunctionAssociation extends AbstractAssociation<Role, Role> {

    private static final long serialVersionUID = 1L;

    /**
     * Associated parent function.
     */
    @ManyToOne
    @JoinColumn(name = "parentId", nullable = true)
    public Role getParent() {
        return parent;
    }

    /**
     * Associated child function.
     */
    @ManyToOne
    @JoinColumn(name = "childId", nullable = true)
    public Role getChild() {
        return child;
    }

    /**
     * Natural key info.
     */
    @Transient
    public boolean isKeyEmpty() {
        if (this.getChild() != null) {
            return this.getChild().isKeyEmpty();
        }
        throw new IllegalArgumentException("Natural key must not be null");
    }
}
