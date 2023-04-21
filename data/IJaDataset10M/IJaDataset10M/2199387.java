package com.hk.svr.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.bean.CmpFuncRef;
import com.hk.frame.dao.query.Query;
import com.hk.frame.dao.query.QueryManager;
import com.hk.svr.CmpFuncService;

public class CmpFuncServiceImpl implements CmpFuncService {

    @Autowired
    private QueryManager manager;

    public void createCmpFuncRef(CmpFuncRef cmpFuncRef) {
        Query query = this.manager.createQuery();
        if (query.count(CmpFuncRef.class, "companyid=? and funcoid=?", new Object[] { cmpFuncRef.getCompanyId(), cmpFuncRef.getFuncoid() }) > 0) {
            return;
        }
        query.insertObject(cmpFuncRef);
    }

    public void deleteCmpFuncRef(long companyId, long oid) {
        Query query = this.manager.createQuery();
        query.delete(CmpFuncRef.class, "companyid=? and oid=?", new Object[] { companyId, oid });
    }

    public CmpFuncRef getCmpFuncRef(long companyId, long oid) {
        Query query = this.manager.createQuery();
        return query.getObjectEx(CmpFuncRef.class, "companyid=? and oid=?", new Object[] { companyId, oid });
    }

    public List<CmpFuncRef> getCmpFuncRefListByCompanyId(long companyId) {
        Query query = this.manager.createQuery();
        return query.listEx(CmpFuncRef.class, "companyid=?", new Object[] { companyId }, "oid desc");
    }

    public void updateCmpFuncRef(CmpFuncRef cmpFuncRef) {
        Query query = this.manager.createQuery();
        query.updateObject(cmpFuncRef);
    }
}
