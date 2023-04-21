package com.simconomy.dependicy.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @todo add comment for javadoc
 *
 * @author mrba
 * @version $Id: EntityClass.javajet,v 1.6 2008/01/22 21:09:06 jgilbert01 Exp $
 * @generated
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseField implements Serializable {

    /** @generated */
    private static final long serialVersionUID = 1L;

    /** @generated */
    public BaseField() {
    }

    /**
	 * @todo add comment for javadoc
	 * @generated
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    /** @generated */
    public void setId(final Long id) {
        this.id = id;
    }

    /** @generated */
    protected Long id = null;

    /**
	 * @todo add comment for javadoc
	 * @generated
	 */
    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Definition getDefinition() {
        return definition;
    }

    /** @generated */
    public void setDefinition(final Definition definition) {
        this.definition = definition;
    }

    /** @generated */
    private Definition definition = null;

    /**
	 * @todo add comment for javadoc
	 * @generated
	 */
    public String getName() {
        return name;
    }

    /** @generated */
    public void setName(final String name) {
        this.name = name;
    }

    /** @generated */
    private String name = null;

    /**
	 * @todo add comment for javadoc
	 * @generated
	 */
    public Boolean getFinalField() {
        return finalField;
    }

    /** @generated */
    public void setFinalField(final Boolean finalField) {
        this.finalField = finalField;
    }

    /** @generated */
    private Boolean finalField = null;

    /** @generated */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /** @generated */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /** @generated */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final BaseField other = (BaseField) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }
}
