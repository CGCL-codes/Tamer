package net.sf.l2j.gameserver.network.clientpackets;

import java.util.List;
import javolution.util.FastList;
import net.sf.l2j.gameserver.instancemanager.CursedWeaponsManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.network.serverpackets.ExCursedWeaponList;

/**
 * Format: (ch)
 *
 * @author -Wooden-
 */
public class RequestCursedWeaponList extends L2GameClientPacket {

    private static final String _C__D0_22_REQUESTCURSEDWEAPONLIST = "[C] D0:22 RequestCursedWeaponList";

    @Override
    protected void readImpl() {
    }

    /**
	 * @see net.sf.l2j.gameserver.network.clientpackets.ClientBasePacket#runImpl()
	 */
    @Override
    protected void runImpl() {
        L2Character activeChar = getClient().getActiveChar();
        if (activeChar == null) return;
        List<Integer> list = new FastList<Integer>();
        for (int id : CursedWeaponsManager.getInstance().getCursedWeaponsIds()) list.add(id);
        activeChar.sendPacket(new ExCursedWeaponList(list));
    }

    /**
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
    @Override
    public String getType() {
        return _C__D0_22_REQUESTCURSEDWEAPONLIST;
    }
}
