package ar.com.khronos.core.dao.hb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ar.com.khronos.core.dao.ObjectFilter;

/**
 * Implementacion del filtro de objetos persistentes utilizando
 * <a href="http://www.hibernate.org/">Hibernate</a>, a traves de la
 * clase {@link HibernateDAO}.
 * 
 * @author <a href="mailto:tezequiel@gmail.com">Ezequiel Turovetzky</a>
 *
 * @see ar.com.khronos.core.dao.hb.HibernateHQLObjectFilter
 */
public abstract class HibernateObjectFilter implements ObjectFilter {

    /** Logger */
    protected static final Log logger = LogFactory.getLog(HibernateObjectFilter.class);

    /** DAO de Hibernate */
    protected HibernateDAO hibernateDAO;

    /**
     * Crea una nueva instancia de esta clase.
     */
    public HibernateObjectFilter() {
    }

    public HibernateDAO getHibernateDAO() {
        return hibernateDAO;
    }

    public void setHibernateDAO(HibernateDAO hibernateDAO) {
        this.hibernateDAO = hibernateDAO;
    }
}
