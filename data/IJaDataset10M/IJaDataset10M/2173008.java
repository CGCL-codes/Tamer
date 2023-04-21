package net.sf.l2j.gameserver.model.actor.status;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SummonInstance;
import net.sf.l2j.gameserver.model.entity.Duel;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.util.Util;

public class PcStatus extends PlayableStatus {

    public PcStatus(L2PcInstance activeChar) {
        super(activeChar);
    }

    @Override
    public final void reduceHp(double value, L2Character attacker) {
        reduceHp(value, attacker, true);
    }

    @Override
    public final void reduceHp(double value, L2Character attacker, boolean awake) {
        if (getActiveChar().isInvul()) return;
        if (attacker instanceof L2PcInstance) {
            if (getActiveChar().isInDuel()) {
                if (getActiveChar().getDuelState() == Duel.DUELSTATE_DEAD) return; else if (getActiveChar().getDuelState() == Duel.DUELSTATE_WINNER) return;
                if (((L2PcInstance) attacker).getDuelId() != getActiveChar().getDuelId()) getActiveChar().setDuelState(Duel.DUELSTATE_INTERRUPTED);
            }
            if (getActiveChar().isDead() && !getActiveChar().isFakeDeath()) return;
        } else {
            if (getActiveChar().isInDuel() && !(attacker instanceof L2SummonInstance)) getActiveChar().setDuelState(Duel.DUELSTATE_INTERRUPTED);
            if (getActiveChar().isDead()) return;
        }
        int fullValue = (int) value;
        if (attacker != null && attacker != getActiveChar()) {
            L2Summon summon = getActiveChar().getPet();
            if (summon != null && summon instanceof L2SummonInstance && Util.checkIfInRange(900, getActiveChar(), summon, true)) {
                int tDmg = (int) value * (int) getActiveChar().getStat().calcStat(Stats.TRANSFER_DAMAGE_PERCENT, 0, null, null) / 100;
                if (summon.getCurrentHp() < tDmg) tDmg = (int) summon.getCurrentHp() - 1;
                if (tDmg > 0) {
                    summon.reduceCurrentHp(tDmg, attacker);
                    value -= tDmg;
                    fullValue = (int) value;
                }
            }
            if (attacker instanceof L2PlayableInstance) if (getCurrentCp() >= value) {
                setCurrentCp(getCurrentCp() - value);
                value = 0;
            } else {
                value -= getCurrentCp();
                setCurrentCp(0);
            }
        }
        super.reduceHp(value, attacker, awake);
        if (!getActiveChar().isDead() && getActiveChar().isSitting()) getActiveChar().standUp();
        if (getActiveChar().isFakeDeath()) getActiveChar().stopFakeDeath(null);
        if (attacker != null && attacker != getActiveChar() && fullValue > 0) {
            SystemMessage smsg = new SystemMessage(SystemMessageId.S1_GAVE_YOU_S2_DMG);
            if (Config.DEBUG) _log.fine("Attacker:" + attacker.getName());
            if (attacker instanceof L2NpcInstance) {
                int mobId = ((L2NpcInstance) attacker).getTemplate().idTemplate;
                if (Config.DEBUG) _log.fine("mob id:" + mobId);
                smsg.addNpcName(mobId);
            } else if (attacker instanceof L2Summon) {
                int mobId = ((L2Summon) attacker).getTemplate().idTemplate;
                smsg.addNpcName(mobId);
            } else smsg.addString(attacker.getName());
            smsg.addNumber(fullValue);
            getActiveChar().sendPacket(smsg);
        }
    }

    @Override
    public L2PcInstance getActiveChar() {
        return (L2PcInstance) super.getActiveChar();
    }
}
