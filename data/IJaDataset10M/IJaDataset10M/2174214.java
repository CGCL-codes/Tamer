package com.framedobjects.dashwell.mdninstantmsg.bean;

import com.itbs.aimcer.bean.Group;
import com.itbs.aimcer.bean.GroupFactory;
import com.itbs.aimcer.bean.GroupList;

public class GroupImplFactory implements GroupFactory {

    public Group create(String group) {
        return new GroupImpl();
    }

    public Group create(Group group) {
        return new GroupImpl();
    }

    public GroupList getGroupList() {
        return null;
    }
}
