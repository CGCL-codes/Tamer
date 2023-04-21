package com.arsenal.user.message;

import com.arsenal.message.*;
import com.arsenal.user.*;
import com.arsenal.user.client.*;

public class CreateNewUserOnClientMessage extends UserBaseMessage {

    public void execute() {
        UserList.getInstance().addUserToInactiveUserList((UserBean) getPayload());
    }
}
