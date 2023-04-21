package nakayo.gameserver.network.aion.clientpackets;

import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.network.aion.AionClientPacket;
import nakayo.gameserver.services.AllianceService;
import nakayo.gameserver.services.GroupService;

/**
 * @author Sweetkr
 * @author Simple
 */
public class CM_SHOW_BRAND extends AionClientPacket {

    private int brandId;

    private int targetObjectId;

    /**
     * @param opcode
     */
    public CM_SHOW_BRAND(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        @SuppressWarnings("unused") int unk1 = readD();
        brandId = readD();
        targetObjectId = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        if (player == null) return;
        if (player.isInGroup()) GroupService.getInstance().showBrand(player.getPlayerGroup(), brandId, targetObjectId);
        if (player.isInAlliance()) AllianceService.getInstance().showBrand(player.getPlayerAlliance(), brandId, targetObjectId);
    }
}
