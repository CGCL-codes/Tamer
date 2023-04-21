package net.sf.rottz.tv.server.world;

import net.sf.rottz.tv.common.GameSpeed;
import net.sf.rottz.tv.common.communication.DataPacketBuilder;
import net.sf.rottz.tv.common.communication.GameDataPacket;
import net.sf.rottz.tv.common.world.GamePosition;
import net.sf.rottz.tv.common.world.GameTime;
import net.sf.rottz.tv.common.world.GameColor;
import net.sf.rottz.tv.common.world.RoomTypeClient;
import net.sf.rottz.tv.common.world.RoomTypeServer;
import net.sf.rottz.tv.server.GameServer;

public abstract class RoomServer implements DataPacketBuilder {

    private final int roomId;

    private final RoomTypeServer typeServer;

    private final RoomTypeClient typeClient;

    private final TVStationFullServer owner;

    private GamePosition pos = null;

    private final String roomName;

    private final GameServer gameInfo;

    public RoomServer(GameServer gameInfo, String roomName, int roomId, RoomTypeServer typeServer, RoomTypeClient typeClient, TVStationFullServer owner) {
        this.gameInfo = gameInfo;
        this.roomName = roomName;
        this.roomId = roomId;
        this.typeServer = typeServer;
        this.typeClient = typeClient;
        this.owner = owner;
    }

    @SuppressWarnings("null")
    public final GameDataPacket buildDataPacket() {
        GameDataPacket gdp = new GameDataPacket();
        gdp.addString(roomName, "Room Name");
        gdp.addByte(roomId, "Room Id");
        gdp.addByte(typeClient.ordinal(), "Room Client Type");
        gdp.addData(pos.buildDataPacket(), "Position");
        boolean hasSign = true;
        if (owner == null) hasSign = false; else {
            PlayerServer ownerPlayer = getGameInfo().getPlayerByNum(owner.getTVNum());
            if (ownerPlayer == null) hasSign = false;
        }
        GameCharacterServer character = null;
        if (hasSign) {
            PlayerServer ownerPlayer = getGameInfo().getPlayerByNum(owner.getTVNum());
            character = ownerPlayer.getCharacter();
            if (character == null) {
                System.out.println("Error - player owns a room but doesnt have a character. Maybe left the game?");
                hasSign = false;
            }
        }
        if (hasSign) {
            gdp.addByte(1, "Has Sign");
            GameColor color = character.getAvatar().getColor();
            gdp.addData(color.buildDataPacket(), "Color");
        } else {
            gdp.addByte(0, "Has Sign");
        }
        if (hasDynamicData()) {
            gdp.addByte(1, "Has Dyn Data");
            gdp.addData(getDynamicData(), "Room Dynamic Data");
        } else {
            gdp.addByte(0, "Has Dyn Data");
        }
        return gdp;
    }

    public GamePosition getPosition() {
        return new GamePosition(pos);
    }

    public void setPosition(GamePosition p) {
        this.pos = new GamePosition(p);
    }

    public int getRoomId() {
        return roomId;
    }

    public RoomTypeServer getTypeServer() {
        return typeServer;
    }

    public TVStationFullServer getOwnerTV() {
        return owner;
    }

    @Override
    public String toString() {
        return "Room Type : " + typeServer + " --- Id : " + roomId;
    }

    public boolean hasDynamicData() {
        return false;
    }

    public GameDataPacket getDynamicData() {
        return new GameDataPacket();
    }

    public abstract GameCharacterServer getNextInQueue();

    public abstract GameCharacterServer getCharacterInside();

    public abstract void enterQueue(GameCharacterServer character);

    public abstract void characterEntered(GameCharacterServer character, GameTime enteredTime);

    public abstract void characterExited(GameCharacterServer character);

    public abstract boolean isInQueue(GameCharacterServer c);

    public abstract void updateQueue(GameWorldServer gameWorld, GameSpeed gameSpeed);

    public abstract void updateCore(GameWorldServer gameWorld);

    public GameServer getGameInfo() {
        return gameInfo;
    }
}
