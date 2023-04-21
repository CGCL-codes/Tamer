package com.l2jserver.gameserver.network.clientpackets;

import java.util.List;
import com.l2jserver.gameserver.instancemanager.CursedWeaponsManager;
import com.l2jserver.gameserver.model.CursedWeapon;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.network.serverpackets.ExCursedWeaponLocation;
import com.l2jserver.gameserver.network.serverpackets.ExCursedWeaponLocation.CursedWeaponInfo;
import com.l2jserver.util.Point3D;
import javolution.util.FastList;

/**
 * Format: (ch)
 * @author  -Wooden-
 */
public final class RequestCursedWeaponLocation extends L2GameClientPacket {

    private static final String _C__D0_23_REQUESTCURSEDWEAPONLOCATION = "[C] D0:23 RequestCursedWeaponLocation";

    @Override
    protected void readImpl() {
    }

    /**
	 * @see com.l2jserver.util.network.BaseRecievePacket.ClientBasePacket#runImpl()
	 */
    @Override
    protected void runImpl() {
        L2Character activeChar = getClient().getActiveChar();
        if (activeChar == null) return;
        List<CursedWeaponInfo> list = new FastList<CursedWeaponInfo>();
        for (CursedWeapon cw : CursedWeaponsManager.getInstance().getCursedWeapons()) {
            if (!cw.isActive()) continue;
            Point3D pos = cw.getWorldPosition();
            if (pos != null) list.add(new CursedWeaponInfo(pos, cw.getItemId(), cw.isActivated() ? 1 : 0));
        }
        if (!list.isEmpty()) activeChar.sendPacket(new ExCursedWeaponLocation(list));
    }

    /**
	 * @see com.l2jserver.gameserver.BasePacket#getType()
	 */
    @Override
    public String getType() {
        return _C__D0_23_REQUESTCURSEDWEAPONLOCATION;
    }

    @Override
    protected boolean triggersOnActionRequest() {
        return false;
    }
}
