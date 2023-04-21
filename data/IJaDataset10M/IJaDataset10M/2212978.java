package l1j.server.server.clientpackets;

import java.util.logging.Logger;
import l1j.server.Config;
import l1j.server.server.ClientThread;
import l1j.server.server.datatables.CharacterConfigTable;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_CharcterConfig extends ClientBasePacket {

    private static Logger _log = Logger.getLogger(C_CharcterConfig.class.getName());

    private static final String C_CHARCTER_CONFIG = "[C] C_CharcterConfig";

    public C_CharcterConfig(byte abyte0[], ClientThread client) throws Exception {
        super(abyte0);
        if (Config.CHARACTER_CONFIG_IN_SERVER_SIDE) {
            L1PcInstance pc = client.getActiveChar();
            int length = readD() - 3;
            byte data[] = readByte();
            int count = CharacterConfigTable.getInstance().countCharacterConfig(pc.getId());
            if (count == 0) {
                CharacterConfigTable.getInstance().storeCharacterConfig(pc.getId(), length, data);
            } else {
                CharacterConfigTable.getInstance().updateCharacterConfig(pc.getId(), length, data);
            }
        }
    }

    @Override
    public String getType() {
        return C_CHARCTER_CONFIG;
    }
}
