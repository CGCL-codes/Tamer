package com.qtc.badminton.service.impl;

import com.qtc.badminton.dao.MasterDao;
import com.qtc.badminton.entity.Master;
import com.qtc.badminton.service.MasterService;

public class MasterServiceImpl implements MasterService {

    private MasterDao masterDao;

    public void setMasterDao(MasterDao masterDao) {
        this.masterDao = masterDao;
    }

    @Override
    public String getMasterValue(String masterKey) {
        Master m = masterDao.getMasterValue(masterKey);
        String v = null;
        if (m != null) {
            v = m.getMasterV();
        }
        return v;
    }

    public void setMasterValue(String masterKey, String masterValue) {
        masterDao.setMasterValue(masterKey, masterValue);
    }
}
