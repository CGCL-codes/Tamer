package gameserver.network.aion.clientpackets;

import gameserver.GameServer;
import gameserver.cache.HTMLCache;
import gameserver.configs.AdminConfig;
import gameserver.configs.CustomConfig;
import gameserver.configs.GameServerConfig;
import gameserver.configs.GeneralConfig;
import gameserver.configs.RateConfig;
import gameserver.dao.PlayerPasskeyDAO;
import gameserver.dataholders.DataManager;
import gameserver.model.ChatType;
import gameserver.model.EmotionType;
import gameserver.model.account.Account;
import gameserver.model.account.CharacterPasskey.ConnectType;
import gameserver.model.account.PlayerAccountData;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.gameobjects.state.CreatureVisualState;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.model.siege.Artifact;
import gameserver.model.templates.GuildTemplate;
import gameserver.model.templates.QuestTemplate;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.serverpackets.SM_ABYSS_RANK;
import gameserver.network.aion.serverpackets.SM_CHANNEL_INFO;
import gameserver.network.aion.serverpackets.SM_CHARACTER_SELECT;
import gameserver.network.aion.serverpackets.SM_CUBE_UPDATE;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_EMOTION_LIST;
import gameserver.network.aion.serverpackets.SM_ENTER_WORLD_CHECK;
import gameserver.network.aion.serverpackets.SM_GAME_TIME;
import gameserver.network.aion.serverpackets.SM_INFLUENCE_RATIO;
import gameserver.network.aion.serverpackets.SM_INSTANCE_COOLDOWN;
import gameserver.network.aion.serverpackets.SM_INVENTORY_INFO;
import gameserver.network.aion.serverpackets.SM_ITEM_COOLDOWN;
import gameserver.network.aion.serverpackets.SM_MACRO_LIST;
import gameserver.network.aion.serverpackets.SM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_MOTION;
import gameserver.network.aion.serverpackets.SM_PLAYER_SPAWN;
import gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import gameserver.network.aion.serverpackets.SM_PRICES;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.network.aion.serverpackets.SM_QUEST_LIST;
import gameserver.network.aion.serverpackets.SM_RECIPE_LIST;
import gameserver.network.aion.serverpackets.SM_SIEGE_LOCATION_INFO;
import gameserver.network.aion.serverpackets.SM_SKILL_COOLDOWN;
import gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import gameserver.network.aion.serverpackets.SM_STARTED_QUEST_LIST;
import gameserver.network.aion.serverpackets.SM_STATS_INFO;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_TITLE_LIST;
import gameserver.network.aion.serverpackets.SM_UI_SETTINGS;
import gameserver.quest.QuestEngine;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.AllianceService;
import gameserver.services.ArenaService;
import gameserver.services.BrokerService;
import gameserver.services.ChatService;
import gameserver.services.ClassChangeService;
import gameserver.services.GroupService;
import gameserver.services.GuildService;
import gameserver.services.HTMLService;
import gameserver.services.KiskService;
import gameserver.services.LegionService;
import gameserver.services.MailService;
import gameserver.services.PetitionService;
import gameserver.services.PlayerService;
import gameserver.services.PunishmentService;
import gameserver.services.SiegeService;
import gameserver.services.StigmaService;
import gameserver.services.TeleportService;
import gameserver.services.ToyPetService;
import gameserver.skill.SkillEngine;
import gameserver.skill.effect.EffectId;
import gameserver.skill.model.CreatureWithDistance;
import gameserver.skill.model.Skill;
import gameserver.utils.DualBoxProtection;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;
import gameserver.utils.rates.Rates;
import gameserver.world.World;
import java.net.InetAddress;
import java.util.List;
import org.apache.log4j.Logger;
import commons.database.dao.DAOManager;
import commons.versionning.Version;

/**
 * In this packets aion client is asking if given char [by oid] may login into game [ie start playing].
 */
public class CM_ENTER_WORLD extends AionClientPacket {

    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(CM_ENTER_WORLD.class);

    /**
	* Object Id of player that is entering world
	*/
    private int objectId;

    private static String serverMessage;

    private static String serverMessageRegular;

    private static String serverMessagePremium;

    private static String serverMessageVip;

    /**
	* Constructs new instance of <tt>CM_ENTER_WORLD </tt> packet
	* 
	* @param opcode
	*/
    public CM_ENTER_WORLD(int opcode) {
        super(opcode);
    }

    static {
        String bufferDisplayRev = null;
        if (GeneralConfig.SERVER_MOTD_DISPLAYREV) bufferDisplayRev = LanguageHandler.translate(CustomMessageId.SERVER_REVISION, new Version(GameServer.class).getRevision());
        if (RateConfig.DISPLAY_RATE) {
            String bufferRegular = LanguageHandler.translate(CustomMessageId.WELCOME_REGULAR, GameServerConfig.SERVER_NAME, RateConfig.XP_RATE, RateConfig.QUEST_XP_RATE, RateConfig.DROP_RATE, RateConfig.KINAH_RATE);
            String bufferPremium = LanguageHandler.translate(CustomMessageId.WELCOME_PREMIUM, GameServerConfig.SERVER_NAME, RateConfig.PREMIUM_XP_RATE, RateConfig.PREMIUM_QUEST_XP_RATE, RateConfig.PREMIUM_DROP_RATE, RateConfig.PREMIUM_KINAH_RATE);
            String bufferVip = LanguageHandler.translate(CustomMessageId.WELCOME_VIP, GameServerConfig.SERVER_NAME, RateConfig.VIP_XP_RATE, RateConfig.VIP_QUEST_XP_RATE, RateConfig.VIP_DROP_RATE, RateConfig.VIP_KINAH_RATE);
            if (bufferDisplayRev != null) {
                bufferRegular += bufferDisplayRev;
                bufferPremium += bufferDisplayRev;
                bufferVip += bufferDisplayRev;
            }
            serverMessageRegular = bufferRegular;
            bufferRegular = null;
            serverMessagePremium = bufferPremium;
            bufferPremium = null;
            serverMessageVip = bufferVip;
            bufferVip = null;
        } else {
            String buffer = LanguageHandler.translate(CustomMessageId.WELCOME_BASIC, GameServerConfig.SERVER_NAME);
            if (bufferDisplayRev != null) buffer += bufferDisplayRev;
            serverMessage = buffer;
            buffer = null;
        }
    }

    /**
	* {@inheritDoc}
	*/
    @Override
    protected void readImpl() {
        objectId = readD();
    }

    /**
	* {@inheritDoc}
	*/
    @Override
    protected void runImpl() {
        AionConnection client = getConnection();
        Player player = World.getInstance().findPlayer(objectId);
        if (player != null) {
            if (player.getClientConnection() != null) player.getClientConnection().close(new SM_SYSTEM_MESSAGE(1310052, false, 0), true); else PlayerService.playerLoggedOut(player);
            return;
        }
        if (GeneralConfig.PASSKEY_ENABLE && !client.getAccount().getCharacterPasskey().isPass()) {
            client.getAccount().getCharacterPasskey().setConnectType(ConnectType.ENTER);
            client.getAccount().getCharacterPasskey().setObjectId(objectId);
            boolean isExistPasskey = DAOManager.getDAO(PlayerPasskeyDAO.class).existCheckPlayerPasskey(client.getAccount().getId());
            if (!isExistPasskey) client.sendPacket(new SM_CHARACTER_SELECT(0)); else client.sendPacket(new SM_CHARACTER_SELECT(1));
        } else enterWorld(client, objectId);
    }

    public static void enterWorld(AionConnection client, int objectId) {
        Account account = client.getAccount();
        PlayerAccountData playerAccData = client.getAccount().getPlayerAccountData(objectId);
        if (playerAccData == null) {
            return;
        }
        Player player = PlayerService.getPlayer(objectId, account);
        int lastOnlineTime = player.getLastOnline();
        if (player != null && client.setActivePlayer(player)) {
            player.setClientConnection(client);
            World.getInstance().storeObject(player);
            StigmaService.onPlayerLogin(player);
            client.sendPacket(new SM_SKILL_LIST(player));
            if (player.getSkillCoolDowns() != null) client.sendPacket(new SM_SKILL_COOLDOWN(player.getSkillCoolDowns()));
            if (player.getItemCoolDowns() != null) client.sendPacket(new SM_ITEM_COOLDOWN(player.getItemCoolDowns()));
            client.sendPacket(new SM_QUEST_LIST(player));
            client.sendPacket(new SM_STARTED_QUEST_LIST(player));
            client.sendPacket(new SM_RECIPE_LIST(player.getRecipeList().getRecipeList()));
            client.sendPacket(new SM_ENTER_WORLD_CHECK());
            byte[] uiSettings = player.getPlayerSettings().getUiSettings();
            byte[] shortcuts = player.getPlayerSettings().getShortcuts();
            if (uiSettings != null) client.sendPacket(new SM_UI_SETTINGS(uiSettings, 0));
            if (shortcuts != null) client.sendPacket(new SM_UI_SETTINGS(shortcuts, 1));
            int cubeSize = player.getCubeSize();
            player.getInventory().setLimit(27 + cubeSize * 9);
            Storage inventory = player.getInventory();
            List<Item> equipedItems = player.getEquipment().getEquippedItems();
            if (equipedItems.size() != 0) {
                client.sendPacket(new SM_INVENTORY_INFO(player.getEquipment().getEquippedItems(), cubeSize));
            }
            List<Item> unequipedItems = inventory.getAllItems();
            int itemsSize = unequipedItems.size();
            if (itemsSize != 0) {
                int index = 0;
                while (index + 10 < itemsSize) {
                    client.sendPacket(new SM_INVENTORY_INFO(unequipedItems.subList(index, index + 10), cubeSize));
                    index += 10;
                }
                client.sendPacket(new SM_INVENTORY_INFO(unequipedItems.subList(index, itemsSize), cubeSize));
            }
            client.sendPacket(new SM_INVENTORY_INFO());
            PlayerService.playerLoggedIn(player);
            long secondsOffline = (System.currentTimeMillis() / 1000) - lastOnlineTime;
            if (secondsOffline > 14400) {
                int hours = Math.round(secondsOffline / 3600);
                long maxRespose = ((player.getLevel() * 1000) * 2) * player.getLevel();
                if (hours > 20) hours = 20;
                hours *= 5;
                int currentResposePercent = Math.round((player.getCommonData().getRepletionState() / maxRespose) * 100);
                if (currentResposePercent + hours >= 100) {
                    player.getCommonData().setRepletionState(maxRespose);
                } else {
                    currentResposePercent += hours;
                    player.getCommonData().setRepletionState((maxRespose * currentResposePercent) / 100);
                }
            }
            client.sendPacket(new SM_MOTION(player, true));
            client.sendPacket(new SM_STATS_INFO(player));
            client.sendPacket(new SM_CUBE_UPDATE(player, 6, player.getCommonData().getadvancedStigmaSlotSize()));
            KiskService.onLogin(player);
            TeleportService.sendSetBindPoint(player);
            if (player.isInAlliance()) AllianceService.getInstance().onLogin(player);
            client.sendPacket(new SM_INSTANCE_COOLDOWN(player));
            client.sendPacket(new SM_MACRO_LIST(player));
            client.sendPacket(new SM_GAME_TIME());
            QuestEngine.getInstance().onLvlUp(new QuestCookie(null, player, 0, 0));
            player.getController().updateNearbyQuests();
            client.sendPacket(new SM_TITLE_LIST(player));
            client.sendPacket(new SM_CHANNEL_INFO(player.getPosition()));
            client.sendPacket(new SM_PLAYER_SPAWN(player));
            client.sendPacket(new SM_EMOTION_LIST(player));
            client.sendPacket(new SM_INFLUENCE_RATIO());
            client.sendPacket(new SM_SIEGE_LOCATION_INFO());
            client.sendPacket(new SM_PRICES(player.getPrices()));
            client.sendPacket(new SM_ABYSS_RANK(player.getAbyssRank()));
            if (serverMessage != null) {
                client.sendPacket(new SM_MESSAGE(0, null, serverMessage, ChatType.ANNOUNCEMENTS));
            } else {
                if (client.getAccount().getMembership() == 1) {
                    client.sendPacket(new SM_MESSAGE(0, null, serverMessagePremium, ChatType.ANNOUNCEMENTS));
                } else {
                    if (client.getAccount().getMembership() == 2) {
                        client.sendPacket(new SM_MESSAGE(0, null, serverMessageVip, ChatType.ANNOUNCEMENTS));
                    } else {
                        client.sendPacket(new SM_MESSAGE(0, null, serverMessageRegular, ChatType.ANNOUNCEMENTS));
                    }
                }
            }
            if (player.isInPrison()) PunishmentService.updatePrisonStatus(player);
            if (player.isLegionMember()) LegionService.getInstance().onLogin(player);
            if (player.isInGroup()) GroupService.getInstance().onLogin(player);
            player.setRates(Rates.getRatesFor(client.getAccount().getMembership()));
            ToyPetService.getInstance().onPlayerLogin(player);
            if (AdminConfig.ANNOUNCE_GM_CONNECTION) {
                if (player.isGM()) {
                    String playerName = "";
                    if (AdminConfig.GMTAG_DISPLAY) {
                        if (player.getAccessLevel() == 1) {
                            playerName += AdminConfig.GM_LEVEL1.trim();
                        } else if (player.getAccessLevel() == 2) {
                            playerName += AdminConfig.GM_LEVEL2.trim();
                        } else if (player.getAccessLevel() == 3) {
                            playerName += AdminConfig.GM_LEVEL3.trim();
                        } else if (player.getAccessLevel() == 4) {
                            playerName += AdminConfig.GM_LEVEL4.trim();
                        } else if (player.getAccessLevel() == 5) {
                            playerName += AdminConfig.GM_LEVEL5.trim();
                        }
                    }
                    playerName += player.getName();
                    final String _playerName = playerName;
                    World.getInstance().doOnAllPlayers(new Executor<Player>() {

                        @Override
                        public boolean run(Player p) {
                            PacketSendUtility.sendMessage(p, LanguageHandler.translate(CustomMessageId.ANNOUNCE_GM_CONNECTION, _playerName));
                            return true;
                        }
                    });
                }
            }
            if (player.isGM()) {
                if (AdminConfig.INVIS_GM_CONNECTION) {
                    player.getEffectController().setAbnormal(EffectId.INVISIBLE_RELATED.getEffectId());
                    player.setVisualState(CreatureVisualState.HIDE20);
                    PacketSendUtility.broadcastPacket(player, new SM_PLAYER_STATE(player), true);
                    PacketSendUtility.sendMessage(player, "! YOU LOGGED IN INVISIBLE MODE !");
                }
                if (AdminConfig.INVUL_GM_CONNECTION) {
                    player.setInvul(true);
                    PacketSendUtility.sendMessage(player, "! YOU LOGGED IN INVULNERABLE MODE !");
                }
                if (AdminConfig.NEUTRAL_GM_CONNECTION) {
                    player.setAdminNeutral(true);
                    PacketSendUtility.sendMessage(player, "! YOU LOGGED IN NEUTRAL MODE !");
                }
                if (AdminConfig.SILENCE_GM_CONNECTION) {
                    player.setWhisperable(true);
                    PacketSendUtility.sendMessage(player, "! YOU LOGGED IN SILENT MODE !");
                }
                if (AdminConfig.SPEED_GM_CONNECTION > 0) {
                    int speed = 6000;
                    int flyspeed = 9000;
                    player.getGameStats().setStat(StatEnum.SPEED, (speed + (speed * AdminConfig.SPEED_GM_CONNECTION) / 100));
                    player.getGameStats().setStat(StatEnum.FLY_SPEED, (flyspeed + (flyspeed * AdminConfig.SPEED_GM_CONNECTION) / 100));
                    PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_EMOTE2, 0, 0), true);
                    PacketSendUtility.sendMessage(player, "! YOU LOGGED IN SPEED MODE !");
                }
            }
            ClassChangeService.showClassChangeDialog(player);
            MailService.getInstance().onPlayerLogin(player);
            BrokerService.getInstance().onPlayerLogin(player);
            if (!GeneralConfig.DISABLE_CHAT_SERVER) ChatService.onPlayerLogin(player);
            PetitionService.getInstance().onPlayerLogin(player);
            SiegeService.getInstance().onPlayerLogin(player);
            player.getLifeStats().updateCurrentStats();
            if (GeneralConfig.ENABLE_HTML_WELCOME) HTMLService.showHTML(player, HTMLCache.getInstance().getHTML("welcome.xhtml"));
            if (CustomConfig.DUALBOX_PROTECTION) DualBoxProtection.checkConnection(player);
            if (CustomConfig.ENABLE_SURVEYS) HTMLService.onPlayerLogin(player);
            if (player.getGuild().getGuildId() != 0) {
                int currentQuest = player.getGuild().getCurrentQuest();
                if (currentQuest == 0) {
                    GuildTemplate guildTemplate = DataManager.GUILDS_DATA.getGuildTemplateByGuildId(player.getGuild().getGuildId());
                    GuildService.getInstance().sendDailyQuest(player, guildTemplate);
                } else {
                    QuestState qs = player.getQuestStateList().getQuestState(currentQuest);
                    QuestTemplate template = DataManager.QUEST_DATA.getQuestById(currentQuest);
                    if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat(template.getMaxRepeatCount())) PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(6, currentQuest));
                }
            }
            if (ArenaService.getInstance().isInArena(player)) player.setInArena(true);
            for (Artifact art : World.getInstance().getArtifacts()) {
                if (art.getController().lastArtifactActivation > 0 && art.getTemplate().getEffectTemplate().getRange().equals("WORLD") && art.getController().getRemainingCooldownSecs() > 0) {
                    if (art.getObjectTemplate().getRace() == player.getCommonData().getRace() && player.getEffectController().hasAbnormalEffect(art.getTemplate().getEffectTemplate().getSkillId())) {
                        Skill skill = SkillEngine.getInstance().getSkill(art, art.getTemplate().getEffectTemplate().getSkillId(), 1, player);
                        skill.setFirstTargetRangeCheck(false);
                        skill.getEffectedList().clear();
                        skill.getEffectedList().add(new CreatureWithDistance(player, 0));
                        skill.endCast();
                    }
                }
            }
            player.getController().onEnterWorld();
        } else {
        }
    }
}
