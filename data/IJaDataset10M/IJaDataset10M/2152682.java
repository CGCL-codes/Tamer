package no.ugland.utransprod.dao.hibernate;

import java.util.List;
import no.ugland.utransprod.dao.GulvsponPackageVDAO;
import no.ugland.utransprod.model.GulvsponPackageV;
import no.ugland.utransprod.model.PackableListItem;
import no.ugland.utransprod.model.ProductAreaGroup;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * Implementasjon av DAO for GULVSPON_PACKAGE_V
 * 
 * @author atle.brekka
 * 
 */
public class GulvsponPackageVDAOHibernate extends BaseDAOHibernate<GulvsponPackageV> implements GulvsponPackageVDAO {

    /**
	 * Konstrukt�r
	 */
    public GulvsponPackageVDAOHibernate() {
        super(GulvsponPackageV.class);
    }

    /**
	 * @return ordre med gulvspon
	 * @see no.ugland.utransprod.dao.VeggProductionVDAO#findAll()
	 */
    @SuppressWarnings("unchecked")
    public List<PackableListItem> findAll() {
        return getHibernateTemplate().find("from GulvsponPackageV order by transportYear,transportWeek,loadingDate,transportDetails,loadTime");
    }

    /**
	 * @param orderNr
	 * @return ordre med gulvspon
	 * @see no.ugland.utransprod.dao.GulvsponPackageVDAO#findByOrderNr(java.lang.String)
	 */
    @SuppressWarnings("unchecked")
    public List<PackableListItem> findByOrderNr(final String orderNr) {
        return (List<PackableListItem>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                return session.createCriteria(GulvsponPackageV.class).add(Restrictions.eq("orderNr", orderNr)).list();
            }
        });
    }

    /**
	 * @see no.ugland.utransprod.dao.GulvsponPackageVDAO#refresh(no.ugland.utransprod.model.GulvsponPackageV)
	 */
    public void refresh(GulvsponPackageV gulvsponPackageV) {
        getHibernateTemplate().flush();
        getHibernateTemplate().load(gulvsponPackageV, gulvsponPackageV.getOrderLineId());
    }

    /**
	 * @see no.ugland.utransprod.dao.GulvsponPackageVDAO#findByCustomerNr(java.lang.Integer)
	 */
    @SuppressWarnings("unchecked")
    public List<PackableListItem> findByCustomerNr(final Integer customerNr) {
        return (List<PackableListItem>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                return session.createCriteria(GulvsponPackageV.class).add(Restrictions.eq("customerNr", customerNr)).list();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public List<PackableListItem> findByCustomerNrProductAreaGroup(final Integer customerNr, final ProductAreaGroup productAreaGroup) {
        return (List<PackableListItem>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                Criteria criteria = session.createCriteria(GulvsponPackageV.class).add(Restrictions.eq("customerNr", customerNr));
                if (productAreaGroup != null && !productAreaGroup.getProductAreaGroupName().equalsIgnoreCase("Alle")) {
                    criteria.add(Restrictions.eq("productAreaGroupName", productAreaGroup.getProductAreaGroupName()));
                }
                return criteria.list();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public List<PackableListItem> findByOrderNrAndProductAreaGroup(final String orderNr, final ProductAreaGroup productAreaGroup) {
        return (List<PackableListItem>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                Criteria criteria = session.createCriteria(GulvsponPackageV.class).add(Restrictions.eq("orderNr", orderNr));
                if (productAreaGroup != null && !productAreaGroup.getProductAreaGroupName().equalsIgnoreCase("Alle")) {
                    criteria.add(Restrictions.eq("productAreaGroupName", productAreaGroup.getProductAreaGroupName()));
                }
                return criteria.list();
            }
        });
    }
}
