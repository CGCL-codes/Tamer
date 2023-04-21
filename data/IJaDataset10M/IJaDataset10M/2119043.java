package com.liferay.portlet.imagegallery.service.spring;

import com.liferay.portal.spring.util.SpringUtil;
import org.springframework.context.ApplicationContext;

/**
 * <a href="IGFolderLocalServiceFactory.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class IGFolderLocalServiceFactory {

    public static final String CLASS_NAME = IGFolderLocalServiceFactory.class.getName();

    public static IGFolderLocalService getService() {
        ApplicationContext ctx = SpringUtil.getContext();
        IGFolderLocalServiceFactory factory = (IGFolderLocalServiceFactory) ctx.getBean(CLASS_NAME);
        return factory._service;
    }

    public void setService(IGFolderLocalService service) {
        _service = service;
    }

    private IGFolderLocalService _service;
}
