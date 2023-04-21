package com.arsenal.user.message;

import com.arsenal.message.*;
import com.arsenal.user.*;
import com.arsenal.user.client.*;

public class RemoveFromClientUserListMessage extends UserBaseMessage {

    public void execute() {
        UserList.getInstance().removeUser((UserBean) getPayload());
    }
}
