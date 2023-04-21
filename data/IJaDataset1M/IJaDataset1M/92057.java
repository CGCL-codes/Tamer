package nakayo.gameserver.network.aion.clientpackets;

import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.network.aion.AionClientPacket;
import nakayo.gameserver.services.BrokerService;

/**
 * @author kosyachok
 */
public class CM_BROKER_LIST extends AionClientPacket {

    @SuppressWarnings("unused")
    private int brokerId;

    private int sortType;

    private int page;

    private int listMask;

    public CM_BROKER_LIST(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        this.brokerId = readD();
        this.sortType = readC();
        this.page = readH();
        this.listMask = readH();
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        BrokerService.getInstance().showRequestedItems(player, listMask, sortType, page);
    }
}
