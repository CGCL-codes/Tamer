package mx.com.nyak.orden.dao;

import java.util.List;
import mx.com.nyak.empresa.exception.DataAccesException;
import mx.com.nyak.orden.dto.TipoItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TipoItemDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TipoItemDAO.class);

    public void save(TipoItem transientInstance) throws DataAccesException {
        log.debug("saving TipoItem instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (Exception re) {
            log.error("get failed", re);
            throw new DataAccesException(re);
        }
    }

    public void delete(TipoItem persistentInstance) throws DataAccesException {
        log.debug("deleting TipoItem instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (Exception re) {
            log.error("get failed", re);
            throw new DataAccesException(re);
        }
    }

    public TipoItem findById(java.lang.Integer id) throws DataAccesException {
        log.debug("getting TipoItem instance with id: " + id);
        try {
            TipoItem instance = (TipoItem) getHibernateTemplate().get("mx.com.nyak.orden.dto.TipoItem", id);
            return instance;
        } catch (Exception re) {
            log.error("get failed", re);
            throw new DataAccesException(re);
        }
    }

    @SuppressWarnings("unchecked")
    public List<TipoItem> findByProperty(String propertyName, Object value) throws DataAccesException {
        log.debug("finding TipoItem instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from TipoItem as model where model." + propertyName + "= ?";
            return getHibernateTemplate().find(queryString, value);
        } catch (Exception re) {
            log.error("get failed", re);
            throw new DataAccesException(re);
        }
    }

    public List findAll() throws DataAccesException {
        log.debug("finding all TipoItem instances");
        try {
            String queryString = "from TipoItem";
            return getHibernateTemplate().find(queryString);
        } catch (Exception re) {
            log.error("get failed", re);
            throw new DataAccesException(re);
        }
    }
}
