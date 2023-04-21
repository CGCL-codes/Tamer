package handlers.skillhandlers;

import l2.universe.gameserver.datatables.SkillTable;
import l2.universe.gameserver.handler.ISkillHandler;
import l2.universe.gameserver.model.L2Object;
import l2.universe.gameserver.model.L2Skill;
import l2.universe.gameserver.model.actor.L2Character;
import l2.universe.gameserver.model.actor.instance.L2PcInstance;
import l2.universe.gameserver.network.SystemMessageId;
import l2.universe.gameserver.network.serverpackets.SystemMessage;
import l2.universe.gameserver.templates.skills.L2SkillType;

/**
 * 
 * @author nBd
 */
public class Soul implements ISkillHandler {

    private static final L2SkillType[] SKILL_IDS = { L2SkillType.CHARGESOUL };

    @Override
    public void useSkill(final L2Character activeChar, final L2Skill skill, final L2Object[] targets) {
        if (!(activeChar instanceof L2PcInstance) || activeChar.isAlikeDead()) return;
        final L2PcInstance player = (L2PcInstance) activeChar;
        int level = player.getSkillLevel(467);
        if (level > 0) {
            final L2Skill soulmastery = SkillTable.getInstance().getInfo(467, level);
            if (soulmastery != null) {
                if (player.getSouls() < soulmastery.getNumSouls()) {
                    int count = 0;
                    if (player.getSouls() + skill.getNumSouls() <= soulmastery.getNumSouls()) count = skill.getNumSouls(); else count = soulmastery.getNumSouls() - player.getSouls();
                    player.increaseSouls(count);
                } else {
                    final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.SOUL_CANNOT_BE_INCREASED_ANYMORE);
                    player.sendPacket(sm);
                    return;
                }
            }
        }
    }

    @Override
    public L2SkillType[] getSkillIds() {
        return SKILL_IDS;
    }
}
