package gameserver.services;

import gameserver.configs.DBConfig;
import gameserver.dataholders.DataManager;
import gameserver.model.Race;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Monster;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.model.group.GroupEvent;
import gameserver.model.group.LootGroupRules;
import gameserver.model.group.LootRuleType;
import gameserver.model.group.PlayerGroup;
import gameserver.model.instances.Dredgion;
import gameserver.model.templates.portal.PortalTemplate;
import gameserver.network.aion.serverpackets.SM_GROUP_INFO;
import gameserver.network.aion.serverpackets.SM_GROUP_MEMBER_INFO;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.network.aion.serverpackets.SM_SHOW_BRAND;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.quest.QuestEngine;
import gameserver.quest.model.QuestCookie;
import gameserver.restrictions.RestrictionsManager;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.Util;
import gameserver.utils.idfactory.IDFactory;
import gameserver.utils.stats.StatFunctions;
import gameserver.world.World;
import gameserver.world.WorldMapInstance;
import gameserver.world.WorldType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import javolution.util.FastMap;

public class GroupService {

    /**
	 * Caching group members
	 */
    private final FastMap<Integer, PlayerGroup> groupMembers;

    /**
	 * Caching remove group member schedule
	 */
    private FastMap<Integer, ScheduledFuture<?>> playerGroup;

    public static final GroupService getInstance() {
        return SingletonHolder.instance;
    }

    /**
	 * @param playerGroup
	 */
    private GroupService() {
        groupMembers = new FastMap<Integer, PlayerGroup>();
        playerGroup = new FastMap<Integer, ScheduledFuture<?>>();
    }

    /**
	 * This method will add a member to the group member cache
	 * 
	 * @param player
	 */
    private void addGroupMemberToCache(Player player) {
        if (!groupMembers.containsKey(player.getObjectId())) groupMembers.put(player.getObjectId(), player.getPlayerGroup());
    }

    private void removeGroupMemberFromCache(int playerObjId) {
        if (groupMembers.containsKey(playerObjId)) groupMembers.remove(playerObjId);
    }

    /**
	 * @param playerObjId
	 * @return returns true if player is in the cache
	 */
    public boolean isGroupMember(int playerObjId) {
        return groupMembers.containsKey(playerObjId);
    }

    /**
	 * Returns the player's group
	 * 
	 * @param playerObjId
	 * @return PlayerGroup
	 */
    private PlayerGroup getGroup(int playerObjId) {
        return groupMembers.get(playerObjId);
    }

    /**
	 * This method will handle everything to a player that is invited for a group
	 * 
	 * @param inviter
	 * @param invited
	 */
    public void invitePlayerToGroup(final Player inviter, final Player invited) {
        if (RestrictionsManager.canInviteToGroup(inviter, invited)) {
            RequestResponseHandler responseHandler = new RequestResponseHandler(inviter) {

                @Override
                public void acceptRequest(Creature requester, Player responder) {
                    final PlayerGroup group = inviter.getPlayerGroup();
                    if (group != null && group.isFull()) return;
                    PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.REQUEST_GROUP_INVITE(invited.getName()));
                    if (group != null) {
                        inviter.getPlayerGroup().addPlayerToGroup(invited);
                        addGroupMemberToCache(invited);
                    } else {
                        new PlayerGroup(IDFactory.getInstance().nextId(), inviter);
                        inviter.getPlayerGroup().addPlayerToGroup(invited);
                        addGroupMemberToCache(inviter);
                        addGroupMemberToCache(invited);
                    }
                }

                @Override
                public void denyRequest(Creature requester, Player responder) {
                    PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.REJECT_GROUP_INVITE(responder.getName()));
                }
            };
            boolean result = invited.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_REQUEST_GROUP_INVITE, responseHandler);
            if (result) {
                PacketSendUtility.sendPacket(invited, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_REQUEST_GROUP_INVITE, 0, inviter.getName()));
            }
        }
    }

    /**
	* This method will handle everything to a player that is invited for a group
	*
	* @param captain
	* @param player
	*/
    public void requestToGroup(final Player player, final Player captain) {
        if (RestrictionsManager.canInviteToGroup(captain, player)) {
            RequestResponseHandler responseHandler = new RequestResponseHandler(player) {

                @Override
                public void acceptRequest(Creature requester, Player responder) {
                    final PlayerGroup group = captain.getPlayerGroup();
                    if (group != null && group.isFull()) return;
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.REQUEST_GROUP_INVITE(captain.getName()));
                    if (group != null) {
                        captain.getPlayerGroup().addPlayerToGroup(player);
                        addGroupMemberToCache(player);
                    } else {
                        new PlayerGroup(IDFactory.getInstance().nextId(), captain);
                        captain.getPlayerGroup().addPlayerToGroup(player);
                        addGroupMemberToCache(captain);
                        addGroupMemberToCache(player);
                    }
                }

                @Override
                public void denyRequest(Creature requester, Player responder) {
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.REJECT_GROUP_INVITE(captain.getName()));
                }
            };
            boolean result = captain.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_REQUEST_GROUP_INVITE, responseHandler);
            if (result) {
                PacketSendUtility.sendPacket(captain, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_REQUEST_GROUP_INVITE, 0, player.getName()));
            }
        }
    }

    /**
	 * @param player
	 */
    public void removePlayerFromGroup(final Player player) {
        if (player.isInGroup()) {
            final PlayerGroup group = player.getPlayerGroup();
            int playerObj = player.getObjectId();
            group.removePlayerFromGroup(player);
            removeGroupMemberFromCache(playerObj);
            if (player.isInInstance()) {
                ThreadPoolManager.getInstance().schedule(new Runnable() {

                    @Override
                    public void run() {
                        if (!player.isInGroup()) {
                            WorldMapInstance instance = InstanceService.getRegisteredInstance(player.getWorldId(), group.getGroupId());
                            PortalTemplate portalTemplate = DataManager.PORTAL_DATA.getInstancePortalTemplate(player.getWorldId(), player.getCommonData().getRace());
                            if (portalTemplate != null && portalTemplate.isGroup()) InstanceService.moveToEntryPoint(player, portalTemplate, true); else if (instance != null && instance instanceof Dredgion) TeleportService.moveToBindLocation(player, true);
                        }
                    }
                }, 10000);
            }
            if (group.size() == 1) disbandGroup(group);
        }
    }

    /**
	 * @param player
	 */
    public void startMentoring(Player player) {
        if (player.isInGroup()) {
            PlayerGroup group = player.getPlayerGroup();
            group.startMentoring(player);
        }
    }

    /**
	 * @param player
	 */
    private void setGroupLeader(Player player) {
        final PlayerGroup group = player.getPlayerGroup();
        group.setGroupLeader(player);
        group.updateGroupUIToEvent(player.getPlayerGroup().getGroupLeader(), GroupEvent.CHANGELEADER);
    }

    /**
	 * @param status
	 * @param playerObjId
	 * @param player
	 */
    public void playerStatusInfo(int status, Player player) {
        switch(status) {
            case 2:
                removePlayerFromGroup(player);
                break;
            case 3:
                setGroupLeader(player);
                break;
            case 6:
                removePlayerFromGroup(player);
                break;
            case 10:
                startMentoring(player);
                break;
        }
    }

    /**
	 * @param player
	 * @param amount
	 */
    public void groupDistribution(Player player, long amount) {
        PlayerGroup pg = player.getPlayerGroup();
        if (pg == null) return;
        long availableKinah = player.getInventory().getKinahItem().getItemCount();
        if (availableKinah < amount) {
            return;
        }
        long rewardcount = pg.size() - 1;
        if (rewardcount <= amount) {
            long reward = amount / rewardcount;
            for (Player groupMember : pg.getMembers()) {
                if (groupMember.equals(player)) {
                    groupMember.getInventory().decreaseKinah(amount);
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1390247, false, 0, amount, rewardcount, reward));
                } else {
                    groupMember.getInventory().increaseKinah(reward);
                    PacketSendUtility.sendPacket(groupMember, new SM_SYSTEM_MESSAGE(1390248, false, 0, player.getName(), amount, rewardcount, reward));
                }
            }
        }
    }

    /**
	* This method will send a reward if a player is in a group
	*
	* @param player
	*/
    public void doReward(PlayerGroup group, Monster owner) {
        List<Player> players = new ArrayList<Player>();
        int partyLvlSum = 0;
        boolean checkMentor = false;
        int lowestLevel = 60;
        int highestLevel = 0;
        for (Player member : group.getMembers()) {
            if (!member.isOnline()) {
                continue;
            }
            if (MathUtil.isIn3dRange(member, owner, DBConfig.GROUP_MAX_DISTANCE)) {
                if (member.getLifeStats().isAlreadyDead()) continue;
                if (group.isMentoring(member)) {
                    checkMentor = true;
                    continue;
                }
                players.add(member);
                partyLvlSum += member.getLevel();
                if (member.getLevel() > highestLevel) {
                    highestLevel = member.getLevel();
                }
            }
        }
        for (Player member : players) {
            if (member.getLevel() < lowestLevel) lowestLevel = member.getLevel();
        }
        if (players.size() == 0) {
            return;
        }
        int apRewardPerMember = 0;
        WorldType worldType = owner.getWorldType();
        if (worldType == WorldType.ABYSS) {
            apRewardPerMember = Math.round(StatFunctions.calculateGroupAPReward(highestLevel, owner) / players.size());
        }
        long expReward = StatFunctions.calculateGroupExperienceReward(highestLevel, owner);
        double mod = 1;
        if (players.size() > 1) {
            mod = 1 + (((players.size() - 1) * 10) / 100);
        }
        expReward *= mod;
        for (Player member : players) {
            if ((highestLevel - member.getLevel()) < 10 || group.isMenteering(member)) {
                if (!group.isMentoring(member) || players.size() == 1) {
                    if (checkMentor) expReward = StatFunctions.calculateGroupExperienceReward(member.getLevel(), owner);
                    if (group.isMentoring(member) && players.size() == 1) partyLvlSum = member.getLevel();
                    long currentExp = member.getCommonData().getExp();
                    long reward = (expReward * member.getLevel()) / partyLvlSum;
                    reward *= member.getRates().getGroupXpRate();
                    member.getCommonData().setExp(currentExp + reward);
                    if (owner == null || owner.getObjectTemplate() == null) PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.EXP(Long.toString(reward))); else PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.EXP(reward, owner.getObjectTemplate().getNameId()));
                    int currentDp = member.getCommonData().getDp();
                    int dpReward = StatFunctions.calculateGroupDPReward(member, owner);
                    member.getCommonData().setDp(dpReward + currentDp);
                    if (apRewardPerMember > 0) if (players.contains(World.getInstance().findPlayer(Util.convertName(member.getCommonData().getPartner())))) member.getCommonData().addAp(Math.round(apRewardPerMember * member.getRates().getApNpcRate()) * 2); else member.getCommonData().addAp(Math.round(apRewardPerMember * member.getRates().getApNpcRate()));
                }
            }
            QuestEngine.getInstance().onKill(new QuestCookie(owner, member, 0, 0));
        }
        Player leader = group.getGroupLeader();
        if (checkMentor == true) {
            DropService.getInstance().registerDrop(owner, leader, lowestLevel, players);
        } else {
            DropService.getInstance().registerDrop(owner, leader, highestLevel, players);
        }
        instanceSpecialRewardCheck(owner, group);
    }

    /**
	 * This method will send the show brand to every groupmember
	 * 
	 * @param playerGroup
	 * @param modeId
	 * @param brandId
	 * @param targetObjectId
	 */
    public void showBrand(PlayerGroup playerGroup, int modeId, int brandId, int targetObjectId) {
        for (Player member : playerGroup.getMembers()) {
            PacketSendUtility.sendPacket(member, new SM_SHOW_BRAND(modeId, brandId, targetObjectId));
        }
    }

    /**
	 * This method is called when a group is disbanded
	 */
    private void disbandGroup(PlayerGroup group) {
        group.getGroupLeader().setPlayerGroup(null);
        PacketSendUtility.sendPacket(group.getGroupLeader(), SM_SYSTEM_MESSAGE.DISBAND_GROUP());
        DredgionInstanceService.getInstance().onGroupDisband(group);
        group.disband();
    }

    /**
	 * @param player
	 */
    public void onLogin(Player activePlayer) {
        final PlayerGroup group = activePlayer.getPlayerGroup();
        PacketSendUtility.sendPacket(activePlayer, new SM_GROUP_INFO(group));
        for (Player member : group.getMembers()) {
            if (!activePlayer.equals(member)) PacketSendUtility.sendPacket(activePlayer, new SM_GROUP_MEMBER_INFO(group, member, GroupEvent.ENTER));
        }
    }

    /**
	 * @param playerGroupCache
	 * The playerGroupCache to set
	 */
    private void addPlayerGroupCache(int playerObjId, ScheduledFuture<?> future) {
        if (!playerGroup.containsKey(playerObjId)) playerGroup.put(playerObjId, future);
    }

    /**
	 * This method will remove a schedule to remove a player from a group
	 * 
	 * @param playerObjId
	 */
    private void cancelScheduleRemove(int playerObjId) {
        if (playerGroup.containsKey(playerObjId)) {
            playerGroup.get(playerObjId).cancel(true);
            playerGroup.remove(playerObjId);
        }
    }

    /**
	 * This method will create a schedule to remove a player from a group
	 * 
	 * @param player
	 */
    public void scheduleRemove(final Player player) {
        ScheduledFuture<?> future = ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                removePlayerFromGroup(player);
                playerGroup.remove(player.getObjectId());
            }
        }, DBConfig.GROUP_REMOVE_TIME * 1000);
        addPlayerGroupCache(player.getObjectId(), future);
        player.getPlayerGroup().getMembers().remove(player.getObjectId());
        for (Player groupMember : player.getPlayerGroup().getMembers()) {
            PacketSendUtility.sendPacket(groupMember, SM_SYSTEM_MESSAGE.PARTY_HE_BECOME_OFFLINE(player.getName()));
        }
    }

    /**
	 * @param player
	 */
    public void setGroup(Player player) {
        if (!isGroupMember(player.getObjectId())) return;
        final PlayerGroup group = getGroup(player.getObjectId());
        if (group.size() < 2) {
            removeGroupMemberFromCache(player.getObjectId());
            cancelScheduleRemove(player.getObjectId());
            return;
        }
        player.setPlayerGroup(group);
        group.onGroupMemberLogIn(player);
        cancelScheduleRemove(player.getObjectId());
        if (group.getGroupLeader().getObjectId() == player.getObjectId()) group.setGroupLeader(player);
    }

    /**
	 * @return FastMap<Integer, Boolean>
	 */
    public List<Integer> getMembersToRegistrateByRules(Player player, PlayerGroup group, Npc npc) {
        LootGroupRules lootRules = group.getLootGroupRules();
        LootRuleType lootRule = lootRules.getLootRule();
        List<Integer> luckyMembers = new ArrayList<Integer>();
        switch(lootRule) {
            case ROUNDROBIN:
                int roundRobinMember = group.getRoundRobinMember(npc);
                if (roundRobinMember != 0) {
                    luckyMembers.add(roundRobinMember);
                    break;
                }
            case FREEFORALL:
                luckyMembers = getGroupMembers(group, false);
                break;
            case LEADER:
                luckyMembers.add(group.getGroupLeader().getObjectId());
                break;
        }
        return luckyMembers;
    }

    /**
	 * This method will get all group members
	 * 
	 * @param group
	 * @param except
	 * @return list of group members
	 */
    public List<Integer> getGroupMembers(final PlayerGroup group, boolean except) {
        List<Integer> luckyMembers = new ArrayList<Integer>();
        for (int memberObjId : group.getMemberObjIds()) {
            if (except) {
                if (group.getGroupLeader().getObjectId() != memberObjId) luckyMembers.add(memberObjId);
            } else luckyMembers.add(memberObjId);
        }
        return luckyMembers;
    }

    /**
	 * @param player
	 */
    public Player getLuckyPlayer(Player player) {
        final PlayerGroup group = player.getPlayerGroup();
        switch(group.getLootGroupRules().getAutodistribution()) {
            case NORMAL:
                return player;
            case ROLL_DICE:
                return player;
            case BID:
                return player;
        }
        return player;
    }

    private void instanceSpecialRewardCheck(Monster owner, PlayerGroup group) {
        switch(owner.getWorldId()) {
            case 300040000:
                DarkPoetaInstanceService.getInstance().onGroupReward(owner, group);
                break;
            case 300130000:
                MirenInstanceService.getInstance().onGroupReward(owner, group);
                break;
            case 300120000:
                KysisInstanceService.getInstance().onGroupReward(owner, group);
                break;
            case 300140000:
                KrotanInstanceService.getInstance().onGroupReward(owner, group);
                break;
            case 300110000:
            case 300210000:
                Player winner = owner.getAggroList().getMostPlayerDamage();
                int totalDamage = owner.getAggroList().getTotalDamage();
                if (totalDamage == 0 || winner == null) {
                    return;
                }
                DredgionInstanceService.getInstance().doReward(winner, owner);
                break;
            case 300220000:
                DivineInstanceService.getInstance().onGroupReward(owner, group);
                break;
            case 300170000:
                BeshmundirInstanceService.getInstance().onGroupReward(owner, group);
                break;
            case 300250000:
                EsoterraceInstanceService.getInstance().onGroupReward(owner, group);
                break;
            case 300300000:
                AcademyBootcampService.getInstance().onReward(group, owner);
                break;
        }
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final GroupService instance = new GroupService();
    }
}
