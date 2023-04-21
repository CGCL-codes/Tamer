package net.sf.coredd.library.domain;

import java.io.Serializable;
import net.sf.coredd.library.exception.BusinessException;
import net.sf.coredd.library.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/** Base class for data access
 * @author roberto.reinert
 *
 * @param <CODE>
 * @param <ID>
 * @param <T>
 */
public abstract class BaseCodeDAO<CODE extends Serializable, ID extends Serializable, T extends BaseDTO<ID>> extends BaseDAO<ID, T> implements ICodeDAO<CODE, ID, T> {

    private static Logger log = Logger.getLogger(BaseCodeDAO.class);

    @SuppressWarnings("unchecked")
    public T findByCode(CODE code) throws BusinessException {
        log.debug("findByCode " + getPersistentClass().getName());
        Criteria c = HibernateUtil.getSession().createCriteria(getPersistentClass());
        c.add(Restrictions.eq("code", code));
        this.fetchCriteria(c);
        return (T) c.uniqueResult();
    }
}
