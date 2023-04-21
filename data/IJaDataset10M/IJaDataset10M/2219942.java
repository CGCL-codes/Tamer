package com.liferay.portal.service.spring;

import com.liferay.portal.spring.util.SpringUtil;
import org.springframework.context.ApplicationContext;

/**
 * <a href="OrganizationLocalServiceFactory.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class OrganizationLocalServiceFactory {

    public static final String CLASS_NAME = OrganizationLocalServiceFactory.class.getName();

    public static OrganizationLocalService getService() {
        ApplicationContext ctx = SpringUtil.getContext();
        OrganizationLocalServiceFactory factory = (OrganizationLocalServiceFactory) ctx.getBean(CLASS_NAME);
        return factory._service;
    }

    public void setService(OrganizationLocalService service) {
        _service = service;
    }

    private OrganizationLocalService _service;
}
