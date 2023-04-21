package org.jw.web.rdc.integration.dto;

import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 	* A data access object (DAO) providing persistence and search support for Crc entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see org.jw.web.rdc.integration.dto.Crc
  * @author MyEclipse Persistence Tools 
 */
public class CrcDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(CrcDAO.class);

    public static final String CIDADE = "cidade";

    public static final String ESTADO = "estado";

    public static final String NOME_PRESIDENTE = "nomePresidente";

    public static final String DDD_TELEF_PRESIDENTE = "dddTelefPresidente";

    public static final String NUMERO_TELEF_PRESIDENTE = "numeroTelefPresidente";

    public static final String DDD_CELULAR_PRESIDENTE = "dddCelularPresidente";

    public static final String NUMERO_CELULAR_PRESIDENTE = "numeroCelularPresidente";

    public static final String SKYPE_PRESIDENTE = "skypePresidente";

    protected void initDao() {
    }

    public void save(Crc transientInstance) {
        log.debug("saving Crc instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(Crc persistentInstance) {
        log.debug("deleting Crc instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public Crc findById(java.lang.Integer id) {
        log.debug("getting Crc instance with id: " + id);
        try {
            Crc instance = (Crc) getHibernateTemplate().get("org.jw.web.rdc.integration.dto.Crc", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(Crc instance) {
        log.debug("finding Crc instance by example");
        try {
            List results = getHibernateTemplate().findByExample(instance);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding Crc instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from Crc as model where model." + propertyName + "= ?";
            return getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByCidade(Object cidade) {
        return findByProperty(CIDADE, cidade);
    }

    public List findByEstado(Object estado) {
        return findByProperty(ESTADO, estado);
    }

    public List findByNomePresidente(Object nomePresidente) {
        return findByProperty(NOME_PRESIDENTE, nomePresidente);
    }

    public List findByDddTelefPresidente(Object dddTelefPresidente) {
        return findByProperty(DDD_TELEF_PRESIDENTE, dddTelefPresidente);
    }

    public List findByNumeroTelefPresidente(Object numeroTelefPresidente) {
        return findByProperty(NUMERO_TELEF_PRESIDENTE, numeroTelefPresidente);
    }

    public List findByDddCelularPresidente(Object dddCelularPresidente) {
        return findByProperty(DDD_CELULAR_PRESIDENTE, dddCelularPresidente);
    }

    public List findByNumeroCelularPresidente(Object numeroCelularPresidente) {
        return findByProperty(NUMERO_CELULAR_PRESIDENTE, numeroCelularPresidente);
    }

    public List findBySkypePresidente(Object skypePresidente) {
        return findByProperty(SKYPE_PRESIDENTE, skypePresidente);
    }

    public List findAll() {
        log.debug("finding all Crc instances");
        try {
            String queryString = "from Crc";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public Crc merge(Crc detachedInstance) {
        log.debug("merging Crc instance");
        try {
            Crc result = (Crc) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Crc instance) {
        log.debug("attaching dirty Crc instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(Crc instance) {
        log.debug("attaching clean Crc instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static CrcDAO getFromApplicationContext(ApplicationContext ctx) {
        return (CrcDAO) ctx.getBean("CrcDAO");
    }
}
