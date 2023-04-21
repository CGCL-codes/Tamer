package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_ShowPolyList extends ServerBasePacket {

    public S_ShowPolyList(int objid) {
        writeC(Opcodes.S_OPCODE_SHOWHTML);
        writeD(objid);
        writeS("monlist");
    }

    @Override
    public byte[] getContent() {
        return getBytes();
    }
}
