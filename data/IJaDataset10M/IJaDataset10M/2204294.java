package net.zylk.kerozain.portal.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * <p>
 * This class is a wrapper for {@link UserInfoService}.
 * </p>
 *
 * @author    zylk.net
 * @see       UserInfoService
 * @generated
 */
public class UserInfoServiceWrapper implements UserInfoService, ServiceWrapper<UserInfoService> {

    public UserInfoServiceWrapper(UserInfoService userInfoService) {
        _userInfoService = userInfoService;
    }

    /**
	 * @deprecated Renamed to {@link #getWrappedService}
	 */
    public UserInfoService getWrappedUserInfoService() {
        return _userInfoService;
    }

    /**
	 * @deprecated Renamed to {@link #setWrappedService}
	 */
    public void setWrappedUserInfoService(UserInfoService userInfoService) {
        _userInfoService = userInfoService;
    }

    public UserInfoService getWrappedService() {
        return _userInfoService;
    }

    public void setWrappedService(UserInfoService userInfoService) {
        _userInfoService = userInfoService;
    }

    private UserInfoService _userInfoService;
}
