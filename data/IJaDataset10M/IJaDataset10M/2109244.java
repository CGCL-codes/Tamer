package games.midhedava.client.entity;

import java.util.List;
import marauroa.common.game.RPAction;

public class Item extends PassiveEntity {

    @Override
    public ActionType defaultAction() {
        return ActionType.USE;
    }

    @Override
    protected void buildOfferedActions(List<String> list) {
        list.add(ActionType.USE.getRepresentation());
        super.buildOfferedActions(list);
    }

    @Override
    public void onAction(final ActionType at, final String... params) {
        switch(at) {
            case USE:
                RPAction rpaction = new RPAction();
                rpaction.put("type", at.toString());
                int id = getID().getObjectID();
                if (params.length > 0) {
                    rpaction.put("baseobject", params[0]);
                    rpaction.put("baseslot", params[1]);
                    rpaction.put("baseitem", id);
                } else {
                    rpaction.put("target", id);
                }
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
        return new Item2DView(this);
    }
}
