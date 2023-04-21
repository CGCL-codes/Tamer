package br.com.bafonline.model.dao;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import br.com.bafonline.model.dao.implementation.GenericDAOImpl;
import br.com.bafonline.model.dto.QualificacaoDTO;
import br.com.bafonline.util.exception.hibernate.HibernateSessionFactoryException;

/**
 * Classe DAO (Data Access Object) que tem por finalidade persistir e pesquisar dados 
 * da entidade Qualifica��o. <br>
 * Herda todas as funcionalidades de GenericDAO.
 * @see br.com.bafonline.model.dto.QualificacaoDTO
 * @see br.com.bafonline.model.dao.implementation.GenericDAOImpl
 * @author bafonline
 *
 */
public class QualificacaoDAO extends GenericDAOImpl {

    private static final Log log = LogFactory.getLog(QualificacaoDAO.class);

    public static final String DESCRICAO = "descricao";

    public void save(QualificacaoDTO transientInstance) {
        log.debug("saving Qualificacao instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        } catch (HibernateSessionFactoryException e) {
            log.error("save failed", e);
        }
    }

    public void delete(QualificacaoDTO persistentInstance) {
        log.debug("deleting Qualificacao instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        } catch (HibernateSessionFactoryException e) {
            log.error("delete failed", e);
        }
    }

    public QualificacaoDTO findById(java.lang.Integer id) {
        log.debug("getting Qualificacao instance with id: " + id);
        try {
            QualificacaoDTO instance = (QualificacaoDTO) getSession().get("br.com.bafonline.model.dto.QualificacaoDTO", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        } catch (HibernateSessionFactoryException e) {
            log.error("get failed", e);
        }
        return null;
    }

    public List<?> findByExample(QualificacaoDTO instance) {
        log.debug("finding Qualificacao instance by example");
        try {
            List<?> results = getSession().createCriteria("br.com.bafonline.model.dto.QualificacaoDTO").add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        } catch (HibernateSessionFactoryException e) {
            log.error("find by example failed", e);
        }
        return null;
    }

    public List<?> findByProperty(String propertyName, Object value) {
        log.debug("finding Qualificacao instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from QualificacaoDTO as model where model." + propertyName + "= ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        } catch (HibernateSessionFactoryException e) {
            log.error("find by property name failed", e);
        }
        return null;
    }

    public List<?> findByDescricao(Object descricao) {
        return findByProperty(DESCRICAO, descricao);
    }

    public List<?> findAll() {
        log.debug("finding all Qualificacao instances");
        try {
            String queryString = "from QualificacaoDTO";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        } catch (HibernateSessionFactoryException e) {
            log.error("find all failed", e);
        }
        return null;
    }

    public QualificacaoDTO merge(QualificacaoDTO detachedInstance) {
        log.debug("merging Qualificacao instance");
        try {
            QualificacaoDTO result = (QualificacaoDTO) getSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        } catch (HibernateSessionFactoryException e) {
            log.error("merge failed", e);
        }
        return null;
    }

    public void attachDirty(QualificacaoDTO instance) {
        log.debug("attaching dirty Qualificacao instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        } catch (HibernateSessionFactoryException e) {
            log.error("attach failed", e);
        }
    }

    public void attachClean(QualificacaoDTO instance) {
        log.debug("attaching clean Qualificacao instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        } catch (HibernateSessionFactoryException e) {
            log.error("attach failed", e);
        }
    }
}
