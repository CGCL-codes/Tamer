package net.deytan.wofee.gwt.client;

import net.deytan.wofee.gwt.bean.UserBean;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GWTUserService</code>.
 */
public interface GWTUserServiceAsync {

    void loginUser(AsyncCallback<UserBean> callback);

    void logoutUser(AsyncCallback<String> callback);

    void getUserName(AsyncCallback<String> callback);

    void addUser(AsyncCallback<String> callback);
}
