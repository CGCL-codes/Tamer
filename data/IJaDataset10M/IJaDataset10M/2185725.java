package net.sf.l2j.gameserver.serverpackets;

import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ClanMember;
import net.sf.l2j.gameserver.model.L2Clan.SubPledge;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 *
 *
 * sample
 * 0000: 68
 * b1010000
 * 48 00 61 00 6d 00 62 00 75 00 72 00 67 00 00 00   H.a.m.b.u.r.g...
 * 43 00 61 00 6c 00 61 00 64 00 6f 00 6e 00 00 00   C.a.l.a.d.o.n...
 * 00000000  crestid | not used (nuocnam)
 * 00000000 00000000 00000000 00000000
 * 22000000 00000000 00000000
 * 00000000 ally id
 * 00 00	ally name
 * 00000000 ally crrest id
 *
 * 02000000
 *
 * 6c 00 69 00 74 00 68 00 69 00 75 00 6d 00 31 00 00 00  l.i.t.h.i.u.m...
 * 0d000000		level
 * 12000000 	class id
 * 00000000
 * 01000000 	offline 1=true
 * 00000000
 *
 * 45 00 6c 00 61 00 6e 00 61 00 00 00   E.l.a.n.a...
 * 08000000
 * 19000000
 * 01000000
 * 01000000
 * 00000000
 *
 *
 * format   dSS dddddddddSdd d (Sddddd)
 *          dddSS dddddddddSdd d (Sdddddd)
 *
 * @version $Revision: 1.6.2.2.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class PledgeShowMemberListAll extends L2GameServerPacket {

    private static final String _S__68_PLEDGESHOWMEMBERLISTALL = "[S] 5a PledgeShowMemberListAll";

    private L2Clan _clan;

    private L2PcInstance _activeChar;

    private L2ClanMember[] _members;

    private int _pledgeType;

    public PledgeShowMemberListAll(L2Clan clan, L2PcInstance activeChar) {
        _clan = clan;
        _activeChar = activeChar;
        _members = _clan.getMembers();
    }

    @Override
    protected final void writeImpl() {
        _pledgeType = 0;
        writePledge(0);
        SubPledge[] subPledge = _clan.getAllSubPledges();
        for (int i = 0; i < subPledge.length; i++) {
            _activeChar.sendPacket(new PledgeReceiveSubPledgeCreated(subPledge[i]));
        }
        for (L2ClanMember m : _members) {
            if (m.getPledgeType() == 0) continue;
            _activeChar.sendPacket(new PledgeShowMemberListAdd(m));
        }
        _activeChar.sendPacket(new UserInfo(_activeChar));
    }

    void writePledge(int mainOrSubpledge) {
        writeC(0x5a);
        writeD(mainOrSubpledge);
        writeD(_clan.getClanId());
        writeD(_pledgeType);
        writeS(_clan.getName());
        writeS(_clan.getLeaderName());
        writeD(_clan.getCrestId());
        writeD(_clan.getLevel());
        writeD(_clan.getHasCastle());
        writeD(_clan.getHasHideout());
        writeD(0x00);
        writeD(_clan.getRank());
        writeD(_clan.getReputationScore());
        writeD(0);
        writeD(0);
        writeD(_clan.getAllyId());
        writeS(_clan.getAllyName());
        writeD(_clan.getAllyCrestId());
        writeD(_clan.isAtWar());
        writeD(_clan.getSubPledgeMembersCount(_pledgeType));
        for (L2ClanMember m : _members) {
            if (m.getPledgeType() != _pledgeType) continue;
            writeS(m.getName());
            writeD(m.getLevel());
            writeD(m.getClassId());
            L2PcInstance player;
            if ((player = m.getPlayerInstance()) != null) {
                writeD(player.getAppearance().getSex() ? 1 : 0);
                writeD(player.getRace().ordinal());
            } else {
                writeD(1);
                writeD(1);
            }
            writeD(m.isOnline() ? m.getObjectId() : 0);
            writeD(m.getSponsor() != 0 ? 1 : 0);
        }
    }

    @Override
    public String getType() {
        return _S__68_PLEDGESHOWMEMBERLISTALL;
    }
}
