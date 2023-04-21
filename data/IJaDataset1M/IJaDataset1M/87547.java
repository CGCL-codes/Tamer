package com.liferay.portal.service;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;

/**
 * <a href="ThemeServiceFactory.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is responsible for the lookup of the implementation for
 * <code>com.liferay.portal.service.ThemeService</code>.
 * Spring manages the lookup and lifecycle of the beans. This means you can
 * modify the Spring configuration files to return a different implementation or
 * to inject additional behavior.
 * </p>
 *
 * <p>
 * See the <code>spring.configs</code> property in portal.properties for
 * additional information on how to customize the Spring XML files.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.ThemeService
 * @see com.liferay.portal.service.ThemeServiceUtil
 *
 */
public class ThemeServiceFactory {

    /**
	 * @deprecated
	 */
    public static ThemeService getService() {
        return _getFactory()._service;
    }

    /**
	 * @deprecated
	 */
    public static ThemeService getImpl() {
        if (_impl == null) {
            _impl = (ThemeService) PortalBeanLocatorUtil.locate(_IMPL);
        }
        return _impl;
    }

    /**
	 * @deprecated
	 */
    public static ThemeService getTxImpl() {
        if (_txImpl == null) {
            _txImpl = (ThemeService) PortalBeanLocatorUtil.locate(_TX_IMPL);
        }
        return _txImpl;
    }

    /**
	 * @deprecated
	 */
    public void setService(ThemeService service) {
        _service = service;
    }

    private static ThemeServiceFactory _getFactory() {
        if (_factory == null) {
            _factory = (ThemeServiceFactory) PortalBeanLocatorUtil.locate(_FACTORY);
        }
        return _factory;
    }

    private static final String _FACTORY = ThemeServiceFactory.class.getName();

    private static final String _IMPL = ThemeService.class.getName() + ".impl";

    private static final String _TX_IMPL = ThemeService.class.getName() + ".transaction";

    private static ThemeServiceFactory _factory;

    private static ThemeService _impl;

    private static ThemeService _txImpl;

    private ThemeService _service;
}
