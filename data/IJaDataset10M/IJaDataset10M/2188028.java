package gameserver.model.gameobjects.stats;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.network.aion.serverpackets.SM_SUMMON_UPDATE;
import gameserver.services.LifeStatsRestoreService;
import gameserver.utils.PacketSendUtility;

public class SummonLifeStats extends CreatureLifeStats<Summon> {

    public SummonLifeStats(Summon owner) {
        super(owner, owner.getGameStats().getCurrentStat(StatEnum.MAXHP), owner.getGameStats().getCurrentStat(StatEnum.MAXMP));
    }

    @Override
    protected void onIncreaseHp(TYPE type, int value, int skillId, int logId) {
        Creature master = getOwner().getMaster();
        sendAttackStatusPacketUpdate(type, value, skillId, logId);
        if (master instanceof Player) {
            PacketSendUtility.sendPacket((Player) master, new SM_SUMMON_UPDATE(getOwner()));
        }
    }

    @Override
    protected void onIncreaseMp(TYPE type, int value, int skillId, int logId) {
    }

    @Override
    protected void onReduceHp() {
    }

    @Override
    protected void onReduceMp() {
    }

    @Override
    public Summon getOwner() {
        return (Summon) super.getOwner();
    }

    @Override
    protected void triggerRestoreTask() {
        if (lifeRestoreTask == null && !alreadyDead) {
            this.lifeRestoreTask = LifeStatsRestoreService.getInstance().scheduleHpRestoreTask(this);
        }
    }
}
