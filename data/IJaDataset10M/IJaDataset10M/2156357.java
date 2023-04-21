package com.narirelays.ems.tasks.global;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.narirelays.ems.resources.StorageService;
import com.narirelays.ems.services.MaintainMeasurePoints4WinccService;
import com.narirelays.ems.tasks.MyJob;

public class MaintainMeasurePoints4Wincc extends MyJob {

    private static final Logger LOG = LoggerFactory.getLogger(MaintainMeasurePoints4Wincc.class);

    @Override
    public void myexecute(JobExecutionContext context) throws JobExecutionException {
        if (StorageService.ctx.containsBean("maintainMeasurePoints4WinccService")) {
            MaintainMeasurePoints4WinccService service = (MaintainMeasurePoints4WinccService) StorageService.ctx.getBean("maintainMeasurePoints4WinccService");
            if (service.maintainMeasurePointsAndSaveLog() == true) {
                LOG.info("Wincc MeasurePoints update succeed!");
            } else {
                LOG.info("Wincc MeasurePoints update failed!");
            }
        }
    }
}
