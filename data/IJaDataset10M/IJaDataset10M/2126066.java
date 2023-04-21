package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_ChinSword extends ServerBasePacket {

    public S_ChinSword(int value) {
        writeC(Opcodes.S_OPCODE_UNKNOWN2);
        writeC(0x4b);
        writeC(value);
    }

    @Override
    public byte[] getContent() {
        return getBytes();
    }
}
