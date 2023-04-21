package handlers.skillhandlers;

import l2.universe.gameserver.handler.ISkillHandler;
import l2.universe.gameserver.model.L2Effect;
import l2.universe.gameserver.model.L2ItemInstance;
import l2.universe.gameserver.model.L2Object;
import l2.universe.gameserver.model.L2Skill;
import l2.universe.gameserver.model.actor.L2Character;
import l2.universe.gameserver.model.actor.L2Npc;
import l2.universe.gameserver.model.actor.L2Summon;
import l2.universe.gameserver.skills.Formulas;
import l2.universe.gameserver.templates.skills.L2SkillType;
import l2.universe.util.Rnd;

/**
 * 
 * @author DS
 *
 */
public class Cancel implements ISkillHandler {

    private static final L2SkillType[] SKILL_IDS = { L2SkillType.CANCEL, L2SkillType.MAGE_BANE, L2SkillType.WARRIOR_BANE };

    public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets) {
        final L2ItemInstance weaponInst = activeChar.getActiveWeaponInstance();
        if (weaponInst != null) {
            if (skill.isMagic()) {
                if (weaponInst.getChargedSpiritshot() == L2ItemInstance.CHARGED_BLESSED_SPIRITSHOT) weaponInst.setChargedSpiritshot(L2ItemInstance.CHARGED_NONE); else if (weaponInst.getChargedSpiritshot() == L2ItemInstance.CHARGED_SPIRITSHOT) weaponInst.setChargedSpiritshot(L2ItemInstance.CHARGED_NONE);
            }
        } else if (activeChar instanceof L2Summon) {
            final L2Summon activeSummon = (L2Summon) activeChar;
            if (skill.isMagic()) {
                if (activeSummon.getChargedSpiritShot() == L2ItemInstance.CHARGED_BLESSED_SPIRITSHOT) activeSummon.setChargedSpiritShot(L2ItemInstance.CHARGED_NONE); else if (activeSummon.getChargedSpiritShot() == L2ItemInstance.CHARGED_SPIRITSHOT) activeSummon.setChargedSpiritShot(L2ItemInstance.CHARGED_NONE);
            }
        } else if (activeChar instanceof L2Npc) {
            ((L2Npc) activeChar)._soulshotcharged = false;
            ((L2Npc) activeChar)._spiritshotcharged = false;
        }
        L2Character target;
        L2Effect effect;
        final int cancelLvl, minRate, maxRate;
        cancelLvl = skill.getMagicLevel();
        switch(skill.getSkillType()) {
            case MAGE_BANE:
            case WARRIOR_BANE:
                minRate = 40;
                maxRate = 95;
                break;
            default:
                minRate = 25;
                maxRate = 75;
                break;
        }
        for (L2Object obj : targets) {
            if (!(obj instanceof L2Character)) continue;
            target = (L2Character) obj;
            if (target.isDead()) continue;
            int lastCanceledSkillId = 0;
            int count = skill.getMaxNegatedEffects();
            double baseRate = skill.getPower();
            baseRate += Formulas.calcSkillProficiency(skill, activeChar, target);
            baseRate += Formulas.calcSkillVulnerability(activeChar, target, skill);
            final L2Effect[] effects = target.getAllEffects();
            for (int i = effects.length; --i >= 0; ) {
                effect = effects[i];
                if (effect == null) continue;
                if (!effect.canBeStolen()) {
                    effects[i] = null;
                    continue;
                }
                switch(skill.getSkillType()) {
                    case MAGE_BANE:
                        if ("casting_time_down".equalsIgnoreCase(effect.getStackType())) break;
                        if ("ma_up".equalsIgnoreCase(effect.getStackType())) break;
                        effects[i] = null;
                        continue;
                    case WARRIOR_BANE:
                        if ("attack_time_down".equalsIgnoreCase(effect.getStackType())) break;
                        if ("speed_up".equalsIgnoreCase(effect.getStackType())) break;
                        effects[i] = null;
                        continue;
                }
                if (!effect.getSkill().isDance()) continue;
                if (effect.getSkill().getId() == lastCanceledSkillId) {
                    effect.exit();
                    continue;
                }
                if (!calcCancelSuccess(effect, cancelLvl, (int) baseRate, minRate, maxRate)) continue;
                lastCanceledSkillId = effect.getSkill().getId();
                effect.exit();
                count--;
                if (count == 0) break;
            }
            if (count != 0) {
                lastCanceledSkillId = 0;
                for (int i = effects.length; --i >= 0; ) {
                    effect = effects[i];
                    if (effect == null) continue;
                    if (effect.getSkill().isDance()) continue;
                    if (effect.getSkill().getId() == lastCanceledSkillId) {
                        effect.exit();
                        continue;
                    }
                    if (!calcCancelSuccess(effect, cancelLvl, (int) baseRate, minRate, maxRate)) continue;
                    lastCanceledSkillId = effect.getSkill().getId();
                    effect.exit();
                    count--;
                    if (count == 0) break;
                }
            }
            Formulas.calcLethalHit(activeChar, target, skill);
        }
        if (skill.hasSelfEffects()) {
            effect = activeChar.getFirstEffect(skill.getId());
            if (effect != null && effect.isSelfEffect()) {
                effect.exit();
            }
            skill.getEffectsSelf(activeChar);
        }
    }

    private boolean calcCancelSuccess(L2Effect effect, int cancelLvl, int baseRate, int minRate, int maxRate) {
        int rate = 2 * (cancelLvl - effect.getSkill().getMagicLevel());
        rate += (effect.getPeriod() - effect.getTime()) / 1200;
        rate += baseRate;
        if (rate < minRate) rate = minRate; else if (rate > maxRate) rate = maxRate;
        return Rnd.get(100) < rate;
    }

    public L2SkillType[] getSkillIds() {
        return SKILL_IDS;
    }
}
