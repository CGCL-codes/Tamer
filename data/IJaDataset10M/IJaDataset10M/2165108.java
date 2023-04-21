package org.sss.presentation.faces.common;

import org.sss.common.model.IField;
import org.sss.common.model.II18n;
import org.sss.presentation.faces.FacesUtils;

/**
 * MBean类����
 * @author Jason.Hoo (latest modification by $Author: hujianxin78728 $)
 * @version $Revision: 59 $ $Date: 2007-12-24 12:01:53 -0500 (Mon, 24 Dec 2007) $
 */
public class I18nBean extends AbstractMBean {

    public Object get(Object resourceUrl) {
        String url = (String) resourceUrl;
        int pos = url.indexOf(IField.URL_DELIMITER);
        II18n i18n = FacesUtils.getCurrentSupport().getI18nInstance();
        return i18n.getString(url.substring(0, pos), url.substring(pos + 1), FacesUtils.getCurrentLocale());
    }

    public Object put(String fieldUrl, Object value) {
        throw new UnsupportedOperationException("I18n bean counld'nt set value.");
    }
}
