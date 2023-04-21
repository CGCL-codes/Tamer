package games.midhedava.server.entity.npc.fsm;

import games.midhedava.server.entity.npc.SpeakerNPC;
import games.midhedava.server.entity.player.Player;

/**
 * a condition to check before an transition is executed
 *
 * @author hendrik
 */
public interface PreTransitionCondition {

    /**
	 * can the transition be done?
	 *
	 * @param player player who caused the transition
	 * @param text   text he/she said
	 * @param engine the NPC doing the transition
	 * @return true, if the transition is possible, false otherwise
	 */
    public boolean fire(Player player, String text, SpeakerNPC engine);
}
