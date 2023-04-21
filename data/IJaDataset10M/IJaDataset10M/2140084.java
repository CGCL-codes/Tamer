package com.aionemu.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.List;
import com.aionemu.gameserver.controllers.attack.AttackResult;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * 
 * @author -Nemesiss-, Sweetkr
 * 
 */
public class SM_ATTACK extends AionServerPacket {

    private int attackno;

    private int time;

    private int type;

    private List<AttackResult> attackList;

    private Creature attacker;

    private Creature target;

    public SM_ATTACK(Creature attacker, Creature target, int attackno, int time, int type, List<AttackResult> attackList) {
        this.attacker = attacker;
        this.target = target;
        this.attackno = attackno;
        this.time = time;
        this.type = type;
        this.attackList = attackList;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, attacker.getObjectId());
        writeC(buf, attackno);
        writeH(buf, time);
        writeC(buf, type);
        writeD(buf, target.getObjectId());
        int attackerMaxHp = attacker.getLifeStats().getMaxHp();
        int attackerCurrHp = attacker.getLifeStats().getCurrentHp();
        int targetMaxHp = target.getLifeStats().getMaxHp();
        int targetCurrHp = target.getLifeStats().getCurrentHp();
        writeC(buf, 100 * targetCurrHp / targetMaxHp);
        writeC(buf, 100 * attackerCurrHp / attackerMaxHp);
        switch(attackList.get(0).getAttackStatus().getId()) {
            case -60:
            case 4:
                writeH(buf, 32);
                break;
            case -62:
            case 2:
                writeH(buf, 64);
                break;
            case -64:
            case 0:
                writeH(buf, 128);
                break;
            case -58:
            case 6:
                writeH(buf, 256);
                break;
            default:
                writeH(buf, 0);
                break;
        }
        writeC(buf, attackList.size());
        for (AttackResult attack : attackList) {
            writeD(buf, attack.getDamage());
            writeC(buf, attack.getAttackStatus().getId());
            writeC(buf, attack.getShieldType());
            switch(attack.getShieldType()) {
                case 1:
                    writeD(buf, 0x00);
                    writeD(buf, 0x00);
                    writeD(buf, 0x00);
                    writeD(buf, 0);
                    writeD(buf, 0);
                    break;
                case 2:
                default:
                    break;
            }
        }
        writeC(buf, 0);
    }
}
