package org.jumpmind.pos.activity;

import javax.annotation.Resource;
import org.jumpmind.pos.IActivity;
import org.jumpmind.pos.common.store.WorkstationInfo;
import org.jumpmind.pos.domain.business.Workstation;
import org.jumpmind.pos.service.business.IBusinessService;

@Activity
public class OpenWorkstationActivity extends AbstractActivity {

    @Resource
    IBusinessService storeService;

    @Resource
    WorkstationInfo workstationInfo;

    @Override
    public ActivityStatus enter(IActivity lastActivity) {
        Workstation workstation = storeService.openWorkstation(workstationInfo);
        context.setWorkstation(workstation);
        return ActivityStatus.DONE;
    }
}
