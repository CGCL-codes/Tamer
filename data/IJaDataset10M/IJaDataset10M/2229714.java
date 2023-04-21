package com.narirelays.ems.tasks.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.narirelays.ems.resources.StorageService;
import com.narirelays.ems.services.MaintainMeasurePoints4WinccService;

public class MaintainIndex4Wincc {

    private static final Logger LOG = LoggerFactory.getLogger(MaintainIndex4Wincc.class);

    public void maintain() {
        LOG.info("Maintaining Wincc Index!");
        if (StorageService.ctx.containsBean("maintainMeasurePoints4WinccService")) {
            MaintainMeasurePoints4WinccService service = (MaintainMeasurePoints4WinccService) StorageService.ctx.getBean("maintainMeasurePoints4WinccService");
            if (service.maintainIndex4Wincc() == true) {
                LOG.info("Wincc Index update succeed!");
            } else {
                LOG.info("Wincc Index update failed!");
            }
        }
    }
}
