package net.sf.l2j.gameserver.clientpackets;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.GmListTable;
import net.sf.l2j.gameserver.instancemanager.PetitionManager;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * <p>Format: (c) Sd
 * <ul>
 * <li>S: content</li>
 * <li>d: type</li>
 * </ul></p>
 * @author -Wooden-, TempyIncursion
 *
 */
public final class RequestPetition extends L2GameClientPacket {

    private static final String _C__7F_RequestPetition = "[C] 7F RequestPetition";

    private String _content;

    private int _type;

    @Override
    protected void readImpl() {
        _content = readS();
        _type = readD();
    }

    @Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) return;
        if (!GmListTable.getInstance().isGmOnline(false)) {
            activeChar.sendPacket(new SystemMessage(SystemMessageId.NO_GM_PROVIDING_SERVICE_NOW));
            return;
        }
        if (!PetitionManager.getInstance().isPetitioningAllowed()) {
            activeChar.sendPacket(new SystemMessage(SystemMessageId.GAME_CLIENT_UNABLE_TO_CONNECT_TO_PETITION_SERVER));
            return;
        }
        if (PetitionManager.getInstance().isPlayerPetitionPending(activeChar)) {
            activeChar.sendPacket(new SystemMessage(SystemMessageId.ONLY_ONE_ACTIVE_PETITION_AT_TIME));
            return;
        }
        if (PetitionManager.getInstance().getPendingPetitionCount() == Config.MAX_PETITIONS_PENDING) {
            activeChar.sendPacket(new SystemMessage(SystemMessageId.PETITION_SYSTEM_CURRENT_UNAVAILABLE));
            return;
        }
        int totalPetitions = PetitionManager.getInstance().getPlayerTotalPetitionCount(activeChar) + 1;
        if (totalPetitions > Config.MAX_PETITIONS_PER_PLAYER) {
            SystemMessage sm = new SystemMessage(SystemMessageId.WE_HAVE_RECEIVED_S1_PETITIONS_TODAY);
            sm.addNumber(totalPetitions);
            activeChar.sendPacket(sm);
            sm = null;
            return;
        }
        if (_content.length() > 255) {
            activeChar.sendPacket(new SystemMessage(SystemMessageId.PETITION_MAX_CHARS_255));
            return;
        }
        int petitionId = PetitionManager.getInstance().submitPetition(activeChar, _content, _type);
        SystemMessage sm = new SystemMessage(SystemMessageId.PETITION_ACCEPTED_RECENT_NO_S1);
        sm.addNumber(petitionId);
        activeChar.sendPacket(sm);
        sm = new SystemMessage(SystemMessageId.SUBMITTED_YOU_S1_TH_PETITION_S2_LEFT);
        sm.addNumber(totalPetitions);
        sm.addNumber(Config.MAX_PETITIONS_PER_PLAYER - totalPetitions);
        activeChar.sendPacket(sm);
        sm = new SystemMessage(SystemMessageId.S1_PETITION_ON_WAITING_LIST);
        sm.addNumber(PetitionManager.getInstance().getPendingPetitionCount());
        activeChar.sendPacket(sm);
        sm = null;
    }

    @Override
    public String getType() {
        return _C__7F_RequestPetition;
    }
}
