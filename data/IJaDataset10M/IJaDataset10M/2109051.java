package org.slasoi.businessManager.violationPenalty.impl;

import org.apache.log4j.Logger;
import org.slasoi.businessManager.common.service.SlaViolationManager;
import org.slasoi.businessManager.common.ws.types.GTStatus;
import org.slasoi.businessManager.common.ws.types.Status;
import org.slasoi.businessManager.violationPenalty.GTStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "gtStatusService")
public class GTStatusServiceImpl implements GTStatusService {

    private static Logger logger = Logger.getLogger(GTStatusServiceImpl.class.getName());

    @Autowired
    private SlaViolationManager slaViolationService;

    public GTStatus getGTStatus(String slaId, String guaranteeTermId) {
        if (slaViolationService.getViolationsBySlaIdAndGTidAndEndTimeNull(slaId.toString(), guaranteeTermId).size() > 0) {
            return new GTStatus(Status.VIOLATED);
        } else {
            return new GTStatus(Status.OK);
        }
    }

    public GTStatus getGTStatus(String slaId, String guaranteeTermId, String KPI) {
        if (slaViolationService.getViolationsBySlaIdAndGTidAndEndTimeNull(slaId.toString(), guaranteeTermId).size() > 0) {
            return new GTStatus(Status.VIOLATED);
        } else {
            return new GTStatus(Status.OK);
        }
    }
}
