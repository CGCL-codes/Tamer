package com.hk.svr.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.bean.CmpFrLink;
import com.hk.frame.dao.query.Query;
import com.hk.frame.dao.query.QueryManager;
import com.hk.svr.CmpFrLinkService;

public class CmpFrLinkServiceImpl implements CmpFrLinkService {

    @Autowired
    private QueryManager manager;

    public void createCmpFrLink(CmpFrLink cmpFrLink) {
        Query query = this.manager.createQuery();
        query.insertObject(cmpFrLink);
    }

    public void deleteCmpFrLink(long linkId) {
        Query query = this.manager.createQuery();
        query.deleteById(CmpFrLink.class, linkId);
    }

    public List<CmpFrLink> getCmpFrLinkListByCompanyId(long companyId) {
        Query query = this.manager.createQuery();
        return query.listEx(CmpFrLink.class, "companyid=?", new Object[] { companyId }, "linkid desc");
    }

    public void updateCmpFrLink(CmpFrLink cmpFrLink) {
        Query query = this.manager.createQuery();
        query.updateObject(cmpFrLink);
    }

    public CmpFrLink getCmpFrLink(long linkId) {
        Query query = this.manager.createQuery();
        return query.getObjectById(CmpFrLink.class, linkId);
    }
}
