package com.jd.mysql.mgr.dao.impl;

import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.jd.mysql.mgr.dao.MgrInspectReportDAO;
import com.jd.mysql.mgr.pojo.MgrInspectReport;
import com.jd.mysql.mgr.util.GlobalStaticVariables;

public class MgrInspectReportDAOImpl extends HibernateDaoSupport implements MgrInspectReportDAO {

    public void deleteMgrInspectReport(MgrInspectReport mir) {
        HibernateTemplate tmp = this.getHibernateTemplate();
        tmp.setFlushMode(tmp.FLUSH_EAGER);
        tmp.delete(mir);
    }

    public MgrInspectReport findMgrInspectReportById(Integer id) {
        return this.getHibernateTemplate().get(MgrInspectReport.class, id);
    }

    public void saveMgrInspectReport(MgrInspectReport mir) {
        this.getHibernateTemplate().save(mir);
    }

    public void updateMgrInspectReport(MgrInspectReport mir) {
        HibernateTemplate tmp = this.getHibernateTemplate();
        tmp.setFlushMode(tmp.FLUSH_EAGER);
        tmp.update(mir);
    }

    public List<MgrInspectReport> getMgrInspectReportList(Date date, Integer limit, Integer start) {
        String hql = "from MgrInspectReport where 1=1 ";
        if (date != null) {
            hql += "and createDate=:date";
        } else {
            hql += "and createDate=(select max(createDate) from MgrInspectReport)";
        }
        Session session = (Session) this.getSession();
        Query query = session.createQuery(hql);
        if (GlobalStaticVariables.limitless != limit) {
            query.setMaxResults(limit);
            query.setFirstResult(start);
        }
        List<MgrInspectReport> list = query.list();
        return (list != null && list.size() > 0) ? list : null;
    }

    public Long getMgrInspectReportListCount(Date date) {
        String hql = "select count(*) from MgrInspectReport where 1=1 ";
        if (date != null) {
            hql += "and createDate=:date";
        } else {
            hql += "and createDate=(select max(createDate) from MgrInspectReport)";
        }
        Session session = (Session) this.getSession();
        Query query = session.createQuery(hql);
        List<Long> list = query.list();
        return (list != null && list.size() > 0) ? list.get(0) : 0;
    }
}
