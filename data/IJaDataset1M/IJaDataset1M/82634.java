package net.taylor.portal.entity.menu;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * @todo add comment for javadoc
 *
 * @author jgilbert
 * @generated
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Menu implements Serializable, Cloneable {

    /** @generated */
    private static final long serialVersionUID = 1L;

    /** @generated */
    public Menu() {
    }

    /**
	 * ------------------------------------------------------------------------
	 * The primary key.
	 * ------------------------------------------------------------------------
	 * @generated
	 */
    @Id
    public String getId() {
        return id;
    }

    /** @generated */
    public void setId(final String id) {
        this.id = id;
    }

    /** @generated */
    private String id = null;

    /**
	 * ------------------------------------------------------------------------
	 * @todo add comment for javadoc
	 * ------------------------------------------------------------------------
	 * @generated
	 */
    public String getLabel() {
        return label;
    }

    /** @generated */
    public void setLabel(final String label) {
        this.label = label;
    }

    /** @generated */
    private String label = null;

    /**
	 * ------------------------------------------------------------------------
	 * @todo add comment for javadoc
	 * ------------------------------------------------------------------------
	 * @generated
	 */
    public String getRendered() {
        return rendered;
    }

    /** @generated */
    public void setRendered(final String rendered) {
        this.rendered = rendered;
    }

    /** @generated */
    private String rendered = null;

    /**
	 * ------------------------------------------------------------------------
	 * @todo add comment for javadoc
	 * ------------------------------------------------------------------------
	 * @generated
	 */
    public String getDisabled() {
        return disabled;
    }

    /** @generated */
    public void setDisabled(final String disabled) {
        this.disabled = disabled;
    }

    /** @generated */
    private String disabled = null;

    /**
	 * ------------------------------------------------------------------------
	 * @todo add comment for javadoc
	 * ------------------------------------------------------------------------
	 * @generated
	 */
    @Index(name = "MENU_SORT_IDX")
    public Long getSortOrder() {
        return sortOrder;
    }

    /** @generated */
    public void setSortOrder(final Long sortOrder) {
        this.sortOrder = sortOrder;
    }

    /** @generated */
    private Long sortOrder = null;

    /**
	 * ------------------------------------------------------------------------
	 * @todo add comment for javadoc
	 * ------------------------------------------------------------------------
	 * @generated
	 */
    public String getViewId() {
        return viewId;
    }

    /** @generated */
    public void setViewId(final String viewId) {
        this.viewId = viewId;
    }

    /** @generated */
    private String viewId = null;

    /**
	 * ------------------------------------------------------------------------
	 * @todo add comment for javadoc
	 * ------------------------------------------------------------------------
	 * @generated
	 */
    public String getConversationPropagation() {
        return conversationPropagation;
    }

    /** @generated */
    public void setConversationPropagation(final String conversationPropagation) {
        this.conversationPropagation = conversationPropagation;
    }

    /** @generated */
    private String conversationPropagation = null;

    /**
	 * ------------------------------------------------------------------------
	 * @todo add comment for javadoc
	 * ------------------------------------------------------------------------
	 * @generated
	 */
    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @ForeignKey(name = "FK_MENU_TO_PARNT")
    @Index(name = "MENU_PARNT_IDX")
    public Menu getParent() {
        return parent;
    }

    /** @generated */
    public void setParent(final Menu parent) {
        this.parent = parent;
    }

    /** @generated */
    private Menu parent = null;

    /** @generated */
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
        builder.append("id", getId());
        builder.append("label", getLabel());
        builder.append("rendered", getRendered());
        builder.append("disabled", getDisabled());
        builder.append("sortOrder", getSortOrder());
        builder.append("viewId", getViewId());
        builder.append("conversationPropagation", getConversationPropagation());
        builder.append("parent", getParent());
        return builder.toString();
    }

    /** @generated */
    public Menu deepClone() throws Exception {
        Menu clone = (Menu) super.clone();
        clone.setId(null);
        return clone;
    }

    /** @generated */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((id == null) ? super.hashCode() : id.hashCode());
        return result;
    }

    /** @generated */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Menu)) return false;
        final Menu other = (Menu) obj;
        if (id == null) {
            if (other.getId() != null) return false;
        } else if (!id.equals(other.getId())) return false;
        return true;
    }
}
