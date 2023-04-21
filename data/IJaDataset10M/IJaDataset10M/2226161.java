package net.sf.l2j.gameserver.serverpackets;

/**
 */
public class PetDelete extends L2GameServerPacket {

    private static final String _S__CF_PETDELETE = "[S] b7 PetDelete";

    private int _petId;

    private int _petObjId;

    public PetDelete(int petId, int petObjId) {
        _petId = petId;
        _petObjId = petObjId;
    }

    @Override
    protected final void writeImpl() {
        writeC(0xb7);
        writeD(_petId);
        writeD(_petObjId);
    }

    @Override
    public String getType() {
        return _S__CF_PETDELETE;
    }
}
