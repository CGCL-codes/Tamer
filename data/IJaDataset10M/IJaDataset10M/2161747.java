package sg.com.fastwire.mediation.plugins.huaweiMTOSI.hibernate.daoimpl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import sg.com.fastwire.mediation.plugins.huaweiMTOSI.dao.DAOException;
import sg.com.fastwire.mediation.plugins.huaweiMTOSI.dao.ProtectionGroupDao;
import sg.com.fastwire.mediation.plugins.huaweiMTOSI.entity.ProtectionGroup;

public class ProtectionGroupDaoImpl extends AbstractDAOImpl implements ProtectionGroupDao {

    public void saveOrUpdatePG(ProtectionGroup protectionGroup) throws DAOException {
        super.saveOrUpdate(protectionGroup);
    }

    public List findProtectionGroupbyNEName(String neID) throws DAOException {
        Session session = HibernateUtil.openSession();
        try {
            Criteria criteria = session.createCriteria(ProtectionGroup.class);
            criteria = criteria.add(getStatusCriteria());
            criteria.add(Restrictions.like("name", neID, MatchMode.ANYWHERE));
            return criteria.list();
        } catch (Exception ex) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new DAOException(ex);
        } finally {
            HibernateUtil.close(session);
        }
    }

    public ProtectionGroup findProtectionGroupbyMemberName(String tunnel, String neID) throws DAOException {
        Session session = HibernateUtil.openSession();
        try {
            Criteria criteria = session.createCriteria(ProtectionGroup.class);
            criteria = criteria.add(getStatusCriteria());
            Criterion criterion = Restrictions.like("working", tunnel, MatchMode.ANYWHERE);
            criterion = Restrictions.or(criterion, Restrictions.like("workingReverse", tunnel, MatchMode.ANYWHERE));
            criterion = Restrictions.or(criterion, Restrictions.like("protection", tunnel, MatchMode.ANYWHERE));
            criterion = Restrictions.or(criterion, Restrictions.like("protectionReverse", tunnel, MatchMode.ANYWHERE));
            criterion = Restrictions.and(criterion, Restrictions.like("emsNativeName", neID, MatchMode.ANYWHERE));
            return (ProtectionGroup) criteria.add(criterion).uniqueResult();
        } catch (Exception ex) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new DAOException(ex);
        } finally {
            HibernateUtil.close(session);
        }
    }
}
