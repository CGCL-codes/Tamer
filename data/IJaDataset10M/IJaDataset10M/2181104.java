package games.midhedava.server.entity.npc;

import games.midhedava.server.entity.player.Player;

public abstract class Behaviour {

    /**
	 * Transacts the deal that has been agreed on earlier via
	 * setChosenItem() and setAmount().
	 * @param seller The NPC who sells/buys
	 * @param player The player who buys/sells
	 * @return true iff the transaction was successful.
	 */
    protected abstract boolean transactAgreedDeal(SpeakerNPC seller, Player player);
}
