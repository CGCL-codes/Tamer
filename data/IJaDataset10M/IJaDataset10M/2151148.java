package net.sf.rottz.tv.server.communication.commands;

import java.util.List;
import net.sf.rottz.tv.common.character.CharMovingState;
import net.sf.rottz.tv.common.communication.BuyAdResponseType;
import net.sf.rottz.tv.common.media.AdTape;
import net.sf.rottz.tv.common.world.GameTime;
import net.sf.rottz.tv.common.world.RoomTypeServer;
import net.sf.rottz.tv.server.GameCommandCreator;
import net.sf.rottz.tv.server.GameServer;
import net.sf.rottz.tv.server.communication.updates.infomessages.ServerUpdateBuyAdResponse;
import net.sf.rottz.tv.server.rooms.AdRoomServer;
import net.sf.rottz.tv.server.world.CharPositionServer;
import net.sf.rottz.tv.server.world.GameCharacterServer;
import net.sf.rottz.tv.server.world.PlayerServer;
import net.sf.rottz.tv.server.world.RoomServer;
import net.sf.rottz.tv.server.world.TVStationFullServer;

/**
 * I know it's not a "buying" act, but it's easier to remember and it's a very
 * similar action to the one of buying a movie
 * 
 * @author pedrodaros
 */
public class GameCommandBuyAdvertisement extends GameCommand {

    private final PlayerServer playerBuyingAd;

    private final int adTapeId;

    public GameCommandBuyAdvertisement(GameCommandCreator cmdCreator, PlayerServer playerBuyingad, int adTapeId) {
        super(cmdCreator);
        this.playerBuyingAd = playerBuyingad;
        this.adTapeId = adTapeId;
    }

    @Override
    public void executeCore(GameServer game) {
        System.out.println("Executing " + getCommandName() + " command. cmd created by " + getCreatorName() + " to buy ad tape id : " + adTapeId);
        GameCharacterServer charBuyingAd = playerBuyingAd.getCharacter();
        CharPositionServer charPos = charBuyingAd.getCharPos();
        if (charPos.isOutside() == true) {
            game.addUpdate(new ServerUpdateBuyAdResponse(playerBuyingAd, adTapeId, BuyAdResponseType.CHAR_NOT_IN_ROOM));
            System.out.println("Error - Char was outside : should be inside the ad shop");
            return;
        }
        RoomServer room = charPos.getRoom();
        if (room.getTypeServer() != RoomTypeServer.AD_STORE) {
            game.addUpdate(new ServerUpdateBuyAdResponse(playerBuyingAd, adTapeId, BuyAdResponseType.CHAR_NOT_IN_ROOM));
            System.out.println("Error - Char was in a room but not the ad shop");
            return;
        }
        if (charBuyingAd.getMovingState() != CharMovingState.STANDING) {
            game.addUpdate(new ServerUpdateBuyAdResponse(playerBuyingAd, adTapeId, BuyAdResponseType.CHAR_NOT_STANDING));
            System.out.println("Error - Char was in the ad room but wasnt standing idle");
            return;
        }
        if (playerBuyingAd.getFreeAdSlots() == 0) {
            game.addUpdate(new ServerUpdateBuyAdResponse(playerBuyingAd, adTapeId, BuyAdResponseType.BRIEFCASE_FULL));
            System.out.println("Error - Char is already with briefcase full");
            return;
        }
        AdRoomServer adRoom = (AdRoomServer) room;
        List<AdTape> ads = adRoom.getAvailableAds();
        AdTape adToBuy = null;
        for (AdTape m : ads) {
            if (m.getAdTapeId() == adTapeId) {
                adToBuy = m;
                break;
            }
        }
        if (adToBuy == null) {
            game.addUpdate(new ServerUpdateBuyAdResponse(playerBuyingAd, adTapeId, BuyAdResponseType.AD_NOT_FOUND));
            System.out.println("Error - couldnt find an available ad with given id : " + adTapeId);
            return;
        }
        TVStationFullServer playerStation = game.getGameWorld().getTVByPlayer(playerBuyingAd);
        if (playerStation == null) {
            game.addUpdate(new ServerUpdateBuyAdResponse(playerBuyingAd, adTapeId, BuyAdResponseType.PLAYER_HASNT_TV));
            System.out.println("Error - Char doesnt seem to be associated with any TV Station");
            return;
        }
        GameTime currentTime = game.getGameWorld().getCurrentTime();
        adToBuy.setContractTime(currentTime);
        playerBuyingAd.addAdToBriefcase(adToBuy);
        adRoom.removeAd(adToBuy);
        game.addUpdate(new ServerUpdateBuyAdResponse(playerBuyingAd, adTapeId, BuyAdResponseType.SUCCESS));
    }

    @Override
    public String getCommandName() {
        return "Buy Ad";
    }
}
