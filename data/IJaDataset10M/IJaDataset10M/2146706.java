package handlers.actionhandlers;

import l2.universe.Config;
import l2.universe.gameserver.GeoData;
import l2.universe.gameserver.ai.CtrlIntention;
import l2.universe.gameserver.handler.IActionHandler;
import l2.universe.gameserver.model.L2Object;
import l2.universe.gameserver.model.L2Object.InstanceType;
import l2.universe.gameserver.model.actor.L2Character;
import l2.universe.gameserver.model.actor.instance.L2PcInstance;
import l2.universe.gameserver.model.actor.instance.L2PetInstance;
import l2.universe.gameserver.network.SystemMessageId;
import l2.universe.gameserver.network.serverpackets.MyTargetSelected;
import l2.universe.gameserver.network.serverpackets.PetStatusShow;
import l2.universe.gameserver.network.serverpackets.StatusUpdate;
import l2.universe.gameserver.network.serverpackets.SystemMessage;
import l2.universe.gameserver.network.serverpackets.ValidateLocation;

public class L2PetInstanceAction implements IActionHandler {

    public boolean action(final L2PcInstance activeChar, final L2Object target, final boolean interact) {
        if (activeChar.isLockedTarget() && activeChar.getLockedTarget() != target) {
            activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.FAILED_CHANGE_TARGET));
            return false;
        }
        final boolean isOwner = activeChar.getObjectId() == ((L2PetInstance) target).getOwner().getObjectId();
        activeChar.sendPacket(new ValidateLocation((L2Character) target));
        if (isOwner && activeChar != ((L2PetInstance) target).getOwner()) ((L2PetInstance) target).updateRefOwner(activeChar);
        if (activeChar.getTarget() != target) {
            if (Config.DEBUG) _log.fine("new target selected:" + target.getObjectId());
            activeChar.setTarget(target);
            activeChar.sendPacket(new MyTargetSelected(target.getObjectId(), activeChar.getLevel() - ((L2Character) target).getLevel()));
            final StatusUpdate su = new StatusUpdate(target);
            su.addAttribute(StatusUpdate.CUR_HP, (int) ((L2Character) target).getCurrentHp());
            su.addAttribute(StatusUpdate.MAX_HP, ((L2Character) target).getMaxHp());
            activeChar.sendPacket(su);
        } else if (interact) {
            if (target.isAutoAttackable(activeChar) && !isOwner) {
                if (Config.GEODATA > 0) {
                    if (GeoData.getInstance().canSeeTarget(activeChar, target)) {
                        activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
                        activeChar.onActionRequest();
                    }
                } else {
                    activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
                    activeChar.onActionRequest();
                }
            } else if (!((L2Character) target).isInsideRadius(activeChar, 150, false, false)) {
                if (Config.GEODATA > 0) {
                    if (GeoData.getInstance().canSeeTarget(activeChar, target)) {
                        activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, target);
                        activeChar.onActionRequest();
                    }
                } else {
                    activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, target);
                    activeChar.onActionRequest();
                }
            } else if (isOwner) activeChar.sendPacket(new PetStatusShow((L2PetInstance) target));
        }
        return true;
    }

    public InstanceType getInstanceType() {
        return InstanceType.L2PetInstance;
    }
}
