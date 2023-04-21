package org.powerstone.workflow.dao.hibernate;

import java.util.List;
import org.powerstone.workflow.model.WorkflowMeta;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.powerstone.workflow.dao.WorkflowMetaDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Title: PowerStone</p>
 */
public class WorkflowMetaDAOImpl extends HibernateDaoSupport implements WorkflowMetaDAO {

    private static Log log = LogFactory.getLog(WorkflowMetaDAOImpl.class);

    public List getAllWorkflowMetas() {
        return getHibernateTemplate().findByNamedQuery("AllWorkflowMetas");
    }

    public WorkflowMeta getWorkflowMeta(Long flowMetaID) {
        return (WorkflowMeta) getHibernateTemplate().load(WorkflowMeta.class, flowMetaID);
    }

    public void saveWorkflowMeta(WorkflowMeta workflowMeta) {
        getHibernateTemplate().saveOrUpdate(workflowMeta);
        getHibernateTemplate().flush();
    }

    public void removeWorkflowMeta(Long flowMetaID) {
        Object obj = (WorkflowMeta) getHibernateTemplate().load(WorkflowMeta.class, flowMetaID);
        getHibernateTemplate().delete(obj);
        getHibernateTemplate().flush();
    }

    public WorkflowMeta getWorkflowMetaByProcess(String flowProcessID) {
        List result = getHibernateTemplate().findByNamedQuery("WorkflowMetaByProcess", new String[] { flowProcessID });
        if (result != null && result.size() > 0) {
            return (WorkflowMeta) result.get(0);
        }
        return null;
    }

    /**
   * getWorkflowMetasNoBusinessType
   * @return List
   */
    public List getWorkflowMetasNoBusinessType() {
        List result = getHibernateTemplate().findByNamedQuery("WorkflowMetasNoBusinessType");
        return result;
    }
}
