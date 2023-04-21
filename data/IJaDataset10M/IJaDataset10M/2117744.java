package com.brekeke.report.engine.dao.impl;

import java.sql.SQLException;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import com.brekeke.report.engine.dao.CtiLogDao;
import com.brekeke.report.engine.entity.MAcd;
import com.brekeke.report.engine.entity.MAgentActive;
import com.brekeke.report.engine.entity.MAgentStatus;
import com.brekeke.report.engine.entity.MCallStatus;
import com.brekeke.report.engine.entity.MQueue;

public class CtiLogDaoImpl implements CtiLogDao {

    HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public MAgentActive searchAgentActiveLastRecord(final long last, String year, final int tenantId, final String agentId) throws Exception {
        final StringBuffer sql = new StringBuffer("");
        sql.append(" select *");
        sql.append(" from m_agent_active_" + year);
        sql.append(" where ");
        sql.append(" ctime<=:ctime");
        sql.append(" and tenant_id =:tenant_id");
        sql.append(" and agent_id =:agent_id");
        sql.append(" and enter =1");
        sql.append(" order by ctime desc");
        sql.append(" limit 1");
        return (MAgentActive) hibernateTemplate.execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createSQLQuery(sql.toString()).addEntity(MAgentActive.class);
                query.setParameter("ctime", last);
                query.setParameter("tenant_id", tenantId);
                query.setParameter("agent_id", agentId);
                List<MAgentActive> list = query.list();
                if (list.size() > 0) {
                    return (MAgentActive) list.get(0);
                }
                return null;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public MAgentActive searchAgentActiveLastRecordByPhoneId(final long last, String year, final int tenantId, final String phoneId) throws Exception {
        final StringBuffer sql = new StringBuffer("");
        sql.append(" select *");
        sql.append(" from m_agent_active_" + year);
        sql.append(" where ");
        sql.append(" ctime<=:ctime");
        sql.append(" and tenant_id =:tenant_id");
        sql.append(" and phoneid =:phoneid");
        sql.append(" order by ctime desc");
        sql.append(" limit 1");
        return (MAgentActive) hibernateTemplate.execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createSQLQuery(sql.toString()).addEntity(MAgentActive.class);
                query.setParameter("ctime", last);
                query.setParameter("tenant_id", tenantId);
                query.setParameter("phoneid", phoneId);
                List<MAgentActive> list = query.list();
                if (list.size() > 0) {
                    return (MAgentActive) list.get(0);
                }
                return null;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MAgentStatus> searchAgentStatusList(final int tenantId, final long last, String year) throws Exception {
        final StringBuffer sql = new StringBuffer("");
        sql.append(" select *");
        sql.append(" from m_agent_status_" + year);
        sql.append(" where ");
        sql.append(" ctime>:ctime");
        sql.append(" and tenant_id=:tenant_id");
        sql.append(" order by tenant_id,agent_id,ctime");
        return (List<MAgentStatus>) hibernateTemplate.execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createSQLQuery(sql.toString()).addEntity(MAgentStatus.class);
                query.setParameter("tenant_id", tenantId);
                query.setParameter("ctime", last);
                List<MAgentActive> list = query.list();
                return list;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MCallStatus> searchCallStatusList(final int tenantId, final long last, String year) throws Exception {
        final StringBuffer sql = new StringBuffer("");
        sql.append(" select *");
        sql.append(" from m_call_status_" + year);
        sql.append(" where ");
        sql.append(" ptime>:ptime");
        sql.append(" and tenant_id=:tenant_id");
        sql.append(" order by tenant_id,rid,tid,ptime");
        return (List<MCallStatus>) hibernateTemplate.execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createSQLQuery(sql.toString()).addEntity(MCallStatus.class);
                query.setParameter("tenant_id", tenantId);
                query.setParameter("ptime", last);
                List<MCallStatus> list = query.list();
                return list;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MQueue> searchQueueList(final long last, String year, final int tenantId, final long rid) throws Exception {
        final StringBuffer sql = new StringBuffer("");
        sql.append(" select *");
        sql.append(" from m_queue_" + year);
        sql.append(" where ");
        sql.append(" ctime>:ctime");
        sql.append(" and tenant_id =:tenant_id");
        sql.append(" and rid =:rid");
        sql.append(" order by ctime");
        return (List<MQueue>) hibernateTemplate.execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createSQLQuery(sql.toString()).addEntity(MQueue.class);
                query.setParameter("ctime", last);
                query.setParameter("tenant_id", tenantId);
                query.setParameter("rid", rid);
                List<MQueue> list = query.list();
                return list;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MAcd> searchAcdList(final long last, String year, final int tenantId, final long rid) throws Exception {
        final StringBuffer sql = new StringBuffer("");
        sql.append(" select *");
        sql.append(" from m_acd_" + year);
        sql.append(" where ");
        sql.append(" ctime>:ctime");
        sql.append(" and tenant_id =:tenant_id");
        sql.append(" and rid =:rid");
        sql.append(" order by ctime");
        return (List<MAcd>) hibernateTemplate.execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createSQLQuery(sql.toString()).addEntity(MAcd.class);
                query.setParameter("ctime", last);
                query.setParameter("tenant_id", tenantId);
                query.setParameter("rid", rid);
                List<MAcd> list = query.list();
                return list;
            }
        });
    }
}
