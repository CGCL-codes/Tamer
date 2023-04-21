package org.openaion.gameserver.ai.events.handler;

import org.openaion.gameserver.ai.AI;
import org.openaion.gameserver.ai.events.Event;
import org.openaion.gameserver.ai.state.AIState;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.utils.ThreadPoolManager;

/**
 * @author ATracer
 *
 */
public class TalkEventHandler implements EventHandler {

    @Override
    public Event getEvent() {
        return Event.TALK;
    }

    @Override
    public void handleEvent(Event event, AI<?> ai) {
        final Npc owner = (Npc) ai.getOwner();
        if (owner.hasWalkRoutes()) {
            owner.getMoveController().setCanWalk(false);
            owner.getController().stopMoving();
            ThreadPoolManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    owner.getMoveController().setCanWalk(true);
                }
            }, 60000);
        }
        ai.setAiState(AIState.TALKING);
    }
}
