package com.acv.dao.catalog.products.accomodations.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.proxy.HibernateProxyHelper;
import com.acv.dao.catalog.ObjectWithRepOrder;
import com.acv.dao.catalog.deals.model.AdaptedDeal;
import com.acv.dao.catalog.model.ContentWrapperMap;
import com.acv.dao.catalog.products.common.model.Product;

/**
 * An {@link Accomodation} is a subclass of {@link Product}.
 *
 * @author Mickael Guesnon
 * @hibernate.joined-subclass table = "ACCOMODATIONS" lazy = "true" mutable = "false"
 * @hibernate.cache usage = "read-only"
 * @hibernate.key column = "ID" on-delete="cascade"
 */
public class Accomodation extends Product implements ObjectWithRepOrder {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The content hbm. */
    private Map<String, AccomodationI18n> contentHbm = new HashMap<String, AccomodationI18n>();

    /** The adapted deals. */
    private Set<AdaptedDeal> adaptedDeals = new HashSet<AdaptedDeal>();

    /** The type. */
    private String type;

    /** The rep order. */
    private Long repOrder;

    /** The fk id. */
    private Long fkId;

    /**
	 * Gets the type.
	 *
	 * @return the type
	 *
	 * @hibernate.property column="TYPE"
	 */
    public String getType() {
        return type;
    }

    /**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
    public void setType(String type) {
        this.type = type;
    }

    /**
	 * Provide a {@link Set} of {@link AdaptedDeal} for which this {@link Accomodation} is specified.
	 *
	 * @return a {@link Set} of {@link AdaptedDeal}.
	 *
	 * @hibernate.set lazy = "true" inverse = "true"
	 * @hibernate.key column="ACCOMODATION_ID" on-delete="cascade"
	 * @hibernate.collection-one-to-many class="com.acv.dao.catalog.deals.model.AdaptedDeal"
	 */
    public Set<AdaptedDeal> getAdaptedDeals() {
        return adaptedDeals;
    }

    /**
	 * Sets the adapted deals.
	 *
	 * @param adaptedDeals the new adapted deals
	 */
    public void setAdaptedDeals(Set<AdaptedDeal> adaptedDeals) {
        this.adaptedDeals = adaptedDeals;
    }

    /**
	 * The i18n content of the current {@link Accomodation}. Provide a {@link Map} that contains {@link AccomodationI18n}
	 * objects mapped to their proper language code. Deprecated : use getContent() instead.
	 *
	 * @return A {@link Map} that link a language code to its {@link AccomodationI18n} object.
	 *
	 * @hibernate.map inverse="true" lazy = "false"
	 * @hibernate.key on-delete="cascade" column = "ID"
	 * @hibernate.map-key column = "LANG_CODE" type="java.lang.String"
	 * @hibernate.collection-one-to-many class =
	 * "com.acv.dao.catalog.products.accomodations.model.AccomodationI18n"
	 * @hibernate.cache usage = "read-only"
	 */
    @Deprecated
    public Map<String, AccomodationI18n> getContentHbm() {
        return contentHbm;
    }

    /**
	 * Sets the content hbm.
	 *
	 * @param contentHbm the content hbm
	 */
    @Deprecated
    public void setContentHbm(Map<String, AccomodationI18n> contentHbm) {
        this.contentHbm = contentHbm;
    }

    /**
	 * Gets the replicant order.
	 *
	 * @return the rep order
	 *
	 * @hibernate.property column = "REPORDER"
	 */
    public Long getRepOrder() {
        return repOrder;
    }

    /**
	 * Sets the replicant order.
	 *
	 * @param repOrder the new rep order
	 */
    public void setRepOrder(Long repOrder) {
        this.repOrder = repOrder;
    }

    /**
	 * Gets the fk id.
	 *
	 * @return the fk id
	 *
	 * @hibernate.property column="FK_ID"
	 */
    public Long getFkId() {
        return fkId;
    }

    /**
	 * Sets the fk id.
	 *
	 * @param fkId the new fk id
	 */
    public void setFkId(Long fkId) {
        this.fkId = fkId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) return false;
        if (HibernateProxyHelper.getClassWithoutInitializingProxy(obj).equals(HibernateProxyHelper.getClassWithoutInitializingProxy(this))) {
            Accomodation rhs = (Accomodation) obj;
            return new EqualsBuilder().appendSuper(super.equals(obj)).append(contentHbm, rhs.getContentHbm()).isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId()).append(getDateBegin()).append(this.getDateEnd()).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("contentHbm: ", contentHbm).toString();
    }

    public ContentWrapperMap<AccomodationI18n> getContent() {
        return new ContentWrapperMap<AccomodationI18n>(contentHbm, AccomodationI18n.class);
    }

    public Map<String, Boolean> getContainsKey() {
        return getContent().getKey();
    }
}
