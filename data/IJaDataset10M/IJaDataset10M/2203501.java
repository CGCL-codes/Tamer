package org.slasoi.businessManager.common.service.impl;

import java.util.List;
import org.apache.log4j.Logger;
import org.slasoi.businessManager.common.dao.SlaWarningDAO;
import org.slasoi.businessManager.common.model.pac.EmSlaWarning;
import org.slasoi.businessManager.common.service.SlaWarningManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "slaWarningService")
public class SlaWarningManagerImpl implements SlaWarningManager {

    private Logger logger = Logger.getLogger(SlaWarningManagerImpl.class);

    @Autowired
    private SlaWarningDAO slaWarningDAO;

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrUpdate(EmSlaWarning EmSlaWarning) {
        logger.debug("saveOrUpdate()");
        slaWarningDAO.saveOrUpdate(EmSlaWarning);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void delete(EmSlaWarning EmSlaWarning) {
        logger.debug("delete(EmSlaWarning EmSlaWarning)");
        slaWarningDAO.delete(EmSlaWarning);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void delete(List<EmSlaWarning> slaWarningList) {
        logger.debug("delete(List<EmSlaWarning> slaWarningList)");
        for (EmSlaWarning EmSlaWarning : slaWarningList) {
            delete(EmSlaWarning);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<EmSlaWarning> getWarningsByNotificationId(String notificationId) {
        logger.debug("getWarningsByNotificationId " + notificationId);
        return slaWarningDAO.getWarningsByNotificationId(notificationId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<EmSlaWarning> getWarningsBySlaIdAndNotificationId(String slaId, String notificationId) {
        logger.debug("getWarningsBySlaIdAndNotificationId " + notificationId);
        return slaWarningDAO.getWarningsBySlaIdAndNotificationId(slaId, notificationId);
    }
}
