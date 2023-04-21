package com.l2jserver.gameserver.skills.l2skills;

import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.templates.StatsSet;

public class L2SkillAgathion extends L2Skill {

    private int _npcId;

    public L2SkillAgathion(StatsSet set) {
        super(set);
        _npcId = set.getInteger("npcId", 0);
    }

    @Override
    public void useSkill(L2Character caster, L2Object[] targets) {
        if (caster.isAlikeDead() || !(caster instanceof L2PcInstance)) return;
        L2PcInstance activeChar = (L2PcInstance) caster;
        if (activeChar.isInOlympiadMode()) {
            activeChar.sendPacket(new SystemMessage(SystemMessageId.THIS_SKILL_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT));
            return;
        }
        activeChar.setAgathionId(_npcId);
        activeChar.broadcastUserInfo();
    }
}
