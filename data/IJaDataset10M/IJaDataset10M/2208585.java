package com.jd.mysql.mgr.service.impl;

import java.util.List;
import com.jd.mysql.mgr.dao.MgrDbErrorDAO;
import com.jd.mysql.mgr.pojo.MgrDbError;
import com.jd.mysql.mgr.service.IMgrDbErrorService;

public class MgrDbErrorServiceImpl implements IMgrDbErrorService {

    private MgrDbErrorDAO mgrDbErrorDao;

    public void setMgrDbErrorDao(MgrDbErrorDAO mgrDbErrorDao) {
        this.mgrDbErrorDao = mgrDbErrorDao;
    }

    public void deleteMgrDbError(MgrDbError mdbe) {
        mgrDbErrorDao.deleteMgrDbError(mdbe);
    }

    public MgrDbError findMgrDbErrorById(Integer id) {
        return mgrDbErrorDao.findMgrDbErrorById(id);
    }

    public void saveMgrDbError(MgrDbError mdbe) {
        mgrDbErrorDao.saveMgrDbError(mdbe);
    }

    public void updateMgrDbError(MgrDbError mdbe) {
        mgrDbErrorDao.updateMgrDbError(mdbe);
    }

    public List<MgrDbError> getMgrDbErrorList(Integer dbtype, Integer start, Integer limit) {
        return mgrDbErrorDao.getMgrDbErrorList(dbtype, start, limit);
    }

    public Long getMgrDbErrorCount(Integer dbtype) {
        return mgrDbErrorDao.getMgrDbErrorCount(dbtype);
    }
}
