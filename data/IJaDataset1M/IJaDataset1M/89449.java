package org.openaion.gameserver.ai.events.handler;

import org.openaion.gameserver.ai.AI;
import org.openaion.gameserver.ai.events.Event;
import org.openaion.gameserver.ai.state.AIState;
import org.openaion.gameserver.model.ShoutEventType;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.services.NpcShoutsService;

/**
 * @author ATracer
 *
 */
public class MostHatedChangedEventHandler implements EventHandler {

    @Override
    public Event getEvent() {
        return Event.MOST_HATED_CHANGED;
    }

    @Override
    public void handleEvent(Event event, AI<?> ai) {
        ai.setAiState(AIState.THINKING);
        if (ai.getOwner() instanceof Npc) NpcShoutsService.getInstance().handleEvent((Npc) ai.getOwner(), ai.getOwner().getAggroList().getMostHated(), ShoutEventType.SWICHTARGET);
    }
}
