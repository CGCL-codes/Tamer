package com.creawor.hz_market.t_community;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import com.creawor.hz_market.servlet.LoadMapInfoAjax;
import com.creawor.imei.base.AbsQueryMap;

public class t_community_QueryMap extends AbsQueryMap {

    private static final Logger logger = Logger.getLogger(LoadMapInfoAjax.class);

    public t_community_QueryMap() throws HibernateException {
        this.initSession();
    }

    public Iterator findAll() throws HibernateException {
        this.totalrow = ((Integer) this.session.iterate("select count(*) from t_community").next()).intValue();
        String querystr = "from t_community";
        Query query = this.session.createQuery(querystr);
        this.setQueryPage(query);
        return query.iterate();
    }

    public List findAllList() throws HibernateException {
        this.totalrow = ((Integer) this.session.iterate("select count(*) from t_community").next()).intValue();
        String querystr = "from t_community";
        Query query = this.session.createQuery(querystr);
        this.setQueryPage(query);
        return query.list();
    }

    public List findAllByCounty(String county) throws HibernateException {
        this.totalrow = ((Integer) this.session.iterate("select count(*) from t_community where county ='" + county + "'").next()).intValue();
        String querystr = "from t_community  as t where t.county ='" + county + "'";
        Query query = this.session.createQuery(querystr);
        this.setQueryPage(query);
        return query.list();
    }

    public t_community_Form getByID(String ID) throws HibernateException {
        t_community_Form vo = null;
        logger.debug("\nt_community_QueryMap getByID:" + ID);
        this.session.clear();
        try {
            vo = new t_community_Form();
            t_community po = (t_community) this.session.load(t_community.class, new Integer(ID));
            try {
                vo.setId(String.valueOf(po.getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            vo.setFiliale(po.getFiliale());
            vo.setCell_name(po.getCell_name());
            try {
                vo.setX(String.valueOf(po.getX()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                vo.setY(String.valueOf(po.getY()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            vo.setCode(po.getCode());
            vo.setCounty(po.getCounty());
            vo.setTown(po.getTown());
            vo.setJuwei_name(po.getJuwei_name());
            vo.setGeography(po.getGeography());
            vo.setListings(po.getListings());
            vo.setProperty_manage_company(po.getProperty_manage_company());
            vo.setPublicize_res(po.getPublicize_res());
            vo.setBring_listing(po.getBring_listing());
            vo.setRemark(po.getRemark());
            try {
                vo.setInsert_day(com.creawor.km.util.DateUtil.getStr(po.getInsert_day(), "yyyy-MM-dd"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (HibernateException e) {
            logger.debug("\nERROR in getByID @t_community:" + e);
        }
        return vo;
    }
}
