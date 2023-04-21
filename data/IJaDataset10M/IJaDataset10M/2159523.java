package l1j.server.server.clientpackets;

import java.util.logging.Logger;
import l1j.server.server.ClientThread;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_GameTime;

public class C_KeepALIVE extends ClientBasePacket {

    private static Logger _log = Logger.getLogger(C_KeepALIVE.class.getName());

    private static final String C_KEEP_ALIVE = "[C] C_KeepALIVE";

    public C_KeepALIVE(byte decrypt[], ClientThread client) {
        super(decrypt);
    }

    @Override
    public String getType() {
        return C_KEEP_ALIVE;
    }
}
