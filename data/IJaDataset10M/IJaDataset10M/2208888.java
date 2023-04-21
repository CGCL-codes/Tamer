package com.liferay.portal.util.comparator;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.model.PortletCategory;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Locale;

/**
 * <a href="PortletCategoryComparator.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class PortletCategoryComparator implements Comparator, Serializable {

    public PortletCategoryComparator(long companyId, Locale locale) {
        _companyId = companyId;
        _locale = locale;
    }

    public int compare(Object obj1, Object obj2) {
        PortletCategory portletCategory1 = (PortletCategory) obj1;
        PortletCategory portletCategory2 = (PortletCategory) obj2;
        String name1 = LanguageUtil.get(_companyId, _locale, portletCategory1.getName());
        String name2 = LanguageUtil.get(_companyId, _locale, portletCategory2.getName());
        return name1.compareTo(name2);
    }

    private long _companyId;

    private Locale _locale;
}
