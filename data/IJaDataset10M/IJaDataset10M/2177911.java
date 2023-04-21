package com.l2jserver.gameserver.network.clientpackets;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.StartRotation;

/**
 * This class ...
 *
 * @version $Revision: 1.1.4.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class StartRotating extends L2GameClientPacket {

    private static final String _C__4A_STARTROTATING = "[C] 4A StartRotating";

    private int _degree;

    private int _side;

    @Override
    protected void readImpl() {
        _degree = readD();
        _side = readD();
    }

    @Override
    protected void runImpl() {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) return;
        final StartRotation br;
        if (activeChar.isInAirShip() && activeChar.getAirShip().isCaptain(activeChar)) {
            br = new StartRotation(activeChar.getAirShip().getObjectId(), _degree, _side, 0);
            activeChar.getAirShip().broadcastPacket(br);
        } else {
            br = new StartRotation(activeChar.getObjectId(), _degree, _side, 0);
            activeChar.broadcastPacket(br);
        }
    }

    @Override
    public String getType() {
        return _C__4A_STARTROTATING;
    }
}
