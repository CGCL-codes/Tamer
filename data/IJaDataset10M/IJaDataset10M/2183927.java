package net.zylk.kerozain.portal.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * <p>
 * This class is a wrapper for {@link CoreService}.
 * </p>
 *
 * @author    zylk.net
 * @see       CoreService
 * @generated
 */
public class CoreServiceWrapper implements CoreService, ServiceWrapper<CoreService> {

    public CoreServiceWrapper(CoreService coreService) {
        _coreService = coreService;
    }

    /**
	 * @deprecated Renamed to {@link #getWrappedService}
	 */
    public CoreService getWrappedCoreService() {
        return _coreService;
    }

    /**
	 * @deprecated Renamed to {@link #setWrappedService}
	 */
    public void setWrappedCoreService(CoreService coreService) {
        _coreService = coreService;
    }

    public CoreService getWrappedService() {
        return _coreService;
    }

    public void setWrappedService(CoreService coreService) {
        _coreService = coreService;
    }

    private CoreService _coreService;
}
