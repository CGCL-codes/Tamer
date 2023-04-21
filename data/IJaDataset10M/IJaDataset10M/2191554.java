package com.hk.svr.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.bean.Company;
import com.hk.bean.HkObjOrder;
import com.hk.bean.HkObjOrderDef;
import com.hk.frame.dao.query.Query;
import com.hk.frame.dao.query.QueryManager;
import com.hk.frame.util.DataUtil;
import com.hk.svr.CompanyService;
import com.hk.svr.HkObjOrderService;
import com.hk.svr.OrderDefService;
import com.hk.svr.company.exception.NoEnoughMoneyException;
import com.hk.svr.company.exception.SmallerThanMinMoneyException;
import com.hk.svr.pub.HkSvrUtil;

public class HkObjOrderServiceImpl implements HkObjOrderService {

    @Autowired
    private QueryManager manager;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private OrderDefService orderDefService;

    public void run(long oid) {
        this.updateStopflg(oid, HkObjOrder.STOPFLG_N);
    }

    public void stop(long oid) {
        this.updateStopflg(oid, HkObjOrder.STOPFLG_Y);
    }

    public boolean updateHkObjOrder(HkObjOrder hkObjOrder) throws NoEnoughMoneyException, SmallerThanMinMoneyException {
        Company company = this.companyService.getCompany(hkObjOrder.getHkObjId());
        HkObjOrder o = this.getHkObjOrder(hkObjOrder.getOid());
        if (DataUtil.isSameDay(new Date(), o.getUtime())) {
            return false;
        }
        if (o.getKind() != hkObjOrder.getKind() || o.getCityId() != hkObjOrder.getCityId()) {
            if (isDuplicate(hkObjOrder)) {
                return true;
            }
        }
        int amount = hkObjOrder.getMoney() - o.getMoney();
        if (amount > 0) {
            if (company.getMoney() < amount) {
                throw new NoEnoughMoneyException("no enough money [ " + company.getMoney() + " , " + hkObjOrder.getMoney() + " ]");
            }
            this.companyService.addMoney(hkObjOrder.getHkObjId(), -amount);
            company = this.companyService.getCompany(hkObjOrder.getHkObjId());
        }
        if (hkObjOrder.getPday() != o.getPday() || hkObjOrder.getMoney() != o.getMoney()) {
            if (hkObjOrder.getMoney() != o.getMoney()) {
                int minmoney = 0;
                HkObjOrderDef hkObjOrderDef = this.orderDefService.getHkObjOrderDef(hkObjOrder.getKind(), company.getKindId(), hkObjOrder.getCityId());
                if (hkObjOrderDef == null) {
                    minmoney = HkSvrUtil.getMinOrderMoney();
                } else {
                    minmoney = hkObjOrderDef.getMoney();
                }
                if (hkObjOrder.getMoney() < minmoney) {
                    SmallerThanMinMoneyException e = new SmallerThanMinMoneyException("must bigger than minmoney [ " + hkObjOrder.getMoney() + " , " + minmoney + " ]");
                    e.setMinmoney(minmoney);
                    throw e;
                }
            }
            int res = hkObjOrder.getPday() * hkObjOrder.getMoney();
            if (res > company.getMoney()) {
                throw new NoEnoughMoneyException("no enough money for remain [ " + company.getMoney() + " , " + res + " ]");
            }
        }
        hkObjOrder.setUtime(new Date());
        Query query = manager.createQuery();
        query.addField("hkobjid", hkObjOrder.getHkObjId());
        query.addField("kind", hkObjOrder.getKind());
        query.addField("stopflg", hkObjOrder.getStopflg());
        query.addField("money", hkObjOrder.getMoney());
        query.addField("cityid", hkObjOrder.getCityId());
        query.addField("pday", hkObjOrder.getPday());
        query.addField("utime", hkObjOrder.getUtime());
        query.addField("userid", hkObjOrder.getUserId());
        query.update(HkObjOrder.class, "oid=?", new Object[] { hkObjOrder.getOid() });
        return true;
    }

    private boolean isDuplicate(HkObjOrder hkObjOrder) {
        Query query = manager.createQuery();
        if (query.count(HkObjOrder.class, "hkobjid=? and kind=? and cityid=?", new Object[] { hkObjOrder.getHkObjId(), hkObjOrder.getKind(), hkObjOrder.getCityId() }) > 0) {
            return true;
        }
        return false;
    }

    public boolean createHkObjOrder(HkObjOrder hkObjOrder) throws NoEnoughMoneyException, SmallerThanMinMoneyException {
        Company company = this.companyService.getCompany(hkObjOrder.getHkObjId());
        if (isDuplicate(hkObjOrder)) {
            this.updateHkObjOrder(hkObjOrder);
            return true;
        }
        HkObjOrderDef hkObjOrderDef = this.orderDefService.getHkObjOrderDef(hkObjOrder.getKind(), company.getKindId(), hkObjOrder.getCityId());
        int minmoney = 0;
        if (hkObjOrderDef == null) {
            minmoney = HkSvrUtil.getMinOrderMoney();
        } else {
            minmoney = hkObjOrderDef.getMoney();
        }
        if (hkObjOrder.getMoney() < minmoney) {
            SmallerThanMinMoneyException e = new SmallerThanMinMoneyException("must bigger than minmoney [ " + hkObjOrder.getMoney() + " , " + minmoney + " ]");
            e.setMinmoney(minmoney);
            throw e;
        }
        int res = hkObjOrder.getResultMoney();
        if (company.getMoney() < res) {
            throw new NoEnoughMoneyException("no enough money [ " + company.getMoney() + " , " + res + " ]");
        }
        this.companyService.addMoney(hkObjOrder.getHkObjId(), -hkObjOrder.getMoney());
        hkObjOrder.setUtime(new Date());
        Query query = manager.createQuery();
        query.addField("hkobjid", hkObjOrder.getHkObjId());
        query.addField("kind", hkObjOrder.getKind());
        query.addField("stopflg", hkObjOrder.getStopflg());
        query.addField("money", hkObjOrder.getMoney());
        query.addField("cityid", hkObjOrder.getCityId());
        query.addField("pday", hkObjOrder.getPday());
        query.addField("utime", hkObjOrder.getUtime());
        query.addField("userid", hkObjOrder.getUserId());
        query.insert(HkObjOrder.class);
        return true;
    }

    public int countHkObjOrder(byte stopflg) {
        Query query = manager.createQuery();
        return query.count(HkObjOrder.class, "stopflg=?", new Object[] { stopflg });
    }

    public List<HkObjOrder> getHkObjOrderList(byte stopflg, int begin, int size) {
        Query query = manager.createQuery();
        return query.listEx(HkObjOrder.class, "stopflg=?", new Object[] { stopflg }, begin, size);
    }

    private void updateStopflg(long oid, byte stopflg) {
        Query query = manager.createQuery();
        query.addField("stopflg", stopflg);
        query.update(HkObjOrder.class, "oid=?", new Object[] { oid });
    }

    public List<HkObjOrder> getHkObjOrderListForOrder(byte kind, int cityId, int begin, int size) {
        Query query = manager.createQuery();
        return query.listEx(HkObjOrder.class, "kind=? and cityid=? and stopflg=?", new Object[] { kind, cityId, HkObjOrder.STOPFLG_N }, "money desc,utime desc", begin, size);
    }

    public List<HkObjOrder> getHkObjOrderListByObjId(long objId, int begin, int size) {
        Query query = manager.createQuery();
        return query.listEx(HkObjOrder.class, "hkobjid=?", new Object[] { objId }, "oid desc", begin, size);
    }

    public List<HkObjOrder> getHkObjOrderListByUserId(long userId, int cityId, int begin, int size) {
        Query query = manager.createQuery();
        if (cityId < 0) {
            return query.listEx(HkObjOrder.class, "userid=?", new Object[] { userId }, "oid desc", begin, size);
        }
        return query.listEx(HkObjOrder.class, "userid=? and cityid=?", new Object[] { userId, cityId }, "oid desc", begin, size);
    }

    public List<HkObjOrder> getHkObjOrderListByObjId(long objId, int cityId, int begin, int size) {
        Query query = manager.createQuery();
        if (cityId < 0) {
            return query.listEx(HkObjOrder.class, "hkobjid=?", new Object[] { objId }, "oid desc", begin, size);
        }
        return query.listEx(HkObjOrder.class, "hkobjid=? and cityid=?", new Object[] { objId, cityId }, "oid desc", begin, size);
    }

    public void calculate(HkObjOrder hkObjOrder) {
        if (hkObjOrder.isStop()) {
            return;
        }
        if (hkObjOrder.getPday() <= 0) {
            return;
        }
        Company company = this.companyService.getCompany(hkObjOrder.getHkObjId());
        if (hkObjOrder.getMoney() > company.getMoney()) {
            return;
        }
        this.companyService.addMoney(hkObjOrder.getHkObjId(), -hkObjOrder.getMoney());
        Query query = manager.createQuery();
        query.addField("pday", hkObjOrder.getPday() - 1);
        query.update(HkObjOrder.class, "oid=?", new Object[] { hkObjOrder.getOid() });
    }

    public HkObjOrder getHkObjOrder(long oid) {
        Query query = manager.createQuery();
        return query.getObjectById(HkObjOrder.class, oid);
    }

    public HkObjOrder getHkObjOrder(long objId, byte kind, int cityId) {
        Query query = manager.createQuery();
        return query.getObjectEx(HkObjOrder.class, "hkobjid=? and kind=? and cityid=?", new Object[] { objId, kind, cityId });
    }
}
