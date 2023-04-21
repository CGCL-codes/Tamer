package org.libreplan.web.resourceload;

import org.libreplan.business.orders.entities.Order;
import org.libreplan.business.planner.entities.TaskElement;

public interface IResourceLoadModel {

    ResourceLoadDisplayData calculateDataToDisplay(ResourceLoadParameters parameters);

    Order getOrderByTask(TaskElement task);

    boolean userCanRead(Order order, String loginName);

    boolean isExpandResourceLoadViewCharts();
}
