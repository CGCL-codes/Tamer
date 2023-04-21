package desview.model.dao;

import desview.model.entities.Historic;
import desview.util.persistence.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

/**
 * DAO for historic entity.
 * @author Diones Rossetto.
 * @author Luiz Mello.
 * @since 02/05/2010.
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class HistoricDAO implements DAO {

    /**
     * Default constructor.
     */
    public HistoricDAO() {
    }

    @Override
    public SessionFactory getSession() {
        return HibernateUtil.getSessionFactory();
    }

    @Override
    public void delete(Object historic) {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        try {
            sessao.delete(historic);
            transacao.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transacao.rollback();
        }
    }

    @Override
    public boolean saveOrUpdate(Object historic) {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        try {
            sessao.saveOrUpdate(historic);
            transacao.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            transacao.rollback();
            return false;
        }
    }

    @Override
    public Object findById(Long id) {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        Object leitura = null;
        try {
            leitura = sessao.get(Historic.class, id);
            transacao.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transacao.rollback();
        }
        return leitura;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Historic> get() {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        List<Historic> resultados = null;
        try {
            Query query = sessao.createQuery("from Historic h");
            resultados = query.list();
            transacao.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transacao.rollback();
        }
        return resultados;
    }

    @Override
    public Integer count() {
        List lista = get();
        Integer i = new Integer(lista.size());
        return i.intValue();
    }

    @Override
    public void finalizeSession() {
        Session sessao = getSession().getCurrentSession();
        if (sessao.isOpen()) {
            sessao.close();
        }
    }

    @Override
    public List getAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @param oid
     * @return historic by parameters
     */
    @SuppressWarnings("unchecked")
    public List<Historic> getHistoricByParameters(String oid) {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        List<Historic> resultados = new ArrayList<Historic>();
        try {
            Query q = sessao.getNamedQuery("Reading.findDistinct");
            resultados = q.list();
            transacao.commit();
        } catch (RuntimeException r) {
            r.printStackTrace();
        }
        return resultados;
    }

    @SuppressWarnings("unchecked")
    public List<Historic> getOIDAvgByYear() {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        Criteria criteria = sessao.createCriteria(Historic.class);
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.avg("readValue"));
        projectionList.add(Projections.groupProperty("year"));
        projectionList.add(Projections.groupProperty("oid"));
        criteria.setProjection(projectionList);
        List results = criteria.list();
        return results;
    }

    @SuppressWarnings("unchecked")
    public List<Historic> getOIDAvgByMonth() {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        Criteria criteria = sessao.createCriteria(Historic.class);
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.avg("readValue"));
        projectionList.add(Projections.groupProperty("day"));
        projectionList.add(Projections.groupProperty("oid"));
        criteria.setProjection(projectionList);
        List results = criteria.list();
        return results;
    }

    @SuppressWarnings("unchecked")
    public List<Historic> getOIDAvgByDay() {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        Criteria criteria = sessao.createCriteria(Historic.class);
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.avg("readValue"));
        projectionList.add(Projections.groupProperty("day"));
        projectionList.add(Projections.groupProperty("month"));
        projectionList.add(Projections.groupProperty("year"));
        projectionList.add(Projections.groupProperty("oid"));
        criteria.setProjection(projectionList);
        List results = criteria.list();
        return results;
    }

    @SuppressWarnings("unchecked")
    public List<Historic> getOIDAvgByDayInMonth(String month) {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        StringBuilder sql = new StringBuilder();
        sql.append("select day, avg(historic.read_value) from historic where month ='").append(month).append("' group by day;");
        return sessao.createSQLQuery(sql.toString()).list();
    }

    @SuppressWarnings("unchecked")
    public List<Historic> getAvgDayOIDByOIDYearMonth(String oid, String year, String month) {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        StringBuilder sql = new StringBuilder();
        sql.append("select day, oid, avg(historic.read_value) from historic where month ='").append(month).append("' and year ='").append(year).append("' and oid ='").append(oid).append("' group by day, oid;");
        return sessao.createSQLQuery(sql.toString()).list();
    }

    @SuppressWarnings("unchecked")
    public List<Historic> getValuesByOIDYear(String oid, String year) {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        StringBuilder sql = new StringBuilder();
        sql.append("select month, read_value from historic where year ='").append(year).append("' and oid ='").append(oid).append("' order by month;");
        return sessao.createSQLQuery(sql.toString()).list();
    }

    @SuppressWarnings("unchecked")
    public List<Historic> getValuesAMM() {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        StringBuilder sql = new StringBuilder();
        sql.append("select variable_name, min(read_value), avg(read_value), max(read_value) from historic group by variable_name;");
        return sessao.createSQLQuery(sql.toString()).list();
    }
}
