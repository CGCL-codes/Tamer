package com.liferay.portal.util;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Locale;
import javax.servlet.ServletContext;
import com.liferay.portal.model.Portlet;

/**
 * <a href="PortletTitleComparator.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.6 $
 *
 */
public class PortletTitleComparator implements Comparator, Serializable {

    public PortletTitleComparator(ServletContext application, Locale locale) {
        _application = application;
        _locale = locale;
    }

    public int compare(Object obj1, Object obj2) {
        Portlet portlet1 = (Portlet) obj1;
        Portlet portlet2 = (Portlet) obj2;
        String portlet1Title = PortalUtil.getPortletConfig(portlet1, _application).getResourceBundle(_locale).getString(WebKeys.JAVAX_PORTLET_TITLE);
        String portlet2Title = PortalUtil.getPortletConfig(portlet2, _application).getResourceBundle(_locale).getString(WebKeys.JAVAX_PORTLET_TITLE);
        return portlet1Title.compareTo(portlet2Title);
    }

    private ServletContext _application;

    private Locale _locale;
}
