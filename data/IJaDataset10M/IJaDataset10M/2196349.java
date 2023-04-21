package org.broadleafcommerce.core.order.dao;

import java.util.List;
import org.broadleafcommerce.core.order.domain.FulfillmentGroup;
import org.broadleafcommerce.core.order.domain.FulfillmentGroupItem;

public interface FulfillmentGroupItemDao {

    FulfillmentGroupItem readFulfillmentGroupItemById(Long fulfillmentGroupItemId);

    FulfillmentGroupItem save(FulfillmentGroupItem fulfillmentGroupItem);

    List<FulfillmentGroupItem> readFulfillmentGroupItemsForFulfillmentGroup(FulfillmentGroup fulfillmentGroup);

    void delete(FulfillmentGroupItem fulfillmentGroupItem);

    FulfillmentGroupItem create();
}
