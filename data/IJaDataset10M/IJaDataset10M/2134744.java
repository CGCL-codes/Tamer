package com.scs.base.dao.impl.part;

import java.util.List;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;
import com.scs.base.dao.part.PartFamilyDAO;
import com.scs.base.model.part.PartFamily;

public class PartFamilyDAOImpl extends HibernateDaoSupport implements PartFamilyDAO {

    private static Log log = LogFactory.getLog(PartFamilyDAOImpl.class);

    public List getPartFamilies() {
        return getHibernateTemplate().find("from PartFamily");
    }

    public List getPartFamiliesByName(String partFamilyName) {
        return getHibernateTemplate().find("from PartFamily partFamily where partFamily.partFamilyName like '" + partFamilyName + "%'");
    }

    public PartFamily getPartFamily(Long id) {
        return (PartFamily) getHibernateTemplate().get(PartFamily.class, id);
    }

    public void removePartFamily(Long id) {
        Object partFamily = getHibernateTemplate().load(PartFamily.class, id);
        getHibernateTemplate().delete(partFamily);
    }

    public void savePartFamily(PartFamily partFamily) {
        getHibernateTemplate().saveOrUpdate(partFamily);
        if (log.isDebugEnabled()) {
            log.debug("partFamilyId set to: " + partFamily.getId());
        }
    }

    public Long getPartFamilyId(String partFamilyName) {
        Long partFamilyId = (Long) getHibernateTemplate().find("from PartFamily partFamily where partFamily.partFamilyName = " + partFamilyName).get(0);
        return partFamilyId;
    }

    public void saveAllPartFamilies(List partFamilyList) {
        try {
            Session session = getHibernateTemplate().getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            for (int i = 0; i < partFamilyList.size(); i++) {
                session.save((PartFamily) partFamilyList.get(i));
                if (i % 100 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}
