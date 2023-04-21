package net.sf.bootstrap.framework.dao.impl;

import java.util.Date;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import net.sf.bootstrap.framework.dao.AuditLogDAO;
import net.sf.bootstrap.framework.model.AuditLogRecord;

/**
 * Hibernate implementation of the AuditLogDAO.
 *
 * @author Mark Moloney
 */
public class AuditLogDAOHibernate extends HibernateDaoSupport implements AuditLogDAO {

    public List findAllAuditLogRecords() {
        return getHibernateTemplate().loadAll(AuditLogRecord.class);
    }

    public List findAuditLogRecords(String event, Date sinceDate) {
        return getHibernateTemplate().find("from AuditLogRecord where event = ? and updatedDate >= ? order by entityId", new Object[] { event, sinceDate });
    }
}
