package net.naijatek.myalumni.modules.common.service.impl;

import java.util.List;
import net.naijatek.myalumni.framework.exceptions.MyAlumniException;
import net.naijatek.myalumni.modules.common.domain.ReminisceVO;
import net.naijatek.myalumni.modules.common.persistence.iface.ReminisceDao;
import net.naijatek.myalumni.modules.common.service.IReminisceService;
import net.naijatek.myalumni.util.BaseConstants;

public class ReminisceServiceImpl implements IReminisceService {

    private ReminisceDao reminisceDao;

    public ReminisceServiceImpl(ReminisceDao reminisceDao) {
        this.reminisceDao = reminisceDao;
    }

    public List<ReminisceVO> findAll() {
        return reminisceDao.findAll();
    }

    public List<ReminisceVO> findAllByStatus(String status) {
        return reminisceDao.findAllByStatus(status);
    }

    public ReminisceVO findById(String id) {
        return reminisceDao.findById(id);
    }

    public void hardDelete(String id) throws MyAlumniException {
        reminisceDao.hardDeleteObject(id);
    }

    public void merge(ReminisceVO entity) {
        reminisceDao.mergeObject(entity);
    }

    public void save(ReminisceVO entity) {
        entity.setStatus(BaseConstants.APPROVAL_NEEDED);
        reminisceDao.save(entity);
    }

    public void softDelete(String id, String lastModifiedBy) throws MyAlumniException {
        reminisceDao.softDeleteObject(id, lastModifiedBy);
    }
}
