package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1NpcInstance;

public class S_PetCtrlMenu extends ServerBasePacket {

    public S_PetCtrlMenu(L1Character cha, L1NpcInstance npc, boolean open) {
        writeC(Opcodes.S_OPCODE_PETCTRL);
        writeC(0x0c);
        if (open) {
            writeH(cha.getPetList().size() * 3);
            writeD(0x00000000);
            writeD(npc.getId());
            writeH(npc.getMapId());
            writeH(0x0000);
            writeH(npc.getX());
            writeH(npc.getY());
            writeS(npc.getName());
        } else {
            writeH(cha.getPetList().size() * 3 - 3);
            writeD(0x00000001);
            writeD(npc.getId());
        }
    }

    @Override
    public byte[] getContent() {
        return getBytes();
    }
}
