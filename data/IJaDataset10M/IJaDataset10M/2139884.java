package net.sf.l2j.gameserver.skills.l2skills;

import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.effects.EffectCharge;
import net.sf.l2j.gameserver.templates.StatsSet;

public class L2SkillNeedCharge extends L2Skill {

    final int numCharges;

    final int chargeSkillId;

    public L2SkillNeedCharge(StatsSet set) {
        super(set);
        numCharges = set.getInteger("num_charges", getLevel());
        chargeSkillId = set.getInteger("charge_skill_id");
    }

    @Override
    public void useSkill(L2Character activeChar, L2Object[] targets) {
        if (activeChar.isAlikeDead()) return;
        EffectCharge effect = (EffectCharge) activeChar.getFirstEffect(chargeSkillId);
        if (effect == null || effect.numCharges < numCharges) {
            SystemMessage sm = new SystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
            sm.addSkillName(getId());
            activeChar.sendPacket(sm);
            return;
        }
        effect.numCharges -= numCharges;
        activeChar.updateEffectIcons();
        if (effect.numCharges == 0) effect.exit();
        if (hasEffects()) for (L2Object target : targets) getEffects(activeChar, (L2Character) target);
    }
}
