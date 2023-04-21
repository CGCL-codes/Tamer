package com.jot.admin;

import com.jot.system.Role;
import com.jot.system.messages.p2p.CoMid;
import com.jot.system.pjson.Guid;

public class PoolGotDotNet extends Pool {

    public String host = "207.111.197.7";

    public CoMid getConnectionPeer() {
        CoMid peer = new CoMid(new Guid("RemotePool"));
        peer.hostname = host;
        peer.port = 8080;
        peer.setName(new Guid("RemotePool"));
        peer.role = Role.Connection;
        return peer;
    }

    public void start() throws Exception {
    }
}
