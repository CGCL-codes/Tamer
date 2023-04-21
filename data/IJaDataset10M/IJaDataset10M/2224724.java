package net.sf.rottz.tv.common.world;

import net.sf.rottz.tv.client.GameClientInfo;
import net.sf.rottz.tv.common.communication.GameDataPacket;
import net.sf.rottz.tv.common.communication.GameDataPacketReader;

public class ElevatorPosition {

    private int floor;

    private GameTime timestamp;

    public ElevatorPosition(ElevatorPosition pos) {
        this(pos.floor, pos.timestamp);
    }

    public ElevatorPosition(int floorNum, GameTime posTime) {
        this.floor = floorNum;
        this.timestamp = new GameTime(posTime);
    }

    public GameDataPacket buildDataPacket() {
        GameDataPacket gdp = new GameDataPacket();
        gdp.addByte(floor, "Floor Number");
        gdp.addData(timestamp.buildDataPacket(), "Timestamp");
        return gdp;
    }

    public static ElevatorPosition create(GameClientInfo game, GameDataPacketReader reader) {
        int floorNum = reader.readByte();
        GameTime ts = GameTime.create(game, reader);
        ElevatorPosition elevPos = new ElevatorPosition(floorNum, ts);
        return elevPos;
    }

    public int getFloorNum() {
        return floor;
    }

    public GameTime getTimeStamp() {
        return new GameTime(timestamp);
    }

    public void setTimeStamp(GameTime ts) {
        this.timestamp = new GameTime(ts);
    }
}
