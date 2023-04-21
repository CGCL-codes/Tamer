package com.arsenal.group.message;

import com.arsenal.message.*;
import com.arsenal.group.*;

public class CreateNewGroupMessage extends GroupBaseMessage {

    public void execute() {
        GroupManager.getInstance().createNewGroup((GroupBean) getPayload());
    }
}
