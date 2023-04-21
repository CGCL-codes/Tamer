package pl.org.minions.stigma.client.requests;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.request.Drop;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.Position;

/**
 * A request to drop item.
 */
public class DropRequest extends UnInterruptibleRequest {

    private int itemId;

    /**
     * Request to drop the item with given id.
     * @param itemId
     *            id of the item
     */
    public DropRequest(int itemId) {
        this.itemId = itemId;
    }

    /** {@inheritDoc} */
    @Override
    public Command getNextCommand(Actor playerActor, World world, Command previousCommandResponse) {
        final Item item = world.getItem(itemId);
        if (item == null) return null;
        if (item.isOnGround()) return null;
        if (playerActor.getInventory().contains(item)) return new Drop(itemId); else return null;
    }

    /** {@inheritDoc} */
    @Override
    public Position getTargetLocation() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Drop(#" + itemId + ")";
    }
}
