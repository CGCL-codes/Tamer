package nakayo.gameserver.controllers;

import nakayo.gameserver.ai.events.Event;
import nakayo.gameserver.model.gameobjects.Creature;
import nakayo.gameserver.model.gameobjects.Servant;
import nakayo.gameserver.model.gameobjects.player.Player;

/**
 * @author ATracer
 */
public class ServantController extends NpcController {

    @Override
    public void onDie(Creature lastAttacker) {
        super.onDelete();
        getOwner().getAi().handleEvent(Event.DIED);
    }

    @Override
    public void onDialogRequest(Player player) {
        return;
    }

    @Override
    public Servant getOwner() {
        return (Servant) super.getOwner();
    }
}
