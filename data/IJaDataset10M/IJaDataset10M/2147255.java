package gleam.executive.workflow.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.jbpm.persistence.db.DbPersistenceService;

public class JbpmDbPersistenceService extends DbPersistenceService {

    private static final long serialVersionUID = 1L;

    protected JbpmDbPersistenceServiceFactory persistenceServiceFactory = null;

    protected JbpmGraphSession graphSession = null;

    protected JbpmTaskMgmtSession taskMgmtSession = null;

    public JbpmDbPersistenceService(JbpmDbPersistenceServiceFactory persistenceServiceFactory) {
        super(persistenceServiceFactory);
    }

    public JbpmGraphSession getGraphSession() {
        if (graphSession == null) {
            Session session = getSession();
            if (session != null) {
                graphSession = new JbpmGraphSession(session);
            }
        }
        return graphSession;
    }

    public JbpmTaskMgmtSession getTaskMgmtSession() {
        if (taskMgmtSession == null) {
            Session session = getSession();
            if (session != null) {
                taskMgmtSession = new JbpmTaskMgmtSession(session);
            }
        }
        return taskMgmtSession;
    }

    private static Log log = LogFactory.getLog(JbpmDbPersistenceService.class);
}
