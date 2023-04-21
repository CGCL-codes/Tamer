package pl.net.bluesoft.archetype.gwt.rpc.service.command.order.action;

import com.google.gwt.user.client.rpc.IsSerializable;
import pl.net.bluesoft.archetype.gwt.common.wrappers.OrderWrapper;
import pl.net.bluesoft.archetype.gwt.rpc.service.command.Action;

/**
 * Command representing a request for updating an existing Order
 * @author pstepaniak
 *
 */
public class UpdateOrderAction implements Action<UpdateOrderActionResponse>, IsSerializable {

    private OrderWrapper order;

    public UpdateOrderAction() {
    }

    public UpdateOrderAction(final OrderWrapper order) {
        this.order = order;
    }

    public OrderWrapper getOrder() {
        return order;
    }

    public void setOrder(final OrderWrapper order) {
        this.order = order;
    }
}
