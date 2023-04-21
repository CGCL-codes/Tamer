package uk.co.marcoratto.jtrovacap.hsqldb;

import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import uk.co.marcoratto.util.HibernateUtil;

/**
 * This class provides methods to populate DB Table of CAP
 */
public class CapDAO {

    private Session session;

    private static CapDAO istance = null;

    private static Logger logger = Logger.getLogger("uk.co.marcoratto.jtrovacap");

    private CapDAO() {
    }

    public static CapDAO getInstance() {
        if (istance == null) {
            istance = new CapDAO();
        }
        return istance;
    }

    public int getTotal(String comune, String provincia, String toponimo, String cap) {
        int outValue = -1;
        this.session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(Cap.class);
        criteria.setProjection(Projections.rowCount());
        if (comune != null) {
            criteria.add(Restrictions.or(Restrictions.like("comu", "%" + comune.toUpperCase() + "%"), Restrictions.like("fraz", "%" + comune.toUpperCase() + "%")));
        }
        if (provincia != null) {
            criteria.add(Restrictions.eq("prov", provincia.toUpperCase()));
        }
        if (toponimo != null) {
            criteria.add(Restrictions.like("topo", "%" + toponimo.toUpperCase() + "%"));
        }
        if (cap != null) {
            criteria.add(Restrictions.like("capi", "%" + cap + "%"));
        }
        outValue = ((Integer) criteria.list().get(0)).intValue();
        logger.debug("Total=" + outValue);
        session.getTransaction().commit();
        return outValue;
    }

    public List<Cap> getElenco(String comune, String provincia, String toponimo, String cap) throws SQLException {
        this.session = HibernateUtil.getSessionFactory().getCurrentSession();
        this.session.beginTransaction();
        Criteria criteria = this.session.createCriteria(Cap.class);
        if (comune != null) {
            criteria.add(Restrictions.or(Restrictions.like("comu", "%" + comune.toUpperCase() + "%"), Restrictions.like("fraz", "%" + comune.toUpperCase() + "%")));
        }
        if (provincia != null) {
            criteria.add(Restrictions.eq("prov", provincia.toUpperCase()));
        }
        if (toponimo != null) {
            criteria.add(Restrictions.like("topo", "%" + toponimo.toUpperCase() + "%"));
        }
        if (cap != null) {
            criteria.add(Restrictions.like("capi", "%" + cap + "%"));
        }
        List<Cap> l = (List<Cap>) criteria.list();
        this.session.getTransaction().commit();
        logger.debug("Total=" + l.size());
        return l;
    }

    public List<String> getElencoProvince() throws CapDAOException {
        List<String> out;
        try {
            this.session = HibernateUtil.getSessionFactory().getCurrentSession();
            this.session.beginTransaction();
            Criteria criteria = this.session.createCriteria(Cap.class);
            criteria.addOrder(Order.asc("prov"));
            criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("prov"), "prov"));
            out = criteria.list();
            this.session.getTransaction().commit();
            logger.debug("Total=" + out.size());
        } catch (Throwable t) {
            throw new CapDAOException(t);
        }
        return out;
    }
}
