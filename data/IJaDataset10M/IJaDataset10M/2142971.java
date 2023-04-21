package com.liferay.portal.service;

/**
 * <a href="ImageLocalServiceFactory.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class is responsible for the lookup of the implementation for <code>com.liferay.portal.service.ImageService</code>.
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
 * @see com.liferay.portal.service.ImageService
 * @see com.liferay.portal.service.ImageServiceUtil
 *
 */
public class ImageLocalServiceFactory {

    public static ImageLocalService getService() {
        return _getFactory()._service;
    }

    public static ImageLocalService getImpl() {
        if (_impl == null) {
            _impl = (ImageLocalService) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_IMPL);
        }
        return _impl;
    }

    public static ImageLocalService getTxImpl() {
        if (_txImpl == null) {
            _txImpl = (ImageLocalService) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_TX_IMPL);
        }
        return _txImpl;
    }

    public void setService(ImageLocalService service) {
        _service = service;
    }

    private static ImageLocalServiceFactory _getFactory() {
        if (_factory == null) {
            _factory = (ImageLocalServiceFactory) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_FACTORY);
        }
        return _factory;
    }

    private static final String _FACTORY = ImageLocalServiceFactory.class.getName();

    private static final String _IMPL = ImageLocalService.class.getName() + ".professional";

    private static final String _TX_IMPL = ImageLocalService.class.getName() + ".transaction";

    private static ImageLocalServiceFactory _factory;

    private static ImageLocalService _impl;

    private static ImageLocalService _txImpl;

    private ImageLocalService _service;
}
