package com.jot.admin;

import com.jot.system.Role;
import com.jot.system.messages.p2p.CoMid;
import com.jot.system.pjson.Guid;

public class PoolLocal extends Pool {

    public CoMid getConnectionPeer() {
        CoMid peer = new CoMid(new Guid("LocalPool"));
        peer.hostname = "localhost";
        peer.port = 8080;
        peer.setName(new Guid("LocalPool"));
        peer.role = Role.none;
        return peer;
    }

    public void start() throws Exception {
    }
}
