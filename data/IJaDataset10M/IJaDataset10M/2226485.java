package handlers.skillhandlers;

import l2.universe.gameserver.handler.ISkillHandler;
import l2.universe.gameserver.model.L2Object;
import l2.universe.gameserver.model.L2Skill;
import l2.universe.gameserver.model.actor.L2Character;
import l2.universe.gameserver.model.actor.instance.L2PcInstance;
import l2.universe.gameserver.network.SystemMessageId;
import l2.universe.gameserver.network.serverpackets.SystemMessage;
import l2.universe.gameserver.network.serverpackets.UserInfo;
import l2.universe.gameserver.templates.skills.L2SkillType;

public class GiveVitality implements ISkillHandler {

    private static final L2SkillType[] SKILL_IDS = { L2SkillType.GIVE_VITALITY };

    @Override
    public void useSkill(final L2Character activeChar, final L2Skill skill, final L2Object[] targets) {
        for (final L2Object target : targets) if (target instanceof L2PcInstance) {
            if (skill.hasEffects()) {
                ((L2PcInstance) target).stopSkillEffects(skill.getId());
                skill.getEffects(activeChar, ((L2PcInstance) target));
                final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT);
                sm.addSkillName(skill);
                target.sendPacket(sm);
            }
            ((L2PcInstance) target).updateVitalityPoints((float) skill.getPower(), false, false);
            ((L2PcInstance) target).sendPacket(new UserInfo((L2PcInstance) target));
        }
    }

    @Override
    public L2SkillType[] getSkillIds() {
        return SKILL_IDS;
    }
}
