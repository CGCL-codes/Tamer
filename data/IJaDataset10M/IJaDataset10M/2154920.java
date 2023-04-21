package org.openaion.gameserver.ai.npcai;

import org.openaion.gameserver.ai.AI;
import org.openaion.gameserver.ai.events.Event;
import org.openaion.gameserver.ai.events.EventHandlers;
import org.openaion.gameserver.ai.events.handler.EventHandler;
import org.openaion.gameserver.ai.state.StateHandlers;
import org.openaion.gameserver.model.gameobjects.Npc;

/**
 * @author ATracer
 *
 */
public class NpcAi extends AI<Npc> {

    public NpcAi() {
        this.addEventHandler(EventHandlers.NOTHINGTODO_EH.getHandler());
        this.addEventHandler(EventHandlers.TIREDATTACKING_EH.getHandler());
        this.addEventHandler(EventHandlers.MOST_HATED_CHANGED_EH.getHandler());
        this.addEventHandler(EventHandlers.RESPAWNED_EH.getHandler());
        this.addEventHandler(EventHandlers.DIED_EH.getHandler());
        this.addEventHandler(EventHandlers.DESPAWN_EH.getHandler());
        this.addEventHandler(EventHandlers.TALK_EH.getHandler());
        this.addEventHandler(EventHandlers.BACKHOME_EH.getHandler());
        this.addEventHandler(EventHandlers.ATTACKED_EH.getHandler());
        this.addStateHandler(StateHandlers.MOVINGTOHOME_SH.getHandler());
        this.addStateHandler(StateHandlers.ACTIVE_NPC_SH.getHandler());
        this.addStateHandler(StateHandlers.TALKING_SH.getHandler());
        this.addStateHandler(StateHandlers.ATTACKING_SH.getHandler());
        this.addStateHandler(StateHandlers.THINKING_SH.getHandler());
        this.addStateHandler(StateHandlers.RESTING_SH.getHandler());
    }

    @Override
    public void handleEvent(Event event) {
        super.handleEvent(event);
        if (event != Event.DIED && owner.getLifeStats().isAlreadyDead()) return;
        EventHandler eventHandler = eventHandlers.get(event);
        if (eventHandler != null) eventHandler.handleEvent(event, this);
    }
}
