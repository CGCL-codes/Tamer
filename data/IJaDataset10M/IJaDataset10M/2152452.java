package nakayo.gameserver.network.aion.clientpackets;

import nakayo.gameserver.network.aion.AionClientPacket;

/**
 * @author xavier
 *         <p/>
 *         Packet sent by client when player may quit game in 10 seconds
 */
public class CM_MAY_QUIT extends AionClientPacket {

    /**
     * @param opcode
     */
    public CM_MAY_QUIT(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
    }
}
