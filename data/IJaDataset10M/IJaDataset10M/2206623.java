package l1j.server.server.clientpackets;

import java.util.logging.Logger;
import l1j.server.Leaf;
import l1j.server.server.ClientThread;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Unknown2;
import l1j.server.server.serverpackets.S_Unknown3;

public class C_NewCharSelect extends ClientBasePacket {

    private static final String C_NEW_CHAR_SELECT = "[C] C_NewCharSelect";

    private static Logger _log = Logger.getLogger(C_NewCharSelect.class.getName());

    public C_NewCharSelect(byte[] decrypt, ClientThread client) {
        super(decrypt);
        client.CharReStart(true);
        client.sendPacket(new S_Unknown3());
        if (client.getActiveChar() != null) {
            L1PcInstance pc = client.getActiveChar();
            l1j.server.Leaf.list.remove(pc.getName());
            _log.fine("Disconnect from: " + pc.getName());
            ClientThread.quitGame(pc);
            synchronized (pc) {
                pc.logout();
                client.setActiveChar(null);
            }
        } else {
            _log.fine("Disconnect Request from Account : " + client.getAccountName());
        }
    }

    @Override
    public String getType() {
        return C_NEW_CHAR_SELECT;
    }
}
