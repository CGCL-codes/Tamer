package com.l2jserver.gameserver.model;

import java.util.List;
import javolution.util.FastList;
import com.l2jserver.gameserver.instancemanager.TownManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ExManagePartyRoomMember;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Gnacik
 *
 */
public class PartyMatchRoom {

    private int _id;

    private String _title;

    private int _loot;

    private int _location;

    private int _minlvl;

    private int _maxlvl;

    private int _maxmem;

    private final List<L2PcInstance> _members = new FastList<L2PcInstance>();

    public PartyMatchRoom(int id, String title, int loot, int minlvl, int maxlvl, int maxmem, L2PcInstance owner) {
        _id = id;
        _title = title;
        _loot = loot;
        _location = TownManager.getClosestLocation(owner);
        _minlvl = minlvl;
        _maxlvl = maxlvl;
        _maxmem = maxmem;
        _members.add(owner);
    }

    public List<L2PcInstance> getPartyMembers() {
        return _members;
    }

    public void addMember(L2PcInstance player) {
        _members.add(player);
    }

    public void deleteMember(L2PcInstance player) {
        if (player != getOwner()) {
            _members.remove(player);
            notifyMembersAboutExit(player);
        } else if (_members.size() == 1) {
            PartyMatchRoomList.getInstance().deleteRoom(_id);
        } else {
            changeLeader(_members.get(1));
            deleteMember(player);
        }
    }

    public void notifyMembersAboutExit(L2PcInstance player) {
        for (L2PcInstance _member : getPartyMembers()) {
            SystemMessage sm = new SystemMessage(SystemMessageId.C1_LEFT_PARTY_ROOM);
            sm.addCharName(player);
            _member.sendPacket(sm);
            _member.sendPacket(new ExManagePartyRoomMember(player, this, 2));
        }
    }

    public void changeLeader(L2PcInstance newLeader) {
        L2PcInstance oldLeader = _members.get(0);
        _members.remove(newLeader);
        _members.set(0, newLeader);
        _members.add(oldLeader);
        for (L2PcInstance member : getPartyMembers()) {
            member.sendPacket(new ExManagePartyRoomMember(newLeader, this, 1));
            member.sendPacket(new ExManagePartyRoomMember(oldLeader, this, 1));
            member.sendPacket(SystemMessageId.PARTY_ROOM_LEADER_CHANGED);
        }
    }

    public int getId() {
        return _id;
    }

    public int getLootType() {
        return _loot;
    }

    public int getMinLvl() {
        return _minlvl;
    }

    public int getMaxLvl() {
        return _maxlvl;
    }

    public int getLocation() {
        return _location;
    }

    public int getMembers() {
        return _members.size();
    }

    public int getMaxMembers() {
        return _maxmem;
    }

    public String getTitle() {
        return _title;
    }

    public L2PcInstance getOwner() {
        return _members.get(0);
    }

    public void setMinLvl(int minlvl) {
        _minlvl = minlvl;
    }

    public void setMaxLvl(int maxlvl) {
        _maxlvl = maxlvl;
    }

    public void setLocation(int loc) {
        _location = loc;
    }

    public void setLootType(int loot) {
        _loot = loot;
    }

    public void setMaxMembers(int maxmem) {
        _maxmem = maxmem;
    }

    public void setTitle(String title) {
        _title = title;
    }
}
