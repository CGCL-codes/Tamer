package com.cafe.serve.view.usermanagement;

import com.cafe.serve.controller.usermanagement.UserManagementMainController;
import com.cafe.serve.view.AbstractGenericView;
import com.cafe.serve.view.usermanagement.panels.UserManagementMainPanel;

public class UserManagementMainView extends AbstractGenericView<UserManagementMainController, UserManagementMainPanel> {

    private UserManagementMainPanel userManagementMainPanel;

    public UserManagementMainView(final UserManagementMainController controller) {
        super(controller);
        controller.setView(this);
    }

    protected void initialize() {
        userManagementMainPanel = new UserManagementMainPanel();
        userManagementMainPanel.getCreateNewUserView().setParent(this);
        userManagementMainPanel.getCreatePermissionView().setParent(this);
        userManagementMainPanel.getCreateRoleView().setParent(this);
        userManagementMainPanel.getManageUsersView().setParent(this);
        setComponent(userManagementMainPanel);
    }
}
