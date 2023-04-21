package com.hk.svr.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.bean.CmpSellNet;
import com.hk.bean.CmpSellNetKind;
import com.hk.svr.CmpSellNetService;

public class CmpSellNetProcessor {

    @Autowired
    private CmpSellNetService cmpSellNetService;

    public List<CmpSellNet> getCmpSellNetListByCompanyIdEx(long companyId, String name, long kindId, boolean buildKind, int begin, int size) {
        List<CmpSellNet> list = this.cmpSellNetService.getCmpSellNetListByCompanyIdEx(companyId, name, kindId, begin, size);
        if (buildKind) {
            this.buildCmpSellNetKind(list);
        }
        return list;
    }

    public List<CmpSellNet> getCmpSellNetListByCompanyId(long companyId, boolean buildKind, int begin, int size) {
        List<CmpSellNet> list = this.cmpSellNetService.getCmpSellNetListByCompanyId(companyId, begin, size);
        if (buildKind) {
            this.buildCmpSellNetKind(list);
        }
        return list;
    }

    public List<CmpSellNet> getCmpSellNetListByCompanyId(long companyId, long kindId, boolean buildKind, int begin, int size) {
        List<CmpSellNet> list = this.cmpSellNetService.getCmpSellNetListByCompanyId(companyId, kindId, begin, size);
        if (buildKind) {
            this.buildCmpSellNetKind(list);
        }
        return list;
    }

    private void buildCmpSellNetKind(List<CmpSellNet> list) {
        List<Long> idList = new ArrayList<Long>();
        for (CmpSellNet o : list) {
            if (o.getKindId() > 0) {
                idList.add(o.getKindId());
            }
        }
        if (idList.size() == 0) {
            return;
        }
        Map<Long, CmpSellNetKind> map = this.cmpSellNetService.getCmpSellNetKindMapInId(idList);
        for (CmpSellNet o : list) {
            o.setCmpSellNetKind(map.get(o.getKindId()));
        }
    }
}
