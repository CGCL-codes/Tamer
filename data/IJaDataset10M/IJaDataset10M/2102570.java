package org.openremote.web.console.client.rpc;

import org.openremote.web.console.domain.AppSetting;
import org.openremote.web.console.domain.UserInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserCacheRPCServiceAsync {

    void getUserInfo(AsyncCallback<UserInfo> callback);

    void saveUser(String username, String password, AsyncCallback<Void> callback);

    void getAppSetting(AsyncCallback<AppSetting> callback);

    void saveAppSetting(AppSetting appSetting, AsyncCallback<Void> callback);
}
