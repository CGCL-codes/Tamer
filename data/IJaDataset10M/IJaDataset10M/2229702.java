package net.sf.rottz.tv.client.rottzclient.screens.rooms;

import net.sf.rottz.tv.client.rottzclient.GameScreenTypes;
import net.sf.rottz.tv.client.rottzclient.RottzClientMain;

public class GameScreenRoomBetty extends GameScreenRoom {

    public GameScreenRoomBetty(RottzClientMain rottzClient, int roomId) {
        super(rottzClient, roomId);
    }

    @Override
    public GameScreenTypes getScreenType() {
        return GameScreenTypes.ROOM_BETTY;
    }

    @Override
    public void keyPress(int keyCode) {
    }
}
