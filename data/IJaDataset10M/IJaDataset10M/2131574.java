package com.sin.server;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.sin.shared.FbFriend;

public class FBFriends implements IsSerializable {

    private static final Logger log = Logger.getLogger(FBFriends.class.getName());

    private List<FbFriend> data = new LinkedList<FbFriend>();

    public List<FbFriend> getData() {
        return data;
    }

    public void setData(final List<FbFriend> data) {
        this.data = data;
    }
}
