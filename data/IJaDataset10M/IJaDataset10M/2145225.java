package architecture.ee.model.impl;

import java.io.Serializable;
import architecture.common.util.StringUtils;
import architecture.ee.i18n.I18nLocale;
import architecture.ee.model.I18nLocaleModel;

/**
 * @author  donghyuck
 */
public class I18nLocaleModelImpl extends BaseModelObject<I18nLocale> implements I18nLocaleModel, I18nLocale {

    /**
	 */
    private long localeId = -1L;

    /**
	 */
    private String language;

    /**
	 */
    private String country;

    /**
	 */
    private String encoding;

    /**
	 */
    private String variant;

    /**
	 * @return
	 */
    public long getLocaleId() {
        return localeId;
    }

    /**
	 * @param localeId
	 */
    public void setLocaleId(long localeId) {
        this.localeId = localeId;
    }

    /**
	 * @return
	 */
    public String getLanguage() {
        return language;
    }

    /**
	 * @param language
	 */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
	 * @return
	 */
    public String getCountry() {
        return country;
    }

    /**
	 * @param country
	 */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
	 * @return
	 */
    public String getEncoding() {
        return encoding;
    }

    /**
	 * @param encoding
	 */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
	 * @return
	 */
    public String getVariant() {
        return variant;
    }

    /**
	 * @param variant
	 */
    public void setVariant(String variant) {
        this.variant = variant;
    }

    public int compareTo(architecture.ee.i18n.I18nLocale o) {
        long pk = o.getLocaleId();
        if (getLocaleId() < pk) {
            return -1;
        } else if (getLocaleId() > pk) {
            return 1;
        } else {
            return 0;
        }
    }

    public java.util.Locale toJavaLocale() {
        return new java.util.Locale(language, StringUtils.isNotEmpty(country) ? country : "", StringUtils.isNotEmpty(variant) ? variant : "");
    }

    public int hashCode() {
        return (int) getLocaleId();
    }

    public Object clone() {
        I18nLocaleModelImpl impl = new I18nLocaleModelImpl();
        impl.setLocaleId(getLocaleId());
        impl.setLanguage(getLanguage());
        impl.setCountry(getCountry());
        impl.setVariant(getVariant());
        impl.setEncoding(getEncoding());
        impl.setCreationDate(getCreationDate());
        impl.setModifiedDate(getModifiedDate());
        return impl;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{localeId=");
        sb.append(getLocaleId());
        sb.append(", language=");
        sb.append(getLanguage());
        sb.append(", country=");
        sb.append(getCountry());
        sb.append(", variant=");
        sb.append(getVariant());
        sb.append(", creattionDate=");
        sb.append(getCreationDate());
        sb.append(", modifiedDate=");
        sb.append(getModifiedDate());
        sb.append("}");
        return sb.toString();
    }

    public Serializable getPrimaryKeyObject() {
        return getLocaleId();
    }

    public void setPrimaryKeyObject(Serializable primaryKeyObj) {
        setLocaleId(((Long) primaryKeyObj).longValue());
    }

    public int getObjectType() {
        return 10;
    }
}
