package com.liferay.portlet.wiki.service;

/**
 * <a href="WikiPageLocalServiceFactory.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class is responsible for the lookup of the implementation for <code>com.liferay.portlet.wiki.service.WikiPageService</code>.
 * Spring manages the lookup and lifecycle of the beans. This means you can modify
 * the Spring configuration files to return a different implementation or to inject
 * additional behavior.
 * </p>
 *
 * <p>
 * See the <code>spring.configs</code> property in portal.properties for additional
 * information on how to customize the Spring XML files.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.wiki.service.WikiPageService
 * @see com.liferay.portlet.wiki.service.WikiPageServiceUtil
 *
 */
public class WikiPageLocalServiceFactory {

    public static WikiPageLocalService getService() {
        return _getFactory()._service;
    }

    public static WikiPageLocalService getImpl() {
        if (_impl == null) {
            _impl = (WikiPageLocalService) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_IMPL);
        }
        return _impl;
    }

    public static WikiPageLocalService getTxImpl() {
        if (_txImpl == null) {
            _txImpl = (WikiPageLocalService) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_TX_IMPL);
        }
        return _txImpl;
    }

    public void setService(WikiPageLocalService service) {
        _service = service;
    }

    private static WikiPageLocalServiceFactory _getFactory() {
        if (_factory == null) {
            _factory = (WikiPageLocalServiceFactory) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_FACTORY);
        }
        return _factory;
    }

    private static final String _FACTORY = WikiPageLocalServiceFactory.class.getName();

    private static final String _IMPL = WikiPageLocalService.class.getName() + ".professional";

    private static final String _TX_IMPL = WikiPageLocalService.class.getName() + ".transaction";

    private static WikiPageLocalServiceFactory _factory;

    private static WikiPageLocalService _impl;

    private static WikiPageLocalService _txImpl;

    private WikiPageLocalService _service;
}
