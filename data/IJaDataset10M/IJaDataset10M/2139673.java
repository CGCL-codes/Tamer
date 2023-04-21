package com.dgtalize.netc.domain;

import java.net.InetAddress;

/**
 *
 * @author DGtalize
 */
public class UserManager {

    private UserTreeModel userModel;

    public UserManager() {
    }

    public UserTreeModel getUserModel() {
        return userModel;
    }

    public void prepareUserModel() throws Exception {
        userModel = new UserTreeModel();
        userModel.loadStructureFromXML("userList.xml");
    }

    public NetCUser findUsedByAddress(InetAddress address) {
        return (NetCUser) userModel.findUserNode(address).getUserObject();
    }
}
