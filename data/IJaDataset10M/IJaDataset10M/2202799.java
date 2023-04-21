package com.acv.dao.catalog.companies.airlines.model;

import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.proxy.HibernateProxyHelper;
import com.acv.dao.catalog.model.GenericI18n;

/**
 * {@link Airline} specific i18n model class. This class extends {@link GenericI18n} class
 * and so implements the {@link Map} interface.
 *
 * @author Mickael Guesnon
 * @hibernate.class table="AIRLINE_I18N" lazy = "true" mutable = "false"
 * @hibernate.cache usage = "read-only"
 * @hibernate.composite-id unsaved-value="none"
 */
public class AirlineI18n extends GenericI18n {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Gets the description.
	 *
	 * @return the description
	 *
	 * @hibernate.property column="DESCRIPTION" length = "4000"
	 */
    public String getDescription() {
        return i18nMap.get("description");
    }

    /**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
    public void setDescription(String description) {
        if (description != null) i18nMap.put("description", description);
    }

    /**
	 * Gets the id.
	 *
	 * @return the id
	 *
	 * @hibernate.key-property column="ID"
	 */
    public Long getId() {
        return Long.valueOf(i18nMap.get("id"));
    }

    /**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
    public void setId(Long id) {
        if (id != null) i18nMap.put("id", String.valueOf(id));
    }

    /**
	 * Gets the lang code.
	 *
	 * @return the lang code
	 *
	 * @hibernate.key-property column="LANG_CODE" length = "2"
	 */
    public String getLangCode() {
        return i18nMap.get("langCode");
    }

    /**
	 * Sets the lang code.
	 *
	 * @param langCode the new lang code
	 */
    public void setLangCode(String langCode) {
        if (langCode != null) i18nMap.put("langCode", langCode);
    }

    /**
	 * Gets the subtitle.
	 *
	 * @return the subtitle
	 *
	 * @hibernate.property column="SUBTITLE"
	 */
    public String getSubtitle() {
        return i18nMap.get("subtitle");
    }

    /**
	 * Sets the subtitle.
	 *
	 * @param subtitle the new subtitle
	 */
    public void setSubtitle(String subtitle) {
        if (subtitle != null) i18nMap.put("subtitle", subtitle);
    }

    /**
	 * Gets the title.
	 *
	 * @return the title
	 *
	 * @hibernate.property column="TITLE"
	 */
    public String getTitle() {
        return i18nMap.get("title");
    }

    /**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
    public void setTitle(String title) {
        if (title != null) i18nMap.put("title", title);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (HibernateProxyHelper.getClassWithoutInitializingProxy(obj).equals(HibernateProxyHelper.getClassWithoutInitializingProxy(this))) {
            final AirlineI18n other = (AirlineI18n) obj;
            return new EqualsBuilder().append(i18nMap, other.getI18nMap()).isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(i18nMap).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("entry map", i18nMap).toString();
    }
}
