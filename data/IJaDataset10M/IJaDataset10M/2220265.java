package com.cci.bmc.dao.impl;

import java.sql.SQLException;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.cci.bmc.action.Page;
import com.cci.bmc.dao.StaticAddressDao;
import com.cci.bmc.domain.Cmts;
import com.cci.bmc.domain.Scope;
import com.cci.bmc.domain.StaticAddress;

public class HibernateStaticAddressDao extends HibernateDaoSupport implements StaticAddressDao {

    public StaticAddress getByIp(String ipAddress) {
        return (StaticAddress) DataAccessUtils.uniqueResult(getHibernateTemplate().find("from StaticAddress where ipAddress = ?", ipAddress));
    }

    @SuppressWarnings("unchecked")
    public StaticAddress acquireStaticCpe(final Cmts cmts) {
        return (StaticAddress) DataAccessUtils.singleResult((List<StaticAddress>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery("from StaticAddress sa where sa.scope.type = ? and sa.inUse = ? and sa.scope.cmts.id = ?");
                query.setParameter(0, "Static CPE");
                query.setParameter(1, false);
                query.setParameter(2, cmts.getId());
                query.setFirstResult(0);
                query.setMaxResults(1);
                return query.list();
            }
        }));
    }

    @SuppressWarnings("unchecked")
    public Page<StaticAddress> list(final Scope scope, final int pageSize, final int page) {
        return new Page<StaticAddress>((List<StaticAddress>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createSQLQuery("SELECT * FROM static_addresses sa WHERE sa.scope_id = ? ORDER BY INET_ATON(sa.ip_address)").addEntity(StaticAddress.class);
                query.setParameter(0, scope.getId());
                query.setFirstResult((page - 1) * pageSize);
                query.setMaxResults(pageSize);
                return query.list();
            }
        }), pageSize, page);
    }

    public long count(Scope scope) {
        return (Long) DataAccessUtils.longResult(getHibernateTemplate().find("select count(*) from StaticAddress sa where sa.scope.id = ?", scope.getId()));
    }

    public StaticAddress get(Long id) {
        return (StaticAddress) getHibernateTemplate().load(StaticAddress.class, id);
    }

    public StaticAddress getAssignment(String macAddress) {
        return (StaticAddress) DataAccessUtils.uniqueResult(getHibernateTemplate().find("from StaticAddress where macAddress = ? and account is not null", macAddress));
    }

    public void save(StaticAddress staticAddress) {
        if (staticAddress.getId() == null) getHibernateTemplate().saveOrUpdate(staticAddress); else getHibernateTemplate().merge(staticAddress);
    }

    public void save(StaticAddress staticAddress, long sequence) {
        if (staticAddress.getId() == null) getHibernateTemplate().saveOrUpdate(staticAddress); else getHibernateTemplate().merge(staticAddress);
        if (sequence % 25 == 0) {
            getSession().flush();
            getSession().clear();
        }
    }
}
