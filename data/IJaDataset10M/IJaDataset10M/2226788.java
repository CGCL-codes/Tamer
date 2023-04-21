package org.jumpmind.pos.activity;

import javax.annotation.Resource;
import org.jumpmind.pos.IActivity;
import org.jumpmind.pos.common.store.WorkstationInfo;
import org.jumpmind.pos.domain.business.BusinessUnit;
import org.jumpmind.pos.service.business.IBusinessService;

@Activity
public class CloseStoreActivity extends AbstractActivity {

    @Resource
    IBusinessService storeService;

    @Resource
    WorkstationInfo workstationInfo;

    @Override
    public ActivityStatus enter(IActivity lastActivity) {
        BusinessUnit store = storeService.closeStore(workstationInfo);
        context.setRetailStore(store);
        return ActivityStatus.DONE;
    }
}
