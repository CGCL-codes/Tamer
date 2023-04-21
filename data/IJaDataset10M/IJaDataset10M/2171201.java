package com.liferay.portal.service.spring;

import com.liferay.portal.spring.util.SpringUtil;
import org.springframework.context.ApplicationContext;

/**
 * <a href="OrganizationServiceFactory.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class OrganizationServiceFactory {

    public static final String CLASS_NAME = OrganizationServiceFactory.class.getName();

    public static OrganizationService getService() {
        ApplicationContext ctx = SpringUtil.getContext();
        OrganizationServiceFactory factory = (OrganizationServiceFactory) ctx.getBean(CLASS_NAME);
        return factory._service;
    }

    public void setService(OrganizationService service) {
        _service = service;
    }

    private OrganizationService _service;
}
