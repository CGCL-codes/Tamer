package org.bionote.om.dao.hibernate;

import java.sql.SQLException;
import java.util.List;
import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.LockMode;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bionote.om.IPage;
import org.bionote.om.IPageAnnotation;
import org.bionote.om.IPageAnnotationType;
import org.bionote.om.PageAnnotation;
import org.bionote.om.dao.PageAnnotationDAO;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

/**
 * @author mbreese
 *
 */
public class PageAnnotationDAOImpl extends HibernateDaoSupport implements PageAnnotationDAO {

    protected final Log logger = LogFactory.getLog(getClass());

    public PageAnnotationDAOImpl() {
        super();
    }

    public IPageAnnotation findPageAnnotation(long id) {
        IPageAnnotation pageAnnotation = (IPageAnnotation) getHibernateTemplate().get(PageAnnotation.class, new Long(id));
        return pageAnnotation;
    }

    public void update(IPageAnnotation pageAnnotation) {
        getHibernateTemplate().saveOrUpdate(pageAnnotation);
    }

    public void delete(IPageAnnotation pageAnnotation) {
        getHibernateTemplate().delete(pageAnnotation);
    }

    public IPageAnnotation findPageAnnotationByType(final IPage page, final IPageAnnotationType type) {
        if (page == null || type == null) {
            logger.debug("nulls");
            return null;
        }
        return (IPageAnnotation) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session arg0) throws HibernateException, SQLException {
                Criteria crit = arg0.createCriteria(PageAnnotation.class);
                arg0.lock(page, LockMode.NONE);
                arg0.lock(type, LockMode.NONE);
                logger.debug("checking for page:" + page.getName() + " type:" + type.getName());
                crit.add(Expression.eq("page", page));
                crit.add(Expression.eq("type", type));
                crit.add(Expression.isNull("disabledDate"));
                crit.setFetchSize(1);
                return crit.uniqueResult();
            }
        });
    }

    public List findPageAnnotationByValue(final IPageAnnotationType at, final String value) {
        if (at == null || value == null) {
            logger.debug("nulls");
            return null;
        }
        return (List) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session arg0) throws HibernateException, SQLException {
                Criteria crit = arg0.createCriteria(PageAnnotation.class);
                logger.debug("looking for a page with an annotation of type:" + at.getName() + " with a value of: " + value);
                crit.add(Expression.eq("type", at));
                crit.add(Expression.eq("value", value));
                return crit.list();
            }
        });
    }
}
