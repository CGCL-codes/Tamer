package l1j.server.server.clientpackets;

import java.util.logging.Logger;
import l1j.server.server.ClientThread;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_OwnCharPack;

public class C_Ship extends ClientBasePacket {

    private static final String C_SHIP = "[C] C_Ship";

    private static Logger _log = Logger.getLogger(C_Ship.class.getName());

    public C_Ship(byte abyte0[], ClientThread client) {
        super(abyte0);
        int shipMapId = readH();
        int locX = readH();
        int locY = readH();
        L1PcInstance pc = client.getActiveChar();
        int mapId = pc.getMapId();
        if (mapId == 5) {
            pc.getInventory().consumeItem(40299, 1);
        } else if (mapId == 6) {
            pc.getInventory().consumeItem(40298, 1);
        } else if (mapId == 83) {
            pc.getInventory().consumeItem(40300, 1);
        } else if (mapId == 84) {
            pc.getInventory().consumeItem(40301, 1);
        } else if (mapId == 446) {
            pc.getInventory().consumeItem(40303, 1);
        } else if (mapId == 447) {
            pc.getInventory().consumeItem(40302, 1);
        }
        pc.sendPackets(new S_OwnCharPack(pc));
        L1Teleport.teleport(pc, locX, locY, (short) shipMapId, 0, false);
    }

    @Override
    public String getType() {
        return C_SHIP;
    }
}
