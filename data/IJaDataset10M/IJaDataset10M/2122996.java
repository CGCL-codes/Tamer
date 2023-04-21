package org.osmius.dao.hibernate;

import org.osmius.dao.OsmTaskDao;
import org.osmius.model.OsmTask;
import org.osmius.model.OsmTyptask;
import org.osmius.model.OsmMasteragent;
import org.osmius.model.OsmAgent;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import java.util.List;
import java.util.Date;
import java.sql.Timestamp;

/**
 * @see org.osmius.dao.OsmTaskDao
 */
public class OsmTaskDaoHibernate extends BaseDaoHibernate implements OsmTaskDao {

    /**
    * @see org.osmius.dao.OsmTaskDao#getOsmTasks(org.osmius.model.OsmTask)
    */
    public List getOsmTasks(OsmTask osmTask) {
        List osmTasks = null;
        if (osmTask == null) {
            osmTasks = getHibernateTemplate().find("from OsmTask order by dtiInitask asc");
        }
        return osmTasks;
    }

    /**
    * @see org.osmius.dao.OsmTaskDao#getOsmTasks(org.osmius.model.OsmTask, int, int, String)
    */
    public List getOsmTasks(final OsmTask osmTask, final int startPosition, final int pageSize, final String orderBy) {
        HibernateCallback callback = new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                StringBuffer queryString = new StringBuffer("select tk from OsmTask tk");
                queryString.append(" order by ").append(orderBy);
                String strSQL = queryString.toString();
                Query query = session.createQuery(strSQL);
                query.setFirstResult(startPosition);
                query.setMaxResults(pageSize);
                return query.list();
            }
        };
        return (List) getHibernateTemplate().execute(callback);
    }

    /**
    * @see org.osmius.dao.OsmTaskDao#getOsmTasksByMaster(String)
    */
    public List getOsmTasksByMaster(String idnMaster) {
        List osmTasks = null;
        if (idnMaster == null || idnMaster.equals("")) {
            osmTasks = getHibernateTemplate().find("from OsmTask order by dtiInitask asc");
        } else {
            osmTasks = getHibernateTemplate().find("from OsmTask where osmMasteragent.idnMaster=? order by dtiInitask asc", idnMaster);
        }
        return osmTasks;
    }

    /**
    * @see org.osmius.dao.OsmTaskDao#getSizeOsmTasks(org.osmius.model.OsmTask)
    */
    public Long getSizeOsmTasks(final OsmTask osmTask) {
        HibernateCallback callback = new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                StringBuffer queryString = new StringBuffer("select count(*) from OsmTask");
                String strSQL = queryString.toString();
                Query query = session.createQuery(strSQL);
                return query.list();
            }
        };
        return (Long) ((List) getHibernateTemplate().execute(callback)).get(0);
    }

    /**
    * @see org.osmius.dao.OsmTaskDao#deleteTask(String[])
    */
    public void deleteTask(String[] splittedChecks) {
        Long[] tasks = new Long[splittedChecks.length];
        StringBuffer delete = new StringBuffer("delete OsmTask where idnTask in (");
        for (int i = 0; i < splittedChecks.length; i++) {
            if (i == 0) delete.append("?"); else delete.append(", ?");
            tasks[i] = new Long(splittedChecks[i]);
        }
        delete.append(")");
        getHibernateTemplate().bulkUpdate(delete.toString(), tasks);
    }

    /**
    * @see org.osmius.dao.OsmTaskDao#saveTask(org.osmius.model.OsmTask)
    */
    public void saveTask(OsmTask osmTask) {
        getHibernateTemplate().save(osmTask);
    }

    public void startMA(OsmMasteragent osmMasteragent) {
        OsmTask task = new OsmTask();
        OsmTyptask osmTyptask = new OsmTyptask();
        osmTyptask.setTypTask(new Integer(2));
        task.setOsmTyptask(osmTyptask);
        task.setOsmMasteragent(osmMasteragent);
        task.setDtiExecute(new Date(getActualTimestamp().getTime()));
        task.setIndState(new Integer(0));
        task.setNumRetries(new Integer(0));
        getHibernateTemplate().save(task);
    }

    public void stopMA(OsmMasteragent osmMasterAgent) {
        OsmTask task = new OsmTask();
        OsmTyptask osmTyptask = new OsmTyptask();
        osmTyptask.setTypTask(new Integer(3));
        task.setOsmTyptask(osmTyptask);
        task.setOsmMasteragent(osmMasterAgent);
        task.setDtiExecute(new Date(getActualTimestamp().getTime()));
        task.setIndState(new Integer(0));
        task.setNumRetries(new Integer(0));
        getHibernateTemplate().save(task);
    }

    public void pauseMA(OsmMasteragent osmMasterAgent) {
        OsmTask task = new OsmTask();
        OsmTyptask osmTyptask = new OsmTyptask();
        osmTyptask.setTypTask(new Integer(4));
        task.setOsmTyptask(osmTyptask);
        task.setOsmMasteragent(osmMasterAgent);
        task.setDtiExecute(new Date(getActualTimestamp().getTime()));
        task.setIndState(new Integer(0));
        task.setNumRetries(new Integer(0));
        getHibernateTemplate().save(task);
    }

    public void removeTasksByMasterAgent(String idnMasteragent) {
        StringBuffer delete = new StringBuffer("delete OsmTask where osmMasteragent.idnMaster = ? )");
        String[] masterAgents = new String[] { idnMasteragent };
        getHibernateTemplate().bulkUpdate(delete.toString(), masterAgents);
    }

    public void startAG(OsmAgent osmAgent) {
        OsmTask task = new OsmTask();
        OsmTyptask osmTyptask = new OsmTyptask();
        osmTyptask.setTypTask(new Integer(8));
        task.setOsmTyptask(osmTyptask);
        task.setOsmMasteragent(osmAgent.getOsmMasteragent());
        task.setOsmTypagent(osmAgent.getOsmTypagent());
        task.setDtiExecute(new Date(getActualTimestamp().getTime()));
        task.setIndState(new Integer(0));
        task.setNumRetries(new Integer(0));
        getHibernateTemplate().save(task);
    }

    public void stopAG(OsmAgent osmAgent) {
        OsmTask task = new OsmTask();
        OsmTyptask osmTyptask = new OsmTyptask();
        osmTyptask.setTypTask(new Integer(8));
        task.setOsmTyptask(osmTyptask);
        task.setOsmMasteragent(osmAgent.getOsmMasteragent());
        task.setOsmTypagent(osmAgent.getOsmTypagent());
        task.setDtiExecute(new Date(getActualTimestamp().getTime()));
        task.setIndState(new Integer(0));
        task.setNumRetries(new Integer(0));
        getHibernateTemplate().save(task);
    }

    public void resetTask(String task) {
        OsmTask osmTask = (OsmTask) getHibernateTemplate().load(OsmTask.class, new Long(task));
        osmTask.setNumRetries(0);
        getHibernateTemplate().saveOrUpdate(osmTask);
    }

    public void resetState(String task) {
        OsmTask osmTask = (OsmTask) getHibernateTemplate().load(OsmTask.class, new Long(task));
        osmTask.setIndState(0);
        getHibernateTemplate().saveOrUpdate(osmTask);
    }

    public void removeByInstance(String instance) {
        getHibernateTemplate().bulkUpdate("delete OsmTask where osmInstance.idnInstance=?", instance);
    }
}
