package gameserver.model.gameobjects.stats;

import gameserver.configs.CustomConfig;
import gameserver.dataholders.PlayerStatsData;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.gameobjects.stats.id.ItemStatEffectId;
import gameserver.model.gameobjects.stats.id.StatEffectId;
import gameserver.model.gameobjects.stats.modifiers.AddModifier;
import gameserver.model.gameobjects.stats.modifiers.MeanModifier;
import gameserver.model.gameobjects.stats.modifiers.SetModifier;
import gameserver.model.gameobjects.stats.modifiers.SimpleModifier;
import gameserver.model.gameobjects.stats.modifiers.StatModifier;
import gameserver.model.items.ItemSlot;
import gameserver.model.templates.stats.PlayerStatsTemplate;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_STATS_INFO;
import gameserver.utils.PacketSendUtility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import org.apache.log4j.Logger;

public class PlayerGameStats extends CreatureGameStats<Player> {

    private int currentRunSpeed = 0;

    private int currentFlySpeed = 0;

    private int currentAttackSpeed = 0;

    /**
	 * 
	 * @param owner
	 */
    public PlayerGameStats(Player owner) {
        super(owner);
    }

    /**
	 * 
	 * @param playerStatsData
	 * @param owner
	 */
    public PlayerGameStats(PlayerStatsData playerStatsData, final Player owner) {
        super(owner);
        PlayerStatsTemplate pst = playerStatsData.getTemplate(owner.getPlayerClass(), owner.getLevel());
        initStats(pst, owner.getLevel());
        log.debug("loading base game stats for player " + owner.getName() + " (id " + owner.getObjectId() + "): " + this);
    }

    @Override
    public void recomputeStats() {
        try {
            if (!recomputing.compareAndSet(false, true)) return;
            resetStats();
            Map<StatEnum, StatModifiers> orderedModifiers = new HashMap<StatEnum, StatModifiers>();
            List<StatModifier> aspeedBonuses = new ArrayList<StatModifier>();
            boolean attackSpeedApplied = false;
            float power = this.getCurrentStat(StatEnum.POWER) * 0.01f;
            boolean magicalAttack = owner.getAttackType().isMagic();
            synchronized (statsModifiers) {
                List<StatEffectId> statEffectIds = new ArrayList<StatEffectId>(statsModifiers.keySet());
                Collections.sort(statEffectIds);
                for (StatEffectId eid : statEffectIds) {
                    TreeSet<StatModifier> modifiers = statsModifiers.get(eid);
                    int slots = 0;
                    if (modifiers == null) continue;
                    for (StatModifier modifier : modifiers) {
                        if (eid instanceof ItemStatEffectId) {
                            slots = ((ItemStatEffectId) eid).getSlot();
                        }
                        if (slots == 0) slots = ItemSlot.NONE.getSlotIdMask();
                        if (modifier.getStat().isMainOrSubHandStat(magicalAttack)) {
                            if (slots != ItemSlot.MAIN_HAND.getSlotIdMask() && slots != ItemSlot.SUB_HAND.getSlotIdMask()) {
                                if (((Player) owner).getEquipment().getOffHandWeaponType() != null) slots = ItemSlot.MAIN_OR_SUB.getSlotIdMask(); else {
                                    slots = ItemSlot.MAIN_HAND.getSlotIdMask();
                                    setStat(StatEnum.OFF_HAND_ACCURACY, 0, false);
                                }
                            } else if (slots == ItemSlot.MAIN_HAND.getSlotIdMask()) setStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK, 0);
                            if (slots == ItemSlot.MAIN_HAND.getSlotIdMask() || slots == ItemSlot.SUB_HAND.getSlotIdMask()) {
                                if (modifier.isBonus()) slots = ItemSlot.MAIN_OR_SUB.getSlotIdMask();
                            }
                        }
                        List<ItemSlot> oSlots = ItemSlot.getSlotsFor(slots);
                        for (ItemSlot slot : oSlots) {
                            StatEnum statToModify = modifier.getStat().getMainOrSubHandStat(slot, magicalAttack);
                            if (owner.getEquipment().isDualWieldEquipped()) {
                                if (slot == ItemSlot.SUB_HAND) {
                                    if (statToModify == StatEnum.PARRY && !modifier.isBonus()) continue;
                                    if (statToModify == StatEnum.MAGICAL_ACCURACY && !modifier.isBonus()) continue;
                                }
                                if (slot == ItemSlot.SUB_HAND || slot == ItemSlot.MAIN_HAND) {
                                    if (statToModify == StatEnum.ATTACK_SPEED && modifier.isBonus()) {
                                        aspeedBonuses.add(modifier);
                                        continue;
                                    } else if (statToModify == StatEnum.ATTACK_SPEED && !modifier.isBonus() && slot == ItemSlot.SUB_HAND) {
                                        if (attackSpeedApplied) modifier = AddModifier.newInstance(modifier.getStat(), Math.round((float) ((SimpleModifier) modifier).getValue() * 0.25f), false); else {
                                            attackSpeedApplied = true;
                                            modifier = SetModifier.newInstance(modifier.getStat(), Math.round((float) ((SimpleModifier) modifier).getValue() * 0.25f), false);
                                        }
                                    } else if (statToModify == StatEnum.ATTACK_SPEED && !modifier.isBonus() && slot == ItemSlot.MAIN_HAND) {
                                        if (attackSpeedApplied) modifier = AddModifier.newInstance(modifier.getStat(), ((SimpleModifier) modifier).getValue(), false); else {
                                            attackSpeedApplied = true;
                                            modifier = SetModifier.newInstance(modifier.getStat(), ((SimpleModifier) modifier).getValue(), false);
                                        }
                                    }
                                }
                            }
                            if (statToModify == StatEnum.MAIN_HAND_PHYSICAL_ATTACK && !modifier.isBonus()) {
                                if (modifier instanceof AddModifier) modifier = AddModifier.newInstance(modifier.getStat(), Math.round(((SimpleModifier) modifier).getValue() * power), false); else if (modifier instanceof MeanModifier) {
                                    modifier = SetModifier.newInstance(modifier.getStat(), Math.round(modifier.apply(0, 0) * power), false);
                                } else Logger.getLogger(this.getClass()).warn("Different modifer for Mainhandpower base stat: " + modifier.toString());
                            }
                            if (!orderedModifiers.containsKey(statToModify)) {
                                orderedModifiers.put(statToModify, new StatModifiers());
                            }
                            orderedModifiers.get(statToModify).add(modifier);
                        }
                    }
                }
            }
            StatModifier aspeedBonus = null;
            for (StatModifier mod : aspeedBonuses) {
                if (aspeedBonus == null) aspeedBonus = mod; else if (((SimpleModifier) aspeedBonus).getValue() > ((SimpleModifier) mod).getValue()) aspeedBonus = mod;
            }
            if (aspeedBonus != null) orderedModifiers.get(StatEnum.ATTACK_SPEED).add(aspeedBonus);
            aspeedBonuses.clear();
            for (Entry<StatEnum, StatModifiers> entry : orderedModifiers.entrySet()) {
                applyModifiers(entry.getKey(), entry.getValue());
            }
            orderedModifiers.clear();
            applyLimits();
            int newRunSpeed = getCurrentStat(StatEnum.SPEED);
            int newFlySpeed = getCurrentStat(StatEnum.FLY_SPEED);
            int newAttackSpeed = getCurrentStat(StatEnum.ATTACK_SPEED);
            if (newRunSpeed != currentRunSpeed || currentFlySpeed != newFlySpeed || newAttackSpeed != currentAttackSpeed) {
                PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, 0), true);
                if (newFlySpeed < 6000 && owner.isInState(CreatureState.GLIDING)) {
                    owner.getFlyController().endFly();
                }
            }
            PacketSendUtility.sendPacket(owner, new SM_STATS_INFO(owner));
            this.currentRunSpeed = newRunSpeed;
            this.currentFlySpeed = newFlySpeed;
            this.currentAttackSpeed = newAttackSpeed;
            if (owner.getEquipment().getOffHandWeaponType() == null) {
                this.setStat(StatEnum.OFF_HAND_CRITICAL, 0);
                this.setStat(StatEnum.OFF_HAND_CRITICAL, 0, true);
                this.setStat(StatEnum.OFF_HAND_PHYSICAL_ATTACK, 0);
                this.setStat(StatEnum.OFF_HAND_PHYSICAL_ATTACK, 0, true);
                this.setStat(StatEnum.OFF_HAND_ACCURACY, 0);
                this.setStat(StatEnum.OFF_HAND_ACCURACY, 0, true);
            }
        } finally {
            recomputing.set(false);
        }
    }

    @Override
    protected void applyLimits() {
        super.applyLimits();
        int MAX_SPEED = 12000;
        int MAX_FLY_SPEED = 16000;
        int MAX_BOOST_MAGICAL_SKILL = 2600;
        int bonus = 0;
        if (getCurrentStat(StatEnum.SPEED) > MAX_SPEED) {
            bonus = MAX_SPEED - this.getBaseStat(StatEnum.SPEED);
            this.setStat(StatEnum.SPEED, bonus, true);
            bonus = 0;
        }
        if (getCurrentStat(StatEnum.FLY_SPEED) > MAX_FLY_SPEED) {
            bonus = MAX_FLY_SPEED - this.getBaseStat(StatEnum.FLY_SPEED);
            this.setStat(StatEnum.FLY_SPEED, bonus, true);
            bonus = 0;
        }
        if (-getStatBonus(StatEnum.ATTACK_SPEED) > (getBaseStat(StatEnum.ATTACK_SPEED) / 2f)) {
            bonus = Math.round(-getBaseStat(StatEnum.ATTACK_SPEED) / 2f);
            setStat(StatEnum.ATTACK_SPEED, bonus, true);
            bonus = 0;
        }
        if (getCurrentStat(StatEnum.BOOST_MAGICAL_SKILL) > MAX_BOOST_MAGICAL_SKILL) {
            bonus = MAX_BOOST_MAGICAL_SKILL - this.getBaseStat(StatEnum.BOOST_MAGICAL_SKILL);
            this.setStat(StatEnum.BOOST_MAGICAL_SKILL, bonus, true);
            bonus = 0;
        }
    }

    /**
	 * 
	 * @param pst
	 * @param level
	 */
    private void initStats(PlayerStatsTemplate pst, int level) {
        this.initStats(pst.getMaxHp(), pst.getMaxMp(), pst.getPower(), pst.getHealth(), pst.getAgility(), pst.getAccuracy(), pst.getKnowledge(), pst.getWill(), pst.getMainHandAttack(), pst.getMainHandCritRate(), Math.round(pst.getAttackSpeed() * 1000), 1500, Math.round(pst.getRunSpeed() * 1000), Math.round(pst.getFlySpeed() * 1000));
        setAttackCounter(1);
        initStat(StatEnum.PARRY, pst.getParry());
        initStat(StatEnum.BLOCK, pst.getBlock());
        initStat(StatEnum.EVASION, pst.getEvasion());
        initStat(StatEnum.MAGICAL_ACCURACY, pst.getMagicAccuracy());
        initStat(StatEnum.MAIN_HAND_ACCURACY, pst.getMainHandAccuracy());
        initStat(StatEnum.FLY_TIME, CustomConfig.BASE_FLYTIME);
        initStat(StatEnum.REGEN_HP, level + 3);
        initStat(StatEnum.REGEN_MP, level + 8);
        initStat(StatEnum.MAXDP, 4000);
    }

    /**
	 * 
	 * @param maxHp
	 * @param maxMp
	 * @param power
	 * @param health
	 * @param agility
	 * @param accuracy
	 * @param knowledge
	 * @param will
	 * @param mainHandAttack
	 * @param mainHandCritRate
	 * @param attackSpeed
	 * @param attackRange
	 * @param runSpeed
	 * @param flySpeed
	 */
    protected void initStats(int maxHp, int maxMp, int power, int health, int agility, int accuracy, int knowledge, int will, int mainHandAttack, int mainHandCritRate, int attackSpeed, int attackRange, int runSpeed, int flySpeed) {
        stats.clear();
        initStat(StatEnum.MAXHP, maxHp);
        initStat(StatEnum.MAXMP, maxMp);
        initStat(StatEnum.POWER, power);
        initStat(StatEnum.ACCURACY, accuracy);
        initStat(StatEnum.HEALTH, health);
        initStat(StatEnum.AGILITY, agility);
        initStat(StatEnum.KNOWLEDGE, knowledge);
        initStat(StatEnum.WILL, will);
        initStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK, Math.round(18 * (power * 0.01f)));
        initStat(StatEnum.MAIN_HAND_CRITICAL, mainHandCritRate);
        initStat(StatEnum.OFF_HAND_PHYSICAL_ATTACK, 0);
        initStat(StatEnum.OFF_HAND_CRITICAL, 0);
        initStat(StatEnum.ATTACK_SPEED, attackSpeed);
        initStat(StatEnum.ATTACK_RANGE, attackRange);
        initStat(StatEnum.PHYSICAL_DEFENSE, 0);
        initStat(StatEnum.PARRY, Math.round(agility * 3.1f - 248.5f + 12.4f * owner.getLevel()));
        initStat(StatEnum.EVASION, Math.round(agility * 3.1f - 248.5f + 12.4f * owner.getLevel()));
        initStat(StatEnum.BLOCK, Math.round(agility * 3.1f - 248.5f + 12.4f * owner.getLevel()));
        initStat(StatEnum.DAMAGE_REDUCE, 0);
        initStat(StatEnum.MAIN_HAND_ACCURACY, Math.round((accuracy * 2 - 10) + 8 * owner.getLevel()));
        initStat(StatEnum.OFF_HAND_ACCURACY, Math.round((accuracy * 2 - 10) + 8 * owner.getLevel()));
        initStat(StatEnum.MAGICAL_RESIST, 0);
        initStat(StatEnum.WIND_RESISTANCE, 0);
        initStat(StatEnum.FIRE_RESISTANCE, 0);
        initStat(StatEnum.WATER_RESISTANCE, 0);
        initStat(StatEnum.EARTH_RESISTANCE, 0);
        initStat(StatEnum.MAGICAL_ACCURACY, Math.round(14.26f * owner.getLevel()));
        initStat(StatEnum.BOOST_MAGICAL_SKILL, 0);
        initStat(StatEnum.SPEED, runSpeed);
        initStat(StatEnum.FLY_SPEED, flySpeed);
        initStat(StatEnum.PVP_ATTACK_RATIO, 0);
        initStat(StatEnum.PVP_DEFEND_RATIO, 0);
        initStat(StatEnum.BOOST_CASTING_TIME, 100);
        initStat(StatEnum.BOOST_HATE, 100);
        initStat(StatEnum.BOOST_HEAL, 100);
        initStat(StatEnum.MAGICAL_CRITICAL, 50);
    }

    /**
	 * 
	 * @param playerStatsData
	 * @param level
	 */
    public void doLevelUpgrade() {
        initStats(owner.getPlayerStatsTemplate(), owner.getLevel());
        recomputeStats();
    }
}
