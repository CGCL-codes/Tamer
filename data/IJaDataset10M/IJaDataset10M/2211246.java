package org.itracker.persistence.dao;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.itracker.model.WorkflowScript;

/**
 * 
 */
public class WorkflowScriptDAOImpl extends BaseHibernateDAOImpl<WorkflowScript> implements WorkflowScriptDAO {

    private static final Logger log = Logger.getLogger(WorkflowScript.class);

    /**
     * Find a <code>WorkflowScript</code> by its primary key
     *
     * @param id primary key of the <code>WorkflowScript</code>
     * @return The <code>WorkflowScript</code> found
     */
    public WorkflowScript findByPrimaryKey(Integer id) {
        try {
            WorkflowScript workflowScriptBean = (WorkflowScript) getSession().get(WorkflowScript.class, id);
            return workflowScriptBean;
        } catch (HibernateException ex) {
            log.error("findByPrimaryKey: failed with hibernate exception", ex);
            throw convertHibernateAccessException(ex);
        }
    }

    /**
     * Finds all <code>WorkflowScript</code>s
     *
     * @return a <code>Collection</code> with all <code>WorkflowScript</code>s
     */
    @SuppressWarnings("unchecked")
    public List<WorkflowScript> findAll() {
        Criteria criteria = getSession().createCriteria(WorkflowScript.class);
        try {
            return criteria.list();
        } catch (HibernateException e) {
            throw convertHibernateAccessException(e);
        }
    }
}
