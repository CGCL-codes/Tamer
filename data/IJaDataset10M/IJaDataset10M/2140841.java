package com.l2jserver.gameserver.network.serverpackets;

import com.l2jserver.gameserver.model.actor.L2Character;

public class ExMoveToLocationAirShip extends L2GameServerPacket {

    private static final String _S__FE_65_EXAIRSHIPMOVETOLOCATION = "[S] FE:65 ExMoveToLocationAirShip";

    private final int _objId, _tx, _ty, _tz, _x, _y, _z;

    public ExMoveToLocationAirShip(L2Character cha) {
        _objId = cha.getObjectId();
        _tx = cha.getXdestination();
        _ty = cha.getYdestination();
        _tz = cha.getZdestination();
        _x = cha.getX();
        _y = cha.getY();
        _z = cha.getZ();
    }

    @Override
    protected void writeImpl() {
        writeC(0xfe);
        writeH(0x65);
        writeD(_objId);
        writeD(_tx);
        writeD(_ty);
        writeD(_tz);
        writeD(_x);
        writeD(_y);
        writeD(_z);
    }

    @Override
    public String getType() {
        return _S__FE_65_EXAIRSHIPMOVETOLOCATION;
    }
}
