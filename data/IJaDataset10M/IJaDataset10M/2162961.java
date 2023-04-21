package architecture.ee.model.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import architecture.ee.i18n.I18nLocale;
import architecture.ee.i18n.I18nLocalizer;
import architecture.ee.i18n.I18nTextResourceBundle;

/**
 * @author  donghyuck
 */
public class I18nLocalizerModelImpl extends BaseModelObject<I18nLocalizer> implements I18nLocalizer {

    private static final Log log = LogFactory.getLog(I18nLocalizerModelImpl.class);

    /**
	 */
    private long localizerId = -1L;

    /**
	 */
    private String description;

    /**
	 */
    private String name;

    /**
	 */
    private I18nLocale i18nLocale;

    /**
	 */
    private long localeId = -1L;

    /**
	 */
    private ResourceBundle resourceBundle;

    private Map<String, String> texts;

    public I18nLocalizerModelImpl() {
        this.i18nLocale = null;
        this.description = null;
        this.resourceBundle = null;
        this.name = null;
        this.texts = new HashMap<String, String>();
    }

    /**
	 * @return
	 */
    public ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            List<String[]> keyValues = new ArrayList<String[]>();
            for (String key : texts.keySet()) {
                keyValues.add(new String[] { key, texts.get(key) });
            }
            if (getI18nLocale() != null) {
                this.resourceBundle = new I18nTextResourceBundle(keyValues, getI18nLocale().toJavaLocale());
            } else {
                this.resourceBundle = new I18nTextResourceBundle(keyValues);
            }
        }
        return resourceBundle;
    }

    /**
	 * @param resourceBundle
	 */
    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public Serializable getPrimaryKeyObject() {
        return getLocalizerId();
    }

    public void setPrimaryKeyObject(Serializable primaryKeyObj) {
        setLocalizerId(((Long) primaryKeyObj).longValue());
    }

    public int getObjectType() {
        return 11;
    }

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

    public int compareTo(I18nLocalizer o) {
        long pk = o.getLocalizerId();
        if (getLocalizerId() < pk) {
            return -1;
        } else if (getLocalizerId() > pk) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
	 * @return
	 */
    public long getLocalizerId() {
        return localizerId;
    }

    /**
	 * @param localizerId
	 */
    public void setLocalizerId(long localizerId) {
        this.localizerId = localizerId;
    }

    /**
	 * @return
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param resourceBundleName
	 */
    public void setName(String resourceBundleName) {
        this.name = resourceBundleName;
    }

    /**
	 * @return
	 */
    public I18nLocale getI18nLocale() {
        return i18nLocale;
    }

    /**
	 * @param i18nLocale
	 */
    public void setI18nLocale(I18nLocale i18nLocale) {
        this.i18nLocale = i18nLocale;
    }

    @Override
    public Object clone() {
        I18nLocalizerModelImpl impl = new I18nLocalizerModelImpl();
        impl.setLocalizerId(localizerId);
        impl.setName(name);
        impl.setDescription(description);
        impl.setI18nLocale(i18nLocale);
        impl.setI18nTexts(getI18nTexts());
        impl.setCreationDate(getCreationDate());
        impl.setModifiedDate(getModifiedDate());
        return impl;
    }

    public void setI18nTexts(Map<String, String> texts) {
        this.texts = texts;
        this.resourceBundle = null;
        getResourceBundle();
    }

    public Map<String, String> getI18nTexts() {
        return texts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{localizerId=");
        sb.append(getLocalizerId());
        sb.append(", name=");
        sb.append(getName());
        sb.append(", contents=");
        sb.append(getI18nTexts());
        sb.append(", locale=");
        sb.append(getI18nLocale());
        sb.append(", country=");
        sb.append(", creattionDate=");
        sb.append(getCreationDate());
        sb.append(", modifiedDate=");
        sb.append(getModifiedDate());
        sb.append("}");
        return sb.toString();
    }

    public String getString(String key) {
        return texts.get(key);
    }
}
