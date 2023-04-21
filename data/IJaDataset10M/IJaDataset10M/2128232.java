package nakayo.gameserver.network.aion.clientpackets;

import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.network.aion.AionClientPacket;
import nakayo.gameserver.services.DropService;

/**
 * @author alexa026
 *         modified by ATracer
 */
public class CM_LOOT_ITEM extends AionClientPacket {

    private int targetObjectId;

    private int index;

    public CM_LOOT_ITEM(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        targetObjectId = readD();
        index = readC();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        DropService.getInstance().requestDropItem(player, targetObjectId, index);
    }
}
