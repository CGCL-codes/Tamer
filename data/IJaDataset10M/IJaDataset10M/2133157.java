package com.liferay.portal.service;

/**
 * <a href="LayoutServiceFactory.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class is responsible for the lookup of the implementation for <code>com.liferay.portal.service.LayoutService</code>.
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
 * @see com.liferay.portal.service.LayoutService
 * @see com.liferay.portal.service.LayoutServiceUtil
 *
 */
public class LayoutServiceFactory {

    public static LayoutService getService() {
        return _getFactory()._service;
    }

    public static LayoutService getImpl() {
        if (_impl == null) {
            _impl = (LayoutService) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_IMPL);
        }
        return _impl;
    }

    public static LayoutService getTxImpl() {
        if (_txImpl == null) {
            _txImpl = (LayoutService) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_TX_IMPL);
        }
        return _txImpl;
    }

    public void setService(LayoutService service) {
        _service = service;
    }

    private static LayoutServiceFactory _getFactory() {
        if (_factory == null) {
            _factory = (LayoutServiceFactory) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_FACTORY);
        }
        return _factory;
    }

    private static final String _FACTORY = LayoutServiceFactory.class.getName();

    private static final String _IMPL = LayoutService.class.getName() + ".professional";

    private static final String _TX_IMPL = LayoutService.class.getName() + ".transaction";

    private static LayoutServiceFactory _factory;

    private static LayoutService _impl;

    private static LayoutService _txImpl;

    private LayoutService _service;
}
