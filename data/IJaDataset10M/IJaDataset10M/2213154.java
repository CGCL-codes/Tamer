package net.sf.rottz.tv.client.rottzclient.screens.rooms;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import net.sf.rottz.tv.client.GameClientInfo;
import net.sf.rottz.tv.client.communication.command.ClientCommandLeaveRoom;
import net.sf.rottz.tv.client.rottzclient.ImageType;
import net.sf.rottz.tv.client.rottzclient.MouseButton;
import net.sf.rottz.tv.client.rottzclient.RottzClientMain;
import net.sf.rottz.tv.client.rottzclient.screens.GameScreen;
import net.sf.rottz.tv.client.world.GameCharacterClient;
import net.sf.rottz.tv.client.world.rooms.RoomClient;
import net.sf.rottz.tv.client.world.rooms.StaticRoomClient;
import net.sf.rottz.tv.common.RottzTVException;
import net.sf.rottz.tv.common.world.RoomTypeClient;

public abstract class GameScreenRoom extends GameScreen {

    private final int roomId;

    public GameScreenRoom(RottzClientMain rottzClient, int roomId) {
        super(rottzClient);
        this.roomId = roomId;
    }

    @Override
    public void keyPress(int keyCode) {
        if (keyCode == KeyEvent.VK_ESCAPE) System.exit(0);
    }

    @Override
    public void mouseReleaseBase(Point clientPos, MouseButton button) {
        if (button == MouseButton.RIGHT) {
            leaveRoom();
            return;
        }
    }

    public void leaveRoom() {
        GameClientInfo clientInfo = getRottzClient().getClientInfo();
        GameCharacterClient clientChar = clientInfo.getLocalPlayer().getCharacter();
        int charId = clientChar.getCharId();
        clientChar.setRequestedExitRoom();
        ClientCommandLeaveRoom leaveRoomCmd = new ClientCommandLeaveRoom(charId);
        clientInfo.addCommand(leaveRoomCmd);
    }

    @Override
    protected BufferedImage getScreenBkgImage() {
        ImageType screenBkgImageType = getScreenType().getImageType();
        if (screenBkgImageType == null) {
            RoomClient room = getAssociatedRoom();
            if (room.getTypeClient() != RoomTypeClient.STATIC) throw new RottzTVException("Should be static room. If the logic changed and this shouldnt be an error, rethink this method.");
            StaticRoomClient staticRoom = (StaticRoomClient) room;
            screenBkgImageType = staticRoom.getBkgImageType();
        }
        return getImagePool().getImage(screenBkgImageType);
    }

    protected RoomClient getAssociatedRoom() {
        return getRottzClient().getClientInfo().getGameWorld().getRoomById(roomId);
    }
}
