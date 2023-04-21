package net.sf.l2j.gameserver.clientpackets;

import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.serverpackets.SiegeDefenderList;

/**
 * This class ...
 *
 * @version $Revision: 1.3.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestSiegeDefenderList extends L2GameClientPacket {

    private static final String _C__a3_RequestSiegeDefenderList = "[C] a3 RequestSiegeDefenderList";

    private int _castleId;

    @Override
    protected void readImpl() {
        _castleId = readD();
    }

    @Override
    protected void runImpl() {
        Castle castle = CastleManager.getInstance().getCastleById(_castleId);
        if (castle == null) return;
        SiegeDefenderList sdl = new SiegeDefenderList(castle);
        sendPacket(sdl);
    }

    @Override
    public String getType() {
        return _C__a3_RequestSiegeDefenderList;
    }
}
