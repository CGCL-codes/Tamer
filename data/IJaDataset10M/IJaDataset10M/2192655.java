package nakayo.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

/**
 * @author ATracer, Geekswordsman
 */
public class RateConfig {

    /**
     * Display server rates when player enter in game server
     */
    @Property(key = "gameserver.rate.display.rates", defaultValue = "false")
    public static boolean DISPLAY_RATE;

    /**
     * Dp Rate - Regular,Vip and Premium
     */
    @Property(key = "gameserver.rate.regular.dp", defaultValue = "1")
    public static int DP_RATE;

    @Property(key = "gameserver.rate.premium.dp", defaultValue = "2")
    public static int PREMIUM_DP_RATE;

    @Property(key = "gameserver.rate.vip.dp", defaultValue = "3")
    public static int VIP_DP_RATE;

    /**
     * PvP Dp Rate - Regular,Vip and Premium
     */
    @Property(key = "gameserver.rate.regular.pvp.dp", defaultValue = "1")
    public static int PVP_DP_RATE;

    @Property(key = "gameserver.rate.premium.pvp.dp", defaultValue = "2")
    public static int PREMIUM_PVP_DP_RATE;

    @Property(key = "gameserver.rate.vip.pvp.dp", defaultValue = "3")
    public static int VIP_PVP_DP_RATE;

    /**
     * Group Dp Rate - Regular,Vip and Premium
     */
    @Property(key = "gameserver.rate.regular.group.dp", defaultValue = "1")
    public static int GROUPDP_RATE;

    @Property(key = "gameserver.rate.premium.group.dp", defaultValue = "2")
    public static int PREMIUM_GROUPDP_RATE;

    @Property(key = "gameserver.rate.vip.group.dp", defaultValue = "3")
    public static int VIP_GROUPDP_RATE;

    /**
     * Group Xp Rate - Regular,Vip and Premium
     */
    @Property(key = "gameserver.rate.regular.group.xp", defaultValue = "1")
    public static int GROUPXP_RATE;

    @Property(key = "gameserver.rate.premium.group.xp", defaultValue = "2")
    public static int PREMIUM_GROUPXP_RATE;

    @Property(key = "gameserver.rate.vip.group.xp", defaultValue = "3")
    public static int VIP_GROUPXP_RATE;

    /**
     * Xp Rate - Regular,Vip and Premium
     */
    @Property(key = "gameserver.rate.regular.xp", defaultValue = "1")
    public static int XP_RATE;

    @Property(key = "gameserver.rate.premium.xp", defaultValue = "2")
    public static int PREMIUM_XP_RATE;

    @Property(key = "gameserver.rate.vip.xp", defaultValue = "3")
    public static int VIP_XP_RATE;

    /**
     * Experience Bonuses for grouping with larger members
     */
    @Property(key = "gameserver.rate.groupsizebonus.enable", defaultValue = "true")
    public static boolean GROUPSIZEBONUS_XP_ENABLE;

    @Property(key = "gameserver.rate.regular.groupsizebonus.xp", defaultValue = "2")
    public static float GROUPSIZEBONUS_REGULAR_XP_RATE;

    @Property(key = "gameserver.rate.premium.groupsizebonus.xp", defaultValue = "2")
    public static float GROUPSIZEBONUS_PREMIUM_XP_RATE;

    @Property(key = "gameserver.rate.vip.groupsizebonus.xp", defaultValue = "2")
    public static float GROUPSIZEBONUS_VIP_XP_RATE;

    /**
     * Quest Xp Rate - Regular,Vip and Premium
     */
    @Property(key = "gameserver.rate.regular.quest.xp", defaultValue = "1")
    public static int QUEST_XP_RATE;

    @Property(key = "gameserver.rate.premium.quest.xp", defaultValue = "2")
    public static int PREMIUM_QUEST_XP_RATE;

    @Property(key = "gameserver.rate.vip.quest.xp", defaultValue = "3")
    public static int VIP_QUEST_XP_RATE;

    /**
     * Gathering Xp Rate - Regular,Vip and Premium
     */
    @Property(key = "gameserver.rate.regular.gathering.xp", defaultValue = "1")
    public static float GATHERING_XP_RATE;

    @Property(key = "gameserver.rate.premium.gathering.xp", defaultValue = "1")
    public static float PREMIUM_GATHERING_XP_RATE;

    @Property(key = "gameserver.rate.vip.gathering.xp", defaultValue = "2")
    public static float VIP_GATHERING_XP_RATE;

    /**
     * Crafting Xp Rate - Regular,Vip and Premium
     */
    @Property(key = "gameserver.rate.regular.crafting.xp", defaultValue = "1")
    public static float CRAFTING_XP_RATE;

    @Property(key = "gameserver.rate.premium.crafting.xp", defaultValue = "1")
    public static float PREMIUM_CRAFTING_XP_RATE;

    @Property(key = "gameserver.rate.vip.crafting.xp", defaultValue = "2")
    public static float VIP_CRAFTING_XP_RATE;

    /**
     * Quest Kinah Rate - Regular,Vip and Premium
     */
    @Property(key = "gameserver.rate.regular.quest.kinah", defaultValue = "1")
    public static int QUEST_KINAH_RATE;

    @Property(key = "gameserver.rate.premium.quest.kinah", defaultValue = "2")
    public static int PREMIUM_QUEST_KINAH_RATE;

    @Property(key = "gameserver.rate.vip.quest.kinah", defaultValue = "3")
    public static int VIP_QUEST_KINAH_RATE;

    /**
     * Drop Rate - Regular,Vip and Premium
     */
    @Property(key = "gameserver.rate.regular.drop", defaultValue = "1")
    public static int DROP_RATE;

    @Property(key = "gameserver.rate.premium.drop", defaultValue = "2")
    public static int PREMIUM_DROP_RATE;

    @Property(key = "gameserver.rate.vip.drop", defaultValue = "3")
    public static int VIP_DROP_RATE;

    /**
     * Abyss Points Rate - Regular,Vip and Premium
     */
    @Property(key = "gameserver.rate.regular.ap.player", defaultValue = "1")
    public static float AP_PLAYER_RATE;

    @Property(key = "gameserver.rate.regular.ap.lost_player", defaultValue = "1")
    public static float AP_LOST_PLAYER_RATE;

    @Property(key = "gameserver.rate.premium.ap.player", defaultValue = "2")
    public static float PREMIUM_AP_PLAYER_RATE;

    @Property(key = "gameserver.rate.premium.ap.lost_player", defaultValue = "2")
    public static float PREMIUM_AP_LOST_PLAYER_RATE;

    @Property(key = "gameserver.rate.vip.ap.player", defaultValue = "3")
    public static float VIP_AP_PLAYER_RATE;

    @Property(key = "gameserver.rate.vip.ap.lost_player", defaultValue = "3")
    public static float VIP_AP_LOST_PLAYER_RATE;

    @Property(key = "gameserver.rate.regular.ap.npc", defaultValue = "1")
    public static float AP_NPC_RATE;

    @Property(key = "gameserver.rate.premium.ap.npc", defaultValue = "2")
    public static float PREMIUM_AP_NPC_RATE;

    @Property(key = "gameserver.rate.vip.ap.npc", defaultValue = "3")
    public static float VIP_AP_NPC_RATE;

    /**
     * Kinah Rate - Regular,Vip and Premium
     */
    @Property(key = "gameserver.rate.regular.kinah", defaultValue = "1")
    public static int KINAH_RATE;

    @Property(key = "gameserver.rate.premium.kinah", defaultValue = "2")
    public static int PREMIUM_KINAH_RATE;

    @Property(key = "gameserver.rate.vip.kinah", defaultValue = "3")
    public static int VIP_KINAH_RATE;
}
