package org.sss.common.model;

import java.io.Serializable;
import java.util.Locale;

/**
 * I18n类
 * @author Jason.Hoo (latest modification by $Author: hujianxin78728 $)
 * @version $Revision: 446 $ $Date: 2009-08-20 04:52:45 -0400 (Thu, 20 Aug 2009) $
 */
public class I18nValue implements Serializable {

    private String resourceName;

    private String resourceKey;

    public I18nValue(String resourceName, String resourceKey) {
        this.resourceName = resourceName;
        this.resourceKey = resourceKey;
    }

    public String toString(II18n i18n, Locale locale) {
        return i18n.getString(locale, resourceName, resourceKey);
    }
}
