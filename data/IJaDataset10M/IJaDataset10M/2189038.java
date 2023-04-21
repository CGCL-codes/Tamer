package com.liferay.portal.service;

/**
 * <a href="PermissionLocalServiceFactory.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class is responsible for the lookup of the implementation for <code>com.liferay.portal.service.PermissionService</code>.
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
 * @see com.liferay.portal.service.PermissionService
 * @see com.liferay.portal.service.PermissionServiceUtil
 *
 */
public class PermissionLocalServiceFactory {

    public static PermissionLocalService getService() {
        return _getFactory()._service;
    }

    public static PermissionLocalService getImpl() {
        if (_impl == null) {
            _impl = (PermissionLocalService) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_IMPL);
        }
        return _impl;
    }

    public static PermissionLocalService getTxImpl() {
        if (_txImpl == null) {
            _txImpl = (PermissionLocalService) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_TX_IMPL);
        }
        return _txImpl;
    }

    public void setService(PermissionLocalService service) {
        _service = service;
    }

    private static PermissionLocalServiceFactory _getFactory() {
        if (_factory == null) {
            _factory = (PermissionLocalServiceFactory) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_FACTORY);
        }
        return _factory;
    }

    private static final String _FACTORY = PermissionLocalServiceFactory.class.getName();

    private static final String _IMPL = PermissionLocalService.class.getName() + ".professional";

    private static final String _TX_IMPL = PermissionLocalService.class.getName() + ".transaction";

    private static PermissionLocalServiceFactory _factory;

    private static PermissionLocalService _impl;

    private static PermissionLocalService _txImpl;

    private PermissionLocalService _service;
}
