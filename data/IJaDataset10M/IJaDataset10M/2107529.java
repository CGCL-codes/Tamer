package org.pojosoft.lms.web.gwt.client.user;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {

    void changePassword(String userId, String currentValue, String newValue, String verifyValue, AsyncCallback async);

    void changeEsig(String userId, String currentValue, String newValue, String verifyValue, AsyncCallback async);

    void addRoleToUser(String userId, String roleId, AsyncCallback async);

    void removeRoleFromUser(String userId, String roleId, AsyncCallback async);
}
