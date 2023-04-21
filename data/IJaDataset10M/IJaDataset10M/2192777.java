package com.liferay.portlet.expando.service;

import com.liferay.portal.kernel.bean.BeanLocatorUtil;

/**
 * <a href="ExpandoRowServiceFactory.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is responsible for the lookup of the implementation for
 * <code>com.liferay.portlet.expando.service.ExpandoRowService</code>.
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
 * @see com.liferay.portlet.expando.service.ExpandoRowService
 * @see com.liferay.portlet.expando.service.ExpandoRowServiceUtil
 *
 */
public class ExpandoRowServiceFactory {

    public static ExpandoRowService getService() {
        return _getFactory()._service;
    }

    public static ExpandoRowService getImpl() {
        if (_impl == null) {
            _impl = (ExpandoRowService) BeanLocatorUtil.locate(_IMPL);
        }
        return _impl;
    }

    public static ExpandoRowService getTxImpl() {
        if (_txImpl == null) {
            _txImpl = (ExpandoRowService) BeanLocatorUtil.locate(_TX_IMPL);
        }
        return _txImpl;
    }

    public void setService(ExpandoRowService service) {
        _service = service;
    }

    private static ExpandoRowServiceFactory _getFactory() {
        if (_factory == null) {
            _factory = (ExpandoRowServiceFactory) BeanLocatorUtil.locate(_FACTORY);
        }
        return _factory;
    }

    private static final String _FACTORY = ExpandoRowServiceFactory.class.getName();

    private static final String _IMPL = ExpandoRowService.class.getName() + ".impl";

    private static final String _TX_IMPL = ExpandoRowService.class.getName() + ".transaction";

    private static ExpandoRowServiceFactory _factory;

    private static ExpandoRowService _impl;

    private static ExpandoRowService _txImpl;

    private ExpandoRowService _service;
}
