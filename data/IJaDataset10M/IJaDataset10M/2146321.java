package gameserver.network.aion.clientpackets;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;

/**
 * Used on dark poeta, when you get rank F, to be teleported outside instance.
 */
public class CM_EXIT_LOCATION extends AionClientPacket {

    public CM_EXIT_LOCATION(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        final Player activePlayer = getConnection().getActivePlayer();
        if (activePlayer.getInDarkPoeta() || activePlayer.getInDredgion()) {
            switch(activePlayer.getCommonData().getRace().getRaceId()) {
                case 0:
                    TeleportService.teleportTo(activePlayer, 110010000, 1, 1444.9f, 1577.2f, 572.9f, 0);
                    break;
                case 1:
                    TeleportService.teleportTo(activePlayer, 120010000, 1, 1657.5f, 1398.7f, 194.7f, 0);
                    break;
            }
        }
        PacketSendUtility.sendPacket(activePlayer, new SM_INSTANCE_SCORE(0, 14400000, 2097152, 0, 0, 0, 0));
    }
}
