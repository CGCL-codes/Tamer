package no.ugland.utransprod.dao.hibernate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import no.ugland.utransprod.dao.OrdlnDAO;
import no.ugland.utransprod.model.Ord;
import no.ugland.utransprod.model.Ordln;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

public class OrdlnDAOHibernate extends BaseDAOHibernate<Ordln> implements OrdlnDAO {

    /**
	 * Konstrukt�r
	 */
    public OrdlnDAOHibernate() {
        super(Ordln.class);
    }

    @SuppressWarnings("unchecked")
    public final List<Ordln> findByOrderNr(final String orderNr) {
        return (List<Ordln>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(final Session session) {
                String sql = "select ordln from Ordln ordln,Ord ord " + "       where ordln.ordlnPK.ordno=ord.ordno and " + "               ord.inf6=:orderNr " + "       order by ordln.ordlnPK.lnno";
                return session.createQuery(sql).setParameter("orderNr", orderNr).list();
            }
        });
    }

    public Ordln findByOrdNoAndLnNo(final Integer ordNo, final Integer lnNo) {
        return (Ordln) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(final Session session) {
                List<Ordln> list = session.createCriteria(Ordln.class).add(Restrictions.eq("ordlnPK.ordno", ordNo)).add(Restrictions.eq("ordlnPK.lnno", lnNo)).list();
                if (list != null && list.size() == 1) {
                    return list.get(0);
                }
                return null;
            }
        });
    }

    public final BigDecimal getSumCstpr(final String orderNr, final Integer prCatNo) {
        return (BigDecimal) getHibernateTemplate().execute(new HibernateCallback() {

            @SuppressWarnings("unchecked")
            public Object doInHibernate(final Session session) {
                String sql = "select sum(ordln.costPrice) " + "        from Ordln ordln,Ord ord,Prod prod" + "        where ordln.ordlnPK.ordno=ord.ordno and " + "              ord.inf6=:orderNr and " + "              prod=ordln.prod and" + "        prod.prCatNo=:prCatNo";
                List<Object> list = session.createQuery(sql).setParameter("orderNr", orderNr).setParameter("prCatNo", prCatNo).list();
                BigDecimal sum = (BigDecimal) list.get(0);
                if (sum != null) {
                    return sum;
                }
                return BigDecimal.ZERO;
            }
        });
    }

    public Ordln findByOrderNrProdCatNo(final String orderNr, final Integer prodCatNo, final Integer prodCatNo2) {
        return (Ordln) getHibernateTemplate().execute(new HibernateCallback() {

            @SuppressWarnings("unchecked")
            public Object doInHibernate(final Session session) {
                StringBuilder sqlBuilder = new StringBuilder("select ordln from Ordln ordln,Ord ord,Prod prod " + "       where ordln.ordlnPK.ordno=ord.ordno and " + "             ord.inf6=:orderNr and " + "             ordln.prod=prod");
                FindByOrderNrProdCatNoSelector selector = getSqlSelector(prodCatNo, prodCatNo2);
                sqlBuilder.append(selector.getSql());
                Query query = session.createQuery(sqlBuilder.toString());
                query.setParameter("orderNr", orderNr);
                selector.setParameters(query, prodCatNo, prodCatNo2);
                List<Ordln> list = query.list();
                return list != null && list.size() != 0 ? list.get(0) : Ordln.UNKNOWN;
            }
        });
    }

    private FindByOrderNrProdCatNoSelector getSqlSelector(final Integer prodCatNo, final Integer prodCatNo2) {
        String selectorString = prodCatNo != null && prodCatNo2 != null ? "PRODCATNO_PRODCATNO2" : prodCatNo != null ? "PRODCATNO" : "PRODCATNO2";
        return FindByOrderNrProdCatNoSelector.valueOf(selectorString);
    }

    private enum FindByOrderNrProdCatNoSelector {

        PRODCATNO_PRODCATNO2 {

            @Override
            public String getSql() {
                return " and prod.prCatNo=:prCatNo and prod.prCatNo2=:prCatNo2";
            }

            @Override
            public void setParameters(Query query, Integer prodCatNo, Integer prodCatNo2) {
                query.setParameter("prCatNo", prodCatNo).setParameter("prCatNo2", prodCatNo2);
            }
        }
        , PRODCATNO {

            @Override
            public String getSql() {
                return " and prod.prCatNo=:prCatNo and (prod.prCatNo2 is null or prod.prCatNo2 =0)";
            }

            @Override
            public void setParameters(Query query, Integer prodCatNo, Integer prodCatNo2) {
                query.setParameter("prCatNo", prodCatNo);
            }
        }
        , PRODCATNO2 {

            @Override
            public String getSql() {
                return " and prod.prCatNo2=:prCatNo2 and (prod.prCatNo is null or prod.prCatNo =0)";
            }

            @Override
            public void setParameters(Query query, Integer prodCatNo, Integer prodCatNo2) {
                query.setParameter("prCatNo2", prodCatNo2);
            }
        }
        ;

        public abstract String getSql();

        public abstract void setParameters(Query query, Integer prodCatNo, Integer prodCatNo2);
    }

    public BigDecimal getSumCcstpr(final String orderNr, final Integer prodCatNo) {
        return (BigDecimal) getHibernateTemplate().execute(new HibernateCallback() {

            @SuppressWarnings("unchecked")
            public Object doInHibernate(final Session session) {
                String sql = "select sum(ordln.ccstPr*ordln.noOrg) " + "        from Ordln ordln,Ord ord,Prod prod" + "        where ordln.ordlnPK.ordno=ord.ordno and " + "              ord.inf6=:orderNr and " + "              prod=ordln.prod and" + "        prod.prCatNo=:prCatNo";
                List<Object> list = session.createQuery(sql).setParameter("orderNr", orderNr).setParameter("prCatNo", prodCatNo).list();
                BigDecimal sum = (BigDecimal) list.get(0);
                if (sum != null) {
                    return sum;
                }
                return BigDecimal.ZERO;
            }
        });
    }

    public Integer isOrderLineInStorage(final Integer ordNo, final Integer lnNo) {
        return (Integer) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(final Session session) {
                String sql = "select count(stcBal.prodNo) " + "         from Ordln ordln,StcBal stcBal " + "       where ordln.ordlnPK.ordno=:ordno and " + "       ordln.ordlnPK.lnno=:lnno and " + "       ordln.prod.prodNo=stcBal.prodNo and " + "             stcBal.minBal > 0";
                List<Integer> list = session.createQuery(sql).setParameter("ordno", ordNo).setParameter("lnno", lnNo).list();
                Integer count = list != null && list.size() != 0 ? list.get(0) : null;
                return count != 0 ? 1 : 0;
            }
        });
    }

    public Ord findOrdByOrderNr(final String orderNr) {
        return (Ord) getHibernateTemplate().execute(new HibernateCallback() {

            @SuppressWarnings("unchecked")
            public Object doInHibernate(Session session) {
                List<Ord> list = session.createCriteria(Ord.class).add(Restrictions.eq("inf6", orderNr)).list();
                return list.size() == 1 ? list.get(0) : null;
            }
        });
    }

    public Ordln findByOrdnoAndPrCatNo2(final Integer ordno, final Integer prodCatNo2) {
        return (Ordln) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) {
                List<Ordln> list = session.createCriteria(Ordln.class).add(Restrictions.eq("ordlnPK.ordno", ordno)).createCriteria("prod").add(Restrictions.eq("prCatNo2", prodCatNo2)).list();
                return list.size() == 1 ? list.get(0) : null;
            }
        });
    }

    public List<Ordln> findCostLines(final String orderNr) {
        return (List<Ordln>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(final Session session) {
                String sql = "select ordln from Ordln ordln,Ord ord " + "       where ordln.ordlnPK.ordno=ord.ordno and " + "             ord.inf6=:orderNr and " + "             ordln.prod.prodNo in (:prodnoList) " + "       order by ordln.ordlnPK.lnno";
                return session.createQuery(sql).setParameterList("prodnoList", Ordln.CostLine.getCostProdnoList()).setParameter("orderNr", orderNr).list();
            }
        });
    }
}
