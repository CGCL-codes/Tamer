package com.aionemu.gameserver.ai.state.handler;

import com.aionemu.gameserver.ai.AI;
import com.aionemu.gameserver.ai.desires.impl.MoveToHomeDesire;
import com.aionemu.gameserver.ai.state.AIState;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 *
 */
public class MovingToHomeStateHandler extends StateHandler {

    @Override
    public AIState getState() {
        return AIState.MOVINGTOHOME;
    }

    /**
	 * State MOVINGTOHOME
	 * AI MonsterAi
	 * AI GuardAi
	 */
    @Override
    public void handleState(AIState state, AI<?> ai) {
        ai.clearDesires();
        Npc npc = (Npc) ai.getOwner();
        npc.setTarget(null);
        PacketSendUtility.broadcastPacket(npc, new SM_LOOKATOBJECT(npc));
        npc.getAggroList().clear();
        PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, 0));
        PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.NEUTRALMODE, 0, 0));
        ai.addDesire(new MoveToHomeDesire(npc, AIState.MOVINGTOHOME.getPriority()));
        ai.schedule();
    }
}
