package gameserver.network.aion.clientpackets;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.windstreams.Location2D;
import gameserver.model.templates.windstreams.WindstreamTemplate;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_WINDSTREAM_LOCATIONS;
import gameserver.quest.QuestEngine;
import gameserver.quest.model.QuestCookie;
import gameserver.services.WeatherService;
import gameserver.spawn.RiftSpawnManager;
import gameserver.world.World;
import org.apache.log4j.Logger;

/**
 * Client is saying that level[map] is ready.
 */
public class CM_LEVEL_READY extends AionClientPacket {

    private static Logger log = Logger.getLogger(CM_LEVEL_READY.class);

    /**
	* Constructs new instance of <tt>CM_LEVEL_READY </tt> packet
	* 
	* @param opcode
	*/
    public CM_LEVEL_READY(int opcode) {
        super(opcode);
    }

    /**
	* {@inheritDoc}
	*/
    @Override
    protected void readImpl() {
    }

    /**
	* {@inheritDoc}
	*/
    @Override
    protected void runImpl() {
        Player activePlayer = getConnection().getActivePlayer();
        if (activePlayer.isSpawned() && activePlayer.getOldWorldId() == activePlayer.getWorldId()) {
            log.info("[ANTICHEAT] Fake CM_LEVEL_READY sent by player: " + activePlayer.getName() + " IP: " + getConnection().getIP());
            return;
        }
        WindstreamTemplate template = DataManager.WINDSTREAM_DATA.getStreamTemplate(activePlayer.getPosition().getMapId());
        Location2D location;
        if (template != null) for (int i = 0; i < template.getLocations().getLocation().size(); i++) {
            location = template.getLocations().getLocation().get(i);
            sendPacket(new SM_WINDSTREAM_LOCATIONS(location.getBidirectional(), template.getMapid(), location.getId(), location.getBoost()));
        }
        location = null;
        template = null;
        sendPacket(new SM_PLAYER_INFO(activePlayer, false));
        activePlayer.getController().startProtectionActiveTask();
        if (activePlayer.isSpawned()) World.getInstance().despawn(activePlayer);
        World.getInstance().spawn(activePlayer);
        activePlayer.getController().refreshZoneImpl();
        WeatherService.getInstance().loadWeather(activePlayer);
        QuestEngine.getInstance().onEnterWorld(new QuestCookie(null, activePlayer, 0, 0));
        activePlayer.getController().onEnterWorld();
        sendPacket(new SM_SYSTEM_MESSAGE(1390122, false, 0, activePlayer.getPosition().getInstanceId()));
        RiftSpawnManager.sendRiftStatus(activePlayer);
        activePlayer.getEffectController().updatePlayerEffectIcons();
    }
}
