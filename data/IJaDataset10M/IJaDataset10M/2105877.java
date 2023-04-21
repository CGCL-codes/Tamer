package net.sf.l2j.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 *
 * @version $Revision: 1.4.2.1.2.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestReplyStartPledgeWar extends L2GameClientPacket {

    private static final String _C__4e_REQUESTREPLYSTARTPLEDGEWAR = "[C] 4e RequestReplyStartPledgeWar";

    private int _answer;

    @Override
    protected void readImpl() {
        @SuppressWarnings("unused") String _reqName = readS();
        _answer = readD();
    }

    @Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) return;
        L2PcInstance requestor = activeChar.getActiveRequester();
        if (requestor == null) return;
        if (_answer == 1) ClanTable.getInstance().storeclanswars(requestor.getClanId(), activeChar.getClanId()); else requestor.sendPacket(new SystemMessage(SystemMessageId.WAR_PROCLAMATION_HAS_BEEN_REFUSED));
        activeChar.setActiveRequester(null);
        requestor.onTransactionResponse();
    }

    @Override
    public String getType() {
        return _C__4e_REQUESTREPLYSTARTPLEDGEWAR;
    }
}
