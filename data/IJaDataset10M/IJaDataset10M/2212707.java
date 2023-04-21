package org.libreplan.web.orders.labels;

import org.libreplan.business.orders.entities.OrderElement;
import org.libreplan.web.orders.IOrderModel;

/**
 * @author Diego Pino Garcia <dpino@igalia.com>
 */
public interface IAssignedLabelsToOrderElementModel extends IAssignedLabelsModel<OrderElement> {

    /**
     *
     * @param orderModel
     */
    void setOrderModel(IOrderModel orderModel);
}
