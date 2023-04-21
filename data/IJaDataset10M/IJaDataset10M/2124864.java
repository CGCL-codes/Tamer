package l1j.server.server.serverpackets;

import static l1j.server.server.Opcodes.S_OPCODE_LOGINTOGAME;

/**
 * 由3.0的S_Unkown1改名
 * 正式命名為LoginGame
 */
public class S_LoginGame extends ServerBasePacket {

    public S_LoginGame() {
        writeC(S_OPCODE_LOGINTOGAME);
        writeC(0x03);
        writeC(0x00);
        writeC(0xac);
        writeC(0xc2);
        writeC(0x7c);
        writeC(0x00);
        writeC(0xc1);
    }

    public byte[] getContent() {
        return getBytes();
    }
}
