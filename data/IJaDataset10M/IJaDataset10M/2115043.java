package br.com.promove.dao;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import br.com.promove.exception.DAOException;

public class HibernateCRUD implements Serializable {

    /**
	 * Executa uma query baseada no HQL informado.
	 * @param hql
	 * @param end 
	 * @param init 
	 * @return
	 * @throws DAOException
	 */
    public static List executeQuery(String hql, int init, int end) throws DAOException {
        return executeQuery(hql, null, init, end);
    }

    /**
	 * Executa uma query baseado no HQL e nos parï¿½metros passados.
	 * @param hql
	 * @param params
	 * @return
	 */
    public static List executeQuery(String hql, Map params, int init, int end) throws DAOException {
        Session session = null;
        try {
            session = HibernateSessionFactory.getSession();
            Query query = session.createQuery(hql);
            if (params != null && params.size() > 0) {
                Iterator it = params.keySet().iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    Object value = params.get(key);
                    query.setParameter(key, value);
                }
            }
            query.setFirstResult(init);
            query.setMaxResults(end);
            List retorno = query.list();
            return retorno;
        } catch (Exception he) {
            throw new DAOException(he);
        } finally {
        }
    }

    /**
	 * Executa uma query baseada no HQL informado.
	 * @param hql
	 * @return
	 * @throws DAOException 
	 */
    public static Object executeQueryOneResult(String hql) throws DAOException {
        return executeQueryOneResult(hql, null);
    }

    /**
	 * Executa uma query baseado no HQL e nos parï¿½metros passados.
	 * @param hql
	 * @param params
	 * @return
	 */
    public static Object executeQueryOneResult(String hql, Map params) throws DAOException {
        Session session = null;
        try {
            session = HibernateSessionFactory.getSession();
            Query query = session.createQuery(hql);
            if (params != null && params.size() > 0) {
                Iterator it = params.keySet().iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    Object value = params.get(key);
                    query.setParameter(key, value);
                }
            }
            Object retorno = query.uniqueResult();
            return retorno;
        } catch (Exception he) {
            throw new DAOException(he);
        } finally {
        }
    }

    /**
	 * Executa uma query em SQL nativo
	 * @param sql
	 * @return
	 * @throws DAOException
	 */
    public static List executeSQLQuery(String sql) throws DAOException {
        Session session = null;
        try {
            session = HibernateSessionFactory.getSession();
            Query query = session.createSQLQuery(sql);
            List retorno = query.list();
            return retorno;
        } catch (Exception he) {
            throw new DAOException(he);
        }
    }

    /**
	 * Executa um UPDATE/INSERT/DELETE em SQL nativo
	 * @param sql
	 * @return
	 * @throws DAOException
	 */
    public static void executeSQLUpdate(String sql) throws DAOException {
        Session session = null;
        try {
            session = HibernateSessionFactory.getSession();
            Query query = session.createSQLQuery(sql);
            query.executeUpdate();
        } catch (Exception he) {
            throw new DAOException(he);
        }
    }

    /**
	 * Metodo responsavel por realizar uma insercao ou alteracao.
	 * @param obj
	 * @throws DAOException
	 */
    public static void save(Object obj) throws DAOException {
        Session session = null;
        try {
            session = HibernateSessionFactory.getSession();
            session.saveOrUpdate(obj);
            session.flush();
        } catch (Exception he) {
            throw new DAOException(he);
        }
    }

    /**
	 * Metodo responsavel por realizar uma insercao com um objeto que ja possui ID..
	 * @param obj
	 * @throws DAOException
	 */
    public static void saveWithId(Object obj) throws DAOException {
        Session session = null;
        try {
            session = HibernateSessionFactory.getSession();
            session.merge(obj);
        } catch (Exception he) {
            throw new DAOException(he);
        } finally {
        }
    }

    /** 
	 * Atualiza as informacoes do objeto ja carregado na session
	 * @param obj
	 * @throws DAOException
	 */
    public static void updateInSession(Object obj) throws DAOException {
        Session session = null;
        try {
            session = HibernateSessionFactory.getSession();
            session.merge(obj);
        } catch (Exception he) {
            throw new DAOException(he);
        }
    }

    /**
	 * Metodo responsavel por retornar um objeto generico pelo seu ID(PrimaryKey)
	 * @param klass
	 * @param id
	 * @return
	 * @throws DAOException
	 */
    public static Object getById(Class klass, Serializable id) throws DAOException {
        Session session = null;
        try {
            session = HibernateSessionFactory.getSession();
            Object obj = session.get(klass, id);
            return obj;
        } catch (Exception he) {
            throw new DAOException(he);
        } finally {
        }
    }

    /**
	 * Metodo responsavel por excluir uma entidade.
	 * @param obj
	 * @throws DAOException
	 */
    public static void delete(Object obj) throws DAOException {
        Session session = null;
        try {
            session = HibernateSessionFactory.getSession();
            session.delete(obj);
        } catch (Exception he) {
            throw new DAOException(he);
        }
    }

    /**
	 * Retorna todos os registros de uma determinada entidade.
	 * @param obj
	 * @return
	 * @throws DAOException
	 */
    public static List getAll(String entityName) throws DAOException {
        return getAll(entityName, "id");
    }

    public static List getAll(String entityName, String sortField) throws DAOException {
        Session session = null;
        try {
            session = HibernateSessionFactory.getSession();
            String hql = "from " + entityName + " x order by x." + sortField;
            Query query = session.createQuery(hql);
            List retorno = query.list();
            return retorno;
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new DAOException(he);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    /**
	 * Remove o objeto da sessï¿½o do hibernate
	 * @param obj
	 * @throws DAOException
	 */
    public static void unloadObject(Object obj) throws DAOException {
        Session session = null;
        try {
            session = HibernateSessionFactory.getSession();
            session.evict(obj);
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new DAOException(he);
        }
    }

    public static void refreshObject(Object obj) throws DAOException {
        Session session = null;
        try {
            session = HibernateSessionFactory.getSession();
            session.refresh(obj);
        } catch (Exception he) {
            he.printStackTrace();
            throw new DAOException(he);
        }
    }

    /**
	 * Limpa a sessÃ£o do Hibernate
	 * @throws DAOException 
	 */
    public static void clearSession() throws DAOException {
        Session session = null;
        try {
            session = HibernateSessionFactory.getSession();
            session.clear();
        } catch (Exception he) {
            he.printStackTrace();
            throw new DAOException(he);
        }
    }

    /**
	 * Realiza um flush na sessï¿½o do Hibernate.
	 * @throws DAOException 
	 */
    public static void flushSession() throws DAOException {
        Session session = null;
        try {
            session = HibernateSessionFactory.getSession();
            session.flush();
        } catch (Exception he) {
            he.printStackTrace();
            throw new DAOException(he);
        }
    }

    /**
	 * Realiza um rebuild na sessï¿½o do Hibernate.
	 * @throws DAOException 
	 */
    public static void rebuildSession() throws DAOException {
        Session session = null;
        try {
            session = HibernateSessionFactory.getSession();
            session.getTransaction().rollback();
            session.close();
            HibernateSessionFactory.getSession().beginTransaction();
        } catch (Exception he) {
            he.printStackTrace();
            throw new DAOException(he);
        }
    }
}
