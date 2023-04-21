package com.acv.dao.catalog.categories.promotions.model;

import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.proxy.HibernateProxyHelper;
import com.acv.dao.catalog.model.GenericI18n;

/**
 * {@link Promotion} specific i18n model class. This class extends {@link GenericI18n} class
 * and so implements the {@link Map} interface.
 *
 * @author Mickael Guesnon
 * @hibernate.class table = "PROMOTION_I18N" lazy = "true" mutable = "false"
 * @hibernate.cache usage = "read-only"
 * @hibernate.composite-id unsaved-value="none"
 */
public class PromotionI18n extends GenericI18n {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The popup. */
    private Boolean popup;

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

    /**
	 * Gets the tagline.
	 *
	 * @return the tagline
	 *
	 * @hibernate.property column="TAGLINE" length="4000"
	 */
    public String getTagline() {
        return i18nMap.get("tagline");
    }

    /**
	 * Sets the tagline.
	 *
	 * @param tagline the new tagline
	 */
    public void setTagline(String tagline) {
        if (tagline != null) i18nMap.put("tagline", tagline);
    }

    /**
	 * Gets the type.
	 *
	 * @return the type
	 *
	 * @hibernate.property column="TYPE"
	 */
    public String getType() {
        return i18nMap.get("type");
    }

    /**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
    public void setType(String type) {
        if (type != null) i18nMap.put("type", type);
    }

    /**
	 * Gets the text title.
	 *
	 * @return the text title
	 *
	 * @hibernate.property column="TEXT_TITLE"
	 */
    public String getTextTitle() {
        return i18nMap.get("textTitle");
    }

    /**
	 * Sets the text title.
	 *
	 * @param textTitle the new text title
	 */
    public void setTextTitle(String textTitle) {
        if (textTitle != null) i18nMap.put("textTitle", textTitle);
    }

    /**
	 * Gets the text desc.
	 *
	 * @return the text desc
	 *
	 * @hibernate.property column="TEXT_DESC" type = "com.acv.dao.util.StringClobType"
	 */
    public String getTextDesc() {
        return i18nMap.get("textDesc");
    }

    /**
	 * Sets the text desc.
	 *
	 * @param textDesc the new text desc
	 */
    public void setTextDesc(String textDesc) {
        if (textDesc != null) i18nMap.put("textDesc", textDesc);
    }

    /**
	 * Gets the url value.
	 *
	 * @return the URL value
	 *
	 * @hibernate.property column="URL_VALUE"
	 */
    public String getURLValue() {
        return i18nMap.get("urlValue");
    }

    /**
	 * Sets the uRL value.
	 *
	 * @param urlValue the new uRL value
	 */
    public void setURLValue(String urlValue) {
        if (urlValue != null) i18nMap.put("urlValue", urlValue);
    }

    /**
	 * Gets the label.
	 *
	 * @return the label
	 *
	 * @hibernate.property column="LABEL"
	 */
    public String getLabel() {
        return i18nMap.get("label");
    }

    /**
	 * Sets the label.
	 *
	 * @param label the new label
	 */
    public void setLabel(String label) {
        if (label != null) i18nMap.put("label", label);
    }

    /**
	 * Gets the pdf value.
	 *
	 * @return the PDF value
	 *
	 * @hibernate.property column="PDF_VALUE"
	 */
    public String getPDFValue() {
        return i18nMap.get("pdfValue");
    }

    /**
	 * Sets the pDF value.
	 *
	 * @param pdfValue the new pDF value
	 */
    public void setPDFValue(String pdfValue) {
        if (pdfValue != null) i18nMap.put("pdfValue", pdfValue);
    }

    /**
	 * Gets the popup.
	 *
	 * @return the popup
	 *
	 * @hibernate.property column = "POPUP" type = "com.acv.dao.util.BooleanUserType"
	 */
    public Boolean getPopup() {
        return popup;
    }

    /**
	 * Sets the popup.
	 *
	 * @param popup the new popup
	 */
    public void setPopup(Boolean popup) {
        this.popup = popup;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (HibernateProxyHelper.getClassWithoutInitializingProxy(obj).equals(HibernateProxyHelper.getClassWithoutInitializingProxy(this))) {
            final PromotionI18n other = (PromotionI18n) obj;
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
