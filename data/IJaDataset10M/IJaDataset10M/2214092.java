package com.hilaver.dzmis.dao;

import java.util.List;
import org.hibernate.Query;
import com.hilaver.dzmis.log.Log;
import com.hilaver.dzmis.log.LogIp;

public class LogDAO extends BaseHibernateDAO {

    public List<LogIp> getLogIpFrom(Integer ipTotal) throws Exception {
        String hql = "from " + LogIp.class.getName() + " where ip1 <= ? and ip2 >= ?";
        try {
            Query queryObject = getSession().createQuery(hql);
            queryObject.setParameter(0, ipTotal);
            queryObject.setParameter(1, ipTotal);
            return queryObject.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Log> getIllegalTimePagination(int offset, int limit, String sort, String order, String[] filters) throws Exception {
        String hql = "from " + Log.class.getName() + getWhereStatement(filters);
        if ("".equals(getWhereStatement(filters))) {
            hql += " where ";
        } else {
            hql += " and ";
        }
        hql += "(DAYNAME(visitDate) = 'Saturday' or DAYNAME(visitDate) = 'Sunday' or hour(visitDate) < 9 or hour(visitDate) > 18)";
        hql += " order by " + sort + " " + order;
        try {
            Query queryObject = getSession().createQuery(hql);
            queryObject.setFirstResult(offset);
            queryObject.setMaxResults(limit);
            return queryObject.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Log> getIllegalZonePagination(int offset, int limit, String sort, String order, String[] filters) throws Exception {
        String hql = "from " + Log.class.getName() + getWhereStatement(filters);
        if ("".equals(getWhereStatement(filters))) {
            hql += " where ";
        } else {
            hql += " and ";
        }
        hql += "( countryCn not like '%北京%' and countryCn not like '%法国%' and countryCn not like '%香港%')";
        hql += " order by " + sort + " " + order;
        try {
            Query queryObject = getSession().createQuery(hql);
            queryObject.setFirstResult(offset);
            queryObject.setMaxResults(limit);
            return queryObject.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
