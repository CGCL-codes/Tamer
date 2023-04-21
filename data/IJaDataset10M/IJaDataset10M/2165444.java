package org.gestionabierta.dominio.persistencia;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gestionabierta.utilidades.excepciones.DataAccessLayerException;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 *
 * @author Franky Villadiego
 */
public abstract class GenericDao implements IGenericDao {

    private static final Logger log = Logger.getLogger(GenericDao.class.getName());

    private HibernateTemplate hibernateTemplate;

    private SessionFactory sessionFactory;

    public GenericDao() {
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    @Autowired
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <T> void guardar(T entity) throws DataAccessLayerException {
        try {
            getHibernateTemplate().save(entity);
        } catch (DataAccessException ex) {
            log.log(Level.WARNING, "DataAccessException={0}", ex.getMessage());
            throw new DataAccessLayerException(ex.getMessage());
        }
    }

    public <T> void eliminar(T entity) throws DataAccessLayerException {
        try {
            getHibernateTemplate().delete(entity);
        } catch (DataAccessException ex) {
            log.log(Level.WARNING, "DataAccessException={0}", ex.getMessage());
            throw new DataAccessLayerException(ex.getMessage());
        }
    }

    public <T> void actualizar(T entity) throws DataAccessLayerException {
        try {
            getHibernateTemplate().update(entity);
        } catch (DataAccessException ex) {
            log.log(Level.WARNING, "DataAccessException={0}", ex.getMessage());
            throw new DataAccessLayerException(ex.getMessage());
        }
    }

    public <T> List<T> traerTodos(Class<T> entityClass) throws DataAccessLayerException {
        List<T> lst = null;
        try {
            lst = getHibernateTemplate().loadAll(entityClass);
        } catch (DataAccessException ex) {
            log.log(Level.WARNING, "DataAccessException={0}", ex.getMessage());
            throw new DataAccessLayerException(ex.getMessage());
        }
        return lst;
    }

    public <T> T traerPorId(Class<T> entityClass, Serializable id) throws DataAccessLayerException {
        try {
            T o = (T) getHibernateTemplate().get(entityClass, id);
            return o;
        } catch (DataAccessException ex) {
            log.log(Level.WARNING, "DataAccessException={0}", ex.getMessage());
            throw new DataAccessLayerException(ex.getMessage());
        }
    }

    public int ejecutarSqlQuery(final String query) throws DataAccessLayerException {
        try {
            Integer registrosAfectados = (Integer) getHibernateTemplate().execute(new HibernateCallback() {

                public Integer doInHibernate(Session sn) throws HibernateException, SQLException {
                    SQLQuery sqlQ = sn.createSQLQuery(query);
                    int rowsAfectadas = sqlQ.executeUpdate();
                    if (rowsAfectadas == 0) {
                        log.log(Level.INFO, "La consulta [{0}] no afecto ningun registro", query);
                    }
                    return new Integer(rowsAfectadas);
                }
            });
            if (registrosAfectados == null) {
                return 0;
            } else {
                return registrosAfectados.intValue();
            }
        } catch (DataAccessException ex) {
            log.log(Level.WARNING, "DataAccessException={0}", ex.getMessage());
            throw new DataAccessLayerException(ex.getMessage());
        }
    }

    public <T> Integer contarRegistros(Class<T> clazz) throws DataAccessLayerException {
        Integer can = 0;
        List lst = null;
        try {
            DetachedCriteria cr = DetachedCriteria.forClass(clazz);
            cr.setProjection(Projections.rowCount());
            lst = getHibernateTemplate().findByCriteria(cr);
            if (lst == null || lst.isEmpty()) {
                return 0;
            } else {
                can = Integer.valueOf(lst.get(0).toString());
            }
        } catch (DataAccessException ex) {
            log.log(Level.WARNING, "DataAccessException={0}", ex.getMessage());
            throw new DataAccessLayerException(ex);
        } catch (Exception ex) {
            log.log(Level.WARNING, "Exception={0}", ex.getMessage());
            throw new DataAccessLayerException(ex);
        }
        return can;
    }

    public <T> Integer contarRegistros(Class<T> clazz, List<Criterio> criterios) throws DataAccessLayerException {
        Integer can = 0;
        List lst = null;
        try {
            DetachedCriteria cr = DetachedCriteria.forClass(clazz);
            cr.setProjection(Projections.rowCount());
            Iterator it = criterios.iterator();
            while (it.hasNext()) {
                Criterion criterion = ((Criterio) it.next()).getCriterion();
                cr.add(criterion);
            }
            lst = getHibernateTemplate().findByCriteria(cr);
            if (lst == null || lst.isEmpty()) {
                return 0;
            } else {
                can = Integer.valueOf(lst.get(0).toString());
            }
        } catch (DataAccessException ex) {
            log.log(Level.WARNING, "DataAccessException={0}", ex.getMessage());
            throw new DataAccessLayerException(ex);
        } catch (Exception ex) {
            log.log(Level.WARNING, "Exception={0}", ex.getMessage());
            throw new DataAccessLayerException(ex);
        }
        return can;
    }

    @Override
    public <T> List<T> consultar(Class<T> clazz, List<Criterio> criterios, int fila, int cantidadRegistros) throws DataAccessLayerException {
        List<T> lst = null;
        try {
            DetachedCriteria cr = DetachedCriteria.forClass(clazz);
            Iterator it = criterios.iterator();
            while (it.hasNext()) {
                Criterion criterion = ((Criterio) it.next()).getCriterion();
                cr.add(criterion);
            }
            lst = getHibernateTemplate().findByCriteria(cr, fila, cantidadRegistros);
        } catch (DataAccessException ex) {
            log.log(Level.WARNING, "DataAccessException={0}", ex.getMessage());
            throw new DataAccessLayerException(ex.getMessage());
        } catch (Exception ex) {
            log.log(Level.WARNING, "Exception={0}", ex.getMessage());
            throw new DataAccessLayerException(ex.getMessage());
        }
        return lst;
    }

    @Override
    public <T> List<T> consultar(Class<T> clazz, List<Criterio> criterios) throws DataAccessLayerException {
        List<T> lst = null;
        try {
            DetachedCriteria cr = DetachedCriteria.forClass(clazz);
            Iterator it = criterios.iterator();
            while (it.hasNext()) {
                Criterion criterion = ((Criterio) it.next()).getCriterion();
                cr.add(criterion);
            }
            lst = getHibernateTemplate().findByCriteria(cr);
        } catch (DataAccessException ex) {
            log.log(Level.WARNING, "DataAccessException={0}", ex.getMessage());
            throw new DataAccessLayerException(ex.getMessage());
        } catch (Exception ex) {
            log.log(Level.WARNING, "Exception={0}", ex.getMessage());
            throw new DataAccessLayerException(ex.getMessage());
        }
        return lst;
    }

    /**
     * <p>Retorna un tipo MatchMode (coincidencia de Hibernate).
     * <p>El parametro coincidencia se puede conseguir en la
     * clase <code>Criterio</code>
     * 
     * @param coincidencia
     * @return
     */
    protected MatchMode getModoCoincidencia(int coincidencia) {
        MatchMode coincide = null;
        if (coincidencia <= 0 || coincidencia > 3) {
            coincide = MatchMode.EXACT;
        } else if (coincidencia == Criterio.START) {
            coincide = MatchMode.START;
        } else if (coincidencia == Criterio.ANY_WHERE) {
            coincide = MatchMode.ANYWHERE;
        } else if (coincidencia == Criterio.END) {
            coincide = MatchMode.END;
        }
        return coincide;
    }
}
