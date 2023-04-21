package com.hk.svr.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.bean.BizCircle;
import com.hk.bean.City;
import com.hk.bean.Company;
import com.hk.bean.CompanyBizCircle;
import com.hk.bean.Province;
import com.hk.frame.dao.query.Query;
import com.hk.frame.dao.query.QueryManager;
import com.hk.frame.util.DataUtil;
import com.hk.frame.util.HkUtil;
import com.hk.svr.BizCircleService;
import com.hk.svr.ZoneService;

public class BizCircleServiceImpl implements BizCircleService {

    @Autowired
    private QueryManager manager;

    public boolean createBizCircle(BizCircle bizCircle) {
        String name = bizCircle.getName();
        int cityId = bizCircle.getCityId();
        Query query = this.manager.createQuery();
        query.setTable(BizCircle.class);
        query.where("name=? and cityid=?").setParam(name).setParam(cityId);
        if (query.count() == 0) {
            query.addField("name", name);
            query.addField("cityid", bizCircle.getCityId());
            query.addField("provinceid", bizCircle.getProvinceId());
            query.addField("cmpcount", bizCircle.getCmpCount());
            query.insert(BizCircle.class);
            return true;
        }
        return false;
    }

    public void deleteBizCircle(int circleId) {
        Query query = this.manager.createQuery();
        query.deleteById(BizCircle.class, circleId);
    }

    public void updateBizCircle(BizCircle bizCircle) {
        Query query = this.manager.createQuery();
        query.setTable(BizCircle.class);
        query.addField("name", bizCircle.getName());
        query.addField("cityid", bizCircle.getCityId());
        query.addField("provinceid", bizCircle.getProvinceId());
        query.addField("cmpcount", bizCircle.getCmpCount());
        query.where("circleid=?").setParam(bizCircle.getCircleId());
        query.update();
    }

    public List<BizCircle> getBizCircleList(String name, int cityId, int provinceId) {
        Query query = this.manager.createQuery();
        List<Object> olist = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder("select * from bizcircle where 1=1");
        if (!DataUtil.isEmpty(name)) {
            sql.append(" and name like ?");
            olist.add("%" + name + "%");
        }
        if (cityId > 0) {
            sql.append(" and cityid=?");
            olist.add(cityId);
        } else if (provinceId > 0) {
            sql.append(" and provinceid=?");
            olist.add(provinceId);
        }
        sql.append(" order by circleid desc");
        return query.listBySqlParamList("ds1", sql.toString(), BizCircle.class, olist);
    }

    public List<BizCircle> getBizCircleListForHasCmp(int cityId) {
        Query query = this.manager.createQuery();
        List<Object> olist = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder("select * from bizcircle where cmpcount>0");
        if (cityId > 0) {
            sql.append(" and cityid=?");
            olist.add(cityId);
        }
        sql.append(" order by circleid desc");
        return query.listBySqlParamList("ds1", sql.toString(), BizCircle.class, olist);
    }

    public BizCircle getBizCircle(int circleId) {
        Query query = this.manager.createQuery();
        return query.getObjectById(BizCircle.class, circleId);
    }

    public void createCompanyBizCircle(long companyId, int circleId) {
        BizCircle o = this.getBizCircle(circleId);
        if (o == null) {
            return;
        }
        Query query = manager.createQuery();
        query.setTable(CompanyBizCircle.class);
        query.where("companyid=? and circleid=?").setParam(companyId).setParam(circleId);
        if (query.count() == 0) {
            query.addField("companyid", companyId);
            query.addField("circleid", circleId);
            query.insert(CompanyBizCircle.class);
        }
        this.updateBizCircleCmpCount(circleId);
    }

    public void deleteCompanyBizCircle(long companyId, int circleId) {
        Query query = manager.createQuery();
        query.setTable(CompanyBizCircle.class);
        query.where("companyid=? and circleid=?").setParam(companyId).setParam(circleId);
        query.delete();
        this.updateBizCircleCmpCount(circleId);
    }

    public void updateBizCircleCmpCount(int circleId) {
        Query query = manager.createQuery();
        int count = query.count(CompanyBizCircle.class, "circleid=?", new Object[] { circleId });
        query.addField("cmpcount", count);
        query.updateById(BizCircle.class, circleId);
    }

    public List<Company> getCompanyList(int circleId, int kindId, int parentId, int begin, int size) {
        List<Object> olist = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder("select c.* from companybizcircle bc,company c where bc.circleid=? and bc.companyid=c.companyid");
        olist.add(circleId);
        if (kindId > 0) {
            sql.append(" and c.kindid=?");
            olist.add(kindId);
        } else if (parentId > 0) {
            sql.append(" and c.parentkindid=?");
            olist.add(parentId);
        }
        sql.append(" order by bc.companyid");
        Query query = manager.createQuery();
        return query.listBySqlParamList("ds1", sql.toString(), begin, size, Company.class, olist);
    }

    public int countCompany(int circleId, int kindId, int parentId, int cityId) {
        List<Object> olist = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder("select count(*) from companybizcircle bc,company c where bc.circleid=? and bc.companyid=c.companyid");
        olist.add(circleId);
        if (kindId > 0) {
            sql.append(" and c.kindid=?");
            olist.add(kindId);
        } else if (parentId > 0) {
            sql.append(" and c.parentkindid=?");
            olist.add(parentId);
        }
        if (cityId > 0) {
            sql.append(" and c.pcityid=?");
            olist.add(cityId);
        }
        Query query = manager.createQuery();
        return query.countBySql("ds1", sql.toString(), olist);
    }

    public void updatecitydata() {
        ZoneService zoneService = (ZoneService) HkUtil.getBean("zoneService");
        Query query = manager.createQuery();
        List<BizCircle> list = query.listEx(BizCircle.class);
        for (BizCircle o : list) {
            if (o.getCityId() == 0 && o.getProvinceId() != 0) {
                Province province = zoneService.getProvince(o.getProvinceId());
                if (province != null) {
                    String name = DataUtil.filterZoneName(province.getProvince());
                    City city = zoneService.getCityLike(name);
                    if (city != null) {
                        o.setCityId(city.getCityId());
                        this.updateBizCircle(o);
                    }
                }
            }
        }
    }
}
