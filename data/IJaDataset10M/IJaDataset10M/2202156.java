package org.helianto.core;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.helianto.core.base.AbstractAssociation;
import org.helianto.core.def.AssociationState;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * User group associations.
 * 
 * @author Mauricio Fernandes de Castro	
 */
@javax.persistence.Entity
@Table(name = "core_userassoc", uniqueConstraints = { @UniqueConstraint(columnNames = { "parentId", "childId" }) })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("A")
public class UserAssociation extends AbstractAssociation<UserGroup, UserGroup> implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Date associationDate;

    private char resolution;

    /** 
     * Default constructor.
     */
    public UserAssociation() {
        super();
        setResolutionAsEnum(AssociationState.ACTIVE);
        setAssociationDate(new Date());
    }

    /** 
     * Parent constructor.
     * 
     * @param parent
     */
    public UserAssociation(UserGroup parent) {
        this();
        setParent(parent);
    }

    /** 
     * Child constructor.
     * 
     * @param parent
     * @param child
     */
    public UserAssociation(UserGroup parent, UserGroup child) {
        this(parent);
        setChild(child);
    }

    /** 
     * Key constructor.
     * 
     * @param parent
     * @param child
     */
    public UserAssociation(UserGroup parent, User child) {
        this(parent);
        setChild(child);
    }

    /** 
     * Credential constructor.
     * 
     * @param parent
     * @param childCredential
     */
    public UserAssociation(UserGroup parent, Credential childCredential) {
        this(parent, new User(parent.getEntity(), childCredential));
    }

    @Transient
    public void reset() {
        setResolution(' ');
        setAssociationDate(null);
    }

    /**
     * Parent user group.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId", nullable = true)
    public UserGroup getParent() {
        return this.parent;
    }

    /**
     * Child user group.
     */
    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "childId", nullable = true)
    public UserGroup getChild() {
        return this.child;
    }

    @Override
    protected int compareChild(AbstractAssociation<UserGroup, UserGroup> other) {
        if (this.getChild() != null && other.getChild() != null) {
            return this.getChild().compareTo((UserGroup) other.getChild());
        }
        return 0;
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

    /**
     * Association resolution.
     */
    public char getResolution() {
        return resolution;
    }

    public void setResolution(char resolution) {
        this.resolution = resolution;
    }

    public void setResolutionAsEnum(AssociationState associationState) {
        this.resolution = associationState.getValue();
    }

    /**
     * Association date.
     */
    @DateTimeFormat(style = "SS")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getAssociationDate() {
        return associationDate;
    }

    public void setAssociationDate(Date associationDate) {
        this.associationDate = associationDate;
    }

    /**
    * equals
    */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof UserAssociation)) return false;
        return super.equals(other);
    }
}
