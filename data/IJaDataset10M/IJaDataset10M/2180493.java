package org.broadleafcommerce.core.order.service;

import javax.annotation.Resource;
import org.broadleafcommerce.core.order.dao.FulfillmentGroupDao;
import org.broadleafcommerce.core.order.domain.FulfillmentGroup;
import org.springframework.stereotype.Service;

@Service("blFulfillmentGroupService")
public class FulfillmentGroupServiceImpl implements FulfillmentGroupService {

    @Resource(name = "blFulfillmentGroupDao")
    protected FulfillmentGroupDao fulfillmentGroupDao;

    public FulfillmentGroup save(FulfillmentGroup fulfillmentGroup) {
        return fulfillmentGroupDao.save(fulfillmentGroup);
    }

    public FulfillmentGroup createEmptyFulfillmentGroup() {
        return fulfillmentGroupDao.create();
    }

    public FulfillmentGroup findFulfillmentGroupById(Long fulfillmentGroupId) {
        return fulfillmentGroupDao.readFulfillmentGroupById(fulfillmentGroupId);
    }

    public void delete(FulfillmentGroup fulfillmentGroup) {
        fulfillmentGroupDao.delete(fulfillmentGroup);
    }
}
