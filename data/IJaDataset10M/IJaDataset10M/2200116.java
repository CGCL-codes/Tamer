package com.tysanclan.site.projectewok.beans;

import com.tysanclan.site.projectewok.entities.MumbleServer;
import com.tysanclan.site.projectewok.ws.mumble.ServerStatus;

/**
 * @author jeroen
 */
public interface MumbleService {

    MumbleServer createServer(int serverId, String name, String address, String password, String apiToken, String apiSecret);

    ServerStatus getServerStatus(MumbleServer server);
}
