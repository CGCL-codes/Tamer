package org.openaion.gameserver.network.aion.clientpackets;

import org.apache.log4j.Logger;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Summon;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.state.CreatureState;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 *
 */
public class CM_SUMMON_EMOTION extends AionClientPacket {

    private static final Logger log = Logger.getLogger(CM_SUMMON_EMOTION.class);

    @SuppressWarnings("unused")
    private int objId;

    private int emotionTypeId;

    public CM_SUMMON_EMOTION(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        objId = readD();
        emotionTypeId = readC();
    }

    @Override
    protected void runImpl() {
        EmotionType emotionType = EmotionType.getEmotionTypeById(emotionTypeId);
        if (emotionType == EmotionType.UNK) log.error("Unknown emotion type? 0x" + Integer.toHexString(emotionTypeId).toUpperCase());
        Player activePlayer = getConnection().getActivePlayer();
        if (activePlayer == null) return;
        Summon summon = activePlayer.getSummon();
        if (summon == null) return;
        switch(emotionType) {
            case FLY:
            case LAND:
                PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, emotionType));
                break;
            case ATTACKMODE:
                summon.setState(CreatureState.WEAPON_EQUIPPED);
                PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, emotionType));
                break;
            case NEUTRALMODE:
                summon.unsetState(CreatureState.WEAPON_EQUIPPED);
                PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, emotionType));
                break;
        }
    }
}
