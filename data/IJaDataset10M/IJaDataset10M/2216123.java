package com.liferay.portal.service;

/**
 * <a href="ResourceCodeLocalServiceFactory.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class is responsible for the lookup of the implementation for <code>com.liferay.portal.service.ResourceCodeService</code>.
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
 * @see com.liferay.portal.service.ResourceCodeService
 * @see com.liferay.portal.service.ResourceCodeServiceUtil
 *
 */
public class ResourceCodeLocalServiceFactory {

    public static ResourceCodeLocalService getService() {
        return _getFactory()._service;
    }

    public static ResourceCodeLocalService getImpl() {
        if (_impl == null) {
            _impl = (ResourceCodeLocalService) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_IMPL);
        }
        return _impl;
    }

    public static ResourceCodeLocalService getTxImpl() {
        if (_txImpl == null) {
            _txImpl = (ResourceCodeLocalService) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_TX_IMPL);
        }
        return _txImpl;
    }

    public void setService(ResourceCodeLocalService service) {
        _service = service;
    }

    private static ResourceCodeLocalServiceFactory _getFactory() {
        if (_factory == null) {
            _factory = (ResourceCodeLocalServiceFactory) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_FACTORY);
        }
        return _factory;
    }

    private static final String _FACTORY = ResourceCodeLocalServiceFactory.class.getName();

    private static final String _IMPL = ResourceCodeLocalService.class.getName() + ".professional";

    private static final String _TX_IMPL = ResourceCodeLocalService.class.getName() + ".transaction";

    private static ResourceCodeLocalServiceFactory _factory;

    private static ResourceCodeLocalService _impl;

    private static ResourceCodeLocalService _txImpl;

    private ResourceCodeLocalService _service;
}
