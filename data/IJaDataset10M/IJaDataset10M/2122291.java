package com.scs.base.dao.impl.location;

import java.util.List;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;
import com.scs.base.dao.location.RegionDAO;
import com.scs.base.model.location.Region;

public class RegionDAOImpl extends HibernateDaoSupport implements RegionDAO {

    private static Log log = LogFactory.getLog(RegionDAOImpl.class);

    public Region getRegion(Long id) {
        return (Region) getHibernateTemplate().get(Region.class, id);
    }

    public List getRegions() {
        return getHibernateTemplate().find("from Region");
    }

    public List getRegionsByName(String regionName) {
        return getHibernateTemplate().find("from Region region where region.regionName like '" + regionName + "%'");
    }

    public void removeRegion(Long id) {
        Object region = getHibernateTemplate().load(Region.class, id);
        getHibernateTemplate().delete(region);
    }

    public void saveRegion(Region region) {
        getHibernateTemplate().saveOrUpdate(region);
        if (log.isDebugEnabled()) {
            log.debug("regionId set to: " + region.getId());
        }
    }

    public Long getRegionId(String regionName) {
        Long regionId = (Long) getHibernateTemplate().find("from Region region where region.regionName = " + regionName).get(0);
        return regionId;
    }

    public void saveAllRegions(List regionList) {
        try {
            Session session = getHibernateTemplate().getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            for (int i = 0; i < regionList.size(); i++) {
                session.save((Region) regionList.get(i));
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
