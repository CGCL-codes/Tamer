package com.l2jserver.gameserver.network.clientpackets;

import com.l2jserver.gameserver.instancemanager.TerritoryWarManager;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ExShowDominionRegistry;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 *
 * @author Gigiikun
 */
public final class RequestJoinDominionWar extends L2GameClientPacket {

    private static final String _C__57_RequestJoinDominionWar = "[C] 57 RequestJoinDominionWar";

    private int _territoryId;

    private int _isClan;

    private int _isJoining;

    @Override
    protected void readImpl() {
        _territoryId = readD();
        _isClan = readD();
        _isJoining = readD();
    }

    @Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) return;
        L2Clan clan = activeChar.getClan();
        int castleId = _territoryId - 80;
        if (TerritoryWarManager.getInstance().getIsRegistrationOver()) {
            activeChar.sendPacket(new SystemMessage(SystemMessageId.NOT_TERRITORY_REGISTRATION_PERIOD));
            return;
        } else if (clan != null && TerritoryWarManager.getInstance().getTerritory(castleId).getOwnerClan() == clan) {
            activeChar.sendPacket(new SystemMessage(SystemMessageId.THE_TERRITORY_OWNER_CLAN_CANNOT_PARTICIPATE_AS_MERCENARIES));
            return;
        }
        if (_isClan == 0x01) {
            if ((activeChar.getClanPrivileges() & L2Clan.CP_CS_MANAGE_SIEGE) != L2Clan.CP_CS_MANAGE_SIEGE) {
                activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT));
                return;
            }
            if (clan == null) return;
            if (_isJoining == 1) {
                if (System.currentTimeMillis() < clan.getDissolvingExpiryTime()) {
                    activeChar.sendPacket(new SystemMessage(SystemMessageId.CANT_PARTICIPATE_IN_SIEGE_WHILE_DISSOLUTION_IN_PROGRESS));
                    return;
                } else if (TerritoryWarManager.getInstance().checkIsRegistered(-1, clan)) {
                    activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_ALREADY_REQUESTED_TW_REGISTRATION));
                    return;
                }
                TerritoryWarManager.getInstance().registerClan(castleId, clan);
            } else TerritoryWarManager.getInstance().removeClan(castleId, clan);
        } else {
            if (activeChar.getLevel() < 40 || activeChar.getClassId().level() < 2) {
                return;
            }
            if (_isJoining == 1) {
                if (TerritoryWarManager.getInstance().checkIsRegistered(-1, activeChar.getObjectId())) {
                    activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_ALREADY_REQUESTED_TW_REGISTRATION));
                    return;
                } else if (clan != null && TerritoryWarManager.getInstance().checkIsRegistered(-1, clan)) {
                    activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_ALREADY_REQUESTED_TW_REGISTRATION));
                    return;
                }
                TerritoryWarManager.getInstance().registerMerc(castleId, activeChar);
            } else TerritoryWarManager.getInstance().removeMerc(castleId, activeChar);
        }
        activeChar.sendPacket(new ExShowDominionRegistry(castleId, activeChar));
    }

    @Override
    public String getType() {
        return _C__57_RequestJoinDominionWar;
    }
}
