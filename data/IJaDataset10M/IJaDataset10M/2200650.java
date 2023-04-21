package l1j.server.server.serverpackets;

import java.util.logging.Logger;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1MonsterInstance;

public class S_MoveNpcPacket extends ServerBasePacket {

    private static final String _S__1F_S_MOVENPCPACKET = "[S] S_MoveNpcPacket";

    private static Logger _log = Logger.getLogger(S_MoveNpcPacket.class.getName());

    public S_MoveNpcPacket(L1MonsterInstance npc, int x, int y, int heading) {
        writeC(Opcodes.S_OPCODE_MOVEOBJECT);
        writeD(npc.getId());
        writeH(x);
        writeH(y);
        writeC(heading);
        writeC(0x81);
        writeD(0x00000000);
    }

    @Override
    public byte[] getContent() {
        return getBytes();
    }

    public String getType() {
        return _S__1F_S_MOVENPCPACKET;
    }
}
