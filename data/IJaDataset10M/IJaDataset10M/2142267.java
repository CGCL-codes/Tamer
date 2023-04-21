package com.liferay.portal.service;

/**
 * <a href="LayoutSetLocalServiceFactory.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class is responsible for the lookup of the implementation for <code>com.liferay.portal.service.LayoutSetService</code>.
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
 * @see com.liferay.portal.service.LayoutSetService
 * @see com.liferay.portal.service.LayoutSetServiceUtil
 *
 */
public class LayoutSetLocalServiceFactory {

    public static LayoutSetLocalService getService() {
        return _getFactory()._service;
    }

    public static LayoutSetLocalService getImpl() {
        if (_impl == null) {
            _impl = (LayoutSetLocalService) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_IMPL);
        }
        return _impl;
    }

    public static LayoutSetLocalService getTxImpl() {
        if (_txImpl == null) {
            _txImpl = (LayoutSetLocalService) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_TX_IMPL);
        }
        return _txImpl;
    }

    public void setService(LayoutSetLocalService service) {
        _service = service;
    }

    private static LayoutSetLocalServiceFactory _getFactory() {
        if (_factory == null) {
            _factory = (LayoutSetLocalServiceFactory) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_FACTORY);
        }
        return _factory;
    }

    private static final String _FACTORY = LayoutSetLocalServiceFactory.class.getName();

    private static final String _IMPL = LayoutSetLocalService.class.getName() + ".professional";

    private static final String _TX_IMPL = LayoutSetLocalService.class.getName() + ".transaction";

    private static LayoutSetLocalServiceFactory _factory;

    private static LayoutSetLocalService _impl;

    private static LayoutSetLocalService _txImpl;

    private LayoutSetLocalService _service;
}
