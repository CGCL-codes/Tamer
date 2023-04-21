package org.openaion.gameserver.ai.state.handler;

import org.openaion.gameserver.ai.AI;
import org.openaion.gameserver.ai.state.AIState;
import org.openaion.gameserver.model.gameobjects.Npc;

/**
 * @author ATracer
 *
 */
public class ThinkingStateHandler extends StateHandler {

    @Override
    public AIState getState() {
        return AIState.THINKING;
    }

    /**
	 * State THINKING
	 * AI MonsterAi
	 * AI AggressiveAi
	 */
    @Override
    public void handleState(AIState state, AI<?> ai) {
        ai.clearDesires();
        Npc owner = (Npc) ai.getOwner();
        if (owner.getAggroList().getMostHated() != null) {
            ai.setAiState(AIState.ATTACKING);
            return;
        }
        if (!owner.isAtSpawnLocation()) {
            ai.setAiState(AIState.MOVINGTOHOME);
            return;
        }
        if (!owner.getLifeStats().isFullyRestoredHp()) {
            ai.setAiState(AIState.RESTING);
            return;
        }
        ai.setAiState(AIState.ACTIVE);
    }
}
