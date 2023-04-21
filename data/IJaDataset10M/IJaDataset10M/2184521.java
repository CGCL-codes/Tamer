package net.kano.joustsim.oscar.oscar.service.chatrooms;

import net.kano.joscar.snaccmd.FullRoomInfo;
import net.kano.joscar.snaccmd.MiniRoomInfo;
import net.kano.joustsim.oscar.oscar.BasicConnection;

public interface RoomFinderServiceListener {

    /**
   * Called when the BasicConnection is created, just before connecting
   */
    void handleNewChatRoom(RoomFinderService service, FullRoomInfo roomInfo, BasicConnection connection);

    void handleRoomInfo(RoomFinderService service, MiniRoomInfo mini, FullRoomInfo info);
}
