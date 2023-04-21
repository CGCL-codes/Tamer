package games.midhedava.client.entity;

import java.util.List;
import marauroa.common.game.RPAction;

/**
 * @author daniel
 *
 */
public class GoldSource extends Entity {

    @Override
    public ActionType defaultAction() {
        return ActionType.PROSPECT;
    }

    @Override
    protected void buildOfferedActions(List<String> list) {
        super.buildOfferedActions(list);
        list.add(ActionType.PROSPECT.getRepresentation());
    }

    @Override
    public void onAction(final ActionType at, final String... params) {
        switch(at) {
            case PROSPECT:
                RPAction rpaction = new RPAction();
                rpaction.put("type", at.toString());
                int id = getID().getObjectID();
                rpaction.put("target", id);
                at.send(rpaction);
                break;
            default:
                super.onAction(at, params);
                break;
        }
    }

    /**
	 * Transition method. Create the screen view for this entity.
	 *
	 * @return	The on-screen view of this entity.
	 */
    @Override
    protected Entity2DView createView() {
        return new GoldSource2DView(this);
    }

    /**
	 * Determine if this is an obstacle for another entity.
	 *
	 * @param	entity		The entity to check against.
	 *
	 * @return	<code>true</code> the entity can not enter this
	 *		entity's area.
	 */
    @Override
    public boolean isObstacle(final Entity entity) {
        return false;
    }
}
