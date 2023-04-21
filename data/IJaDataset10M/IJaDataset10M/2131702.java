package com.liferay.portal.service;

/**
 * <a href="OrganizationLocalServiceFactory.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class is responsible for the lookup of the implementation for <code>com.liferay.portal.service.OrganizationService</code>.
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
 * @see com.liferay.portal.service.OrganizationService
 * @see com.liferay.portal.service.OrganizationServiceUtil
 *
 */
public class OrganizationLocalServiceFactory {

    public static OrganizationLocalService getService() {
        return _getFactory()._service;
    }

    public static OrganizationLocalService getImpl() {
        if (_impl == null) {
            _impl = (OrganizationLocalService) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_IMPL);
        }
        return _impl;
    }

    public static OrganizationLocalService getTxImpl() {
        if (_txImpl == null) {
            _txImpl = (OrganizationLocalService) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_TX_IMPL);
        }
        return _txImpl;
    }

    public void setService(OrganizationLocalService service) {
        _service = service;
    }

    private static OrganizationLocalServiceFactory _getFactory() {
        if (_factory == null) {
            _factory = (OrganizationLocalServiceFactory) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_FACTORY);
        }
        return _factory;
    }

    private static final String _FACTORY = OrganizationLocalServiceFactory.class.getName();

    private static final String _IMPL = OrganizationLocalService.class.getName() + ".professional";

    private static final String _TX_IMPL = OrganizationLocalService.class.getName() + ".transaction";

    private static OrganizationLocalServiceFactory _factory;

    private static OrganizationLocalService _impl;

    private static OrganizationLocalService _txImpl;

    private OrganizationLocalService _service;
}
