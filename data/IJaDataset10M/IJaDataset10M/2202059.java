package com.liferay.iweb.service;

/**
 * <a href="CallBackLocalServiceFactory.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class CallBackLocalServiceFactory {

    public static CallBackLocalService getService() {
        return _getFactory()._service;
    }

    public static CallBackLocalService getImpl() {
        if (_impl == null) {
            _impl = (CallBackLocalService) com.liferay.util.bean.PortletBeanLocatorUtil.locate(_IMPL);
        }
        return _impl;
    }

    public static CallBackLocalService getTxImpl() {
        if (_txImpl == null) {
            _txImpl = (CallBackLocalService) com.liferay.util.bean.PortletBeanLocatorUtil.locate(_TX_IMPL);
        }
        return _txImpl;
    }

    public void setService(CallBackLocalService service) {
        _service = service;
    }

    private static CallBackLocalServiceFactory _getFactory() {
        if (_factory == null) {
            _factory = (CallBackLocalServiceFactory) com.liferay.util.bean.PortletBeanLocatorUtil.locate(_FACTORY);
        }
        return _factory;
    }

    private static final String _FACTORY = CallBackLocalServiceFactory.class.getName();

    private static final String _IMPL = CallBackLocalService.class.getName() + ".impl";

    private static final String _TX_IMPL = CallBackLocalService.class.getName() + ".transaction";

    private static CallBackLocalServiceFactory _factory;

    private static CallBackLocalService _impl;

    private static CallBackLocalService _txImpl;

    private CallBackLocalService _service;
}
