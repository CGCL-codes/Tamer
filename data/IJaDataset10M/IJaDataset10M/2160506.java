package desview.model.dao;

import desview.model.entities.Variable;
import desview.util.persistence.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;

/**
 * DAO for entity variable.
 * @author Diones Rossetto.
 * @author Luiz Mello.
 * @since 08/04/2010.
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class VariableDAO implements DAO {

    /**
     * Default constructor.
     */
    public VariableDAO() {
    }

    @Override
    public SessionFactory getSession() {
        return HibernateUtil.getSessionFactory();
    }

    @Override
    public void delete(Object var) {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        try {
            sessao.delete(var);
            transacao.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transacao.rollback();
        } finally {
            finalizeSession();
        }
    }

    @Override
    public boolean saveOrUpdate(Object var) {
        boolean retorno = false;
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        try {
            sessao.saveOrUpdate(var);
            transacao.commit();
            retorno = true;
        } catch (Exception e) {
            e.printStackTrace();
            transacao.rollback();
            retorno = false;
        } finally {
            finalizeSession();
        }
        return retorno;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Variable> getAll() {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        List<Variable> lista = new ArrayList<Variable>();
        try {
            lista = sessao.createCriteria(Variable.class).list();
            transacao.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transacao.rollback();
        } finally {
            finalizeSession();
        }
        return lista;
    }

    @Override
    public Object findById(Long id) {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        Object variavel = null;
        try {
            variavel = sessao.get(Variable.class, id);
            transacao.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transacao.rollback();
        } finally {
            finalizeSession();
        }
        return variavel;
    }

    @SuppressWarnings("unchecked")
    public Variable find(String oid) {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        Variable variavel = null;
        try {
            Query query = sessao.createQuery("from Variable v");
            List<Variable> resultados = query.list();
            for (Variable v : resultados) {
                if (oid.equals(v.getOid())) {
                    variavel = v;
                }
            }
            transacao.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transacao.rollback();
        } finally {
            finalizeSession();
        }
        return variavel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Variable> get() {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        List<Variable> resultados = null;
        try {
            Query query = sessao.createQuery("from Variable v");
            resultados = query.list();
            transacao.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transacao.rollback();
        } finally {
            finalizeSession();
        }
        return resultados;
    }

    @Override
    public Integer count() {
        List lista = get();
        finalizeSession();
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

    public Variable getByLabel(String label) {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        Variable resultado = null;
        try {
            Query q = sessao.getNamedQuery("Variable.findByVariableLabel");
            q.setParameter("label", label);
            resultado = (Variable) q.uniqueResult();
            transacao.commit();
        } catch (RuntimeException r) {
            r.printStackTrace();
        }
        return resultado;
    }

    @SuppressWarnings("unchecked")
    public List<Variable> getNameByLike(String name) {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        List<Variable> resultados;
        Criteria crit = sessao.createCriteria(Variable.class);
        crit.add(Expression.ilike("label", "%" + name + "%", MatchMode.ANYWHERE));
        resultados = crit.list();
        return resultados;
    }
}
