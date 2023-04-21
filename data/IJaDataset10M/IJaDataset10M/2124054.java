package dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.Type;
import org.hibernatespatial.GeometryUserType;
import org.hibernatespatial.criterion.SpatialRestrictions;
import util.HibernateUtil;
import com.vividsolutions.jts.geom.Geometry;
import dao.ColaMarketDao;
import domain.ColaMarket;

/**
 * 
 * @author Sun
 * @version ColaMarketDaoImpl.java 2011-4-6 上午09:15:30
 */
public class ColaMarketDaoImpl implements ColaMarketDao {

    public void add(ColaMarket colaMarket) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(colaMarket);
        session.getTransaction().commit();
    }

    public ColaMarket find(Integer id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        ColaMarket colaMarket = (ColaMarket) session.load(ColaMarket.class, id);
        session.getTransaction().commit();
        return colaMarket;
    }

    public List<ColaMarket> spatialQueryWithin(Geometry geometry) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(ColaMarket.class);
        criteria.add(SpatialRestrictions.within("location", geometry));
        List<ColaMarket> results = criteria.list();
        session.getTransaction().commit();
        return results;
    }

    public List<ColaMarket> spatialQueryWithinHql(Geometry geometry) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query q = session.createQuery("from ColaMarket where within(location, ?) = true");
        Type geometryType = GeometryUserType.TYPE;
        q.setParameter(0, geometry, geometryType);
        List<ColaMarket> results = q.list();
        session.getTransaction().commit();
        return results;
    }
}
