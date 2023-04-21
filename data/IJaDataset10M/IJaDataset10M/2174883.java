package com.liferay.portlet.blogs.service.spring;

import com.liferay.portal.spring.util.SpringUtil;
import org.springframework.context.ApplicationContext;

/**
 * <a href="BlogsCategoryServiceFactory.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class BlogsCategoryServiceFactory {

    public static final String CLASS_NAME = BlogsCategoryServiceFactory.class.getName();

    public static BlogsCategoryService getService() {
        ApplicationContext ctx = SpringUtil.getContext();
        BlogsCategoryServiceFactory factory = (BlogsCategoryServiceFactory) ctx.getBean(CLASS_NAME);
        return factory._service;
    }

    public void setService(BlogsCategoryService service) {
        _service = service;
    }

    private BlogsCategoryService _service;
}
