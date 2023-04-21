package net.sf.rottz.tv.common.media;

import net.sf.rottz.tv.client.GameClientInfo;
import net.sf.rottz.tv.common.communication.DataPacketBuilder;
import net.sf.rottz.tv.common.communication.GameDataPacket;
import net.sf.rottz.tv.common.communication.GameDataPacketReader;
import net.sf.rottz.tv.common.world.GameTime;

public class AdTape implements DataPacketBuilder {

    private int adTapeId;

    private AdInfo adInfo;

    private int numExhibits = 0;

    private GameTime signedTime;

    public AdTape(AdInfo adInfo, int adTapeId) {
        this.adInfo = adInfo;
        this.adTapeId = adTapeId;
    }

    public GameDataPacket buildDataPacket() {
        GameDataPacket gdp = new GameDataPacket();
        gdp.addDWord(adInfo.getAdInfoId(), "Ad Info Id");
        gdp.addDWord(adTapeId, "Ad Tape Id");
        gdp.addByte(numExhibits, "Num Exhibits");
        gdp.addData(signedTime.buildDataPacket(), "Signed Time");
        return gdp;
    }

    public static AdTape create(GameClientInfo game, GameDataPacketReader reader) {
        int adInfoId = reader.readDWord();
        int adTapeId = reader.readDWord();
        int numExhibits = reader.readByte();
        GameTime signedTime = GameTime.create(game, reader);
        AdInfo adInfo = game.getGameWorld().getAdInfoById(adInfoId);
        if (adInfo == null) {
            System.out.println("Ad not found with specified id when creating a AdTape from incoming data. Ad Id : " + adInfoId);
            return null;
        }
        AdTape tape = new AdTape(adInfo, adTapeId);
        tape.setContractTime(signedTime);
        tape.setNumExhibits(numExhibits);
        return tape;
    }

    public void setNumExhibits(int numExhibits) {
        this.numExhibits = numExhibits;
    }

    public void setContractTime(GameTime time) {
        this.signedTime = new GameTime(time);
    }

    public int getAdTapeId() {
        return adTapeId;
    }

    public AdInfo getAdInfo() {
        return adInfo;
    }

    public GameTime getSignedTime() {
        return signedTime;
    }
}
