package com.l2jserver.gameserver.network.serverpackets;

/**
 * Dialog with input field<br>
 * type 0 = char name (Selection screen)<br>
 * type 1 = clan name
 * 
 * @author JIV
 *
 */
public class ExNeedToChangeName extends L2GameServerPacket {

    private int type, subType;

    private String name;

    public ExNeedToChangeName(int type, int subType, String name) {
        super();
        this.type = type;
        this.subType = subType;
        this.name = name;
    }

    @Override
    protected void writeImpl() {
        writeC(0xFE);
        writeH(0x69);
        writeD(type);
        writeD(subType);
        writeS(name);
    }

    @Override
    public String getType() {
        return "[S] FE:69 ExNeedToChangeName".intern();
    }
}
