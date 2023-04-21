package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Summon;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.world.World;

/**
 * @author ATracer
 *
 */
public class CM_SUMMON_CASTSPELL extends AionClientPacket {

    @SuppressWarnings("unused")
    private int summonObjId;

    private int targetObjId;

    private int skillId;

    @SuppressWarnings("unused")
    private int skillLvl;

    @SuppressWarnings("unused")
    private float unk;

    public CM_SUMMON_CASTSPELL(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        summonObjId = readD();
        skillId = readH();
        skillLvl = readC();
        targetObjId = readD();
        unk = readF();
    }

    @Override
    protected void runImpl() {
        Player activePlayer = getConnection().getActivePlayer();
        Summon summon = activePlayer.getSummon();
        if (summon == null) return;
        AionObject targetObject = World.getInstance().findAionObject(targetObjId);
        if (targetObject instanceof Creature) {
            if (summon.getController().checkSkillPacket(skillId, (Creature) targetObject)) summon.getController().useSkill(skillId, (Creature) targetObject);
        }
    }
}
